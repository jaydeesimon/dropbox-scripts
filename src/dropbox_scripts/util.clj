(ns dropbox-scripts.util
  (:require [dropbox-repl.core :as db]))

(defn move-file [dest entry]
  (db/move-file-to-folder (:path_display entry) dest)
  (println "Moved" (:path_display entry)))
