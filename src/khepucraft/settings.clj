(ns khepucraft.settings
  (:require [clojure.java.io :as io]
            [clojure.pprint :as p]
            [clojure.edn :as edn]))

(def settings-path "./resources/settings.edn")

(def settings (atom nil))

(defn read-settings
  []
  (reset! settings (edn/read-string (slurp settings-path))))

(defn save-settings
  "TODO Error handling"
  []
  (let [out (p/get-pretty-writer (io/writer settings-path))]
    (p/pprint @settings out)))

;;(read-settings)
;;(swap! settings assoc :graphics {:resolution [1920 1080]})
;;(save-settings)
;;(print @settings)

