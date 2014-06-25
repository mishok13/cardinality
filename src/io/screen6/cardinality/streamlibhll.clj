(ns io.screen6.cardinality.streamlibhll
  "Clojure API wrapper around AddThis' stream-lib HyperLogLog implementation"
  (:require
   [io.screen6.cardinality.protocol :as p])
  (:import
   [com.clearspring.analytics.stream.cardinality HyperLogLog])
  (:refer-clojure :exclude [merge]))

(defrecord Estimator [hll]

  p/EstimatorProtocol
  (present [this value]
    )
  (cardinality [this]
    )
  (union [this other]
    ))

(defn estimator
  "Create HyperLogLog estimator with a given precision"
  [precision])
