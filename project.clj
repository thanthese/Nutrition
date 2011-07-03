(defproject
  nutrition "0.1-dev"
  :description "Calculate ideal-ish nutrition"
  :main nutrition.core
  :jvm-opts ["-Xmx1g" "-server"]
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/clojure-contrib "1.2.0"] ; java.jdbc not stable
                 [clojureql "1.0.0"]
                 [postgresql/postgresql "8.4-702.jdbc4"]])
