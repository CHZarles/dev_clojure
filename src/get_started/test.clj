(ns get-started.test)

(:5 {:foo 3 :5 7})


;; 定义一个 Map，包含不同类型的键
(def my-map
  {:keyword-key "This is a keyword key"
   'symbol-key "This is a symbol key"
   "string-key" "This is a string key"
   42 "This is a number key"
   [1 2 3] "This is a vector key"})

;; 测试访问不同类型的键
(println "Accessing keyword key:" (:keyword-key my-map))
(println "Accessing symbol key:" (my-map 'symbol-key))
(println "Accessing string key:" (my-map "string-key"))
(println "Accessing number key:" (my-map 42))
(println "Accessing vector key:" (my-map [1 2 3]))


(defonce x `(atom 0))
(reset! x 42)
@x  ; => 42


;; 定义一个使用 :keys 解构的简单函数
(defn greet-user [{:keys [name age]}]
  (println "Hello," name "! You are" age "years old."))

;; 定义一个使用 :strs 解构的函数 (注意 form-params 通常是字符串键的map)
(defn process-form-data [{{:strs [username email]} :form-params}]
  (println "Processing data for:" username ", Email:" email))

;; 定义一个展示 :or (默认值) 和 :as (保留原map) 用法的函数
(defn show-config [{:keys [host port] :or {port 8080} :as full-config}]
  (println "Host:" host ", Port (defaulted if needed):" port)
  (println "Full config was:" full-config))

;; --- 执行示例 ---

;; 示例 1: 调用 greet-user
;; 输入 map 包含解构所需的 key
(greet-user {:name "Alice" :age 30}) 
; 输出: Hello, Alice ! You are 30 years old.

;; 示例 2: 调用 process-form-data
;; 注意内部的 form-params 是一个字符串键的 map
(process-form-data {:form-params {"username" "bob" "email" "bob@example.com"}})
; 输出: Processing data for: bob , Email: bob@example.com

;; 示例 3: 调用 show-config
;; 提供了所有 key
(show-config {:host "localhost" :port 9000})
; 输出: Host: localhost , Port (defaulted if needed): 9000
; 输出: Full config was: {:host "localhost", :port 9000}

;; 示例 4: 调用 show-config，省略 port 以触发默认值
(show-config {:host "example.com"})
; 输出: Host: example.com , Port (defaulted if needed): 8080
; 输出: Full config was: {:host "example.com"}
