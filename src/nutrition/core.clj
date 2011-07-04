(ns nutrition.core
  (:require [nutrition.db-def :as db])
  (:require [clojureql.core :as q])
  (:use [clojure.pprint :only (pprint)])
  (:gen-class))

; ; example: complex-ish cql select
; @(-> (q/table db :blogs)
;    (q/select (q/where (> :id 5)))
;    (q/project [:id :as "myid"]))

(defn sample-carrot-query []
  (let [tables (fc/generate-tables)]
    (fc/extract [:NDB_No :Shrt_Desc]
                (filter #(re-find #"(?i)^carrot" (:Long_Desc %))
                        (:food-description tables)))))

(defn silly-join []
  (let [tables (fc/generate-tables)]
    (fc/extract [:Nutr_No]
                (filter #(= (:NDB_No (first (:food-description tables)))
                            (:NDB_No %))
                        (:nutrient-data tables)))))

(defn -main [& args]
  (do
    (println "Today's nutrition suggestion: eat food.")
    (println "Sample query: descriptions that start with 'carrot'.")
    (pprint (sample-carrot-query))
    (println "Sample join:")
    (println (silly-join))
    (println "fin.")))
