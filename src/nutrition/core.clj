(ns nutrition.core
  (:use [clojure.pprint :only (pprint)])
  (:require [clojure.string :as str])
  ;(:require [clojureql.core :as q])
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
    (print "Total: ")
    (count results)))

(defn search-desc [search-term]
  (query (format "select NDB_No, Long_Desc
                 from food_description
                 where lower(Long_Desc) like '%%%s%%'"
                 (str/lower-case search-term))))

(defn -main [& args]
  (do
    (println "Show me the carrots!")
    (pretty-table (search-desc "carrot"))))
