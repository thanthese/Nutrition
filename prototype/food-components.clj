;;
; Stephen Mann
; 20 June 2011
;
; First stab at parsing raw food-component data.
;
; Requires clojure 1.2.  Run with:
;
;   $> java -server -jar <path-to-clojure.jar> food-components.clj
;

(ns food-components
  (:require [clojure.string :as str]))

(def FOOD_DES-schema
  [{ :field-name "NDB_No"      :type "A" :primary? true  :length 5   :precision 0 :blank "N" :description "5-digit Nutrient Databank number that uniquely identifies a food item.  If this field is defined as numeric, the leading zero will be lost. "}
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
   { :field-name "CHO_Factor"  :type "N" :primary? false :length 4   :precision 2 :blank "Y" :description "Factor for calculating calories from carbohydrate (see p.  11). "} ])

(defn get-table-columns
  "Return table column names as List of Keywords."
  [schema]
  (map (comp keyword :field-name) schema))

(defn get-all-lines
  "Return a Vector of lines from Filepath."
  [filepath]
  (str/split (slurp filepath) #"\n"))

(defn strip-tildes
  "Strip surrounding tildes from Text, if a pair exists."
  [text]
  (if (and (= \~ (first text))
           (= \~ (last text)))
    (subs text 1 (dec (count text)))
    text))

(defn create-table
  "Create a list of hashs to represent a table given Col-names and raw Lines
  (as from an input file)."
  [col-names lines]
  (map (fn [line]
         (zipmap col-names
                 (map strip-tildes (str/split line #"\^"))))
       lines))

; demostrate the hash
(println (create-table FOOD_DES-keys (get-all-lines "../food-components/FOOD_DES.txt")))

; slurp normal file size
(time (take 1 (str/split (slurp "../food-components/FOOD_DES.txt") #"\n")))

; slurp giant file size
(time (last (str/split (slurp "../food-components/NUT_DATA.txt") #"\n")))
