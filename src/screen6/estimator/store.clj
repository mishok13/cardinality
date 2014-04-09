(ns screen6.estimator.store
  "Various stores used by Estimators"
  (:require
   [immutable-bitset :as bitset]
   [primitive-math :as math :refer [/ * - rem]]
   [screen6.estimator.utils :as utils])
  (:import [java.util BitSet])
  (:refer-clojure :exclude [/ * - rem]))

(defprotocol Estimator
  (cardinality [this]
    "Return numerical estimation of cardinality")
  (add [this value]
    "Add the value to estimator"))

(defn- bit-mask
  "Create a bit mask starting at `start` with length of `length`."
  ([] Long/MAX_VALUE)
  ([length]
     (dec (math/<< 1 length)))
  ([start length]
     (-> (bit-mask length)
         (math/<< start))))

(defn- compute-alpha
  "Compute alpha parameter as described in HyperLogLog paper"
  [m]
  (case m
    16 0.673
    32 0.697
    64 0.709
    (/ 0.7213 (math/+ 1.0 (/ 1.079 m)))))

(defn linear-count
  [^long m ^long v]
  (* m (Math/log (/ m v))))

(defn encode-value
  [^long value ^long p ^long p']
  (let [mask 0] nil))

(defn- large-range-correction
  [^double estimator]
  (- (* (Math/pow 2 32) (Math/log (- 1 (/ estimator (Math/pow 2 32)))))))

(def ^:private bits-per-word 64)

(defn- get-word
  [^BitSet bitset start stop]
  ;; Due to peculiar way bitsets are treated we always want to return
  ;; zero even if there's nothing actually set in the bitset.
  (-> bitset
      (.get start stop)
      (.toLongArray)
      (first)
      (or 0)))

(deftype ImmutableBitVector [registers ^long register-size ^short word-size ^long cnt]

  clojure.lang.IPersistentMap
  (assoc [_ index value]
    )

  clojure.lang.ILookup
  (valAt [_ index]
    (let [index (long index)
          bit-index (math/* index word-size)
          register-position (/ bit-index register-size)]
      ;; Check if the word we're trying to get is located on single register
      (if (math/< (math/+ (math/rem bit-index register-size) word-size) register-size)
        ;; Neat! Now shift the register to the beginning of the word
        ;; and then truncate it to specified word-size in bits.
        ;;
        (get-word (get registers register-position)
                  bit-index
                  (+ bit-index word-size))
        ;; When the word spans multiple registers, we need to fetch
        ;; them all one by one and then combine into a number. This is
        ;; quite cumbersome, but should happen fairly rarely, no more
        ;; often than `word-size/register-size`.
        ;; TBD: too lazy today
        (loop [start bit-index
               stop (math/dec (math/+ register-position register-size))
               processed-bits 0
               word 0]
          (if (math/>= start stop)
            word
            (recur start
                   stop
                   (math/+ (math/- stop start) processed-bits)
                   word)))
        )))


  clojure.lang.Seqable
  (seq [this]
    ;; (seq (zipmap (range (count words)) (vec words)))
    ))

(defn bit-vector
  [word count]
  (let [register-size 128
        registers-count (int (Math/ceil (/ (* word count) register-size)))
        registers (vec (repeatedly registers-count #(BitSet. register-size)))]
    (ImmutableBitVector. registers register-size word count)))

(defrecord Google [precision normal-precision sparse-list tmp-set]

  Estimator

  (cardinality
    [this]
    ))

(defrecord LazyMapBased [^int precision store ^long m ^double alpha]

  Estimator

  (cardinality
    [this]
    (let [empty-registers (math/- m (count store))
          estimator (/ (* alpha m m)
                       (+ empty-registers
                          (reduce + 1.0 (map (fn [r] (Math/pow 2 (math/- (long r)))) (vals store)))))]
      (cond
       (> estimator (* (Math/pow 2 32) (/ 1 30))) (large-range-correction estimator)
       (and (pos? empty-registers) (<= estimator (/ (* 5 m) 2))) (linear-count m empty-registers)
       :else estimator)))

  (add [this value]
    (let [value (long value)
          index (inc (bit-and value (bit-mask precision)))
          subvalue (math/inc (utils/lsb-position (math/>>> value precision)))]
      (assoc-in this [:store index] (byte (max (get store index 0) subvalue))))))

(defn lazy-map-based
  [precision]
  (let [m (long (Math/pow 2 precision))]
   (map->LazyMapBased {:precision (int precision)
                       :store {}
                       :m m
                       :alpha (compute-alpha m)})))
