(ns io.screen6.estimators.hll-test
  (:require
   [midje.sweet :refer :all]
   [io.screen6.estimators.hll :refer :all]))

(tabular
 (fact
  (reduce show ?values) => (roughly ?cardinality))
 ?values ?cardinality
 [] 0
 (range 10) 10
 (range 1000) 1000)
