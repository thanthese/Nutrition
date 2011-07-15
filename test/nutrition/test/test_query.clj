(ns nutrition.test.test-query
  (:use [clojure.test])
  (:require [nutrition.query :as q]))

(deftest
  test-search-results
  (is (= 27 (count (q/search-results "carrot"))))
  (is (= 2
         (count (q/search-results "carrot, raw"))
         (count (q/search-results "raw carrot:"))
         (count (q/search-results "rAw CaRrOt:"))))
  (is (re-find #"(?i)carrot"
               (:long_desc (first (q/search-results "carrot, raw"))))))

(def carrot "11124")
(def squash "11953")
(def egg "01129")

(defn nutrient-val [nutrients nutrdesc]
  (:nutr_val (first (filter #(= (:nutrdesc %)
                                nutrdesc)
                            nutrients))))

(deftest
  test-recipe
  (is (=  93.0  (nutrient-val (q/recipe carrot 10000) "Protein")))
  (is (=   9.3  (nutrient-val (q/recipe carrot  1000) "Protein")))
  (is (=   0.93 (nutrient-val (q/recipe carrot   100) "Protein")))
  (is (= 500.0  (nutrient-val (q/recipe egg     1000) "Calcium, Ca")))
  (is (=  50.0  (nutrient-val (q/recipe egg      100) "Calcium, Ca")))
  (is (= (+ 0.093 0.271 1.258)
         (nutrient-val (q/recipe carrot   10 egg 10 squash 10) "Protein")))
  (is (= (+ 0.93 0.271 1.258)
         (nutrient-val (q/recipe carrot  100 egg 10 squash 10) "Protein")))
  (is (= (+ 9.3 0.271 0.1258)
         (nutrient-val (q/recipe carrot 1000 egg  1 squash 10) "Protein"))))
