(ns nutrition.test.test-query
  (:use [clojure.test])
  (:require [nutrition.query :as q])
  (:require [nutrition.ideal-diet :as i]))

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

(def pro "Protein")
(def cal "Calcium, Ca")

(deftest
  test-recipe
  (let [nutr-val (fn [nutrdesc nutrients]
                   (:nutr_val (q/nutrient nutrdesc nutrients)))]
    (is (=  93.0  (nutr-val pro (q/recipe carrot 10000))))
    (is (=   9.3  (nutr-val pro (q/recipe carrot  1000))))
    (is (=   0.93 (nutr-val pro (q/recipe carrot   100))))
    (is (= 500.0  (nutr-val cal (q/recipe egg     1000))))
    (is (=  50.0  (nutr-val cal (q/recipe egg      100))))
    (is (= (+ 0.093 0.271 1.258)
           (nutr-val pro (q/recipe carrot   10 egg 10 squash 10))))
    (is (= (+ 0.93 0.271 1.258)
           (nutr-val pro (q/recipe carrot  100 egg 10 squash 10))))
    (is (= (+ 9.3 0.271 0.1258)
           (nutr-val pro (q/recipe carrot 1000 egg  1 squash 10))))))

(deftest
  test-nutrient
  (is (= {:nutrdesc pro, :units "g", :nutr_val 9.3}
         (q/nutrient pro (q/recipe carrot 1000))))
  (is (= {:nutrdesc cal, :units "mg", :nutr_val 210.0}
         (q/nutrient cal (q/recipe squash 1000)))))

(defn- =ish? [tolerance a b]
  (let [diff (- a b)]
    (and (< diff tolerance)
         (> diff (- tolerance)))))

(deftest
  test-score-recipe
  (let [recipe-percent (fn [nutrdesc recipe]
                         (->> (q/score-recipe recipe i/active-male-ideal)
                           (q/nutrient nutrdesc)
                           :percent))
        carr-recipe (q/recipe carrot 1000)
        eggs-recipe (q/recipe egg 1000)
        comb-recipe (q/recipe carrot 100 egg 100 squash 100)]
    (is (=ish? 1  16 (recipe-percent pro carr-recipe)))
    (is (=ish? 1  33 (recipe-percent cal carr-recipe)))
    (is (=ish? 1 220 (recipe-percent pro eggs-recipe)))
    (is (=ish? 1  50 (recipe-percent cal eggs-recipe)))
    (is (=ish? 1  28 (recipe-percent pro comb-recipe)))
    (is (=ish? 1  10 (recipe-percent cal comb-recipe)))))

(deftest
  test-foods-rich-in
  (is (= (first (q/foods-rich-in "Fluoride, F"))
         {:nutr_val 584.000M,
          :long_desc "Tea, instant, sweetened with sugar, lemon-flavored, without added ascorbic acid, powder"})))

