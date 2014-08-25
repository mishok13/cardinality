(ns io.screen6.cardinality.hll
  "Pure Clojure implementation of HyperLogLog algorithm"
  (:require
   [io.screen6.cardinality.estimator :as estimator]))

(defrecord Estimator
    []

  estimator/EstimatorProtocol

  (present [this value]
    this)
  (cardinality [this] 0)
  (union [this other] this))

(defn estimator
  [precision]
  (->Estimator nil nil))
