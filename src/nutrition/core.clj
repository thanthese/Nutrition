(ns nutrition.core
  (:use [clojure.pprint :only (pprint)])
  (:require [nutrition.query :as q])
  (:gen-class))

(defn pretty-table [results]
  (do
    (doseq [row results]
      (println (interpose "|" (vals row))))
    (println "Total: " (count results))))

(def carrot "11124")
(def squash "11953")
(def egg "01129")
(def protein {:nutrdesc "Protein", :units "g"})

(defn -main [& args]
  (do
    (println "Show types of carrot!")
    (pretty-table (q/search-terms "carrot"))
    (println "Show types of raw carrots!")
    (pretty-table (q/search-terms "raw carrot"))
    (println "Show me what's in 100g of raw carrot!")
    (pprint (q/food-nutrients carrot))
    (println "Protein in 100g carrot:"
             ((q/food-nutrients carrot) protein))
    (println "Protein in 100g squash:"
             ((q/food-nutrients squash) protein))
    (println "Protein in 100g carrot + 100g squash:"
             ((q/food-nutrients carrot squash) protein))
    (println "Protein in 100g each of carrot + squash + egg:"
             ((q/food-nutrients carrot squash egg) protein))
    (println "Protein in 1g, 10g, 100g, 1000g of carrot:"
             ((q/scale-nutrients (q/food-nutrients carrot) 0.01) protein)
             ((q/scale-nutrients (q/food-nutrients carrot) 0.1) protein)
             ((q/scale-nutrients (q/food-nutrients carrot) 1) protein)
             ((q/scale-nutrients (q/food-nutrients carrot) 10) protein))))
