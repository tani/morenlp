;;
;; Copyright (c) 2020 TANIGUCHI Masaya
;; This code is licensed under the MIT license.
;;

(ns morenlp.core
  (:gen-class)
  (:import [edu.stanford.nlp.pipeline StanfordCoreNLP]
           [edu.stanford.nlp.ling CoreAnnotations$SentencesAnnotation]
           [edu.stanford.nlp.trees TreeCoreAnnotations$TreeAnnotation TreeCoreAnnotations$BinarizedTreeAnnotation])
  (:require [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.util.response :refer [response]]
            [clojure.tools.logging :as log]
            [clojure.data.json :as json]
            [clojure.string :as str]))

(defn tree->array-map [tree]
  (if (nil? tree) tree
      {:label (.. tree (label) (value))
       :children (map tree->array-map (.children tree))}))

(defn createPipeline [properties]
  (StanfordCoreNLP.
    (doto (java.util.Properties.)
      (.putAll properties))))

(def createPipelineM (memoize createPipeline))

(defn smart-assoc-in [m ks v]
  (if v (assoc-in m ks v) m))

(defn reduces [f v x & xs]
  (reduce f v (apply map vector x xs)))

(defn process
  ([input] (process input {}))
  ([input options]
   (let [pipeline (createPipelineM options)
         annotation (.process pipeline input)
         parse (->> (.get annotation CoreAnnotations$SentencesAnnotation)
                    (map #(.get % TreeCoreAnnotations$TreeAnnotation))
                    (map tree->array-map))
         binaryParse (->> (.get annotation CoreAnnotations$SentencesAnnotation)
                          (map #(.get % TreeCoreAnnotations$BinarizedTreeAnnotation))
                          (map tree->array-map))
         output (with-open [jsonWriter (java.io.StringWriter.)]
                  (.jsonPrint pipeline annotation jsonWriter)
                  (-> jsonWriter str (json/read-str :key-fn keyword)))]
     (reduces
      (fn [o [p b i]]
        (-> (smart-assoc-in o [:sentences i :parse] p)
            (smart-assoc-in [:sentences i :binaryParse] b)))
      output
      parse
      binaryParse
      (-> output :sentences count range)))))

(def rpcmethods {"process" process})

(defn handler [request]
  (let [id (-> request :body (get "id"))
        method (-> request :body (get "method") rpcmethods)
        params (-> request :body (get "params"))]
    (response {:jsonrpc "2.0" :id id :result (apply method params)})))

(def app (-> handler wrap-json-body wrap-json-response))
