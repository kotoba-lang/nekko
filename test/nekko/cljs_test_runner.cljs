(ns nekko.cljs-test-runner
  "Real-ClojureScript test host for the async namespaces that nbb cannot run.

  Why this exists rather than adding to `test:async`: `nekko.key-journal` goes
  through `chain.core`, whose non-genesis commits encode `prev` as an
  `ipld.core/Link` — a `deftype`. Under nbb/sci that type's field is not
  readable (`ipld/link?` returns true while `ipld/link-cid` returns nil), so
  every commit after genesis throws. Real ClojureScript compiles the deftype
  properly and the same code passes. That is an nbb limitation, not a browser
  one, and the actual consumer is a browser bundle, so this host is the more
  faithful one anyway.

  Needs the west workspace: `:local` overrides the `io-multiformats` git pin,
  which is stale in a way real ClojureScript rejects outright ('Only one
  :require form is allowed per namespace definition' — sci tolerates it, cljs
  does not). It also needs `npm install` for @noble/*.

  Run: npm run test:cljs"
  (:require [cljs.test :as t :refer-macros [run-tests]]
            [nekko.key-journal-test]))

(defmethod t/report [:cljs.test/default :end-run-tests] [m]
  (set! (.-exitCode js/process) (if (t/successful? m) 0 1)))

(defn -main [& _]
  (run-tests 'nekko.key-journal-test))
