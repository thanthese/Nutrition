(ns nutrition.the-algorithm
  (:require [clojure.contrib.math :as math])
  (:require [nutrition.query :as q])
  (:require [nutrition.ideal-diet :as i])
  (:require [nutrition.common-foods :as c]))

(defn iterate-times [f times initial-arg]
  (reduce (fn [previous-result _]
            (f previous-result))
          initial-arg
          (repeat times nil)))

(defn multiply-nodes [factor nodes]
  (reduce
    (fn [acc node]
      (-> acc
        (conj (conj node 0))
        (conj (conj node factor))
        (conj (conj node (- factor)))))
    []
    nodes))

(defn every-combination [length factor]
  (iterate-times (partial multiply-nodes factor)
                 length
                 [[]]))

(defn- standard-deviation [nums]
  (let [average (fn [ns]
                  (/ (apply + ns)
                     (count nums)))
        mean (average nums)]
    (math/sqrt (average (map (fn [n]
                               (let [diff (- n mean)]
                                 (* diff diff)))
                             nums)))))

(defn- score-against-active-male-ideal [recipe]
  (q/score-recipe recipe i/active-male-ideal))

(defn- fitness [score]
  (standard-deviation (conj (map :percent score) 500)))

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

(comment (defn- branches [quantities]
  (map (fn [i]
         (let [qtys (update-in quantities [i 1] (partial + 20))]
           {:quantities qtys
            :fitness (quantities->fitness qtys)}))
       (range (count quantities)))))

(defn- branches [quantities factor]
  (filter
    (fn [branch]
      (every? (comp not neg?) (map second (:quantities branch))))
    (map (fn [diffs]
           (let [qtys (map (fn [[ndb_no grams] diff]
                             [ndb_no (+ grams diff)])
                           quantities
                           diffs)]
             {:quantities qtys
              :fitness (quantities->fitness qtys)} ))
         (every-combination (count quantities) factor))))

(defn- fittest-branch [branches]
  (apply min-key :fitness branches))

(defn- end-condition? [branch]
  (some (fn [[ingredient amount]]
          (> amount 5000))
        (:quantities branch)))

(comment (defn evolve [& ndb_nos]
  (loop [quantities (initial-quantities ndb_nos)]
    (let [winning-br (fittest-branch (branches quantities))]
      (do
        (println (:fitness winning-br))
        (println (map second (:quantities winning-br)))
        (if (end-condition? winning-br)
          winning-br
          (recur (:quantities winning-br))))))))

(defn evolve [& ndb_nos]
  (let [qtys (reduce
               (fn [quantities factor]
                 (let [winning-br (fittest-branch (branches quantities factor))]
                   (do
                     (println (:fitness winning-br))
                     (println (map second (:quantities winning-br)))
                     (:quantities winning-br) )))
               (initial-quantities ndb_nos)
               [1000 500 250 125 62 31 15 7 3 1])]
    {:quantities qtys
     :fitness (quantities->fitness qtys)}))
