;;
; Stephen Mann
; 20 June 2011
;
; First stab at parsing raw food-component data.
;
; Requires clojure 1.2.  Run with:
;
;   $> java -jar <path-to-clojure.jar> food-components.clj
;

(ns food-components
  (:require [clojure.string :as str]))

(def FOOD_DES-keys
  [:NDB_No
   :FdGrp_Cd
   :Long_Desc
   :Shrt_Desc
   :ComName
   :ManufacName
   :Survey
   :Ref_desc
   :Refuse
   :SciName
   :N_Factor
   :Pro_Factor
   :Fat_Factor
   :CHO_Factor])

(defn get-all-lines
  "Return a Vector of lines from Filepath."
  [filepath]
  (with-open [rdr (clojure.java.io/reader filepath)]
    (reduce conj [] (line-seq rdr))))

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
(take 1 (str/split (slurp "../food-components/FOOD_DES.txt") #"\n"))

; slurp giant file size
(last (str/split (slurp "../food-components/NUT_DATA.txt") #"\n"))
