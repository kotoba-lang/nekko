#!/usr/bin/env nbb
;; The portable suite on nbb — no build step, no JVM.
;;
;; `test/nekko/cljs_test_runner.cljs` is the OTHER ClojureScript host, and the
;; split between them is not arbitrary. That one compiles through cljs.main
;; because `chain.core`'s non-genesis commits carry `prev` as an
;; `ipld.core/Link` deftype whose field sci cannot read; the namespaces that
;; walk a chain therefore cannot run here, and are listed there instead.
;; Everything else can, and until 2026-08-17 was running on the JVM only.
;;
;;   nbb --classpath "src:test:$(clojure -Spath)" test/run_portable.cljs
;;
;; The classpath needs `clojure -Spath` because this repo's siblings are git
;; deps; nbb does not resolve deps.edn. Filter it to directories — nbb cannot
;; load the maven jars in it.
;;
;; If a namespace here starts failing with `Cannot read properties of undefined
;; (reading 'lastIndexOf')`, it has grown a chain walk and belongs in the
;; compiled host, not in a reader conditional.
(require '[cljs.test :as t]
         '[kotoba-rad.canonical-test]
         '[nekko.announce-test]
         '[nekko.cacao-delegate-test]
         '[nekko.canonical-test]
         '[nekko.delegate-test]
         '[nekko.journal-test]
         '[nekko.push-gate-test]
         '[nekko.identity-test]
         '[nekko.private-object-test]
         '[nekko.recipient-grant-test]
         '[nekko.recovery-code-test]
         '[nekko.ref-event-test]
         '[nekko.sigref-test])

(defmethod t/report [:cljs.test/default :end-run-tests] [m]
  (when-not (t/successful? m) (set! (.-exitCode js/process) 1)))

(t/run-tests 'kotoba-rad.canonical-test
             'nekko.announce-test
             'nekko.cacao-delegate-test
             'nekko.canonical-test
             'nekko.delegate-test
             'nekko.journal-test
             'nekko.push-gate-test
             'nekko.identity-test
             'nekko.private-object-test
             'nekko.recipient-grant-test
             'nekko.recovery-code-test
             'nekko.ref-event-test
             'nekko.sigref-test)
