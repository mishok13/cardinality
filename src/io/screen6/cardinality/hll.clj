(ns io.screen6.cardinality.hll
  "Pure Clojure implementation of HyperLogLog algorithm"
  (:require
   [io.screen6.cardinality.estimator :as estimator]))

(defn- get-bits
  [n start stop])

(defn- hash32
  [x])

(defn- leading-zeros
  [x])

(defrecord Estimator [registers alpha m p]
  estimator/EstimatorProtocol
  (present [this value]
    (let [hash (hash32 value)
          index (get-bits hash 0 p)
          bits (get-bits hash p 32)]
      (assoc-in this [:registers index]
                (max (get-in this [:registers index] 0) (leading-zeros bits)))))
  (cardinality [this] 0)
  (union [this other] this))

(defn estimator
  [precision]
  (->Estimator nil nil))
