(ns screen6.estimator.utils
  "Various utils for Estimators"
  (:require
   [primitive-math :as math]))

(def ^:private debruijn-bit-positions
  [0 1 28 2 29 14 24 3 30 22 20 15 25 17 4 8 31 27 13 23 21 19 16 7 26 12 18 6 11 5 10 9])

(defn lsb-position
  "Find the position of least significant bit (zero indexed).

  The solution stolen from http://www-graphics.stanford.edu/~seander/bithacks.html"
  [^long value]
  (if (math/zero? value)
    -1
    (let [position (-> (math/bit-and value (math/- value))
                       (math/* 0x077CB531)
                       (math/int->uint)
                       (math/>> 27))]
      (nth debruijn-bit-positions position))))
