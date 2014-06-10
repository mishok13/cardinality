(ns io.screen6.estimators.hll
  "Naive HyperLogLog++ implementation")

(defn show
  "Present cardinality estimator with a value"
  [estimator value])

(defn cardinality
  "Given an estimator, provide a numeric representation of cardinality
  of values seen by estimator"
  [estimator])
