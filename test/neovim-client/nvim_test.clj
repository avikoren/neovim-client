(ns neovim-client.nvim-test
  (:require [clojure.test :as test :refer [deftest use-fixtures is]]
            [neovim-client.nvim :as client.nvim]))

(defn- neovim
  "Make a neovim subprocess."
  []
  (let [p (.exec (Runtime/getRuntime) "nvim --embed")]
    {:process p
     :in (.getInputStream p)
     :out (.getOutputStream p)}))

(defn- stop-neovim
  [{:keys [process]}]
  (.destroy process))

(defmacro with-neovim
  [& body]
  `(let [~'*neovim* (neovim)]
     (try
       ~@body
       (finally (stop-neovim ~'*neovim*)))))

;; TODO - this one will be hard to test. From the client-library's perspective,
;; it will always have access to standard out. If it runs at all, standard
;; out will exist. So is this worth testing?
(deftest connect-fail-not-running
  (is true))

(deftest connect
  (with-neovim
    (let [{:keys [in out]} *neovim*]
      (is (not (client.nvim/version-supported?
                 (client.nvim/new* -1 in out false))))))

  (with-neovim
    (let [{:keys [in out]} *neovim*]
      (is (not (client.nvim/version-supported?
                 (client.nvim/new* 2 in out false))))))

  (with-neovim
    (let [{:keys [in out]} *neovim*]
      (is (client.nvim/version-supported?
            (client.nvim/new* 0 in out false)))))

  (with-neovim
    (let [{:keys [in out]} *neovim*]
      (is (client.nvim/version-supported?
            (client.nvim/new* 1 in out false))))))

#_(clojure.tools.namespace.repl/refresh)
#_(clojure.test/run-tests 'neovim-client.nvim-test)
