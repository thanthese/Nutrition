(ns nutrition.the-algorithm
  (:require [nutrition.query :as q])
  (:require [nutrition.ideal-diet :as i])
  (:require [nutrition.common-foods :as c]))

(defn- score-against-active-male-ideal [recipe]
  (q/score-recipe recipe i/active-male-ideal))

(defn- fitness [score]
  (let [number (count score)
        percents-sum (apply + (map :percent score))]
    (/ percents-sum number)))

(defn- quantities-fitness [quantities]
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
         (let [qtys (update-in quantities [i 1] inc)]
           {:quantities qtys
            :fitness (quantities-fitness qtys)}))
       (range (count quantities))))

(defn- fittest-branch [branches]
  (apply max-key :fitness branches))

(defn- met-end-condition [branch]
  (> (:fitness branch) 10))

(defn evolve [& ndb_nos]
  (loop [quantities (initial-quantities ndb_nos)]
    (let [winning-br (fittest-branch (branches quantities))]
      (if (met-end-condition winning-br)
        winning-br
        (recur (:quantities winning-br))))))
