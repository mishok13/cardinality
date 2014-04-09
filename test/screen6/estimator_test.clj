(ns screen6.estimator-test
  (:require [midje.sweet :refer :all]
            [screen6.estimator :refer :all]))

(fact
 "Estimator is correct"
 (cardinality* (reduce add* (hll) (range 10))) => 10
 (cardinality* (reduce add* (hll) (range 101))) => 101
 ;; (cardinality* (reduce add* (hll) (range 102))) => 102
 ;; (cardinality* (reduce add* (hll) (range 1000))) => 1000
 )
