(ns nutrition.core
  (:use [clojure.pprint :only (pprint)])
  (:require [clojureql.core :as q])
  (:require [nutrition.db-def :as db])
  (:gen-class))

; example: complex-ish cql select
(defn short-desc [id-str]
  (let [result @(-> (q/table db/db :food_description)
                  (q/select (q/where (= :ndb_no id-str)))
                  (q/project [:shrt_desc :as "short_description"]))]
    (:short_description (first result))))

(defn -main [& args]
  (do
    (println "Today's nutrition suggestion: eat food.")
    (let [id "01001"]
      (println "Short desc of id" id "is" (short-desc id)))
    (println "fin.")))
