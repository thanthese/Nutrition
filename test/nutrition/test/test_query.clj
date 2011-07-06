(ns nutrition.test.test-query
  (:use [clojure.test])
  (:require [nutrition.query :as q]))

(def carrot "11124")
(def squash "11953")
(def egg "01129")

(def protein {:nutrdesc "Protein", :units "g"})
(def fructose {:nutrdesc "Fructose", :units "g"})

(defn near [a b]
  (< (- a b) 0.001))

(deftest
  test-search-terms
  (is (= 2
         (count (q/search-terms "carrot, raw"))
         (count (q/search-terms "raw carrot:"))))
  (is (= 27 (count (q/search-terms "carrot"))))
  (is (let [first-desc (:long_desc (first (q/search-terms "carrot, raw")))]
        (re-find #"(?i)carrot" first-desc))))

(deftest
  test-food-nutrients
  (is (= 0.55 ((q/food-nutrients carrot) fructose)))
  (is (= 0.93 ((q/food-nutrients carrot) protein)))
  (is (= 2.71 ((q/food-nutrients squash) protein)))
  (is (= 12.58 ((q/food-nutrients egg) protein)))
  (is (= 3.64 ((q/food-nutrients carrot squash) protein)))
  (is (= 16.22 ((q/food-nutrients carrot squash egg) protein))))

(deftest
  test-scale-nutrients
  (let [scale q/scale-nutrients
        nuts q/food-nutrients]
    (is (near 0.0093 ((scale (nuts carrot) 0.01) protein)))
    (is (near 0.093 ((scale (nuts carrot) 0.1) protein)))
    (is (= 0.93 ((scale (nuts carrot) 1) protein)))
    (is (= 9.3 ((scale (nuts carrot) 10) protein)))
    (is (= 162.2 ((scale (nuts carrot squash egg) 10) protein)))))
