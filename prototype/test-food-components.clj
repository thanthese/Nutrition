;;
; To load this file in the REPL:
;
;   $> (load-file "test-food-components.clj")
;

(ns food-components
  (:use clojure.test))

(def sample-row-FOOD_DES "~01001~^~0100~^~Butter, salted~^~BUTTER,WITH SALT~^~~^~~^~Y~^~~^0^~~^6.38^4.27^8.79^3.87")

(deftest
  test-strip-tildes
  (is (= (strip-tildes "~hello~") "hello") "strip tildes")
  (is (= (strip-tildes "hello") "hello") "don't strip nothing")
  (is (= (strip-tildes "~hello") "~hello") "don't strip unmatched tildeas (start)")
  (is (= (strip-tildes "hello~") "hello~") "don't strip unmatched tildeas (end)")
  (is (= (strip-tildes "hel~lo") "hel~lo") "don't strip unmatched tildeas (middle)"))

(deftest
  test-split-row
  (is (= (split-row sample-row-FOOD_DES) ["01001" "0100" "Butter, salted" "BUTTER,WITH SALT" "" "" "Y" "" "0" "" "6.38" "4.27" "8.79" "3.87"])))

(deftest
  test-that-tables-correctly-loaded
  (is (> (count (:food-description tables))
         7600)))
