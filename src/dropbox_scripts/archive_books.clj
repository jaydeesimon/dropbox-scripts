(ns dropbox-scripts.archive-books
  (:require [clj-time.core :as t]
            [dropbox-repl.core :as db]
            [dropbox-scripts.util :as util]
            [clojure.string :as str]))

(def books-folder "/books")
(def books-archived-folder "/books/archived")

(defn within? [period dt]
  (let [now (t/now)
        interval (t/interval (t/minus now period) now)]
    (or (t/within? interval dt)
        (= dt (t/end interval)))))

(defn archive-older-than [period file-pred from-path archive-path]
  (let [files (->> (filter file-pred (db/list-entries from-path))
                   (remove #(str/starts-with? (:path_lower %) (str/lower-case archive-path)))
                   (filter #(not (within? period (:client_modified_dt %)))))]
    (run! (partial util/move-file books-archived-folder) files)))

(comment

  "Move my ebooks into an archived dir if older than some period."

  (archive-older-than (t/weeks 4) db/ebook? books-folder books-archived-folder)

  )








