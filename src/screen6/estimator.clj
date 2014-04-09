(ns screen6.estimator
  (:require
   [screen6.estimator.store :as store])
  (:import
   [com.google.common.hash Hashing]))

(def ^:private algorithms [::empty ::explicit ::sparse ::full])

(defprotocol EstimatorProtocol
  ;; rename this later on to conj/disj/etc
  (add* [this value])
  (remove* [this value])
  (merge* [this other])
  (cardinality* [this]))

(defn encode-value
  [^long value precision sparse-precision]
  (let [higher-bits-mask ]))

(defrecord HyperLogLog [precision sparse-precision]

  EstimatorProtocol

  (add* [this value]
    (case algorithm
      ::empty (-> this
                  (assoc :storage #{value})
                  (assoc :algorithm ::explicit))
      ::explicit (if (> (count storage) explicit-threshold)
                   ;; promote to sparse
                   (-> this
                       (assoc :storage (reduce store/add (store/sparse) storage))
                       (assoc :algorithm ::sparse)
                       (add* value))
                   ;; just add value
                   (assoc this :storage (conj storage value)))
      ::sparse (if (> (count storage) explicit-threshold)
                 ;; promote to sparse
                 (-> this
                     (assoc :storage (reduce store/add (store/sparse) storage))
                     (assoc :algorithm ::sparse)
                     (add* value))
                 ;; just add value
                 (assoc this :storage (conj storage value)))))

  (cardinality* [this]
    (case algorithm
      ::empty 0
      ::explicit (count storage)
      ::sparse (store/cardinality storage))))

(defn hll
  []
  (->HyperLogLog 0 0 ::empty nil 100))

(defprotocol HashableValue
  (hash64 [this]
    "Hash the value into 64-bit long"))

(def ^:private sip24hasher
  (Hashing/sipHash24))

(def ^:private str-encoding
  (java.nio.charset.Charset/forName "UTF-8"))

(extend-protocol HashableValue

  String
  (hash64
    [this]
    (.asLong (.hashString sip24hasher this str-encoding)))

  Long
  (hash64
    [this]
    (.asLong (.hashLong sip24hasher this))))

(defn offer
  [estimator value]
  (add* estimator (hash64 value)))
