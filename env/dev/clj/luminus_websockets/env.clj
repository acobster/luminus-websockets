(ns luminus-websockets.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [luminus-websockets.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[luminus-websockets started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[luminus-websockets has shut down successfully]=-"))
   :middleware wrap-dev})
