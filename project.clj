(defproject io.screen6/cardinality "0.1.0-SNAPSHOT"
  :description ""
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :profiles {:dev {:dependencies [[org.clojure/tools.namespace "0.2.4"]
                                  [alembic "0.2.1"]
                                  [net.agkn/hll "1.5.1"]
                                  [com.clearspring.analytics/stream "2.6.0"]
                                  [midje "1.6.3"
                                   :exclusions [org.clojure/clojure]]]
                   :source-paths ["dev"]}}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [immutable-bitset "0.1.6"]
                 [primitive-math "0.1.3"]
                 [com.google.guava/guava "16.0.1"]
                 [potemkin "0.3.4"]])
