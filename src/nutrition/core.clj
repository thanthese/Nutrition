(ns nutrition.core
  (:use [clojure.pprint :only (pprint)])
  (:require [clojure.string :as str])
  (:require [clojure.contrib.sql :as sql])
  (:require [nutrition.db-def :as db])
  (:gen-class))

(defn query [select-stmt]
  (sql/with-connection
    db/db
    (sql/with-query-results
      rs [select-stmt]
      (doall rs))))

(defn pretty-table [results]
  (do
    (doseq [row results]
      (println (interpose "|"(vals row))))
    (println "Total: " (count results))))

(defn search-term [search-term]
  (query (format "select food.NDB_No, grp.fdgrp_desc, food.Long_Desc
                 from food_description as food,
                   food_group as grp
                 where food.fdgrp_cd = grp.fdgrp_cd
                   and lower(food.Long_Desc) like '%%%s%%'"
                 (str/lower-case search-term))))

(defn food-nutrients-list [ndb-no]
  (query (format "select data.nutr_val,
                   def.units, def.nutrdesc
                 from nutrient_data as data, nutrient_definition as def
                 where data.nutr_no = def.nutr_no
                   and data.ndb_no = '%s'
                   and data.nutr_val > 0
                 order by data.nutr_val"
                 ndb-no)))

(defn food-nutrients-map
  "Transforms nutrients list into a single map. This makes merging different
  nutrient maps easier."
  [food-nutrients-list]
  (apply merge (map (fn [{:keys [nutrdesc units nutr_val]}]
                      {{:nutrdesc nutrdesc :units units} nutr_val})
                    food-nutrients-list)))

(defn food-nutrients [& ndb-nos]
  (apply (partial merge-with +)
         (map (comp food-nutrients-map food-nutrients-list)
              ndb-nos)))

(defn scale-nutrients [food-nutrients scale-factor]
  (apply merge (map (fn [[k v]]
                      {k (* scale-factor v)})
                    food-nutrients)))

(def carrot "11124")
(def squash "11953")
(def egg "01129")
(def protein {:nutrdesc "Protein", :units "g"})

(defn -main [& args]
  (do
    (println "Show types of carrot!")
    (pretty-table (search-term "carrot"))
    (println "Show me what's in 100g of raw carrot!")
    (pprint (food-nutrients carrot))
    (println "Protein in 100g carrot:"
             ((food-nutrients carrot) protein))
    (println "Protein in 100g squash:"
             ((food-nutrients squash) protein))
    (println "Protein in 100g carrot + 100g squash:"
             ((food-nutrients carrot squash) protein))
    (println "Protein in 100g each of carrot + squash + egg:"
             ((food-nutrients carrot squash egg) protein))
    (println "Protein in 1g, 10g, 100g, 1000g of carrot:"
             ((scale-nutrients (food-nutrients carrot) 0.01) protein)
             ((scale-nutrients (food-nutrients carrot) 0.1) protein)
             ((scale-nutrients (food-nutrients carrot) 1) protein)
             ((scale-nutrients (food-nutrients carrot) 10) protein))))
