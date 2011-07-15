(ns nutrition.query
  (:require [clojure.string :as str])
  (:require [clojure.contrib.sql :as sql])
  (:require [nutrition.db-def :as db]))

(defn- query [select-stmt]
  (sql/with-connection db/db
                       (sql/with-query-results rs [select-stmt]
                                               (doall rs))))

(defn search-results [search-terms]
  (query (str "select food.NDB_No, grp.fdgrp_desc, food.Long_Desc
              from food_description as food, food_group as grp
              where food.fdgrp_cd = grp.fdgrp_cd
              and "
              (str/join " and "
                        (map #(str " lower(food.Long_Desc) like '%" % "%'")
                             (-> search-terms
                               (str/lower-case)
                               (str/replace #"[^a-z ]" "")
                               (str/split #" ")))))))

(defn- food-nutrients [ndb-no]
  (query (str "select def.nutrdesc, def.units, data.nutr_val
              from nutrient_data as data, nutrient_definition as def
              where data.nutr_no = def.nutr_no
              and data.ndb_no = '" ndb-no "'")))

(defn- scale-nutrients [food-nutrients scale-factor]
  (map #(update-in % [:nutr_val] * scale-factor)
       food-nutrients))

(defn- list->map [ls]
  (map (fn [{:keys [nutrdesc units nutr_val]}]
         {{:nutrdesc nutrdesc :units units} nutr_val})
       ls))

(defn- map->list [mp]
  (map (fn [[{:keys [nutrdesc units]} nutr_val]]
         {:nutrdesc nutrdesc :units units :nutr_val nutr_val})
       mp))

(defn recipe [& ndb-nos-and-grams]
  (let [all-nutrients-list
        (flatten (map (fn [[ndb-no grams]]
                        (scale-nutrients (food-nutrients ndb-no)
                                         (* 0.01 grams)))
                      (partition 2 ndb-nos-and-grams)))]
    (->> all-nutrients-list
      (list->map)
      (apply merge-with +)
      (map->list))))
