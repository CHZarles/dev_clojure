(ns get-started.welcome-to-clojure-cn
  (:require [clojure.repl :refer [source apropos dir pst doc find-doc]]
            [clojure.string :as string]
            [clojure.test :refer [is are]]))

;; Welcome to Clojure! ♥️

;; 首先加载这个文件。
;; Ctrl+Alt+C Enter
;; （Alt 键有时也被称为 Option 或 Opt）

;; 然后使用 Alt+Enter 求值这个表达式：

"Hello World"

;; 这就是任何语言中最简洁的 Hello World。
;; 注意这里没有括号。😀

;; 本指南将帮助你建立对 Clojure 语言的基本理解。
;; 这里的“基本”不是指内容浅显，而是指从第一性原理出发打好基础，
;; 让你未来的 Clojure 学习之路更容易理解。

;; 有了这些基础，你将更容易凭直觉写出代码，
;; 更清楚如何提出问题、如何有效搜索资料、
;; 以及如何读懂你在网上遇到的代码，等等。

;; 这里会有一些链接，Ctrl/Cmd + 点击即可在浏览器中打开。
;; 第一个链接：
;; https://clojure.org/guides/learn/syntax
;; 在那里你可以阅读更多关于本指南中提到的概念。

;; 使用本指南的方式是：阅读概念说明，同时亲自求值示例代码。
;; 有时文中会有练习题，但别只做这些题目。
;; 强烈鼓励你修改示例代码，自己添加新代码并求值。
;; 先来热身，求值下面这个：

(comment
  (str "Welcome"
       " to "
       "Clojure!" 
       " "
       "♥️"))

;; 然后试着在中间随意插入一些数字，再求值看看会发生什么。

;; 注意：本指南仍在开发中……
;; 下次你创建 Getting Started REPL 项目时，它可能已经更新了。😀
;; 你也可以随时在这里查看最新版本：
;; https://github.com/BetterThanTomorrow/dram/blob/dev/drams/calva_getting_started/src/get_started/welcome_to_clojure.clj

(comment
  ;; = 表达式 =
  ;; 在 Clojure 中，一切都是表达式。
  ;; （没有语句。）除非求值时出错，否则每个表达式总会有返回值（有时是 `nil`）。
  
  ;; 一个重要特性是：表达式的返回值永远是最后求值的那个形式。例如：
  
  (defn last-eval-wins []
    (println 'side-effect-1)
    1
    (println 'side-effect-2)
    2)

  ;; 这定义了一个名为 `last-eval-wins` 的函数，不接受参数，函数体里有四个表达式。
  ;; （我们稍后会回到函数定义。）
  
  ;; 调用这个函数：
  
  (last-eval-wins) ; <- 求值看看 😄
  
  ;; 会让函数体中的四个表达式全部被求值。调用的返回值是最后求值的那个表达式。
  
  ;; 在输出窗口你还会看到 `println` 的副作用输出。
  ;; `println` 本身也是表达式，返回值是 `nil`。
  ;;(def prints-this-evaluates-to-nil 0)
  (println 'prints-this-evaluates-to-nil)

  ;; 表达式由字面量（求值后仍是自身）和/或以下几种调用组成：
  ;; * 特殊形式（special forms）
  ;; * 宏（macros）
  ;; * 函数（functions）
  
  ;; 开头的 “Hello World” 是一个字符串字面量（因此求值后仍是自身）。
  ;; 字面量我们下一节会详细讲。
  
  ;; 调用写成列表形式，第一个元素是要调用的东西。
  
  (def foo "foo") ; 调用特殊形式 `def`，返回值是它创建的 var（稍后详解）
  
  (for [x '(1 2 3)  ; 调用宏 `for`（列表推导）
        y '(:a :b)]
    [x y])

  (str 1 2 3) ; 调用函数 `str`，参数是 1、2、3。
  
  ;; 只有函数可以作为值传递
  
  (map str [:foo :bar])    ; 正常
  (map for [:foo :bar])    ; 宏不能当作值
  (map def [:foo :bar])    ; def 甚至不是符号
  
  )

(comment
  ;; = 字面量 =
  ;; 字面量求值后仍是自身。
  ;; （记住你的好朋友：Alt+Enter 和 Ctrl+Enter）

  ;; 数值类型
  18        ; 整数
  -1.8      ; 浮点数
  0.18e2    ; 科学计数法
  18.0M     ; 大十进制数
  18/324    ; 有理数
  18N       ; 大整数
  0x12      ; 十六进制
  022       ; 八进制
  2r10010   ; 二进制

  ;; 字符类型
  "hello"         ; 字符串
  \e              ; 字符
  #"[0-9]+"       ; 正则表达式

  ;; 符号和标识符
  map             ; 符号
  +               ; 符号 - 大多数标点符号都允许
  clojure.core/+  ; 带命名空间的符号
  nil             ; 空值（LISP 传统命名）
  true false      ; 布尔值
  :alpha          ; 关键字
  :release/alpha  ; 带命名空间的关键字
  ::alpha         ; 当前命名空间的关键字

  ;; == 关键字 ==
  ;; 关键字以 `:` 开头。它们本身就是一种类型，
  ;; 常被用作标识符或 map 的键（后面会讲 map）。
  ;; 关键字在内存和速度上都非常高效。

  ;; 同一个关键字当然等于自身

  (= :foo :foo)

  ;; 而且它们是相同的（identical）

  (identical? :foo :foo)

  ;; 这意味着它们是同一个对象，占据同一块极小的内存。
  ;; 即使你用非字面量方式构造关键字，它仍然与字面量形式相同

  (identical? (keyword "foo") :foo)

  ;; 这在整个 Clojure 程序中都成立。关键字是全局的。
  ;; 它们支持命名空间语法，以便你控制命名冲突。

  ;; 关键字本身也是函数！稍后会讲。
  ;; 目前只需知道关键字在大多数 Clojure 程序中扮演非常特殊且重要的角色。

  ;; == 字符串 ==
  ;; 在原子字面量和集合之间，还有字符串。
  ;; 字符串有时会被当作序列处理（后面会讲这个强大的抽象）。
  ;; 字符串用双引号括起来。

  "A string can be
   multi-line, but will contain any leading spaces."
  "Write strings
like this, if leading spaces are no-no."

  ;; （单引号有别的用途，稍后你会看到。）
  )

;; = 小贴士：练习 Clojure 的工具 =
;;
;; 下面这两个资源与本指南配合使用效果极好：
;;
;; Rich4Clojure：在 VS Code 中用 4Clojure 题目练习 Clojure
;; * https://github.com/PEZ/Rich4Clojure
;; 用 Koans 学习 Clojure（VS Code + Calva）
;; * https://github.com/DanBunea/clojure-koans
;;
;; 它们都让你在编辑器中连接 REPL，通过解决问题来学习和练习 Clojure。
;; 一种用法是把它们开在不同的 VS Code 窗口，与本指南来回切换，
;; 学到新知识时就去练习，看到需要更多练习时就去做题。

(comment
  ;; = 命名空间 =
  ;; 命名空间非常重要，但本指南不会深入讲解。
  ;; 官方文档讲得最好：
  ;; https://clojure.org/reference/namespaces
  ;;
  ;; 但有些基础知识我们必须了解……
  ;; Clojure 中的符号都定义在命名空间中（用 `def` 特殊形式），
  ;; 其他命名空间可以访问它们。

  (def foo-2 "foo")

  ;; 还有“当前命名空间”的概念（有点像 shell 中的当前工作目录）。
  ;; 上面求值 `def` 时，你会看到 `foo-2` 被定义在哪个命名空间。

  ;; 要使用其他命名空间的符号，必须先定义，否则编译器会报错

  foo-3

  ;; 命名空间本身也必须已经创建

  some-namespace/foo

  ;; 如果你已经加载了 `hello_repl.clj` 文件，
  ;; `hello-repl` 命名空间就创建好了，其顶级符号也已定义。

  get-started.hello-repl/greet

  (get-started.hello-repl/greet "from the welcome-to-clojure namespace")

  ;; 如果报错，你需要先加载 `hello_repl.clj` 文件，
  ;; 或者至少求值它的 `ns` 形式和 `greet` 定义。

  ;; 但不建议依赖某个命名空间已经存在，这会让代码变得脆弱。
  ;; 更好的做法是 `require` 该命名空间。
  ;; 如果还没加载，可以一次性完成：

  (require 'get-started.hello-paredit :reload)

  get-started.hello-paredit/strict-greet
  (get-started.hello-paredit/strict-greet "World")

  ;; 在大多数 Clojure 代码中，你会把代码组织成多个文件，每个文件一个命名空间，
  ;; 用 `ns` 形式（大多数 Clojure 文件开头都有）来 `:require` 需要的命名空间，
  ;; 给它们起别名，有时还会 `:refer` 某些符号，这样就不用写命名空间前缀了。
  ;; 查看本文件的 `ns` 形式，你就知道为什么下面这些代码能正常编译：

  (doc require) ; 查看输出窗口
  (string/split "foo:bar:baz" #":")

  ;; 另见：
  ;; https://clojuredocs.org/clojure.core/ns

  ;; 任何命名空间都可以在 REPL 中创建。
  ;; 但当通过 `require` 或 `use` 或 `ns` 形式引用命名空间时，
  ;; Clojure Reader 会在类路径中查找对应的文件。
  ;; 命名空间中的点分隔目录，短横线会被替换成下划线。
  ;; 例如类路径中有 `src` 目录，文件在 `src/foo/bar_baz.clj`，
  ;; 该文件应有如下 `ns` 形式：

  ;(ns foo.bar-baz ,,,)

  ;; 你可以用下面方式 require 它：

  ;(require 'foo.bar-baz)

  ;; 或者：

  ;(ns welcome-to-clojure
  ;  (:require [foo.bar-baz]))

  ;; 如果你求值这些 require，REPL 会报错并告诉你它查找了哪些文件。

  ;; 两个常见错误：
  ;; 1. 文件名用了短横线而不是下划线。
  ;; 2. 在 `ns` 形式中用了 `(require ...)` 而不是 `(:require ...)`。

  ;; `ns` 形式功能强大，有点复杂。这里有个不错的速查表：
  ;; https://gist.github.com/ghoseb/287710/

  ;; === 带命名空间的关键字 ===
  ;; 关键字也可以带命名空间，但它们不像符号那样注册在命名空间中，
  ;; 所以你可以直接使用，不需要先创建命名空间。

  :foo-whatever
  :whatever-namespace/foo

  ;; 当前命名空间的概念对关键字也适用，双冒号前缀会展开为 `:<当前命名空间>/foo`：

  ::foo

  ;; 这点很重要。`:foo` 在任何命名空间中都指同一个关键字。
  ;; 而 `::foo` 则会因当前命名空间不同而不同。
  )

(comment
  ;; = 集合 =
  ;; Clojure 为四种集合类型提供了字面量语法。
  ;; 它们求值后仍是自身。

  '(1 2 3)     ; 列表（带引用的列表，下面会讲）
  [1 2 3]      ; 向量
  #{1 2 3}     ; 集合
  {:a 1 :b 2}  ; 映射

  ;; 它们可以相互嵌套

  {:foo [1 2]
   :bar #{1 2}}

  ;; 在 Clojure 中，我们大多数操作都只用这几种集合。
  ;; 字面量集合 + 函数就是我们的主要工具。
  )

(comment
  ;; = 函数 =
  ;; 到目前为止你能求值所有示例，是因为我们对列表加了引用。
  ;; 不带引用的列表是这样的：

  (1 2 3)

  ;; 如果你求值，会报错：
  ;; => class java.lang.Long cannot be cast to class clojure.lang.IFn
  ;; （linter 已经警告你了）
  ;; 求值不带引用的列表时，列表第一个元素被视为“函数位置”。
  ;; Clojure 会尝试把 `1` 当作函数调用，但 `1` 不是函数。
  ;; 你可能已经猜到，Clojure 程序本质上就是数据？没错。
  ;; Clojure 代码就是数据。更进一步，Clojure 是同像性（homoiconic）的：
  ;; https://wiki.c2.com/?HomoiconicLanguages
  ;; 这赋予了强大的宏能力，后面会讲。

  ;; 下面是一些第一个元素是正确函数的列表：

  (str 1 2 3 4 5 :foo)
  (< 1 2 3 4 5)
  (*)
  (= "1"
     (str "1")
     (str \1))
  (println "From Clojure with ♥️")
  (reverse [5 4 3 2 1])

  ;; 第一个位置之后的所有元素都会作为参数传给函数。

  ;; 注意：我有时会把字面量、符号、列表和其他集合字面量统称为“形式”（forms），
  ;; 也叫 s-expressions：
  ;; https://en.wikipedia.org/wiki/S-expression
  ;; 上面 `(str 1 2 3 4 5 :foo)` 是一个形式，`str`、`1`、`:foo` 也是。

  ;; 使用宏 `defn` 可以定义新函数并绑定到当前命名空间的符号。
  ;; 它非常灵活。最简单的用法如下：

  (defn add2
    [arg]
    (+ arg 2))

  ;; 这定义了一个名为 `add2` 的函数，接受一个参数。
  ;; 函数体调用核心函数 `+`，参数是 `arg` 和 2。
  ;; 求值后会定义函数，你会看到：
  ;; => #'welcome-to-clojure/add2
  ;; 这是一个持有函数值的 var。
  ;; 现在可以用符号 `add2` 引用这个 var。
  ;; 把 `add2` 放在函数位置，给它参数 3，求值会得到什么？

  (add2 3)

  ;; Clojure 拥有丰富的核心函数和宏库。
  ;; 查看：https://clojuredocs.org （社区驱动的核心库及其他文档搜索引擎）。
  )

(comment
  ;; = 特殊形式和宏 =
  ;; 核心库由库中的函数和宏组成。
  ;; 支撑整个库的是大约 15 个内置的原始形式，也就是“特殊形式”。

  ;; 你已经见过一个特殊形式了：

  (quote (1 2 3))

  ;; `quote` 符号的 doc hover 会告诉你它是特殊形式。

  ;; 想知道之前在哪里见过吗？
  ;; 我用了它的简写语法：

  '(1 2 3)

  ;; 用 `=` 函数验证它们是等价的：

  (= (quote (1 2 3))
     '(1 2 3))

  ;; Clojure 具有值语义。
  ;; 任何求值后相等的数据结构都是相等的，无论多深多大。

  (= [1 [1 #{1 {:a 1 :b '(:foo bar)}}]]
     [1 [1 #{1 {:a (- 3 2) :b (quote (:foo bar))}}]])

  ;; ……刚才有点跑题，回到特殊形式。
  ;; 官方文档：
  ;; https://clojure.org/reference/special_forms#_other_special_forms

  ;; 一个非常重要的特殊形式是 `fn`（实际上有四个，但这里先当作一个）。
  ;; 没有它我们就无法定义新函数。
  ;; 下面的形式求值后得到一个把参数加 2 的函数。

  (fn [arg] (+ arg 2))

  ;; 用参数 3 调用它：

  ((fn [arg] (+ arg 2)) 3)

  ;; 另一个特殊形式是 `def`，用来定义东西，给它们命名空间中的名字。

  (def foo :foo)


  ;; “定义东西”意味着创建一个 var 保存值，并把符号绑定到该 var。
  ;; 求值符号会从绑定的 var 中取值。

  foo

  ;; 也可以用 `var` 特殊形式访问 var。

  (var foo)

  ;; 更常见的是 var 引用的简写：

  #'foo

  ;; 用这两个特殊形式我们可以定义函数：

  (def add2-2 (fn [arg] (+ arg 2)))
  (add2-2 3)

  ;; 这就是宏 `defn` 做的事。
  ;; 我们通常用之前见到的方式定义函数：

  (defn add2-3
    [arg]
    (+ arg 2))

  ;; 可以用 `macroexpand` 函数查看宏展开的结果：

  (macroexpand '(defn add2-3
                  [arg]
                  (+ arg 2)))

  ;; 另一个超级重要的特殊形式： 
  (if 'test
    'value-if-true
    'value-if-false)

  ;; `macroexpand` 对它不起作用，因为 `if` 不是宏：

  (macroexpand '(if test
                  value-if-true
                  value-if-false))

  ;; （它也不是函数）

  ;; 有趣的事实：除了 `case`，Clojure 中所有的条件和控制流结构都是用 `if` 实现的：

  (macroexpand '(when test
                  value-if-true))

  (macroexpand '(or a b))

  (require 'clojure.walk) ;; 需要先求值这行

  (clojure.walk/macroexpand-all '(or a b)) ;; 再求值这行

  (macroexpand '(cond
                  y value-if-y
                  z value-if-z
                  :else value-if-x-neither-y-nor-z))

  (clojure.walk/macroexpand-all '(cond
                                   y value-if-y
                                   z value-if-z
                                   :else value-if-x-neither-y-nor-z))

  ;; 编程语言需要条件判断。
  ;; 但 Clojure 核心几乎只靠 `if` 就够了。
  ;; 几乎是。前面说过，`case` 是个例外。
  ;; 至少在 JVM 上，Clojure 用跳转表实现它：

  (macroexpand '(case x
                  y value-if-x-is-y
                  z value-if-x-is-z
                  value-if-x-is-neither-y-nor-z))

  ;; 我们稍后会回到 `if` 和条件判断。

  ;; == `let` ==
  ;; `let` 是一个特殊形式，允许你在形式体内绑定局部变量。

  (let [x 1
        y 2]
    (str x y))

  ;; 绑定以向量形式作为第一个“参数”提供。
  ;; 这是许多特殊形式和宏用来定义绑定的常见模式。
  ;; 它类似于其他语言的词法作用域（尽管这里更像是结构化作用域）。
  ;; 同级或父级形式看不到这些绑定。
  ;; 示例：

  (do
    (def x :namespace-x)
    (println "`x` in `do` _before_ `let`: " x)
    (let [x :let-x]
      (println "`x` from `let`: " x))
    (println "`x` in `do`, _after_ `let`: " x))

  (println "`x` _outside_ `do`: " x)

  ;; 如前所述，`def` 特殊形式是“全局”（带命名空间）的定义。

  ;; 如果你按照说明检查过这里提到的东西（比如 Ctrl/Cmd+点击代码中的 `let`），
  ;; 你会发现 `core.clj` 中 `let` 被定义为宏。
  ;; 别在意，官方文档仍把它称为特殊形式：
  ;; https://clojure.org/reference/special_forms#let

  ;; 用 `let` 结束特殊形式部分。
  ;; 特殊形式加上 Clojure 如何读取和求值代码，构成了 Clojure 语言本身。
  ;; 下一层构建块是宏。

  ;; 但先让我们看看代码是如何被读取的……
  )

(comment
  ;; = Reader =
  ;; https://clojure.org/reference/reader
  ;; Clojure Reader 负责把文本读取成数据，交给编译器处理。
  ;; 字面量、符号、字符串、列表、向量、映射、集合都是在这里被解析和重组的，
  ;; 同时判断哪些是函数、宏还是特殊形式。

  ;; 空白字符起关键作用，还有一些额外的语法规则。

  ;; == 空白字符 ==
  ;; 大多数你认为的空白字符都是空白，
  ;; 而且 Clojure 作为 LISP，不需要逗号分隔列表项。
  ;; 但逗号也可以用来分隔，因为逗号也是空白。

  (= '(1 2 3)
     '(1,2,3)
     '(1, 2, 3)
     '(1,,,,2,,,,3))

  ;; （Clojure 没有运算符，`=` 是函数。它会检查所有参数是否相等。）

  ;; == 行注释 ==
  ;; Reader 会跳过分号开始的行后面的所有内容。
  ;; 这是非结构化注释，如果你把一个形式的结束括号注释掉，结构就会破坏。

  (range 1 ; 10)
  ;;         ^ 修复结构。

  ;; 如果你去掉上面开头的分号，记得也去掉这里的结束括号。

  ;; 因为整行都被忽略，你可以写任意多个分号。
  ;;;;;;;;;; (被 Reader 跳过)
  ;; 通常用两个分号表示整行注释。

  ;; == 额外语法 ===
  ;; 我们已经见过单引号

  'something

  ;; 它被转换为

  (quote something)

  ;; `quote` 用来阻止 Reader 把内容当作需要求值的代码处理。
  ;; 看看不加引号求值会怎样：

  something

  ;; 以及下面两个的区别：

  (1 2 3 4)
  '(1 2 3 4)

  ;; 还有其他引用和拼接符号，本指南不展开。

  ;; === 解引用（Deref） ===
  ;; Clojure 有引用类型，我们稍后会简要介绍最常见的 `atom`。

  (def an-atom (atom [1 2 3]))
  (type an-atom)

  ;; 取引用类型的值：

  (deref an-atom)
  (type (deref an-atom))

  ;; `deref` 用于很多引用类型，包括 future：
  ;; https://clojure.org/reference/refs
  ;; https://clojure.org/about/concurrent_programming

  ;; `deref` 很常用，所以有简写语法

  @an-atom
  (= (deref an-atom)
     @an-atom)

  ;; 常见错误是忘记解引用

  (first an-atom)
  (first @an-atom)

  ;; === 分派符（#） ===
  ;; # 符号经常出现，有特殊作用，也叫 Dispatch。
  ;; 根据后面的字符，会触发不同功能。下面列出几个有用的：

  ;; 正则表达式有字面量语法，用 # 开头

  #"reg(?:ular )?exp(?:ression)?"

  ;; 正则在宿主平台处理，所以这里是 Java 正则。
  ;; 如果你求值了上面的正则，可以测试它。

  (re-seq *1 "regexp regular expression")

  ;; `*1` 是特殊变量，保存上一次求值结果。
  ;; 直接使用正则可能更容易写对：

  (re-seq #"fooo*" "fo foo fooo")
  (re-find #"fooo*" "fo foo fooo")

  ;; 如果 # 后面是 `(`，Reader 会期待一个函数体。

  #(+ % 2)

  ;; 这是“函数字面量”语法，等价于：

  (fn [arg] (+ arg 2))

  ;; 不允许嵌套函数字面量

  ;(#(+ % (#(- % 2) 3)))

  ;; （谢天谢地）

  ;; 除了集合、正则、函数字面量，我们还见过 var 引用

  #'add2

  ;; 前面也简单提过 `var`。
  ;; 建议你回顾一下，var 是非常重要的概念。
  ;; https://clojure.org/reference/vars

  ;; 还有一个非常有用的 # 分派符，用来让 Reader 忽略下一个形式

  #_(println "Reader 不会把这行函数调用发送给编译器") "这行不会被忽略"

  ;; 测试方法：选中忽略标记和函数调用以及字符串，用 Alt+Enter 发送给 Reader，
  ;; 它会忽略函数调用，只求值字符串。

  ;; 因为 #_ 忽略下一个形式，所以是结构化注释，常用于临时禁用代码或数据

  (str "a" "b" #_(str 1 2 3 [4 5 6]) "c")

  ;; 忽略标记可以叠加

  (str "a" #_#_"b" (str 1 2 3 [4 5 6]) "c")

  ;; 注意 Reader 仍会读取被忽略的形式。
  ;; 如果里面有语法错误，Reader 还是会报错。
  ;; 选中从标记到字符串的部分，用 Ctrl+Enter 测试

  ;#_(#(+ % (#(- % 2) 3))) "foo"

  ;; 另外两个常见的 # 变体是命名空间映射关键字简写和标签字面量。
  ;; 先说前者：

  (= #:foo {:bar 'bar
            :baz 'baz}
     {:foo/bar 'bar
      :foo/baz 'baz})

  ;; 与 #: 无关，还有一种带命名空间关键字的简写。
  ;; 双冒号关键字会自动使用当前命名空间

  ::foo
  (= ::foo :calva-getting-started.src.get-started.welcome-to-clojure/foo)

  ;; 再说标签字面量。
  ;; 它是一种调用绑定到标签的函数的方式。
  ;; https://clojure.org/reference/reader#tagged_literals
  ;; 也叫数据读取器，你可以自己定义。
  ;; 这里只介绍两个内置的。

  ;; #inst 把后面的字符串转为时间点（instant）

  #inst "2018-03-28T10:48:00.000"
  (type *1)

  ;; #uuid 把字符串转为 UUID

  #uuid "0000000-0000-0000-0000-000000000016"
  (java.util.UUID/fromString "0000000-0000-0000-0000-000000000016")

  ;; 你现在已经能读懂（作为 Clojure Reader）大多数 Clojure 代码了。
  ;; 不过我们先跳过主机平台互操作的语法糖和特殊形式。
  ;; https://clojure.org/reference/java_interop
  ;; 先偷看一下：

  (.before #inst "2018-03-28T10:48:00.000"
           #inst "2021-02-17T00:27:00.000")

  ;; 这调用了 2018 年日期对象的 `before` 方法，参数是 2021 年的日期。
  ;; 你会在本指南中看到更多 Java 互操作，会发现主机平台非常容易访问。
  ;; ClojureScript 和 Clojure CLR 也是如此。

  ;; 再次推荐关于 Reader 的重要资源：
  ;; https://clojure.org/reference/reader
  ;; 以及关于所有奇怪字符的指南：
  ;; https://clojure.org/guides/weird_characters
  )

(comment
  ;; = 宏 =
  ;; Clojure 有强大的数据转换能力，稍后会接触。
  ;; 这里要强调的是，这种能力还可以用来扩展语言本身。
  ;; 因为 Clojure 代码是结构化的，且代码即数据，
  ;; 我们可以用 Clojure 从 Clojure 代码生成 Clojure 代码。
  ;; 类似 C 的 `#pragma` 预处理器，但更方便、更强大。
  ;; 你会逐渐爱上并认出很多 Clojure 特性其实是用 Clojure 自己写的宏。

  ;; 本指南主要想让你知道宏是存在的，
  ;; 帮助你快速辨认出当前使用的是宏还是函数。
  ;; 我不会教你如何写宏。

  ;; 区分很重要，因为虽然宏调用看起来很像函数调用，
  ;; 但宏不是一等公民，不能作为参数传递或作为结果返回。
  ;; “一等公民”会在后面函数部分讲。

  ;; == `when` ==
  ;; 先简单看一下宏 `when`。
  ;; 这个宏让代码更易读。
  ;; 比如你想条件性地求值某些东西。
  ;; 你已经知道有特殊形式 `if` 可以做到：

  (if 'this-is-true
    'evaluate-this
    'else-evaluate-this)

  ;; 如果 else 分支什么都不做，`if` 允许省略：

  (if 'this-is-true
    'evaluate-this)

  ;; 这样可以，但阅读时需要额外注意没有 else 分支。
  ;; 短示例还好，真实代码容易混乱。
  ;; 你可以写成：

  (if 'this-is-true
    'evaluate-this
    nil)

  ;; 但这有点傻。
  ;; 有没有办法明确告诉阅读代码的人没有 else 分支？有！

  (when 'this-is-true
    'evaluate-this)

  ;; 查看 `when` 的定义（Ctrl/Cmd+点击跳转到 core.clj），
  ;; 或者用 `macroexpand`：

  (macroexpand '(when 'this-is-true
                  'evaluate-this))

  ;; 你会发现 `when` 用 `(do ...)` 包裹了主体，
  ;; `do` 是一个特殊形式，允许求值多个表达式，返回最后一个的结果。
  ;; https://clojuredocs.org/clojure.core/do
  ;; `do` 很适合在开发时打印调试信息，同时返回结果。

  (do (println "The quick brown fox jumps over the lazy dog")
      (+ 2 2))

  ;; `when` 让你可以这样写：

  (when 'this-is-true
    (println "The quick brown fox jumps over the lazy dog")
    (+ 2 2))

  ;; 不用 `when` 的话要写：

  (if 'this-is-true
    (do
      (println "The quick brown fox jumps over the lazy dog")
      (+ 2 2)))

  ;; 这里 `when` 既减少了扫描 else 分支的负担，也省掉了 `do`。

  ;; 对宏来说，`when` 是最简单的之一。
  ;; 它用 `if` 和 `do` 两个特殊形式组合出更易写易读的代码。

  ;; == `for` ==
  ;; 宏 `for` 真正展示了用 Clojure 扩展 Clojure 的能力。
  ;; 你可能以为它像其他语言的 for 循环，但 Clojure 没有 for 循环。
  ;; `for` 是列表推导（如果你用过 Python，就是那种列表推导）。
  ;; 下面是计算两个向量的笛卡尔积：

  (for [x [1 2 3]
        y [1 2 3 4]]
    [x y])

  ;; 还记得 `let` 吗？`for` 类似，只不过 `x` 和 `y` 会依次绑定到每个序列的值，
  ;; 主体会在所有组合上求值。

  ;; 所有值？`for` 还支持过滤结果：

  (for [x [1 2 3]
        y [1 2 3 4]
        :when (not= x y)]
    [x y])

  ;; 你可以在推导中绑定变量存储中间结果，使代码更清晰

  (for [x [1 2 3]
        y [1 2 3 4]
        :let [d' (- x y)
              d (Math/abs d')]]
    d)

  ;; 等价于：

  (for [x [1 2 3]
        y [1 2 3 4]]
    (Math/abs (- x y)))

  ;; 这个例子中哪个更可读见仁见智…… ¯\_(ツ)_/¯

  ;; 注意上面变量名 `d'`：`d'` 只是普通符号名。
  ;; 单引号只有在开头才有特殊含义。

  ;; 过滤和绑定可以一起用。
  ;; 练习：用 `:let` 和 `:when` 让这个推导返回所有和为奇数的 `[x y]`。
  ;; `+` 和 `odd?` 是你的朋友。

  (for [x [1 2 3]
        y [1 2 3 4]]
    [x y])

  ;; （是的，不用 `:let` 或 `:when` 也能解，迁就我一下 😎）

  ;; 推荐视频：Clojure 列表推导入门
  ;; https://www.youtube.com/watch?v=5lvV9ICwaMo
  ;; 以及 ClojureDocs 上 `for` 的例子和技巧：
  ;; https://clojuredocs.org/clojure.core/for

  ;; 注意 `let` 和 `for` 看起来像函数，但不是。
  ;; 如果你把未定义符号传给函数，编译器会报错。
  ;; 下面是合法代码：

  (let [abc 1]
    2)

  ;; 这不是：

  (str [abc 1]
       1)

  ;; （注意 clj-kondo linter 把第一个标为警告，第二个标为错误）
  ;; 宏扩展了 Clojure 编译器。
  ;; https://clojure.org/reference/macros

  ;; == 线程宏 ==
  ;; 宏可以完全重排你的代码。
  ;; 内置的“线程”宏就是这样。
  ;; 当函数调用嵌套很深时，阅读和括号匹配会变得困难：

  (Math/abs
   (apply -
          (:d (zipmap
               [:a :b :c :d]
               (partition 2 [1 1 2 3 5 8 13 21])))))

  ;; Clojure 从最内层向外读，随着经验增加会越来越容易，
  ;; 但有经验的 Clojure 程序员还是会觉得下面这样更易读：

  (->> [1 1 2 3 5 8 13 21]
       (partition 2)
       (zipmap [:a :b :c :d])
       :d
       (apply -)
       (Math/abs))

  ;; 一起读一下。
  ;; 使用线程尾宏 `->>`，它把第一个参数插入到后续每个形式的最后一个参数位置。
  ;; 第一步单独看：

  (->> [1 1 2 3 5 8 13 21]
       (partition 2))

  ;; 相当于：

  (partition 2 [1 1 2 3 5 8 13 21])

  ;; 分割成 2 个一组 => `((1 1) (2 3) (5 8) (13 21))`
  ;; 然后这个新列表被插入到下一个函数的最后：

  (zipmap [:a :b :c :d] '((1 1) (2 3) (5 8) (13 21)))

  ;; 生成映射 `{:a (1 1), :b (2 3), :c (5 8), :d (13 21)}`
  ;; 然后取 `:d` => `(13 21)`
  ;; 再 `(apply - '(13 21))` => `-8`
  ;; 最后 `(Math/abs -8)` => `8` 🎉

  ;; （很多支持 Clojure 的编辑器，包括 Calva，都有“展开线程”和“嵌套转线程”的命令。
  ;; 在命令面板搜索 “thread”。）
  ;; https://github.com/clojure-emacs/clj-refactor.el/wiki/cljr-unwind-all

  ;; 还有线程首宏 `->`：
  ;; https://clojuredocs.org/clojure.core/-%3E
  ;; 有时你不想首也不想尾，还有 `as->` 宏让你绑定变量名随意放置。

  (as-> 15 foo
    (range 1 foo 3)
    (interpose ":" foo))

  ;; https://clojuredocs.org/clojure.core/as-%3E

  ;; 很多人利用符号命名灵活性，我常用 `$`：

  (as-> 15 $
    (range 1 $ 3)
    (interpose ":" $))

  ;; 其他人用其他名字 😄

  (as-> 15 <>
    (range 1 <> 3)
    (interpose ":" <>))

  ;; 我觉得表情符号要避免，官方文档只提到字母数字加上：
  ;; `*`, `+`, `!`, `-`, `_`, `'`, `?`, `<`, `>`, `=`
  ;; （连 `$` 都不在列），但这里还是来一个：

  (as-> 15 ❤️
    (range 1 ❤️ 3)
    (interpose ":" ❤️))

  ;; 其他核心线程宏还有：
  ;; `cond->`, `cond->>`, `some->`, `some->>`
  ;; https://clojuredocs.org/clojure.core/cond-%3E

  ;; 强烈建议把 ClojureDocs 的例子复制过来玩。
  ;; 这里有一个：

  (cond-> 1        ; 从 1 开始
    true inc       ; 条件为真 → (inc 1) => 2
    false (* 42)   ; 条件为假 → 跳过
    (= 2 2) (* 3)) ; (= 2 2) 为真 → (* 2 3) => 6

  ;; 查看 Stuart Sierra 的“Threading with Style”，
  ;; 了解线程宏的惯用法：
  ;; https://stuartsierra.com/2018/07/06/threading-with-style
  )

;; 特殊形式、Reader 特殊语法和宏共同奠定了 Clojure 语言的基础。
;; 你当然可以用库（包括宏）或自己写宏来进一步扩展语言。
;; 但核心语言加上它的宏已经非常富有表现力。
;; 采用数据导向的方法往往就足够了，甚至是首选，而不是写更多宏。

;; 接下来是流程控制！

(comment
  ;; = 流程控制、条件判断、分支 =
  ;; Clojure 在流程控制方面比大多数语言更丰富。
  ;; 几乎所有核心库的控制结构都是用原始特殊形式 `if` 实现的。
  ;; 对我们 Clojure 程序员来说，`if` 仍是基础。
  ;; 它接受三个形式作为参数：
  ;; 1. 条件
  ;; 2. 条件为真时求值的形式
  ;; 3. 条件为假时的“else”分支
  ;; 掷骰子十到二十次，看看是不是六：

  (if (= 6 (inc (rand-int 6)))
    "One time out of six you get a six"
    "Five times out of six you get something else")

  ;; 因为 Clojure 没有语句，`if` 相当于 C 等语言中的三元表达式：
  ;;   test ? true-expression : false-expression
  ;; 伪代码：
  ;;   int(rand() * 6) + 1 == 6 ?
  ;;     "One time out of six you get a six" :
  ;;     "Five times out of six you get something else";

  ;; == 真值判断 ==
  ;; 在 Clojure 中我们使用表达式求值得到值。
  ;; 用于分支时，所有值要么是真（truthy），要么是假（falsy）。
  ;; 实际上，几乎所有值都是真：

  (if true :truthy :falsy)
  (if :foo :truthy :falsy)
  (if '() :truthy :falsy)
  (if 0 :truthy :falsy)
  (if "" :truthy :falsy)

  ;; 唯一假值是 `false` 和 `nil`

  (if false :truthy :falsy)
  (if nil :truthy :falsy)
  (when false :truthy)

  ;; 最后那个：`when` 在条件为假时返回 `nil`。
  ;; 因为 `nil` 是假值，所以上面的 `when` 会导致 `if` 的 else 分支被执行

  (if (when false :truthy) :true :falsy)

  ;; （超级烂的代码，但反正……）
  ;; 当只需要布尔真假时，有 `true?` 函数

  (true? true)
  (true? 0)
  (true? '())
  (true? nil)
  (true? false)

  ;; 因此

  (if (true? 0) :true :false)

  ;; == `when` ==
  ;; 如前所述，`when` 是只有真分支的 `if`，主体被 `do` 包裹。
  ;; 试试下面这个，然后把 `when` 换成 `if` 再试：

  (when :truthy
    (println "That sounds true to me")
    :truthy-for-you)

  ;; 如果 `when` 条件为假，返回 `nil`。

  (when nil :true-enough?)

  ;; == `cond` ==
  ;; 深层嵌套的 if/else 很难写、读、维护，
  ;; Clojure 核心提供了多个更方便的结构，其中最常用的是宏 `cond`。
  ;; 它接受成对的条件/结果形式，依次测试条件，
  ;; 第一个为真的条件对应的结果被求值并返回，后面的条件不再测试。

  (let [dice-roll (inc (rand-int 6))]
    (cond
      (= 6 dice-roll)  "Six is as high as it gets"
      (odd? dice-roll) (str "An odd roll " dice-roll " is")
      :else            (str "Not six, nor odd, instead: " dice-roll)))

  ;; `:else` 是关键字，求值为自身且为真。
  ;; 这是约定俗成的默认分支写法。
  ;; 没有默认分支时，条件都不满足就返回 `nil`。
  ;; 试试在 `:else` 前加两个忽略标记 `#_ #_`。

  ;; 强烈推荐 ClojureDocs：
  ;; https://clojuredocs.org/clojure.core/cond
  ;; 把例子复制过来玩：

  ;; 另见上面提到的 `cond->`

  ;; == `case` ==
  ;; 类似其他语言的 switch/case，Clojure 有宏 `case`。
  ;; 它接受一个测试表达式，后跟零或多个测试常量/结果对，
  ;; 最后可选一个默认结果。（但测试表达式后的主体不能为空。）

  (let [test-str "foo bar"]
    (case test-str
      "foo bar" (str "That's very " :foo-bar)
      "baz"     :baz
      (count    test-str)))

  ;; 最后的表达式（如果有）作为默认值返回。

  (let [test-str "foo bar"]
    (case test-str
      #_#_"foo bar" (str "That's very " :foo-bar)
      "baz"     :baz
      (count    test-str)))

  ;; 如果没有匹配且没有默认，会抛运行时错误

  (let [test-str "foo bar"]
    (case test-str
      #_#_"foo bar" (str "That's very " :foo-bar)
      "baz"     :baz
      #_(count    test-str)))

  ;; 注意！测试常量必须是编译时字面量，编译器不会帮你发现这种错误：

  (let [test-int 2
        two 2]
    (case test-int
      1     :one
      two   (str "That's not a literal 2")
      (str test-int ": Probably not expected")))

  ;; https://clojuredocs.org/clojure.core/case
  ;; 复制一些 `case` 例子来实验

  ;; Functional Design in Clojure 播客有一期精彩的关于分支的讨论：
  ;; https://clojuredesign.club/episode/089-branching-out/

  ;; == 少写分支代码更好，对吧？ ==
  ;; 核心库中有很多函数帮助你避免写分支代码。
  ;; 你只需提供谓词（predicate）作为条件。
  ;; 常用谓词函数有 `filter`

  (filter even? [0 1 2 3 4 5 6 7 8 9 10 11 12])

  ;; 和它的“兄弟” `remove`

  (remove odd? [0 1 2 3 4 5 6 7 8 9 10 11 12])

  ;; 过滤序列很常见，你只需决定“如何”过滤，写好谓词即可。
  ;; 有时甚至不用自己写，核心库有很多现成的谓词

  (zero? 0)
  (even? 0)
  (neg? 0)
  (pos? 0)
  (nat-int? 0)
  (empty? "")
  (empty? [])
  (empty? (take 0 [1 2 3]))
  (integer? -2/1)
  (indexed? [1 2 3])
  (indexed? '(1 2 3))

  ;; 什么是谓词？在本指南中，
  ;; 谓词是测试真假的函数。
  ;; 约定以 `?` 结尾。很多只接受一个参数。

  ;; 常用的谓词是 `some?`，测试“是否有值”，即不是 `nil`

  (some? nil)
  (some? false)
  (some? '())

  ;; 要测试是否为 `nil`，可以用 `not` 包裹

  (not (some? nil))
  (not (some? false))

  ;; 你可能想定义 `nil?` 函数？不用了，已经有

  (nil? nil)
  (nil? false)

  ;; 核心库还有接受谓词+集合的谓词函数，如 `every?`

  (every? nat-int? [0 1 2])
  (every? nat-int? [-1 0 1 2])

  ;; 查看 `nat-int?` 的文档，自己想一些列表测试，比如

  (every? nat-int? [0 1 2N]) ; 2N 不是固定精度
  (doc nat-int?)

  ;; 这种接受函数作为参数的函数称为“高阶函数”。
  ;; https://en.wikipedia.org/wiki/Higher-order_function
  )

(comment
  ;; = 函数 =
  ;; 在深入高阶函数之前，先来看看函数。
  ;; 函数是 Clojure 的一等公民，也是解决业务问题的主要构建块。

  ;; 我们见过几种创建函数的方式。
  ;; 这是一个匿名函数：如果数字能被 15 整除返回 "fizz buzz"，否则返回数字本身。
  ;; （不是完整的 Fizz Buzz 问题。）

  (fn [n]
    (if (zero? (mod n 15))
      "fizz buzz"
      n))

  ;; 把它定义（绑定到符号）

  (def fizz-buzz-1 (fn [n]
                     (if (zero? (mod n 15))
                       "fizz buzz"
                       n)))
  (fizz-buzz-1 2)
  (fizz-buzz-1 15)

  ;; 有宏 `defn` 让我们一步完成定义和创建

  (defn fizz-buzz-2 [n]
    (if (zero? (mod n 15))
      "fizz buzz"
      n))

  (fizz-buzz-2 4)

  ;; `defn` 可以为函数添加文档

  (defn fizz-buzz-3
    "如果 `n` 能被 15 整除返回 'fizz buzz'，否则返回 `n`"
    [n]
    (if (zero? (mod n 15))
      "fizz buzz"
      n))

  (doc fizz-buzz-3) ; 或 hover `fizz-buzz-3`

  ;; 文档字符串很容易放错位置，尤其是习惯像 `fizz-buzz-2` 那样写时。

  (defn fizz-buzz-4
    [n]
    "如果 `n` 能被 15 整除返回 'fizz buzz'，否则返回 `n`"
    (if (zero? (mod n 15))
      "fizz buzz"
      n))

  ;; 这定义了一个合法函数体，所以 Clojure 不会报错。但：

  (doc fizz-buzz-4)

  ;; clj-kondo 默认配置会帮你发现这类错误。
  ;; 但它帮不了这种：

  (defn only-the-last-eval-returns [x]
    [1 x]
    [2 x])

  (only-the-last-eval-returns "foo")

  ;; 这样写很容易看出来问题，但你还是可能犯这种错，
  ;; 尤其是在写 Hiccup（用 Clojure 数据结构写 HTML 的方式，
  ;; Reagent 库常用）时。
  ;; 当你花一小时排错后，你会听到本指南在耳边低语：
  ;;   “叫你了！”

  ;; `fn`（以及 `defn`）的参数绑定向量按顺序把每个参数绑定到名字。

  (defn coords->str [x y]
    (str "x: " x ", y: " y))

  ;; == 可变参数函数 ==
  ;; 可以在最后一个参数名前加 `&`，表示接受任意数量参数。
  ;; 这个名字会绑定到一个包含剩余参数的序列。

  (defn lead+members [lead & members]
    {:lead lead
     :members members})

  (lead+members "Dave Mustain"
                "Marty Friedman"
                "Nick Menza"
                "David Ellefson")

  ;; == 多重元数（Multi-arity） ==
  ;; Clojure 支持基于参数数量的函数签名。
  ;; `defn` 允许为每个元数单独定义列表。
  ;; 常用于提供默认值

  (defn hello
    ([] (hello "World"))
    ([s] (str "Hello " s "!")))

  (hello)
  (hello "Clojure Friend")

  ;; 或者创建函数的“单位元”（identity value）
  ;; 比如两个 x-y 坐标相加

  (defn add-coords-1 [coord-1 coord-2]
    {:x (+ (:x coord-1)
           (:x coord-2))
     :y (+ (:y coord-1)
           (:y coord-2))})

  (add-coords-1 {:x -2 :y 10}
                {:x 4 :y 6})

  ;; 如果只传一个参数，希望加到原点怎么办？
  ;; （看，我把单位元藏在这里：从原点开始 😎）
  ;; 现在 `add-coords-1` 会失败

  (add-coords-1 {:x -2 :y 10})

  ;; 需要增加一个单参数版本

  (defn add-coords-2
    ([coord]
     (add-coords-2 {:x 0
                    :y 0}
                   coord))
    ([coord-1 coord-2]
     {:x (+ (:x coord-1)
            (:x coord-2))
      :y (+ (:y coord-1)
            (:y coord-2))}))

  (add-coords-2 {:x -2 :y 10})

  ;; 现在如果不传参数，应该返回原点。
  ;; 练习：写 `add-coords-3`，满足：

  (add-coords-3)

  ;; 同时仍能处理：

  (add-coords-3 {:x 3 :y 4})
  (add-coords-3 {:x 2 :y 4}
                {:x -4 :y -4})

  ;; 这与函数可组合性有关。
  ;; 比如 `apply` 函数，它把序列“展开”应用到函数上。
  ;; 现在我们可以用 `apply` 这样调用 `add-coords-2`：

  (apply add-coords-2 [{:x 1 :y 1} {:x 4 :y 4}])

  ;; 也可以：

  (apply add-coords-2 [{:x 1 :y 1}])

  ;; 但不能：

  (apply add-coords-2 [])

  ;; 而你写的 `add-coords-3` 可以：

  (apply add-coords-3 [])

  ;; 但它还不能处理任意长度的坐标序列。
  ;; 需要再加一个元数：

  (defn add-coords-4
    ;; 把你写的 add-coords-3 的零参数和单参数版本放这里
    ([coord-1 coord-2]
     {:x (+ (:x coord-1)
            (:x coord-2))
      :y (+ (:y coord-1)
            (:y coord-2))})
    ([coord-1 coord-2 & more-coords]
     ;; 等你学了高阶函数 `reduce` 后再实现这个元数
     ))

  (apply add-coords-4 [{:x 1 :y 1}
                       {:x 1 :y 1}
                       {:x 1 :y 1}
                       {:x -6 :y -6}])

  ;; 听 Eric Normand 详细解释函数单位元的重要性：
  ;; https://lispcast.com/what-is-a-functions-identity/

  ;; == 闭包 ==
  ;; 用 `fn` 或 `#()` 创建函数时，会生成闭包。
  ;; 就像 JavaScript 等语言一样，这些函数可以访问函数创建时变量的快照

  (defn named-coords-factory [name]
    (fn [x y] {:name name
               :coords {:x x
                        :y y}}))

  (def bob-coords-fn (named-coords-factory "Bob"))
  (def fred-coords-fn (named-coords-factory "Fred"))

  (bob-coords-fn 0 0)
  (fred-coords-fn 5 5)
  (bob-coords-fn 7 7)

  ;; 闭包很适合在 let 绑定中创建低元数函数供主体使用：

  (defn whisper-or-yell-or-ask [command sentence]
    (let [whisper (fn []
                    (str (string/lower-case sentence) command))
          yell (fn []
                 (str (string/upper-case sentence) command))
          ask (fn []
                (str sentence "?"))
          default (fn []
                    (str sentence command " ¯\\_(ツ)_/¯"))]
      (case command
        "" (whisper)
        "!" (yell)
        "?" (ask)
        (default))))

  ;; let 中创建的所有函数都“闭合”了 `command` 和 `sentence`，
  ;; 让 `case` 保持简洁易读。

  (whisper-or-yell-or-ask "" "How wOnDerFuLLY NIce To seE")
  (whisper-or-yell-or-ask "!" "Hello tHERE")
  (whisper-or-yell-or-ask "?" "How are you doing")
  (whisper-or-yell-or-ask ":" "Oh well")

  ;; == 属性映射 ==
  ;; `defn` 允许在函数名后、参数向量前添加属性映射。
  ;; 这些会作为元数据添加到 var 上。

  (defn i-have-attributes
    {:doc "文档也可以这样加"
     :foo "你喜欢的任何属性"}
    []
    "Good for you")

  (doc i-have-attributes)
  (meta #'i-have-attributes)

  ;; 一个有用的属性是测试函数，测试运行器会识别它

  (defn fizz-buzz-5
    "又一个有限的 fizz-buzz 函数"
    {:test (fn []
             (is (= "fizz-buzz" (fizz-buzz-5 15)))
             (is (= 3 (fizz-buzz-5 3))))}
    [n]
    (if (pos? (rem 15 n))
      "fizz-buzz"
      n))

  (clojure.test/test-var #'fizz-buzz-5)
  ;; 哎呀！需要你修复 bug。😀

  ;; 要不要实现完整的 Fizz Buzz？
  ;; https://en.wikipedia.org/wiki/Fizz_buzz

  (defn fizz-buzz
    "我的 Fizz Buzz 解决方案"
    {:test (fn []
             (are [arg expected]
                  (= expected (fizz-buzz arg))
               1  1
               3  "Fizz"
               4  4
               5  "Buzz"
               7  7
               15 "Fizz Buzz"
               20 "Buzz"))}
    [n])

  (clojure.test/test-var #'fizz-buzz)
  (map fizz-buzz (range 1 40))

  ;; 对编译器和核心设施有特殊含义的元数据列在这里：
  ;; https://clojure.org/reference/special_forms

  ;; 接下来是高阶函数！
  )

(comment
  ;; = 高阶函数 =
  ;; Clojure 之所以强大，一个重要原因是函数是“一等公民”
  ;; https://en.wikipedia.org/wiki/First-class_function
  ;; 它们可以作为集合中的值（也可是 map 的键），
  ;; 可以作为参数传给其他函数，也可以作为求值结果返回。
  ;; 你可能在 JavaScript 等语言中见过这个概念。

  ;; 来看一些核心库中的高阶函数。
  ;; `some` 对集合元素逐个调用函数，
  ;; 返回第一个真值结果，如果没有则返回 `nil`。

  (some even? [1 1 2 3 5 8 13 21])

  ;; 别和 `some?` 混淆，后者不是高阶函数。

  (some some? [nil false])
  (some some? [nil nil])

  ;; Clojure 中常见用法是用集合作为谓词查找元素。
  ;; 是的，集合也是函数！当作函数用时会在自身中查找参数。

  (#{"foo" "bar"} "bar")

  ;; 所以

  (some #{"foo"} ["foo" "bar" "baz"])
  (some #{"fubar"} ["foo" "bar" "baz"])

  ;; `apply` 接受函数和集合，把集合“展开”应用到函数上。
  ;; 比如有一堆数字想加起来，这样不行：

  (+ [1 1 2 3 5 8 13 21])

  ;; 用 `apply`：

  (apply + [1 1 2 3 5 8 13 21])

  ;; 把数字连成字符串：

  (apply str [1 1 2 3 5 8 13 21])

  ;; 对比：

  (str [1 1 2 3 5 8 13 21])

  ;; 我们之前见过 `filter` 和 `remove`，也是常用高阶函数。
  ;; 它们和 `map`、`reduce` 属于同一级别。
  ;; 继续读下去 😎
  )

(comment
  ;; = `map` 和 `reduce` =
  ;; 在其他支持一等函数的语言中，你可能用过 `map` 和 `reduce`。
  ;; 它们值得深入学习和练习，这里有一个超级棒的入门：
  ;; https://purelyfunctional.tv/courses/3-functional-tools/

  ;; 我们也简单看一下。
  ;; `map` 从头到尾对一个或多个集合的元素调用函数，
  ;; 返回一个（延迟的，后面会讲）结果序列，顺序相同。
  ;; 比如把列表每个元素减一：

  (map dec '(1 1 2 3 5 8 13 21))

  ;; 再减一：

  (->> '(1 1 2 3 5 8 13 21)
       (map dec)
       (map dec))

  ;; 或者直接减二：

  (map (fn [n] (- n 2)) '(1 1 2 3 5 8 13 21))

  ;; 如果给 `map` 多个集合，它会：
  ;; 1. 从每个集合取下一个元素
  ;; 2. 作为参数传给映射函数
  ;; 3. 把结果加入返回序列
  ;; 直到最短集合耗尽

  (map + [1 2 3] '(0 2 4 6 8))
  (map (fn [n1 s n2] (str n1 ": " s "-" n2))
       (range)
       ["foo" "bar" "baz"]
       (range 2 -1 -1))

  ;; （还没怎么讲 `range`，它生成数字序列。
  ;; 不给参数会生成无限整数序列，注意别卡住 😀
  ;; 幸好其他序列先耗尽了！）

  ;; 很多其他语言用 for 循环完成的任务，在 Clojure 中用 `map` 解决。

  ;; 其他“for 循环式”任务常用 `reduce`。
  ;; 与 `map` 不同，它不限于生成相同长度或形状的结果，
  ;; 而是累积任意形状的结果。比如把数字集合变成字符串

  (reduce (fn [acc n]
            (str acc ":" n))
          [1 1 2 3 5 8 13 21])

  ;; `reduce` 会把函数依次调用，参数是上一次调用的结果和集合下一个数字。
  ;; 开始时特殊，因为还没有结果。
  ;; `reduce` 有两种方式处理，两种元数。
  ;; 两参数版本用集合前两个元素开始：

  (reduce + [1 1 2 3 5 8 13 21])

  ;; 过程从 (+ 1 1) 开始

  ;; 三参数版本用第二个参数作为初始结果：

  (reduce + 100 [1 1 2 3 5 8 13 21])

  ;; 你可能注意到 `+` 函数支持更多（或更少）参数。

  (+)
  (+ 1)
  (+ 1 1)
  (+ 1 1 2 3 5 8 13 21)

  ;; `+` 把第一个参数（如果有）加到“当前值”（初始为 0），
  ;; 然后下一个参数加到新当前值，依此类推，直到结果。
  ;; 这听起来很像 reduce，对吧？

  ;; 如果我们要实现 `+`，怎么做？
  ;; 可以先实现两数相加，然后用 reduce。
  ;; 当然，现在不能用现有 `+`……
  ;; SICP 用 Peano 算术有解：
  ;; https://youtu.be/V_7mmwpgJHU?t=814
  ;; https://en.wikipedia.org/wiki/Peano_axioms#Addition

  (defn plus [x y]
    (if (zero? x)
      y
      (plus (dec x) (inc y))))

  ;; 但这里没考虑到太多情况……
  ;; 简化，只做整数运算，用 Java 的 `Integer.sum`

  (Integer/sum 1 1)

  ;; 好，用它写 `add-two`

  (defn add-two [x y]
    (Integer/sum x y))
  (add-two 1 1)

  ;; 与 `+` 不同，它不能完全与 `apply` 组合

  (apply add-two [])
  (apply add-two [1])
  (apply add-two [1 1])
  (apply add-two [1 1 2 3 5 8 13 21])

  ;; 需要 `add-many`。
  ;; 用 `reduce` 和 `add-two` 可以这样定义：

  (defn add-many [& numbers]
    (reduce add-two numbers))
  ;; 对吧？

  (apply add-many [1])
  (apply add-many [1 1])
  (apply add-many [1 1 2 3 5 8 13 21])

  ;; 零参数版本会炸

  (add-many)

  ;; 内置 `+` 有零作为默认当前值。
  ;; 我们有两种方式加：加零参数签名，或用三参数 `reduce`。
  ;; 用后者，因为我们在学 reduce：

  (defn add* [& numbers]
    (reduce add-two 0 numbers))
  (add*)
  (add* 1)
  (add* 1 1)
  (add* 1 1 2 3)

  ;; 完美。

  ;; 也可以用 `apply`：
  (apply add* [])
  (apply add* [1])
  (apply add* [1 1])
  (apply add* [1 1 2 3 5 8 13 21])

  ;; 或 `reduce`：
  (reduce add* [])
  (reduce add* [1])
  (reduce add* [1 1])
  (reduce add* [1 1 2 3 5 8 13 21])

  ;; 除了只处理整数，我们的 `add*` 与核心 `+` 实现非常相似。
  ;; 查看（输出窗口）：

  (source +)

  ;; 嗯，他们对低元数用了多重元数，可能为了类型转换，但总之 😀

  ;; `reduce` 还有一点要提。
  ;; 写归约函数时，可以用 `reduced` 在序列耗尽前提前终止。
  ;; 比如上面把序列用 `:` 分隔成字符串，但遇到 `nil` 就停：

  (reduce (fn [acc n]
            (str acc ":" n))
          [1 1 2 3 5 8 nil 13 21])

  ;; 用 `reduced` 包装累积值提前结束：

  (reduce (fn [acc n]
            (if (nil? n)
              (reduced acc)
              (str acc ":" n)))
          [1 1 2 3 5 8 nil 13 21])

  ;; 查看文档：

  (doc reduced)

  ;; 归约在 Clojure 中极其重要，因为它是“函数优先”语言。
  ;; 正如 Functional Design 播客所说：
  ;; https://clojuredesign.club/episode/058-reducing-it-down/
  ;; “归约函数是函数式编程的支柱，因为我们没有可变状态。”

  ;; 事实上归约如此重要，Rich Hickey 还专门写了 reducers 库，性能更强：
  ;; https://clojure.org/reference/reducers
  ;; Functional Design 的两位主持人也讨论过这个库：
  ;; https://clojuredesign.club/episode/060-reduce-done-quick/
  ;; 那期有个金句：
  ;;   “seq 抽象，有点懒。”

  ;; 我们就不深入 reducers 库了……
  )

;; ……相反，我们抓住 Nate 和 Christoph 提到的三个超级重要概念：
;; * 不可变性（immutability）
;; * `seq` 抽象
;; * 延迟（laziness）
;; 它们是相关的，或许最好从不可变性开始……

(comment
  ;; = 不可变性 =
  ;; 我们讲了这么久 Clojure，竟然还没深入谈它如何鼓励我们在处理数据时避免可变性。
  ;; Clojurian 们总在不停地说不可变性，对吧？
  ;; 我们有时听起来像在宣扬某种意识形态 😄
  ;; https://www.youtube.com/watch?v=qe60zwUAOqE

  ;; 这在一定程度上是对的，作为 Clojurian，
  ;; 我们常常尽量在操作过程中保持数据转换模式，
  ;; 只在“边界”处理不纯的世界——
  ;; 开始时可能读取输入，结束时可能更新数据库、
  ;; 把结果打印到文件（或屏幕），或修改网页 DOM。

  ;; Clojure 通过多种方式鼓励我们走不可变之路，
  ;; 这里主要提两种：
  ;; * 持久化数据结构（Persistent Data Structures）
  ;; * 纯函数（Pure Functions）

  ;; == 持久化数据结构 ==
  ;; Clojure 通过提供不可变数据结构帮助我们留在不可变世界。
  ;; 它们的实现叫持久化数据结构：
  ;; https://en.wikipedia.org/wiki/Persistent_data_structure
  ;; 实际上数据结构永不改变，
  ;; 我们用来转换它们的函数实际上创建的是拷贝（非常聪明地实现，别担心）。

  ;; 比如定义一个包含数字的向量

  (def eighteen [1 0 0 1 0])
  eighteen

  ;; 想把最后一个 `0` 改成 `1`，用 `assoc` 函数。
  ;; 对向量时接受索引和新值

  (def nineteen (assoc eighteen 4 1))
  nineteen

  ;; 再看 `eighteen`……

  eighteen

  ;; ……它仍然名副其实。
  ;; 在它索引 4 处 assoc `1` 创建了一个拷贝，定义为 `nineteen`。

  ;; 这在局部绑定中也成立。

  (let [origin {:x 0
                :y 0}
        x-travel (assoc origin :x 100)]
    [origin x-travel])

  ;; 这让程序流程非常确定。
  ;; 数据不会在脚下随意改变。
  ;; 不变的数据转换过程更容易并行化，
  ;; 其他线程无法改变你正在处理的数据。
  ;; 一大类 bug 永远没有机会出现！

  ;; 不可变性的另一个好处是 Clojure 可以高效提供值相等性。
  ;; 值是不可变的，这是定义。
  ;; 在 Clojure 中，即使是最深层数据结构也能快速比较。

  ;; 用一个不那么深（但名字很深）的结构演示

  (def universa {:one {"Alice" {:x 100
                                :y 100
                                :z 100}
                       "Bob" {:x 100
                              :y 100
                              :z 100}}
                 :two {"Alice" {:x 100
                                :y 100
                                :z 100}
                       "Bob" {:x 100
                              :y 100
                              :z 99}}})

  (= (:one universa)
     (:two universa))

  ;; `update-in` 是高阶函数，用来按“地址”和函数转换数据结构。
  ;; 用它让 two-Bob 找到 two-Alice，就像 one-Bob 和 one-Alice 那样

  (def unified-universa
    (update-in universa [:two "Bob" :z] inc))
  unified-universa

  (= (:one unified-universa)
     (:two unified-universa))

  (= universa unified-universa)

  ;; 你再也不用写 `equals()` 方法了！😄

  ;; 不可变性也让我们的程序与可变变量的语言不同。
  ;; 适应这个需要时间。（我现在看到很多问题还是先想到可变解法。
  ;; 越来越少，但还是有。你可能比我学得快。）

  ;; 坚持下去绝对值得，回报巨大。
  ;; 如果你只看本指南推荐的一个资源，我建议是 Rafal Dittwald 的这个演讲：
  ;; https://www.youtube.com/watch?v=vK1DazRK_a0
  ;; 剧透：他没用 Clojure

  ;; 当然，演讲中 Rafal 不只是在假装数据不可变。
  ;; 他还在使用函数纯度。

  ;; == 纯函数 ==
  ;; Clojure 不像某些语言（比如 Haskell）强制纯度，
  ;; 但它让写纯函数变得很容易，从而把副作用推到程序“边缘”。

  ;; 纯函数需遵守两条规则：
  ;; 1. 相同输入永远返回相同值
  ;; 2. 不影响外部环境（不修改任何东西，包括不打印、不调用变更 API）

  ;; 纯函数是确定性的，你可以放心调用，
  ;; 它只根据你传入的输入（且仅输入）计算返回值，
  ;; 不会更新应用状态或做其他事。
  )

;; 在考察 `seq` 抽象之前，先简单看一些常见的核心函数用于转换数据结构。

(comment
  ;; = 转换数据结构 =
  ;; Clojure 核心库让“深入”数据结构并操作它变得容易、有趣、可读。
  ;; 创建拷贝并包含操作结果。

  ;; 我们见过 `assoc`，它创建数据结构的拷贝，在指定索引（向量）或键（map）处放入新值

  (def colt-express
    {:name "Colt Express"
     :categories ["Family"
                  "Strategy"]
     :play-time 40
     :ratings {:pez 5
               :kat 5
               :wiv 5
               :vig 3
               :rex 5
               :lun 4}})

  (def exit-haunted
    {:name "EXIT: The Haunted Roller Coaster"
     :categories ["Family"
                  "Co-op"
                  "Puzzle"
                  "Cards"]
     :ratings {:pez 5
               :kat 5
               :wiv 5
               :vig 4
               :rex 5}})

  ;; `assoc` 可以给 map 添加新键

  (def colt-express-w-age
    (assoc colt-express :age-from 10))

  ;; 对向量只能在最后项后追加，不能跳跃添加

  (def board-games-empty
    [])

  (def board-games-w-c-e
    (assoc board-games-empty 0 colt-express))

  ;; board-games-empty 仍然为空，所以

  (def board-games-w-c-e-and-exit-fail
    (assoc board-games-empty 1 exit-haunted))

  (def board-games-w-c-e-and-exit
    (assoc board-games-w-c-e 1 exit-haunted))

  ;; 不过用 `assoc` 添加向量项不常见，通常用 `conj`

  (conj board-games-empty colt-express exit-haunted)

  ;; `assoc` 对 map 可以替换现有值（在拷贝中）

  (def colt-express-w-age-and-adjusted-playtime
    (assoc colt-express-w-age :play-time 45))

  ;; 对向量也可以

  (def board-games-w-adjusted-c-e
    (assoc board-games-w-c-e
           0
           colt-express-w-age-and-adjusted-playtime))

  ;; 可以一次 `assoc` 多个

  (assoc colt-express
         :play-time 50
         :age-from 10)

  (assoc board-games-empty
         0 colt-express
         1 exit-haunted)

  ;; （再次提醒，有 `conj` 做这个。）

  ;; 对 map 还有 `merge`，可以合并多个 map

  (merge colt-express
         {:play-time 45
          :age-from 10})

  ;; 注意：它是浅合并，所以这样加家庭成员评分不行

  (merge exit-haunted
         {:play-time 90
          :ratings {:lun 5}
          :age-from 10})

  ;; `assoc` 也是一样

  (assoc exit-haunted :ratings {:lun 5})

  ;; 核心库没有深合并，但有 `assoc-in` 可以深入
  ;; 它接受“路径”而不是单个键

  (assoc-in exit-haunted [:ratings :lun] 5)

  (assoc-in colt-express [:categories 2] "Planning")
  ;; （但别这样，下面 `update` 部分教你用 `conj` 添加类别。）

  ;; 与 `assoc` 不同，`assoc-in` 一次只能加一个

  ;; 删除 map 中的键用 `dissoc`

  (dissoc colt-express :play-time :ratings :categories)

  ;; REPL 中常用 `dissoc` 查看大结构中感兴趣的部分

  (dissoc colt-express :log)
  ;; （这个结构没 log，所以不变，但反正。）

  ;; 核心库没有 `dissoc-in`，等讲完 `update` 和 `update-in` 再回来。

  ;; `update` 和 `update-in` 类似 `assoc` 系列，
  ;; 但接受函数来操作值，而不是直接值。

  (update exit-haunted :name string/upper-case)

  ;; 练习：把 `colt-express` 的 `:play-time` 加 5 左右

  ;; 函数后的参数会传给函数

  (update colt-express :categories conj "Planning")

  ;; 练习：让加 `:play-time` 的操作把 `5` 作为参数传进去。

  ;; 练习：从 `exit-haunted` 的 `:ratings` 中移除 `:pez` 和 `:wiv`

  ;; `update-in` 与 `assoc-in` 的关系同 `update` 与 `assoc`

  (update-in colt-express [:ratings :lun] inc)

  (update-in colt-express [:ratings :lun] + 9000)
  ;; https://www.youtube.com/watch?v=PCHxU7witPA

  ;; 练习：没有 `dissoc-in`，但你可以用 `update-in` 实现，对吧？

  ;; 奖励：以后少上一次 StackOverflow 😄
  ;; https://stackoverflow.com/a/21942548/44639

  ;; 我们之前用关键字作为 map 查找函数。
  ;; 也可以用 `get` 函数

  (get colt-express :ratings)

  (= (:ratings colt-express)
     (get colt-express :ratings))

  ;; `get` 的第三个参数是默认值，键不存在时返回

  (get exit-haunted :play-time 0)

  ;; 关键字作为查找函数也支持默认值

  (:play-time exit-haunted 0)

  ;; 没有默认值时返回 `nil`，可能导致错误

  (* (get colt-express :play-time) 2)
  (* (get exit-haunted :play-time) 2)

  ;; 安全起见：

  (* (get colt-express :play-time 0) 2)
  (* (get exit-haunted :play-time 0) 2)

  ;; 当然也有 `get-in`
  ;; 练习：用 `get-in` 取我在这两款优秀家庭游戏上的评分

  ;; 你可能注意到本节所有函数都把集合作为第一个参数。
  ;; 这让它们非常适合与线程首宏 `->` 一起使用。
  ;; 这是设计如此，也是 Clojure 的惯用法。

  ;; 常见的数据转换管道像这样：

  (-> exit-haunted
      (assoc :play-time 90)
      (update :categories conj "Scary")
      (assoc-in [:ratings :lun] 5)
      (update-in [:ratings :vig] + 1)
      (dissoc :name)
      (update :log vec)
      (update :log conj "Name redacted")
      (update :log conj "(Because scary)"))

  ;; （虽然可能比这个更有意义）

  ;; 推荐从这里开始在 ClojureDocs 上“See also”浏览：
  ;; https://clojuredocs.org/clojure.core/update-in
  ;; 多复制例子来实验。
  )

(comment
  ;; == 操作 `set` ==
  ;; map、vector 和 set 是大多数 Clojure 程序的面包和黄油。
  ;; 有了它们出色的字面量语法，代码变得易读易推理。
  ;; 操作它们也简单直观。

  ;; `set` 是 `seq`（后面会讲）
)

;; 未完待续...

;; 在这里有更多内容之前，或许是时候看看如何把 Calva 连接到你的 Clojure/ClojureScript 项目了：
;; https://calva.io/connect/

;; 待写主题列表：
;; 元数据
;; 注释
;; 解构
;; atom
;; nil、nil 安全、nil punning
;; seq
;; 延迟
;; loop、recur
;; 调试
;; 一些总结练习

;; 在 https://clojure.org/ 学习更多 Clojure
;; 还有 ClojureScript，同一个精彩语言，运行在 JavaScript VM 上：https://clojurescript.org

;; 本简短指南未涵盖 Clojure 的很多内容。
;; https://clojure.org/ 有完整故事。

;; 需要 Clojure 帮助？查看这些资源：
;; https://ask.clojure.org/
;; https://clojurians.net
;; https://clojureverse.org
;; https://www.reddit.com/r/Clojure/
;; https://exercism.io/tracks/clojure

;; 还有很多其他资源，如：
;; https://clojuredocs.org
;; https://clojure.org/api/cheatsheet

"文件加载完成。欢迎来到 Clojure！♥️"

;; 本指南下载自：
;; https://github.com/BetterThanTomorrow/dram
;; 欢迎贡献。