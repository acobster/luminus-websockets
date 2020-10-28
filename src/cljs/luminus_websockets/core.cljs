(ns luminus-websockets.core
  (:require
   [reagent.core :as reagent :refer [atom]]
   [reagent.dom :as dom]
   [luminus-websockets.websockets :as ws]))


(defonce messages (atom []))

(defn message-list []
  [:ul.messages
   (for [[i message] (map-indexed vector @messages)]
     ^{:key i}
     [:li message])])

(defn message-input []
  (let [value (atom nil)]
    (fn []
      [:input.form-control
       {:type :text
        :placeholder "type here"
        :value @value
        :on-change #(reset! value (-> % .-target .-value))
        :on-key-down
        #(when (= (.-keyCode %) 13)
           (ws/send-transit-msg! {:message @value})
           (reset! value nil))}])))

(defn home-page []
  [:div.container
   [:div.row
    [:div.col-md-12
     [:h2 "WELCOME EARTHLING"]]]
   [:div.row
    [:div.col-sm-6
     [message-list]]
    [:div.col-sm-6
     [message-input]]]])


(defn update-messages!
  "Incoming message handler"
  [{:keys [message]}]
  (swap! messages #(vec (take 10 (conj % message)))))


(defn mount-components []
  (dom/render [home-page] (js/document.getElementById "app")))

(defn init! []
  (ws/make-websocket! (str "ws://" (.-host js/location) "/ws") update-messages!)
  (mount-components))
