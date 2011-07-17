(ns nutrition.query
  (:require [clojure.string :as str])
  (:require [clojure.contrib.sql :as sql])
  (:require [nutrition.db-def :as db] )
  (:require [nutrition.ideal-diet :as i]))

(defn- query [select-stmt]
  (sql/with-connection db/db
                       (sql/with-query-results rs [select-stmt]
                                               (doall rs))))

(defn foods-rich-in [nutrdesc]
  (query (str "select data.nutr_val, food.long_desc
              from nutrient_data as data,
              nutrient_definition as def,
              food_description as food
              where data.nutr_no = def.nutr_no
              and food.ndb_no = data.ndb_no
              and def.nutrdesc = '" nutrdesc
              "' order by data.nutr_val desc")))

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
                               (str/split #" "))))
              " order by grp.fdgrp_desc")))

(def food-nutrients (memoize (fn [ndb-no]
  (query (str "select def.nutrdesc, def.units, data.nutr_val
              from nutrient_data as data, nutrient_definition as def
              where data.nutr_no = def.nutr_no
              and def.units <> 'kJ'
              and data.ndb_no = '" ndb-no "'")))))

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

(defn nutrient [nutrdesc food-nutrients]
  (first (filter #(= (:nutrdesc %) nutrdesc)
                 food-nutrients)))

(defn score-recipe [recipe ideal]
  (map (fn [ideal-nutrient]
         (let [recipe-nutrient (nutrient (:nutrdesc ideal-nutrient) recipe)
               recipe-nutrient-val (or (:nutr_val recipe-nutrient) 0)]
           (-> ideal-nutrient
             (assoc :percent
                    (* 100 (/ recipe-nutrient-val
                              (:nutr_val ideal-nutrient))))
             (assoc :recipe_val recipe-nutrient-val)
             (assoc :ideal_val (:nutr_val ideal-nutrient))
             (dissoc :nutr_val))))
       ideal))
