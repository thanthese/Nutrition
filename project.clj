(defproject
  nutrition "0.1-dev"
  :description "Calculate ideal-ish nutrition"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/clojure-contrib "1.2.0"] ; java.jdbc not stable
                 [postgresql/postgresql "8.4-702.jdbc4"]]
  :main nutrition.core
  :jvm-opts ["-Xmx1g" "-server"])
