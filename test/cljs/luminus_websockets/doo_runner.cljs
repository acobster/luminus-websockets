(ns luminus-websockets.doo-runner
  (:require [doo.runner :refer-macros [doo-tests]]
            [luminus-websockets.core-test]))

(doo-tests 'luminus-websockets.core-test)

