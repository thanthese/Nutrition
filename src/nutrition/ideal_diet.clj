(ns nutrition.ideal-diet)

;;;;
;; Nutrients on which the food-components data are silent:
;; - Molybdenum
;; - Biotin
;; - Iodine
;; - Chromium
;;

(def ideal
  [{:nutrdesc "18:3 undifferentiated"          :units   "g" :amount    1.6 :category :macronutrients}
   {:nutrdesc "18:2 undifferentiated"          :units   "g" :amount   17   :category :macronutrients}
   {:nutrdesc "Carbohydrate, by difference"    :units   "g" :amount  130   :category :macronutrients}
   {:nutrdesc "Cholesterol"                    :units  "mg" :amount  100   :category :macronutrients}
   {:nutrdesc "Fiber, total dietary"           :units   "g" :amount   38   :category :macronutrients}
   {:nutrdesc "Protein"                        :units   "g" :amount   57   :category :macronutrients}
   {:nutrdesc "Calcium, Ca"                    :units  "mg" :amount 1000   :category :minerals}
   {:nutrdesc "Copper, Cu"                     :units  "mg" :amount    0.9 :category :minerals}
   {:nutrdesc "Fluoride, F"                    :units "mcg" :amount 4000   :category :minerals}
   {:nutrdesc "Iron, Fe"                       :units  "mg" :amount   18   :category :minerals}
   {:nutrdesc "Magnesium, Mg"                  :units  "mg" :amount  400   :category :minerals}
   {:nutrdesc "Manganese, Mn"                  :units  "mg" :amount    2.3 :category :minerals}
   {:nutrdesc "Phosphorus, P"                  :units  "mg" :amount  700   :category :minerals}
   {:nutrdesc "Potassium, K"                   :units  "mg" :amount 4700   :category :minerals}
   {:nutrdesc "Selenium, Se"                   :units "mcg" :amount   55   :category :minerals}
   {:nutrdesc "Sodium, Na"                     :units  "mg" :amount 1000   :category :minerals}
   {:nutrdesc "Zinc, Zn"                       :units  "mg" :amount   11   :category :minerals}
   {:nutrdesc "Choline, total"                 :units  "mg" :amount  550   :category :vitamins}
   {:nutrdesc "Folate, total"                  :units "mcg" :amount  400   :category :vitamins}
   {:nutrdesc "Niacin"                         :units  "mg" :amount   16   :category :vitamins}
   {:nutrdesc "Pantothenic acid"               :units  "mg" :amount    5   :category :vitamins}
   {:nutrdesc "Riboflavin"                     :units  "mg" :amount    1.3 :category :vitamins}
   {:nutrdesc "Thiamin"                        :units  "mg" :amount    1.2 :category :vitamins}
   {:nutrdesc "Vitamin A, IU"                  :units  "IU" :amount 3000   :category :vitamins}
   {:nutrdesc "Vitamin B-12"                   :units "mcg" :amount    2.4 :category :vitamins}
   {:nutrdesc "Vitamin B-6"                    :units  "mg" :amount    1.3 :category :vitamins}
   {:nutrdesc "Vitamin C, total ascorbic acid" :units  "mg" :amount   90   :category :vitamins}
   {:nutrdesc "Vitamin D"                      :units  "IU" :amount  200   :category :vitamins}
   {:nutrdesc "Vitamin E (alpha-tocopherol)"   :units  "mg" :amount   15   :category :vitamins}
   {:nutrdesc "Vitamin K (phylloquinone)"      :units "mcg" :amount  120   :category :vitamins}])
