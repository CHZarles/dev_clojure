
待看：https://ithelp.ithome.com.tw/m/users/20151416/ironman/6756
TODO:https://kit-clj.github.io/docs/guestbook.html#guestbook_application

# prepare env




参考： https://clojure.org/guides/install_clojure#java

``` bash
curl -L -O https://github.com/clojure/brew-install/releases/latest/download/linux-install.sh
chmod +x linux-install.sh
sudo ./linux-install.sh
```
修改 /etc/manpath.config 
```
# /etc/manpath.config
MANPATH_MAP     /opt/infrastructure/clojure/bin /opt/infrastructure/clojure/man
```
看环境
```bash
clojure -Sdescribe
```


# vscode shortcut

```bash
# 执行 line 查看结果
Alt + Enter

# 清除行内显示的结果
Esc

# 显示悬停信息
Alt + Enter => Ctrl + k , Ctrl + I

# 强制删除配对括号
Alt + backspace/delete

# 格式化语法
Alt + shift + f

# 加载整个文件
Ctrl+Alt+C + Enter

# 查看 thread 的过程, 在对应的form后面按
Ctrl+Alt + Enter

# 从顶层Form  开始到光标处的内容
Shift+Alt + Enter

```

# Syntax Sample
more detais in : [Clojure101](Clojure101.md)
```clojure
(defn greet 
    "I'll greet you" 
    [s] 
    (str "Hello " s "!") 
)
```
- `defn` 
    - 是宏关键字
- `greet`
    - 宏名字
- `"I'll greet you"` 
    - 说明性字符串，描述函数作用
- `[s]` 
    - 参数vector，s是形参，这里只有一个参数，多个参数用空格间隔
- `(str "Hello " s "!") `  
    - 函数逻辑，用前缀表示法，调用 str 函数(将后续所有参数转成字符串)

```clojure

(map (fn [s]
         (if (< (count s) 5)
           (str "Give me " s "! ~•~ " (last s) "!")
           s))
       ["an R" "an E" "a  P" "an L" "What do you get?" "REPL!"])

```
- `(map (fn ...) [...])`
    - map内置函数,将后面函数应用到列表上的每一个元素，并返回一个新结果
    - 不会改变原始列表
- `["an R" "an E" "a  P" "an L" "What do you get?" "REPL!"]`
    - 字符串vector
- `(fn [s] ...)`
    - 一个匿名函数
    - `count` 这个函数返回字符串的字符数, e.g `(count "1 2 3 4 5") => 10`
    - `(if test-expr then-expr else-expr)`  if 表达式
    - `last expr` 返回值字符串最后一个字符


```clojure
(comment
  (println (greet "World"))
  :rcf)

```
- `(comment ...)` 它告诉编译器：“请忽略括号里的所有可执行代码，不要在加载文件时运行它们。
- `:rcf` —— 标记/约定

```clojure
(str 23 (apply + [2 3]) (:foo {:foo "foo"}))
```
-  `form` 要么是一个"单词/符号"，要么是一个被括号包裹的东西,包括`()`,`{}`,`[]`, 约等同 `https://en.wikipedia.org/wiki/S-expression`。
-  `:foo {}` 相当于对 `{}` 里面的字典取键`foo`
-  `{:foo "foo"}` 字典这不像传统的s-expression的定义, `{:a 1 :b 2 :c}`

```clojure
(Math/abs -1)
```
- `Math`属于`java.lang`
- `Math/abs` 相当于调用静态方法`Math.abs`

```clojure
(import 'java.util.Random)
(def rnd (Random.))
(.nextInt rnd 100)
```
- `import java.util.Random;`
- `Random rnd = new Random();`
- `rnd.nextInt(100);`


```clojure
(comment ; 3
  (defn- divisible
    "Is `n` divisible by `d`?"
    [n d]
    (zero? (mod n d)))

  (defn fizz-buzz [n] ; 2
    (cond ; 1 
      (divisible n (* 5 3)) "FizzBuzz"
      (divisible n 5)       "Buzz"
      (divisible n 3)       "Fizz"
      :else                  n))
  :rcf)
```
- `defn-`  Clojure 的宏，用于定义私有函数,
定义了一个私有函数 `divisible`
- `zero?` 是一个内置函数，判断是数字是否为0
- `cond` 是Clojure的条件分支语法，类似 `if-else`
    - 如果 `n` 能被3和5 整除, 返回 "FizzBuzz"
    - 只能被 3 整除 ，返回 "Buzz"
    - ...
    - 条件都不匹配，返回n




```clojure
(ns get-started.welcome-to-clojure-cn
  (:require [clojure.repl :refer [source apropos dir pst doc find-doc]]
            [clojure.string :as string]
            [clojure.test :refer [is are]]))

```
- `ns` 定义命名空间，
- `(:require ...)` 这是 `ns` 的一个选项，用于引入其他命名空间，具体语法 `(:require [namespace-name :options])`
    - `[clojure.repl :refer [source apropos dir pst doc find-doc]]` 引入了 clojure.rep 命名空间，并选择性地引用了其中的几个函数
    - `[clojure.string :as string]` 引入了 clojure.string 命名空间，并为其指定别名 `string`
    - `[clojure.test :refer [is are]]` 引入了 clojure.test 命名空间，并选择性地引用了其中的两个函数

