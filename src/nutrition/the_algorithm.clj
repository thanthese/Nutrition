(ns nutrition.the-algorithm
  (:require [clojure.contrib.math :as math])
  (:require [nutrition.query :as q])
  (:require [nutrition.ideal-diet :as i])
  (:require [nutrition.common-foods :as c]))

(defn- average [nums]
  (/ (apply + nums) (count nums)))

(defn- standard-deviation [nums]
  (let [mean (average nums)]
    (math/sqrt (average (map (fn [n]
                               (let [diff (- n mean)]
                                 (* diff diff)))
                             nums)))))

(defn- score-against-active-male-ideal [recipe]
  (q/score-recipe recipe i/active-male-ideal))

(defn- fitness [score]
  (standard-deviation (map :percent score)))

(defn- quantities->fitness [quantities]
  (->> quantities
    flatten
    (apply q/recipe)
    score-against-active-male-ideal
    fitness))

(defn- initial-quantities [ndb_nos]
  (vec (map (fn [n]
              [n 0])
            ndb_nos)))

(defn- branches [quantities]
  (map (fn [i]
         (let [qtys (update-in quantities [i 1] (partial + 20))]
           {:quantities qtys
            :fitness (quantities->fitness qtys)}))
       (range (count quantities))))

(defn- fittest-branch [branches]
  (do
    (println "options: " (map (comp int :fitness) branches))
    (apply min-key :fitness branches)))

(defn- end-condition? [branch]
  (some (fn [[ingredient amount]]
          (> amount 5000))
        (:quantities branch)))

(defn evolve [& ndb_nos]
  (loop [quantities (initial-quantities ndb_nos)]
    (let [winning-br (fittest-branch (branches quantities))]
      (do
        (println (:fitness winning-br))
        (println (map second (:quantities winning-br)))
        (if (end-condition? winning-br)
        winning-br
        (recur (:quantities winning-br)))))))
