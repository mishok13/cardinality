(defproject io.screen6/cardinality "0.1.0-SNAPSHOT"
  :description "Cardinality estimation API"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :url "https://github.com/screen6/cardinality"
  :profiles {:1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
             :dev {:plugins [[lein-midje "3.1.1"]]
                   :dependencies [[midje "1.6.3"]
                                  [org.clojure/tools.namespace "0.2.5"]
                                  [com.taoensso/nippy "2.6.3"]]}}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [com.clearspring.analytics/stream "2.7.0"]])
