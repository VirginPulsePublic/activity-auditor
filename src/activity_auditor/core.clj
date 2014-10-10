(ns activity-auditor.core
  (:require [cemerick.bandalore :as sqs]
            [clj-time.coerce :as tm]
            [cheshire.core :refer :all]
            [clojure.tools.logging :as log]))

(use '[carica.core])
(use 'korma.db)
(use 'korma.core)

(defn createdb [env]
  (defdb conn (postgres {:db (config env :database :name)
                       :user (config env :database :username)
                       :password (config env :database :pass)
                       :host (config env :database :hostname)
                       :port (config env :database :portname)})))

(defentity activity
  (pk :id)
  (table :activity_audit))

(defn- recorded? [id]
  (> (count (select activity
              (fields :id)
              (where {:id (str id)})))
      0))

(defn handle-message [m]
  (let [parsed (parse-string (m :body) true)]
    (if (recorded? (parsed :MessageId))
      (log/info "Already saved: " (parsed :MessageId))
      (do
        (insert activity
              (values {:id (parsed :MessageId)
                       :message (parsed :Message)
                       :timestamp (tm/to-sql-time (parsed :Timestamp))}))
        (log/info parsed)))))

(defn consume-messages [client q]
  (future
    (dorun
      (map (sqs/deleting-consumer client handle-message)
        (sqs/polling-receive client q :max-wait Long/MAX_VALUE)))))

(defn- print-banner [env]
  (log/info "Starting auditor on: " env)
  (log/info "Auditing sqs: " (config env :audit)))

(defn -main [env]
  (let [environment (keyword env)
        client (sqs/create-client (config environment :amazon :amazonAccessKey) (config environment :amazon :amazonSecretKey))]
      (print-banner environment)
      (createdb environment)
      (consume-messages client (config environment :audit))))
