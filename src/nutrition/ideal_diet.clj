(ns nutrition.ideal-diet)

;;;;
;; Nutrients on which the food-components data are silent:
;; - Molybdenum
;; - Biotin
;; - Iodine
;; - Chromium
;;

(def active-male-ideal
  [{:nutrdesc "Energy"                         :units "kcal" :nutr_val 3200   :category :macronutrients}
   {:nutrdesc "18:3 undifferentiated"          :units    "g" :nutr_val    1.6 :category :macronutrients}
   {:nutrdesc "18:2 undifferentiated"          :units    "g" :nutr_val   17   :category :macronutrients}
   {:nutrdesc "Carbohydrate, by difference"    :units    "g" :nutr_val  130   :category :macronutrients}
   {:nutrdesc "Cholesterol"                    :units   "mg" :nutr_val  100   :category :macronutrients}
   {:nutrdesc "Fiber, total dietary"           :units    "g" :nutr_val   38   :category :macronutrients}
   {:nutrdesc "Protein"                        :units    "g" :nutr_val   57   :category :macronutrients}
   {:nutrdesc "Calcium, Ca"                    :units   "mg" :nutr_val 1000   :category :minerals}
   {:nutrdesc "Copper, Cu"                     :units   "mg" :nutr_val    0.9 :category :minerals}
   {:nutrdesc "Fluoride, F"                    :units  "mcg" :nutr_val 4000   :category :minerals}
   {:nutrdesc "Iron, Fe"                       :units   "mg" :nutr_val   18   :category :minerals}
   {:nutrdesc "Magnesium, Mg"                  :units   "mg" :nutr_val  400   :category :minerals}
   {:nutrdesc "Manganese, Mn"                  :units   "mg" :nutr_val    2.3 :category :minerals}
   {:nutrdesc "Phosphorus, P"                  :units   "mg" :nutr_val  700   :category :minerals}
   {:nutrdesc "Potassium, K"                   :units   "mg" :nutr_val 4700   :category :minerals}
   {:nutrdesc "Selenium, Se"                   :units  "mcg" :nutr_val   55   :category :minerals}
   {:nutrdesc "Sodium, Na"                     :units   "mg" :nutr_val 1000   :category :minerals}
   {:nutrdesc "Zinc, Zn"                       :units   "mg" :nutr_val   11   :category :minerals}
   {:nutrdesc "Choline, total"                 :units   "mg" :nutr_val  550   :category :vitamins}
   {:nutrdesc "Folate, total"                  :units  "mcg" :nutr_val  400   :category :vitamins}
   {:nutrdesc "Niacin"                         :units   "mg" :nutr_val   16   :category :vitamins}
   {:nutrdesc "Pantothenic acid"               :units   "mg" :nutr_val    5   :category :vitamins}
   {:nutrdesc "Riboflavin"                     :units   "mg" :nutr_val    1.3 :category :vitamins}
   {:nutrdesc "Thiamin"                        :units   "mg" :nutr_val    1.2 :category :vitamins}
   {:nutrdesc "Vitamin A, IU"                  :units   "IU" :nutr_val 3000   :category :vitamins}
   {:nutrdesc "Vitamin B-12"                   :units  "mcg" :nutr_val    2.4 :category :vitamins}
   {:nutrdesc "Vitamin B-6"                    :units   "mg" :nutr_val    1.3 :category :vitamins}
   {:nutrdesc "Vitamin C, total ascorbic acid" :units   "mg" :nutr_val   90   :category :vitamins}
   {:nutrdesc "Vitamin D"                      :units   "IU" :nutr_val  200   :category :vitamins}
   {:nutrdesc "Vitamin E (alpha-tocopherol)"   :units   "mg" :nutr_val   15   :category :vitamins}
   {:nutrdesc "Vitamin K (phylloquinone)"      :units  "mcg" :nutr_val  120   :category :vitamins}])
