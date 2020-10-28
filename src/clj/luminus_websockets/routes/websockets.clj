(ns luminus-websockets.routes.websockets
  (:require
   [clojure.tools.logging :as log]
   [immutant.web.async :as async]))


(defonce channels (atom #{}))

(defn connect! [channel]
  (log/info "channel open")
  (swap! channels conj channel))

(defn disconnect! [channel {:keys [code reason]}]
  (log/info "channel close code:" code "reason:" reason)
  (swap! channels #(remove #{channel} %)))

(defn notify-clients! [_ msg]
  (log/info "message:" msg)
  (doseq [channel @channels]
    (async/send! channel msg)))


(def callbacks
  "callback fns"
  {:on-open connect!
   :on-close disconnect!
   :on-message notify-clients!})


(defn handler [req]
  (async/as-channel req callbacks))

(def websocket-routes
  [["/ws" handler]])