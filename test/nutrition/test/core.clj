(ns nutrition.test.core
  (:use [nutrition.core])
  (:use [clojure.test]))

;;; The following tests depend on /resources ;;;

(deftest
  test-sample-carrot-query
  (is (= (count (sample-carrot-query)) 13))
  (is (= (last (sample-carrot-query))
         ["11960" "CARROTS,BABY,RAW"])))
