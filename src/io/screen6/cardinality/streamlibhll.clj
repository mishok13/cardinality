(ns io.screen6.cardinality.streamlibhll
  "Clojure API wrapper around AddThis' stream-lib HyperLogLog implementation"
  (:require
   [io.screen6.cardinality.estimator :as estimator])
  (:import
   [com.clearspring.analytics.stream.cardinality HyperLogLog])
  (:refer-clojure :exclude [merge]))

(defrecord Estimator [hll precision]

  estimator/EstimatorProtocol

  (present [this value]
    (assert hll "HyperLogLog instance is nil")
    ;; Note that since HyperLogLog is a mutable object we have to rely
    ;; on consumers of this API to be careful.
    (.offer hll value)
    this)

  (cardinality [this]
    (assert hll "HyperLogLog instance is nil")
    (.cardinality hll))

  (union [this other]
    (assert (instance? Estimator other) (format "Can't union with %s" other))
    (assert (= (:precision this) (:precision other)) "Precision mismatch")
    (assoc this :hll (doto (HyperLogLog. precision)
                       (.addAll (:hll this))
                       (.addAll (:hll other))))))

(defn estimator
  "Create HyperLogLog estimator with a given precision"
  [precision]
  (->Estimator (HyperLogLog. precision) precision))
