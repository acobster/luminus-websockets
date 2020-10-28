(ns luminus-websockets.app
  (:require [luminus-websockets.core :as core]))

;;ignore println statements in prod
(set! *print-fn* (fn [& _]))

(core/init!)
