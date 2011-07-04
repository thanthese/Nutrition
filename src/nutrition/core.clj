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

(defn food-nutrients [ndb_no]
  (query (format "select data.nutr_val,
                   def.units, def.nutrdesc
                 from nutrient_data as data, nutrient_definition as def
                 where data.nutr_no = def.nutr_no
                   and data.ndb_no = '%s'
                   and data.nutr_val > 0
                 order by data.nutr_val"
                 ndb_no)))

(defn -main [& args]
  (do
    (println "Show me the carrots!")
    (pretty-table (search-term "carrot"))
    (println "Show me what's in 100g of raw carrots!")
    (pretty-table (food-nutrients "11124"))))
