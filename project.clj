(defproject morenlp "0.1.0-SNAPSHOT"
  :description "JSON-RPC endpoint of Stanford CoreNLP Server"
  :url "https://github.com/tani/morenlp"
  :scm {:name "git" :url "https://github.com/tani/morenlp"}
  :license {:name "MIT License"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [org.clojure/tools.logging "1.1.0"]
                 [org.clojure/data.json "1.0.0"]
                 [edu.stanford.nlp/stanford-corenlp "4.0.0"]
                 [edu.stanford.nlp/stanford-corenlp "4.0.0" :classifier "models"]
                 [org.slf4j/slf4j-simple "1.7.30"]
                 [ring/ring-core "1.8.2"]
                 [ring/ring-jetty-adapter "1.8.2"]
                 [ring/ring-json "0.5.0"]
                 [javax.servlet/javax.servlet-api "4.0.1"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler morenlp.core/app}
  :repl-options {:init-ns morenlp.core})
