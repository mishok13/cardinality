(ns io.screen6.cardinality.mixed-test
  (:require
   [midje.sweet :refer :all]
   [io.screen6.cardinality.mixed :refer :all]
   [io.screen6.cardinality.estimator :refer [present cardinality union]]))

(tabular
 (fact
  "Mixed mode operates correctly"
  (let [estimator (reduce present ?estimator ?values)]
    (cardinality estimator) => (roughly ?result (* ?error ?result))
    (:mode estimator) => ?mode))
 ?estimator ?values ?result ?error ?mode
 (estimator 12) nil 0 0.0 :set
 (estimator 12) (range 400) 400 0.0 :set
 (estimator 12) (range 500) 500 0.025 :hll
 (estimator 12) (range 100000) 100000 0.025 :hll
 (estimator 15) (range 100000) 100000 0.025 :hll
 (estimator 12) (take 100000 (cycle (range 10000))) 10000 0.05 :hll)

(tabular
 (fact
  "Mixed mode estimator handles union correctly"
  (let [estimator (union ?left ?right)]
    (cardinality estimator) => (roughly ?result (* ?error ?result))
    (:mode estimator) => ?mode))
 ?left ?right ?result ?error ?mode
 (estimator 10) (estimator 10) 0 0.0 :set
 (reduce present (estimator 10) (range 1000)) (reduce present (estimator 10) (range 100)) 1000 0.025 :hll
 (reduce present (estimator 10) (range 1000)) (reduce present (estimator 10) (range 1000 1100)) 1100 0.025 :hll)
