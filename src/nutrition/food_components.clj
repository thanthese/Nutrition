;;
; Load food-components data from files and provide manipulation methods.
;

(ns nutrition.food-components
  (:require [clojure.string :as str]))

(def table-definitions
  [{:table-name :food-description
    :path "resources/food-components/FOOD_DES.txt"
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
             { :field-name "CHO_Factor"  :type "N" :primary? false :length 4   :precision 2 :blank "Y" :description "Factor for calculating calories from carbohydrate (see p.  11). "}]}
   {:table-name :nutrient-data
    :path "resources/food-components/NUT_DATA.txt"
    :schema [{ :field-name "NDB_No"        :type "A" :primary? true  :length 5  :precision 0 :blank "N" :description "5-digit Nutrient Databank number."}
             { :field-name "Nutr_No"       :type "A" :primary? true  :length 3  :precision 0 :blank "N" :description "Unique 3-digit identifier code for a nutrient."}
             { :field-name "Nutr_Val"      :type "N" :primary? false :length 10 :precision 3 :blank "N" :description "Amount in 100 grams, edible portion †."}
             { :field-name "Num_Data_Pts"  :type "N" :primary? false :length 5  :precision 0 :blank "N" :description "Number of data points (previously called Sample_Ct) is the number of analyses used to calculate the nutrient value. If the number of data points is 0, the value was calculated or imputed."}
             { :field-name "Std_Error"     :type "N" :primary? false :length 8  :precision 3 :blank "Y" :description "Standard error of the mean. Null if cannot be calculated.  The standard error is also not given if the number of data points is less than three."}
             { :field-name "Src_Cd"        :type "A" :primary? false :length 2  :precision 0 :blank "N" :description "Code indicating type of data."}
             { :field-name "Deriv_Cd"      :type "A" :primary? false :length 4  :precision 0 :blank "Y" :description "Data Derivation Code giving specific information on how the value is determined"}
             { :field-name "Ref_NDB_No"    :type "A" :primary? false :length 5  :precision 0 :blank "Y" :description "NDB number of the item used to impute a missing value.  Populated only for items added or updated starting with SR14.  "}
             { :field-name "Add_Nutr_Mark" :type "A" :primary? false :length 1  :precision 0 :blank "Y" :description "Indicates a vitamin or mineral added for fortification or enrichment. This field is populated for ready-to-eat breakfast cereals and many brand-name hot cereals in food group 8."}
             { :field-name "Num_Studies"   :type "N" :primary? false :length 2  :precision 0 :blank "Y" :description "Number of studies."}
             { :field-name "Min"           :type "N" :primary? false :length 10 :precision 3 :blank "Y" :description "Minimum value."}
             { :field-name "Max"           :type "N" :primary? false :length 10 :precision 3 :blank "Y" :description "Maximum value."}
             { :field-name "DF"            :type "N" :primary? false :length 2  :precision 0 :blank "Y" :description "Degrees of freedom."}
             { :field-name "Low_EB"        :type "N" :primary? false :length 10 :precision 3 :blank "Y" :description "Lower 95% error bound."}
             { :field-name "Up_EB"         :type "N" :primary? false :length 10 :precision 3 :blank "Y" :description "Upper 95% error bound."}
             { :field-name "Stat_cmt"      :type "A" :primary? false :length 10 :precision 0 :blank "Y" :description "Statistical comments. See definitions below."}
             { :field-name "CC"            :type "A" :primary? false :length 1  :precision 0 :blank "Y" :description "Confidence Code indicating data quality, based on evaluation of sample plan, sample handling, analytical method, analytical quality control, and number of samples analyzed. Not included in this release, but is planned for future releases."}]}])

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
  "Load all tables into memory as a map: table name to list of rows.  Expensive
  operation."
  []
  (into {}
        (map (fn [td]
               [(:table-name td)
                (load-table td)])
             table-definitions)))

(defn extract
  "Return field values for each row in table."
  [fields table]
  (map (apply juxt fields) table))
