(defproject
  nutrition "0.1-dev"
  :description "Calculate ideal-ish nutrition"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [org.clojure/java.jdbc "0.0.2"]]
  :main nutrition.core
  :jvm-opts ["-Xmx1g" "-server"])
