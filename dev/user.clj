(ns user
  (:require
   [clojure.java.io :as io]
   [clojure.java.javadoc :refer (javadoc)]
   [clojure.pprint :refer (pprint cl-format)]
   [clojure.reflect :refer (reflect)]
   [clojure.repl :refer (apropos dir doc find-doc pst source)]
   [clojure.set :as set]
   [clojure.string :as str]
   [clojure.test :as test]
   [clojure.tools.namespace.repl :refer (refresh refresh-all)]
   [alembic.still :as alembic]
   [taoensso.nippy :as nippy]
   [io.screen6.cardinality.mixed :as mixed]
   [io.screen6.cardinality.streamlibhll :as streamlibhll]
   [io.screen6.cardinality.estimator :as e]))

(def add-dep alembic/distill)
(def clfmt cl-format)

(def reload-project alembic/load-project)

(def system
  "A Var containing an object representing the application under
development."
  nil)

(defn init
  [])

(defn start
  [])

(defn stop
  [])

(defn go
  "Initializes and starts the system running."
  []
  (init)
  (start)
  :ready)

(defn reset
  "Stops the system, reloads modified source files, and restarts it."
  []
  (stop)
  (refresh :after 'user/go))

(def traceback pst)

(defn populate
  [estimator n]
  (reduce e/present estimator (range n)))

(defn perfect-cutoff-for-mixed-mode
  [precision]
  (->> (iterate inc 1)
       (map (fn [n] (mapv (fn [estimator] (populate (estimator precision) n))
                         [mixed/estimator streamlibhll/estimator])))
       (filter (fn [[m h]] (> (count (nippy/freeze m)) (count (nippy/freeze h)))))
       (ffirst)
       (e/cardinality)))
