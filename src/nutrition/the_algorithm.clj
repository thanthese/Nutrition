(ns nutrition.the-algorithm
  (:require [nutrition.query :as q])
  (:require [nutrition.ideal-diet :as i])
  (:require [nutrition.common-foods :as c]))

(defn- fitness [score]
  (let [number (count score)
        percents-sum (apply + (map :percent score))]
    (/ percents-sum number)))

(defn- score-against-active-male-ideal [recipe]
  (q/score-recipe recipe i/active-male-ideal))

(defn- initial-quantities [ndb_nos]
  (vec (map (fn [n]
              [n 0])
            ndb_nos)))

(defn- quantities-fitness [quantities]
  (->> quantities
    flatten
    (apply q/recipe)
    score-against-active-male-ideal
    fitness))

(defn- branches [quantities]
  (map (fn [i]
         (let [qtys (update-in quantities [i 1] inc)]
           {:quantities qtys
            :fitness (quantities-fitness qtys)}))
       (range (count quantities))))

(defn evolve [& ndb_nos]
  (loop [quantities (initial-quantities ndb_nos)]
    (let [winning-br (apply max-key :fitness (branches quantities))]
      (if (> (:fitness winning-br) 10)
        winning-br
        (recur (:quantities winning-br))))))
