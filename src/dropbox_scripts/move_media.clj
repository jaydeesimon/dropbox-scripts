(ns dropbox-scripts.move-media
  (:require [dropbox-repl.core :as db]
            [dropbox-scripts.util :as util]
            [clj-time.format :as fmt]))

(defn- year-month [dt]
  (fmt/unparse (:year-month fmt/formatters) dt))

(defn organize-by-year-month [entry-pred src dest]
  (let [files (take 1 (filter entry-pred (db/list-entries src)))
        year-month-fn (comp year-month :client_modified_dt)]
    (run! #(util/move-file (str dest "/" (year-month-fn %)) %) files)))

(comment
  ;; The pictures and videos from my phone are automatically
  ;; uploaded to a folder called Camera Uploads. I use this to
  ;; move them from that folder into a Photos folder organized
  ;; by the year and month the photo was taken.
  (organize-by-year-month db/media? "/Camera Uploads" "/Photos")

  )


