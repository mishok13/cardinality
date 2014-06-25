(ns io.screen6.cardinality.protocol)

(defprotocol EstimatorProtocol
  (present [this value]
    "Present a value to estimator.")
  (cardinality [this]
    "Return the cardinality of all presented values.")
  (union [this other]
    "Get a union of two estimators"))
