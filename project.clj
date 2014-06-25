(defproject io.screen6/cardinality "0.1.0-SNAPSHOT"
  :description "Cardinality estimation API"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :profiles {:1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :dev {:plugins [[lein-midje "3.1.1"]]
                   :dependencies [[midje "1.6.3"]]}}
  :dependencies [[org.clojure/clojure "1.5.1"]])
