(ns io.screen6.cardinality.streamlibhll-test
  (:require
   [midje.sweet :refer :all]
   [io.screen6.cardinality.streamlibhll :refer :all]
   [io.screen6.cardinality.estimator :refer [present cardinality union]]))

(tabular
 (fact
  "HyperLogLog correctly estimates cardinality"
  (cardinality (reduce present ?estimator ?values)) => (roughly ?result (* ?error ?result)))
 ?estimator ?values ?result ?error
 (estimator 12) nil 0 0.0
 (estimator 12) (range 100000) 100000 0.025
 (estimator 15) (range 100000) 100000 0.025
 (estimator 12) (take 100000 (cycle (range 10000))) 10000 0.05)

(fact
 "Union doesn't mutate existing values"
 (let [left (reduce present (estimator 12) (range 100000))
       right (reduce present (estimator 12) (range 100000 200000))
       expected-cardinality (+ (cardinality left) (cardinality right))]
   (cardinality (union left right)) => (roughly expected-cardinality 1000)
   (cardinality left) => (roughly 100000 2500)
   (cardinality right) => (roughly 100000 2500)))

(tabular
 (fact
  "Unexpected values are handled correctly in union"
  (union ?this ?other) => (throws AssertionError))
 ?this ?other
 (estimator 12) nil
 (reduce present (estimator 12) (range 1000)) nil
 (estimator 12) (estimator 14))
