(ns nutrition.query
  (:require [clojure.string :as str])
  (:require [clojure.contrib.sql :as sql])
  (:require [nutrition.db-def :as db]))

(defn- query [select-stmt]
  (sql/with-connection
    db/db
    (sql/with-query-results
      rs [select-stmt]
      (doall rs))))

(defn- like-clause [field search-terms]
  (str/join " and " (map #(str field " like '%" % "%'")
                         (-> search-terms
                           (str/lower-case)
                           (str/replace #"[^a-z ]" "")
                           (str/split #" ")))))

(defn search-terms [search-terms]
  (query (str "select food.NDB_No, grp.fdgrp_desc, food.Long_Desc
              from food_description as food,
                food_group as grp
              where food.fdgrp_cd = grp.fdgrp_cd
                and " (like-clause "lower(food.Long_Desc)" search-terms))))

(defn- food-nutrients-list [ndb-no]
  (query (format "select data.nutr_val,
                   def.units, def.nutrdesc
                 from nutrient_data as data, nutrient_definition as def
                 where data.nutr_no = def.nutr_no
                   and data.ndb_no = '%s'
                   and data.nutr_val > 0
                 order by data.nutr_val"
                 ndb-no)))

(defn- food-nutrients-map
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

(defn- fmap [f m]
  (apply merge (map (fn [[k v]]
                      {k (f v)})
                    m)))

(defn scale-nutrients [food-nutrients scale-factor]
  (fmap (partial * scale-factor) food-nutrients))
