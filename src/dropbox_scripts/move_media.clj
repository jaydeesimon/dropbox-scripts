(ns dropbox-scripts.move-media
  (:require [dropbox-repl.core :as db]
            [clj-time.format :as fmt]))

(defn- year-month [dt]
  (fmt/unparse (:year-month fmt/formatters) dt))

(defn- try-create-folder [path]
  (try
    (db/create-folder path)
    (catch Exception e e)))

(defn- move-file [entry dest]
  (let [final-dest (str dest "/" (:name entry))]
    (do (db/move (:path_display entry) final-dest)
        (println "Moved" (:name entry)))))

(defn organize-by-year-month [entry-pred src dest]
  (let [files (filter entry-pred (db/list-entries src))
        year-month-fn (comp year-month :client_modified_dt)
        grouped (group-by year-month-fn files)]
    (do (run! try-create-folder
              (map (partial str dest "/") (keys grouped)))
        (run! #(move-file % (str dest "/" (year-month-fn %)))
              files))))

(comment
  ;; The pictures and videos from my phone are automatically
  ;; uploaded to a folder called Camera Uploads. I use this to
  ;; move them from that folder into a Photos folder organized
  ;; by the year and month the photo was taken.
  (organize-by-year-month db/media? "/Camera Uploads" "/Photos")

  )


