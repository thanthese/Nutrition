(ns nutrition.core
  (:use [clojure.pprint :only (pprint)])
  (:require [nutrition.query :as q])
  (:require [nutrition.ideal-diet :as i])
  (:gen-class))

(defn pretty-search-results [results]
  (do
    (doseq [row results]
      (println (interpose "|" (vals row))))
    (println "Total: " (count results))))

(defn pretty-score [score]
  (doseq [nutrient-type [:macronutrients :minerals :vitamins]]
    (println nutrient-type)
    (doseq [row (->> score
                  (filter #(= (:category %) nutrient-type))
                  (sort-by :percent))]
      (println " " (int (:percent row)) "% " (:nutrdesc row)))))

(def find-food (comp pretty-search-results q/search-results))
(def pick (comp :ndb_no first q/search-results))

(def carrot (pick "carrot raw"))
(def squash (pick "squash zucchini baby raw"))
(def egg (pick "whole egg boiled"))
(def oil (pick "olive oil"))

(defn fitness [score]
  (let [number (count score)
        percents-sum (apply + (map :percent score))]
    (/ percents-sum number)))

(defn -main [& args]
  (do
    (println "Nurition for recipe: 100g carrot, 100g egg, 100g oil")
    (pretty-score (q/score-recipe (q/recipe carrot 100 egg 100 oil 100)
                                  i/active-male-ideal))
    (println "Weak solution:")
    (time (pprint
            (let [ingredients [carrot oil squash egg]]
              (let [branch-fitness (fn [branch]
                                     (fitness (q/score-recipe (apply q/recipe (flatten branch))
                                                              i/active-male-ideal)))]
                (loop [quantities (vec (map (fn [n] [n 0]) ingredients))]
                  (let [branches (map-indexed (fn [i _]
                                                (update-in quantities [i 1] inc))
                                              quantities)
                        winning-branch (apply max-key branch-fitness
                                              branches)]
                    (if (> (branch-fitness winning-branch) 100)
                      [winning-branch (branch-fitness winning-branch)]
                      (recur winning-branch))))))))))

;;;;

(comment (pprint (q/score-recipe (q/recipe carrot 100 egg 100 oil 100)
                        i/active-male-ideal)))

(comment (pretty-score (q/score-recipe (q/recipe carrot 460)
                         i/active-male-ideal)))
