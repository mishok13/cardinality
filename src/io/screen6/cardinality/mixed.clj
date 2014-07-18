(ns io.screen6.cardinality.mixed
  "Mixed-mode HyperLogLog implementation.

  Mixed mode estimator will attempt to use built-in Clojure set, then
  fallback to HyperLogLog implementation."
  (:require
   [io.screen6.cardinality.estimator :as e]
   [io.screen6.cardinality.streamlibhll :as hll]))

(defn ^:private set->hll
  "Convert Clojure set into HyperLogLog instance"
  [coll precision]
  (reduce e/present (hll/estimator precision) coll))

(defn ^:private precision->max-cardinality
  [precision]
  (get {5 58, 6 68, 7 90, 8 127, 9 165, 10 214, 11 295, 12 412, 13 623, 14 1038, 15 1866, 16 3494}
       precision))

(defn upgrade
  [mode estimator precision]
  (case mode
    :set (if (> (count estimator) (precision->max-cardinality precision))
           [:hll (set->hll estimator precision)]
           [:set estimator])
    :hll [:hll estimator]
    nil [:set #{}]))

(extend-protocol e/EstimatorProtocol
  nil
  (present [_ value] #{value})
  (cardinality [_] 0)
  (union [_ other] other)
  clojure.lang.IPersistentSet
  (present [this value] (conj this value))
  (cardinality [this] (count this))
  (union [this other]
    (prn this other)
    (assert (or (set? other) (nil? other)))
    (clojure.set/union this other)))

(defrecord MixedEstimator [estimator mode precision]

  e/EstimatorProtocol

  (present [this value]
    (let [[mode estimator] (upgrade mode estimator precision)]
      (assoc this :estimator (e/present estimator value) :mode mode)))

  (cardinality [this]
    (e/cardinality estimator))

  (union [this other]
    (case [mode (:mode other)]
      [:hll :hll] (assoc this :estimator (e/union estimator (:estimator other)))
      [:hll :set] (assoc this :estimator (e/union estimator (set->hll (:estimator other) precision)))
      [:set :hll] (assoc this :estimator (e/union (set->hll estimator precision) (:estimator other))
                         :mode :hll)
      [:set :set] (let [[mode estimator] (upgrade mode (e/union (:estimator this) (:estimator other)) precision)]
                    (assoc this :estimator estimator :mode mode)))))

(defn estimator
  [precision]
  (->MixedEstimator nil :set precision))
