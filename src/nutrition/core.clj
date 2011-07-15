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

(def find-food (comp pretty-search-results q/search-results))
(def pick (comp :ndb_no first q/search-results))

(def carrot (pick "carrot raw"))
(def squash (pick "squash zucchini baby raw"))
(def egg (pick "whole egg boiled"))

(def protein {:nutrdesc "Protein", :units "g"})

(defn -main [& args]
  (do
    (println "Show types of carrot!")
    (pretty-search-results (q/search-results "carrot"))
    (println "Show types of raw carrots!")
    (pretty-search-results (q/search-results "raw carrot"))
    (println "Show me what's in a recipe of 100g each of carrot, squash, and egg!")
    (pprint (q/recipe carrot 100 squash 100 egg 100))))
