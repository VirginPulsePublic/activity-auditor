(defproject activity-auditor "0.1.0-SNAPSHOT"
  :description "Monitorings and stores raw SQS messages from an audit queue"
  :url "https://github.com/VirginPulsePublic/activity-auditor"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [korma "0.3.0"]
                 [org.postgresql/postgresql "9.2-1002-jdbc4"]
                 [sonian/carica "1.1.0"]
                 [clj-time "0.8.0"]
                 [com.cemerick/bandalore "0.0.5" :exclusions [[joda-time]]]
                 [org.clojure/tools.logging "0.2.4"]
                 [org.slf4j/slf4j-log4j12 "1.7.1"]
                 [log4j/log4j "1.2.17" :exclusions [javax.mail/mail
                                                    javax.jms/jms
                                                    com.sun.jmdk/jmxtools
                                                    com.sun.jmx/jmxri]]]
  :resource-paths ["resources"]
  :main activity-auditor.core)
