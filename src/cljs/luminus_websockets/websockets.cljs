(ns luminus-websockets.websockets
  (:require [cognitect.transit :as t]))


(defonce ws-chan (atom nil))
(def json-reader (t/reader :json))
(def json-writer (t/writer :json))

(defn receive-transit-msg! [update-fn]
  (fn [msg]
    (update-fn (->> msg .-data (t/read json-reader)))))

(defn send-transit-msg! [msg]
  (if @ws-chan
    (.send @ws-chan (t/write json-writer msg))
    ;; TODO nicer error handling
    (throw (js/Error. "Websocket is not available!"))))

(defn make-websocket! [url receive-handler]
  (println "Attempting to connect to websocket...")
  (if-let [chan (js/WebSocket. url)]
    (do
      (set! (.-onmessage chan) (receive-transit-msg! receive-handler))
      (reset! ws-chan chan)
      (println "Established websocket connection at: " url))
    (throw (js/Error. "Websocket connection failed!"))))
