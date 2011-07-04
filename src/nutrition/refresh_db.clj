;;
; Wipe and recreate fresh nutrition database.
;
; Refresh database with:
;
;   $> lein run -m nutrition.refresh-db
;

(ns nutrition.refresh-db
  (:require [clojure.string :as str])
  (:require [clojure.contrib.sql :as sql])
  (:require [nutrition.db-def :as db])
  (:gen-class))

(defn alphanu-field? [field]
  (= (:type field) "A"))

(defn integer-field? [field]
  (and (= (:type field) "N")
       (= (:precision field) 0)))

(defn decimal-field? [field]
  (and (= (:type field) "N")
       (> (:precision field) 0)))

(defn pkey-fieldnames [table-definition]
  (->> (:schema table-definition)
    (filter :primary?)
    (map :field-name)))

(defn sql-field-def [field]
  (let [name (:field-name field)
        type (cond (integer-field? field) "integer"
                   (alphanu-field? field) (format "varchar(%s)" (:length field))
                   (decimal-field? field) (format "decimal(%s, %s)"
                                                  (:length field)
                                                  (:precision field)))]
    [name type]))

(defn pkeys-def [pkeys]
  ["PRIMARY KEY" (format "(%s)" (apply str (interpose ", " pkeys)))])

(defn fields+contraints-def [table-definition]
  (let [fields (map sql-field-def (:schema table-definition))
        pkeys (pkey-fieldnames table-definition)]
    (if (empty? pkeys )
      fields
      (let [contraints (pkeys-def pkeys)]
        (conj fields contraints)))))

(defn drop-table [table-definition]
  (let [table-name (:table-name table-definition)]
    (sql/with-connection
      db/db (try
           (sql/drop-table table-name)
           (catch Exception _
             (println " WARN: No table, cannot drop" table-name))))))

(defn create-table [table-definition]
  (sql/with-connection
    db/db (apply (partial sql/create-table (:table-name table-definition))
              (fields+contraints-def table-definition))))

(defn drop-tables [table-definitions]
  (doseq [table-def table-definitions]
    (drop-table table-def)))

(defn create-tables [table-definitions]
  (doseq [table-def table-definitions]
    (create-table table-def)))

(defn string->num [f s]
  (if (str/blank? s)
    0
    (f s)))

(defn string->int [s]
  (string->num #(Integer/parseInt %) s))

(defn string->double [s]
  (string->num #(Double/parseDouble %) s))

(defn cast-fns [table-definition]
  (map (fn [field]
         (cond (alphanu-field? field) identity
               (integer-field? field) string->int
               (decimal-field? field) string->double))
       (:schema table-definition)))

(defn strip-tildes [text]
  (if-let [match (re-find #"~(.*)~" text)]
    (last match)
    text))

(defn split-into-fields [line]
  (map (comp str/trim strip-tildes)
       (-> line
         (str/replace "^" " ^ ")
         (str/replace "\r" "")
         (str/split #"\^"))))

(defn get-lines [path]
  (-> (slurp path)
    (str/split #"\n")))

(defn load-row-values [table-definition]
  (map (fn [line]
         (->> line
           (split-into-fields)
           (map #(%1 %2) (cast-fns table-definition))))
       (get-lines (:path table-definition))))

(defn populate-table [table-definition]
  (clojure.contrib.sql/with-connection
    db/db (count (apply (partial clojure.contrib.sql/insert-rows
                              (:table-name table-definition))
                     (load-row-values table-definition)))))

(defn populate-tables [table-definitions]
  (doseq [table-def table-definitions]
    (println "Populating table" (:table-name table-def) "...")
    (time (populate-table table-def))
    (println "... done.")))

(defn refresh-database []
  (let [tds db/food-components-schema]
    (do
      (println "Dropping tables...") (drop-tables tds)
      (println "Creating schema...") (create-tables tds)
      (println "Populating tables...") (populate-tables tds))))

(defn -main [& args]
  (refresh-database))
