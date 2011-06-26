;;
; Load food-components data from files and provide manipulation methods.
;

(ns nutrition.food-components
  (:require [clojure.string :as str]))

(def FOOD_DES-definition
  {:path "resources/food-components/FOOD_DES.txt"
   :schema [{ :field-name "NDB_No"      :type "A" :primary? true  :length 5   :precision 0 :blank "N" :description "5-digit Nutrient Databank number that uniquely identifies a food item.  If this field is defined as numeric, the leading zero will be lost. "}
            { :field-name "FdGrp_Cd"    :type "A" :primary? false :length 4   :precision 0 :blank "N" :description "4-digit code indicating food group to which a food item belongs. "}
            { :field-name "Long_Desc"   :type "A" :primary? false :length 200 :precision 0 :blank "N" :description "200-character description of food item. "}
            { :field-name "Shrt_Desc"   :type "A" :primary? false :length 60  :precision 0 :blank "N" :description "60-character abbreviated description of food item.  Generated from the 200-character description using abbreviations in Appendix A. If short description is longer than 60 characters, additional abbreviations are made. "}
            { :field-name "ComName"     :type "A" :primary? false :length 100 :precision 0 :blank "Y" :description "Other names commonly used to describe a food, including local or regional names for various foods, for example, “soda” or “pop” for “carbonated beverages.” "}
            { :field-name "ManufacName" :type "A" :primary? false :length 65  :precision 0 :blank "Y" :description "Indicates the company that manufactured the product, when appropriate. "}
            { :field-name "Survey"      :type "A" :primary? false :length 1   :precision 0 :blank "Y" :description "Indicates if the food item is used in the USDA Food and Nutrient Database for Dietary Studies (FNDDS) and thus has a complete nutrient profile for the 65 FNDDS nutrients. "}
            { :field-name "Ref_desc"    :type "A" :primary? false :length 135 :precision 0 :blank "Y" :description "Description of inedible parts of a food item (refuse), such as seeds or bone. "}
            { :field-name "Refuse"      :type "N" :primary? false :length 2   :precision 0 :blank "Y" :description "Percentage of refuse. "}
            { :field-name "SciName"     :type "A" :primary? false :length 65  :precision 0 :blank "Y" :description "Scientific name of the food item. Given for the least processed form of the food (usually raw), if applicable. "}
            { :field-name "N_Factor"    :type "N" :primary? false :length 4   :precision 2 :blank "Y" :description "Factor for converting nitrogen to protein (see p. 10). "}
            { :field-name "Pro_Factor"  :type "N" :primary? false :length 4   :precision 2 :blank "Y" :description "Factor for calculating calories from protein (see p. 11). "}
            { :field-name "Fat_Factor"  :type "N" :primary? false :length 4   :precision 2 :blank "Y" :description "Factor for calculating calories from fat (see p. 11). "}
            { :field-name "CHO_Factor"  :type "N" :primary? false :length 4   :precision 2 :blank "Y" :description "Factor for calculating calories from carbohydrate (see p.  11). "} ]})

(defn strip-tildes
  "Strip surrounding tildes from Text, if a pair exists."
  [text]
  (if-let [match (re-find #"~(.*)~" text)]
    (second match)
    text))

(defn split-row
  "Split a rows from the raw datasets into a vector of fields."
  [row]
  (map strip-tildes (str/split row #"\^")))

(defn get-lines
  "Return lines from given file as vector of strings."
  [path]
  (str/split (slurp path) #"\n"))

(defn load-table
  "Convert a table definition (path to file, fields) into an in-memory table
  object (list of maps)."
  [{:keys [path schema] :as table-definition}]
  (let [rows (get-lines path)
        col-names (map (comp keyword :field-name) schema)]
    (map #(zipmap col-names (split-row %))
         rows)))

(defn generate-tables
  "Generate map of all tables -- basically return an object containing the
  entire food-component database.  Expensive operation."
  []
  {:food-description (load-table FOOD_DES-definition)})

(defn extract
  "Return field values for each row in table."
  [fields table]
  (map (apply juxt fields) table))
