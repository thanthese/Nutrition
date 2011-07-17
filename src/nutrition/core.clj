(ns nutrition.core
  (:use [clojure.pprint :only (pprint)])
  (:require [nutrition.query :as q])
  (:require [nutrition.ideal-diet :as i])
  (:require [nutrition.common-foods :as c])
  (:require [nutrition.the-algorithm :as the-alg])
  (:gen-class))

(defn pad [d]
  (format "%4d" d))

(defn common-name [ndb-no]
  (key (first (filter (fn [food]
                        (= ndb-no (:ndb_no (val food))))
                      c/common-foods))))

(defn ndb-no [common-name-key]
  (:ndb_no (common-name-key c/common-foods)))

(defn pretty-search-results [results]
  (do
    (doseq [row results]
      (println (interpose "|" (vals row))))
    (println "Total: " (count results))))


(defn pretty-score [score]
  (doseq [category [:macronutrients :minerals :vitamins]]
    (println category)
    (doseq [row (->> score
                  (filter #(= (:category %)
                              category))
                  (sort-by :percent))]
      (println " " (pad (int (:percent row))) "% " (:nutrdesc row)))))

(defn pretty-winning-branch [branch]
  (do
    (println "Winning branch results:")
    (doseq [[ndb-no grams] (:quantities branch)]
      (println " " (pad grams) "g " (common-name ndb-no)))
    (println "Fitness:" (int (:fitness branch)))))

(def find-food (comp pretty-search-results q/search-results))

(defn helper-score [& common-name-key-and-grams]
  (pretty-score
    (q/score-recipe
      (apply q/recipe
             (flatten (map (fn [[ingredient amount]]
                             [(:ndb_no (ingredient c/common-foods)) amount])
                           (partition 2 common-name-key-and-grams))))
      i/active-male-ideal)))

(defn -main [& args]
  (do
    (time (let [f c/common-foods
                ingredients (map :ndb_no (vals c/common-foods))
                winning-branch (apply the-alg/evolve ingredients)]
            (do
              (pretty-score (q/score-recipe
                              (apply q/recipe (flatten (:quantities winning-branch)))
                              i/active-male-ideal))
              (pretty-winning-branch winning-branch))))
    (println "Sample recipe score:")
    (helper-score
      :flaxseed 100
      :apple 100
      :banana 100
      :rice 1000
      :quinoa 1000
      :olive-oil 10
      :spinach 100
      :broccoli 100
      :walnut 100
      :egg 100)))

