;;
; Define database invariants: connection info, schema.
;

(ns nutrition.db-def)

(def db {:classname "org.postgresql.Driver"
         :subprotocol "postgresql"
         :subname "//localhost:5432/nutrition"
         :user "test"
         :password "test"})

(def food-components-schema
  [{:table-name :food_description
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
   {:table-name :nutrient_data
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
             { :field-name "CC"            :type "A" :primary? false :length 1  :precision 0 :blank "Y" :description "Confidence Code indicating data quality, based on evaluation of sample plan, sample handling, analytical method, analytical quality control, and number of samples analyzed. Not included in this release, but is planned for future releases."}]}
   {:table-name :food_group
    :path "resources/food-components/FD_GROUP.txt"
    :schema [{ :field-name "FdGrp_Cd"   :type "A" :primary? true  :length "4"  :precision 0 :blank "N" :description "4-digit code identifying a food group. Only the first 2 digits are currently assigned. In the future, the last 2 digits may be used. Codes may not be consecutive. "}
             { :field-name "FdGrp_Desc" :type "A" :primary? false :length "60" :precision 0 :blank "N" :description "Name of food group. "}]}
   {:table-name :langual_factor
    :path "resources/food-components/LANGUAL.txt"
    ; removed primary key status because field value repeats -- pdf spec was wrong
    :schema [{ :field-name "NDB_No"      :type "A" :primary? false  :length "5" :precision 0 :blank "N" :description "5-digit Nutrient Databank number that uniquely identifies a food item.  If this field is defined as numeric, the leading zero will be lost. "}
             { :field-name "Factor_Code" :type "A" :primary? false :length "5" :precision 0 :blank "N" :description "The LanguaL factor from the Thesaurus "}]}
   {:table-name :langual_description
    :path "resources/food-components/LANGDESC.txt"
    :schema [{ :field-name "Factor_Code" :type "A" :primary? true  :length "5"   :precision 0 :blank "N" :description "The LanguaL factor from the Thesaurus.  Only those codes used to factor the foods contained in the LanguaL Factor file are included in this file "}
             { :field-name "Description" :type "A" :primary? false :length "140" :precision 0 :blank "N" :description "The description of the LanguaL Factor Code from the thesaurus "}]}
   {:table-name :nutrient_definition
    :path "resources/food-components/NUTR_DEF.txt"
    :schema [{ :field-name "Nutr_No"  :type "A" :primary? true  :length "3"  :precision 0 :blank "N" :description "Unique 3-digit identifier code for a nutrient. "}
             { :field-name "Units"    :type "A" :primary? false :length "7"  :precision 0 :blank "N" :description "Units of measure (mg, g, μg, and so on). "}
             { :field-name "Tagname"  :type "A" :primary? false :length "20" :precision 0 :blank "Y" :description "International Network of Food Data Systems (INFOODS) Tagnames.† A unique abbreviation for a nutrient/food component developed by INFOODS to aid in the interchange of data. "}
             { :field-name "NutrDesc" :type "A" :primary? false :length "60" :precision 0 :blank "N" :description "Name of nutrient/food component. "}
             { :field-name "Num_Dec"  :type "A" :primary? false :length "1"  :precision 0 :blank "N" :description "Number of decimal places to which a nutrient value is rounded. "}
             { :field-name "SR_Order" :type "N" :primary? false :length "6"  :precision 0 :blank "N" :description "Used to sort nutrient records in the same order as various reports produced from SR. "}]}
   {:table-name :source_code
    :path "resources/food-components/SRC_CD.txt"
    :schema [{ :field-name "Src_Cd"     :type "A" :primary? true  :length "2"  :precision 0 :blank "N" :description "2-digit code. "}
             { :field-name "SrcCd_Desc" :type "A" :primary? false :length "60" :precision 0 :blank "N" :description "Description of source code that identifies the type of nutrient data. "}]}
   {:table-name :data_derivation
    :path "resources/food-components/DERIV_CD.txt"
    :schema [{ :field-name "Deriv_Cd"   :type "A" :primary? true  :length "4"   :precision 0 :blank "N" :description "Derivation Code. "}
             { :field-name "Deriv_Desc" :type "A" :primary? false :length "120" :precision 0 :blank "N" :description "Description of derivation code giving specific information on how the value was determined. "}]}
   {:table-name :weight_file
    :path "resources/food-components/WEIGHT.txt"
    :schema [{ :field-name "NDB_No"       :type "A" :primary? true  :length "5"  :precision 0 :blank "N" :description "5-digit Nutrient Databank number. "}
             { :field-name "Seq"          :type "A" :primary? true  :length "2"  :precision 0 :blank "N" :description "Sequence number. "}
             { :field-name "Amount"       :type "N" :primary? false :length "5"  :precision 3 :blank "N" :description "Unit modifier (for example, 1 in “1 cup”). "}
             { :field-name "Msre_Desc"    :type "A" :primary? false :length "80" :precision 0 :blank "N" :description "Description (for example, cup, diced, and 1-inch pieces). "}
             { :field-name "Gm_Wgt"       :type "N" :primary? false :length "7"  :precision 1 :blank "N" :description "Gram weight. "}
             ; upped length and precision to reflect data
             { :field-name "Num_Data_Pts" :type "N" :primary? false :length "4"  :precision 1 :blank "Y" :description "Number of data points. "}
             { :field-name "Std_Dev"      :type "N" :primary? false :length "7"  :precision 3 :blank "Y" :description "Standard deviation. "}]}
   {:table-name :footnote
    :path "resources/food-components/FOOTNOTE.txt"
    :schema [{ :field-name "NDB_No"     :type "A" :primary? false :length "5"   :precision 0 :blank "N" :description "5-digit Nutrient Databank number. "}
             { :field-name "Footnt_No"  :type "A" :primary? false :length "4"   :precision 0 :blank "N" :description "Sequence number. If a given footnote applies to more than one nutrient number, the same footnote number is used. As a result, this file cannot be indexed. "}
             { :field-name "Footnt_Typ" :type "A" :primary? false :length "1"   :precision 0 :blank "N" :description "Type of footnote                                                                                                                                              :D = footnote adding information to the food  description;  M = footnote adding information to measure description;  N = footnote providing additional information on a nutrient value. If the Footnt_typ = N, the Nutr_No will also be filled in. "}
             { :field-name "Nutr_No"    :type "A" :primary? false :length "3"   :precision 0 :blank "Y" :description "Unique 3-digit identifier code for a nutrient to which footnote applies. "}
             { :field-name "Footnt_Txt" :type "A" :primary? false :length "200" :precision 0 :blank "N" :description "Footnote text. "}]}
   {:table-name :data_source_link
    :path "resources/food-components/DATSRCLN.txt"
    :schema [{ :field-name "NDB_No"     :type "A" :primary? true :length "5" :precision 0 :blank "N" :description "5-digit Nutrient Databank number. "}
             { :field-name "Nutr_No"    :type "A" :primary? true :length "3" :precision 0 :blank "N" :description "Unique 3-digit identifier code for a nutrient. "}
             { :field-name "DataSrc_ID" :type "A" :primary? true :length "6" :precision 0 :blank "N" :description "Unique ID identifying the reference/source.  "}]}
   {:table-name :data_sources
    :path "resources/food-components/DATA_SRC.txt"
    :schema [{ :field-name "DataSrc_ID"  :type "A" :primary? true  :length "6"   :precision 0 :blank "N" :description "Unique number identifying the reference/source.  "}
             { :field-name "Authors"     :type "A" :primary? false :length "255" :precision 0 :blank "Y" :description "List of authors for a journal article or name of sponsoring organization for other documents. "}
             { :field-name "Title"       :type "A" :primary? false :length "255" :precision 0 :blank "N" :description "Title of article or name of document, such as a report from a company or trade association. "}
             { :field-name "Year"        :type "A" :primary? false :length "4"   :precision 0 :blank "Y" :description "Year article or document was published. "}
             { :field-name "Journal"     :type "A" :primary? false :length "135" :precision 0 :blank "Y" :description "Name of the journal in which the article was published. "}
             { :field-name "Vol_City"    :type "A" :primary? false :length "16"  :precision 0 :blank "Y" :description "Volume number for journal articles, books, or reports; city where sponsoring organization is located. "}
             { :field-name "Issue_State" :type "A" :primary? false :length "5"   :precision 0 :blank "Y" :description "Issue number for journal article; State where the sponsoring organization is located. "}
             { :field-name "Start_Page"  :type "A" :primary? false :length "5"   :precision 0 :blank "Y" :description "Starting page number of article/document. "}
             { :field-name "End_Page"    :type "A" :primary? false :length "5"   :precision 0 :blank "Y" :description "Ending page number of article/document. "}]}])
