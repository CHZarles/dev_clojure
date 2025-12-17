# Clojure 入门指南

llm聊天记录：https://chat.qwen.ai/c/70478da7-880d-46b8-b9da-baa1f09c1044


## 欢迎语

欢迎来到 Clojure! 本指南将为你提供 Clojure 语言的基础知识。这些基础是构建性的，从第一原则出发，帮助你更容易理解未来的 Clojure 旅程。

掌握这些基础后，你将有更好的机会对如何编写代码、如何提出问题、如何有效搜索信息以及如何理解遇到的代码等方面拥有正确的直觉。

## 1. 表达式 (Expressions)

在 Clojure 中，一切都是表达式（没有语句）。除非在求值表达式时发生错误，否则总是有返回值（有时是 `nil`）。

一个重要的方面是，表达式的结果始终是最后被求值的表达式。例如：

```clojure
(defn last-eval-wins []
  (println 'side-effect-1)
  1
  (println 'side-effect-2)
  2)
```

调用此函数将求值函数体中的所有四个表达式，但返回结果将是最后一个被求值的表达式。

`println` 调用也是表达式，它们求值为 `nil`。

表达式由字面量（求值为自身）和/或以下之一的调用组成：
- 特殊形式 (special forms)
- 宏 (macros)
- 函数 (functions)

例如：
```clojure
(def foo "foo") ; 调用特殊形式 `def`
(for [x '(1 2 3) 
      y '(:a :b)] 
  [x y]) ; 调用宏 `for`

(str 1 2 3) ; 调用函数 `str`

(map str [:foo :bar]) ; 函数可以作为值传递
(map for [:foo :bar]) ; 不能获取宏的值
(map def [:foo :bar]) ; def 甚至不是一个符号
```

## 2. 字面量 (Literals)

字面量求值为它们自身。

### 数值类型
```clojure
18        ; 整数
-1.8      ; 浮点数
0.18e2    ; 指数
18.0M     ; 大十进制数
18/324    ; 比率
18N       ; 大整数
0x12      ; 十六进制
022       ; 八进制
2r10010   ; 二进制
```

### 字符类型
```clojure
"hello"   ; 字符串
\e        ; 字符
#"[0-9]+" ; 正则表达式
```

### 符号和标识符
```clojure
map             ; 符号
+               ; 符号 - 允许大多数标点符号
clojure.core/+  ; 命名空间符号
nil             ; null/nil 值 (LISP 传统命名)
true false      ; 布尔值
:alpha          ; 关键字
:release/alpha  ; 命名空间关键字
::alpha         ; 当前命名空间关键字
```

### 关键字 (Keywords)
关键字以 `:` 开头。它们是自身，通常用作标识符和映射中的键。关键字在内存和速度方面非常高效。

相同的关键词不仅相等，而且是同一个对象：
```clojure
(= :foo :foo)                 ; true
(identical? :foo :foo)        ; true
(identical? (keyword "foo") :foo) ; true
```

在整个 Clojure 程序中，关键字是全局的。命名空间语法可以帮助你控制这一点。

关键字也是函数，这在 Clojure 程序中扮演着特殊而重要的角色。

### 字符串 (Strings)
字符串用双引号括起来：
```clojure
"A string can be
multi-line, but will contain any leading spaces."
"Write strings
like this, if leading spaces are no-no."
```
（单引号用于其他用途，稍后会看到。）

## 3. 命名空间 (Namespaces)

命名空间非常重要，但本指南不会过多讨论。官方文档是最好的参考：
https://clojure.org/reference/namespaces

### 基本概念
Clojure 符号在命名空间中定义（使用 `def` 特殊形式），可以从任何其他命名空间访问：
```clojure
(def foo-2 "foo")
```

当求值符号时，它必须已被定义，否则编译器会报错。命名空间也需要被创建。

通常，你会将 Clojure 代码安排到单独的文件中，每个文件一个命名空间，并使用 `ns` 形式来 `:require` 需要的命名空间，为其创建别名，有时 `:refer` 一些符号，以便无需命名空间前缀即可使用它们。

### 命名空间关键字
关键字也可以有命名空间，但它们不像符号那样真正在命名空间中注册，所以你可以直接使用它们：
```clojure
:foo-whatever
:whatever-namespace/foo
```

双冒号前缀 `::` 会扩展为 `:<current-namespace>/foo`：
```clojure
::foo
```

重要的是要知道，`:foo` 无论从哪个命名空间使用，都引用相同的关键字。而 `::foo` 则不是。

## 4. 集合 (Collections)

Clojure 有四种集合类型的字面量语法，它们都求值为自身：
```clojure
'(1 2 3)     ; 列表 (一个引用的列表)
[1 2 3]      ; 向量
#{1 2 3}     ; 集合
{:a 1 :b 2}  ; 映射
```

这些集合可以组合：
```clojure
{:foo [1 2]
 :bar #{1 2}}
```

在 Clojure 中，我们主要使用这些集合、字面量集合和函数来完成大多数事情。

## 5. 函数 (Functions)

到目前为止，你已经能够求值所有示例，因为我们引用了那个列表。实际上，列表看起来像这样：
```clojure
(1 2 3)
```
但如果你求值它，会得到错误：`class java.lang.Long cannot be cast to class clojure.lang.IFn`。当求值未引用的列表时，列表中的第一个元素被视为"函数位置"。这意味着 Clojure 会尝试将 `1` 作为函数调用，这不会成功，因为它不是函数。

你可能开始怀疑 Clojure 程序只是数据？这是正确的。Clojure 代码是数据。更准确地说，Clojure 是同像的(homoiconic)：https://wiki.c2.com/?HomoiconicLanguages。这赋予了宏强大的能力，稍后会讨论。

以下是一些在位置1有正确函数的列表示例：
```clojure
(str 1 2 3 4 5 :foo)
(< 1 2 3 4 5)
(*)
(= "1"
   (str "1")
   (str \1))
(println "From Clojure with ♥️")
(reverse [5 4 3 2 1])
```

位置1之后的所有内容都作为参数传递给函数。

### 定义函数
使用宏 `defn` 定义新函数并将其绑定到当前命名空间中的名称：
```clojure
(defn add2
  [arg]
  (+ arg 2))
```

这定义了一个名为 `add2` 的函数，它接受一个参数。函数体调用核心函数 `+`，参数为 `arg` 和 2。

现在你可以通过将符号 `add2` 放在列表的函数位置，后跟参数来调用它：
```clojure
(add2 3) ; 返回 5
```

Clojure 有一个庞大的核心函数库。参见：https://clojuredocs.org，这是一个社区驱动的 Clojure 核心（及更多）搜索引擎。

## 6. 特殊形式和宏 (Special Forms and Macros)

核心库由库本身的函数和宏组成。引导这个库的是几个（约15个）内置原始形式，也称为"特殊形式"。

### 特殊形式
你已经遇到了其中一个特殊形式：
```clojure
(quote (1 2 3))
```

符号 `quote` 的文档悬停提示会告诉你它是一个特殊形式。你之前使用了它的简写语法：
```clojure
'(1 2 3)
```

验证它们是相同的：
```clojure
(= (quote (1 2 3))
   '(1 2 3))
```

Clojure 有值语义。任何求值为相同数据的数据结构都是相等的，无论结构有多深或多大。
```clojure
(= [1 [1 #{1 {:a 1 :b '(:foo bar)}}]]
   [1 [1 #{1 {:a (- 3 2) :b (quote (:foo bar))}}]]) => true
```
### 重要特殊形式

#### `fn`
一个非常重要的特殊形式是 `fn`（实际上是四个特殊形式，但不管怎样）。没有这个形式，我们就无法定义新函数。以下形式求值为一个函数，该函数将其参数加2：
```clojure
(fn [arg] (+ arg 2))
```

用参数3调用该函数：
```clojure
((fn [arg] (+ arg 2)) 3)
```

#### `def`
另一个特殊形式是 `def`。它定义事物，给它们命名空间名称：
```clojure
(def foo :foo)
```

"定义一个事物"意味着创建一个变量，保存值，并将符号绑定到该变量。求值符号会从它绑定的变量中获取值：
```clojure
foo
```

变量可以使用 `var` 特殊形式访问：
```clojure
(var foo)
```

你通常会看到变量引用简写：
```clojure
#'foo
```

使用这两个特殊形式，我们可以定义函数：
```clojure
(def add2-2 (fn [arg] (+ arg 2)))
(add2-2 3)
```

这就是宏 `defn` 所做的事情。你通常会像下面这样定义函数：
```clojure
(defn add2-3
  [arg]
  (+ arg 2))
```

我们可以使用函数 `macroexpand` 来查看宏产生的内容：
```clojure
(macroexpand '(defn add2-3
                [arg]
                (+ arg 2)))
```

#### `if`
另一个超级重要的特殊形式是 `if`：
```clojure
(if 'test
  'value-if-true
  'value-if-false)
```

`macroexpand` 在这里什么也不做，因为 `if` 不是宏（也不是函数）。

有趣的是：除了 `case` 之外，Clojure 中所有的条件和控制流结构都是使用 `if` 构建的：
```clojure
(macroexpand '(when test
                value-if-true))

(macroexpand '(or a b))

(require 'clojure.walk)
(clojure.walk/macroexpand-all '(or a b))

(macroexpand '(cond
                 y value-if-y
                 z value-if-z
                 :else value-if-x-neither-y-nor-z))

(clojure.walk/macroexpand-all '(cond
                                 y value-if-y
                                 z value-if-z
                                 :else value-if-x-neither-y-nor-z))
```

#### `let`
`let` is a special form that lets you bind values to
  ;; variables that will be used in the body of the form.
```clojure
(let [x 1
      y 2]
  (str x y))
```
The bindings are provided as the first ”argument”,in a vector. This is a pattern that is used by other special forms and macros that let you define bindings.

绑定是局部的，类似于其他编程语言的词法作用域：
```clojure
(do
  (def x :namespace-x)
  (println "`x` in `do` _before_ `let`: " x)
  (let [x :let-x]
    (println "`x` from `let`: " x))
  (println "`x` in `do`, _after_ `let`: " x))

(println "`x` _outside_ `do`: " x)
```

注意：`def` 特殊形式"全局"定义事物，尽管有命名空间。

特殊形式和宏的总结：特殊形式和宏构成了 Clojure 语言本身。与 Clojure 如何读取和求值代码一起，特殊形式构成了 Clojure 语言的核心。下一层次的构建块是宏。

## 7. 读取器 (The Reader)

https://clojure.org/reference/reader

Clojure 读取器负责读取文本，从中生成数据，然后交给编译器。读取器是字面量、符号、字符串、列表、向量、映射和集合被分解和重新组装的地方，确定什么是函数、宏或特殊形式。

### 空白字符
大多数你认为是空白字符的东西都是空白字符，另外，Clojure 作为 LISP 不需要逗号来分隔列表项。不过，逗号可以用作分隔符，因为逗号是空白字符：
```clojure
(= '(1 2 3)
   '(1,2,3)
   '(1,  2, 3)
   '(1,,,,2,,,,3))
```

### 行注释
读取器会跳过从分号开始的行上的所有内容。这是非结构化注释，如果你开始一个form
```clojure
(range 1 ; 10)
```
然后放置一个行注释，使该form的结束括号被注释掉，结构就会被破坏。

由于行上的一切都被忽略，你可以添加任意数量的分号。通常使用两个分号来开始一个完整行的注释。

### 额外语法

#### 引用 (Quote)
我们已经看到了单引号：
```clojure
'something
```
它被转换为：
```clojure
(quote something)
```
`quote` 用于阻止读取器将事物视为应该被求值的东西。

#### 解引用 (Deref)
Clojure 也有引用类型，我们将简要讨论最常见的一个，`atom`：
```clojure
(def an-atom (atom [1 2 3]))
(type an-atom)
```

要从引用中访问值：
```clojure
(deref an-atom)
(type (deref an-atom))
```

`deref` 如此常见，以至于有简写语法：
```clojure
@an-atom
(= (deref an-atom)
   @an-atom)
```

常见错误是忘记解引用：
```clojure
(first an-atom) ; 错误
(first @an-atom) ; 正确
```

#### 调度器 (Dispatcher - # 符号)
`#` 符号时不时出现。它有特殊作用，也称为调度。根据后面的字符，会发生不同的事情。

- 正则表达式有字面量语法，像字符串一样写，但在前面加上 `#` 符号：
```clojure
#"reg(?:ular )?exp(?:ression)?"
(re-seq *1 "regexp regular expression")
(re-seq #"fooo*" "fo foo fooo ")
```
> `*1` is a special symbol for a variable holding the value of the last evaluation result. It might be easier to get a regexp right by using it directly

- 如果 # 符号后跟 `(`，读取器将开始期待一个函数体：
```clojure
#(+ % 2)
```
> % 是一个语法规则，表示这个匿名函数的第一个参数

这是"函数字面量"的特殊语法，指定函数的一种方式。上面的示例等价于这个匿名函数：
```clojure
(fn [arg] (+ arg 2))
```
嵌套函数字面量是禁止的。

- 变量引用简写：
```clojure
#'add2
```

- 读取器忽略下一个形式：
```clojure
#_(println "The reader will not send this function call to the compiler") "This is not ignored"
```
这是一个结构化注释机制，通常用于临时禁用一些代码或数据：
```clojure
(str "a " "b " #_(str 1 2 3 [4 5 6]) "c ") => "abc"
```
忽略标记可以堆叠：
```clojure
(str "a " #_#_ "b " (str 1 2 3 [4 5 6]) "c ") => "ac"
```
注意：读取器_会_读取被忽略的形式。如果其中有语法错误，读取器会报错。

- 命名空间映射关键字简写语法：
```clojure
(= #:foo {:bar 'bar
          :baz 'baz}
   {:foo/bar 'bar
    :foo/baz 'baz})
```
> 就是用 #:foo {:bar 'bar ...} 给字典里的key的命名空间加前缀?

- 双冒号关键字用当前命名空间命名：
```clojure
::foo
(= ::foo :current-namespace/foo)
```

- 标记字面量（也称为数据读取器）：
```clojure
#inst "2018-03-28T10:48:00.000" ; 转换为时间点
#uuid "0000000-0000-0000-0000-000000000016" ; 生成UUID
```
> 会将这些转成java类型

Java 互操作示例：
```clojure
(.before #inst "2018-03-28T10:48:00.000"
         #inst "2021-02-17T00:27:00.000")
```

## 8. 宏 (Macros)

Clojure 有强大的数据转换能力。这里我想强调的是，这种能力可以用于扩展语言本身。由于 Clojure 代码是结构化的，代码是数据，Clojure 可以用于从 Clojure 代码生成 Clojure 代码。这类似于某些语言提供的预处理器功能（如 C 的 `#pragma`），但它更方便、更强大。很多你将学会喜爱并认出的 Clojure 其实是用 Clojure 作为宏创建的。

本指南主要关注让你知道宏是一种东西，帮助你快速意识到何时使用宏而非函数。即，我不会深入讨论如何创建宏。

### 区别
这种区别很重要，因为即使宏调用看起来很像函数调用，宏不是一等公民。它们不能作为参数传递，也不能作为结果返回。

### `when` 宏
让我们简要检查 `when` 宏。这个宏有助于编写更易读的代码。假设你想条件性地求值某些内容。上面你了解到有一个名为 `if` 的特殊形式可用于此：
```clojure
(if 'this-is-true
  'evaluate-this
  'else-evaluate-this)
```

现在假设在 else 情况下你没有要评估的内容。`if` 允许你这样写：
```clojure
(if 'this-is-true
  'evaluate-this)
```

这很好，但你必须额外扫描代码才能看到没有 else 分支。在真实代码中，这可能会变得相当混乱。为了解决这个问题，你可以写：
```clojure
(if 'this-is-true
  'evaluate-this
  nil)
```

但这有点傻，如果有办法告诉阅读代码的人没有 else 分支呢？有！
```clojure
(when 'this-is-true
  'evaluate-this)
```

让我们看看 `when` 是如何定义的，你可以 ctrl/cmd-click `when` 导航到 Clojure `core.clj` 中定义它的地方。你也可以使用函数 `macroexpand`：
```clojure
(macroexpand '(when 'this-is-true
                'evaluate-this))
```

你会注意到 `when` 将主体包装在 `(do ...)` 中，这是一个特殊形式，允许你求值多个表达式，返回最后一个的结果。`do` 在你想要有一些副作用，以及求值某些内容时很有用。在开发中，当你想在表达式求值并返回结果之前 `println` 某些内容时，经常会发生这种情况：
```clojure
(do (println "The quick brown fox jumps over the lazy dog")
    (+ 2 2))
```

`when` 宏让你利用只有一个分支这一事实，所以你可以这样做：
```clojure
(when 'this-is-true
  (println "The quick brown fox jumps over the lazy dog")
  (+ 2 2))
```

没有 `when` 你会写：
```clojure
(if 'this-is-true
  (do
    (println "The quick brown fox jumps over the lazy dog")
    (+ 2 2)))
```

在这里，`when` 既省去了额外扫描 else 分支的麻烦，也省去了使用 `do` 的麻烦。

就宏而言，`when` 是最简单的之一。从两个内置特殊形式 `if` 和 `do`，它组合成一种形式，帮助我们编写易于编写和阅读的代码。

### `for` 宏
`for` 宏真正展示了如何使用 Clojure 扩展 Clojure。你可能会认为它提供了像许多其他语言中的 for 循环那样的循环，但在 Clojure 中没有 for 循环。相反，`for` 是关于列表推导（如果你有 Python 经验，是的，就是那种列表推导）。以下是生成两个向量 `x` 和 `y` 的笛卡尔积的方法：
```clojure
(for [x [1 2 3]
      y [1 2 3 4]]
  [x y])
```

如果你记得上面的 `let` form，以及它如何让你绑定变量在form主体中使用，这很相似，只是 `x` 和 `y` 将绑定到序列中的每个值，并且主体将为 `x` 和 `y` 的所有组合求值。

`for` 也允许你过滤结果：
```clojure
(for [x [1 2 3]
      y [1 2 3 4]
      :when (not= x y)]
  [x y])
```

你可以在推导中绑定变量名以存储中间计算并使代码更具可读性：
```clojure
(for [x [1 2 3]
      y [1 2 3 4]
      :let [d' (- x y)
            d (Math/abs d')]]
  d)
```

这与下面等价：
```clojure
(for [x [1 2 3]
      y [1 2 3 4]]
  (Math/abs (- x y)))
```

过滤器和绑定可以一起使用。使用 `:let` 和 `:when` 使这个推导返回所有 `[x y]` 的列表，其中它们的和是奇数。函数 `+` 和 `odd?` 是你的好朋友。

### 线程宏 (Threading Macros)
宏可以完全重新排列你的代码。内置的"线程"宏就是如此。有时当函数(-ish)调用的嵌套变深时，可能会有点难读，难以跟踪所有的括号：
```clojure
(Math/abs
 (apply -
        (:d (zipmap
             [:a :b :c :d]
             (partition 2 [1 1 2 3 5 8 13 21])))))
```

你从最内层表达式向外读 Clojure，这随着时间的推移会变得更容易，但一个经验丰富的 Clojure 程序员仍然会觉得这个更易读：
```clojure
(->> [1 1 2 3 5 8 13 21]
     (partition 2)
     (zipmap [:a :b :c :d])
     :d
     (apply -)
     (Math/abs))
```

Let's read this together. The thread-last macro,
`->>` is used, it takes its first argument and
places it (threads it) as the last argument to
following function. The first such step in
isolation:

```clojure
(->> [1 1 2 3 5 8 13 21]
       (partition 2))
```
The first argument/element passed to `->>` is `[1 1 2 3 5 8 13 21]`
This is inserted as the last element of the
function call `(partition 2)`, yielding:
```clojure
(partition 2 [1 1 2 3 5 8 13 21])
```

This partitions the list into lists of 2 elements => `((1 1) (2 3) (5 8) (13 21))`
This new list is then inserted (threaded) as the last argument to the next function, yielding:

```clojure
(zipmap [:a :b :c :d] '((1 1) (2 3) (5 8) (13 21)))
```

Which ”zips” together a Clojure map using the first list as keys and the second list as values  => `{:a (1 1), :b (2 3), :c (5 8), :d (13 21)}` . This map is then threaded as the last argument to the function `:d`.
```clojure
(:d '{:a (1 1), :b (2 3), :c (5 8), :d (13 21)})
```
(In clojure keywords are functions that look themselves up in the map handed to them.) => `(13 21)`
You know the drill by now, this is threaded

```clojure
(apply - '(13 21))
```
which applies the `-` function over the list => `-8`, Then this is threaded to `Math/abs`

```clojure
(Math/abs -8)
```



There is also a  hread-first macro `->` 
- https://clojuredocs.org/clojure.core/-%3E

Sometimes you neither want to thread first or last。也有一个宏用于此。`as->` 允许你将变量名绑定到线程化的东西，并在每个函数调用中将其放在你喜欢的任何位置：
```clojure
(as-> 15 foo
  (range 1 foo 3)
  (interpose ": " foo))
```
 It's common to utilize the fact that most characters are available when naming Clojure symbols. I often
use `$` for this threading macro:
```clojure
(as-> 15 $
  (range 1 $ 3)
  (interpose ":" $))
```


其他核心线程宏包括：`cond->`, `cond->>`, `some->`, 和 `some->>` https://clojuredocs.org/clojure.core/cond-%3E

## 9. 流程控制、条件语句、分支 (Flow Control, Conditionals, Branching)

Clojure 的控制流语法很丰富。核心库中几乎所有相关的功能都是使用原始的`if` 实现的。它接受三个形式作为参数：
1. 要求值的条件
2. 如果条件求值为真（truthy）则要评估的内容
3. 如果条件不求值为真（"else" 分支）则要评估的形式

掷骰子，十到二十次，检查是否是6：
```clojure
(if (= 6 (inc (rand-int 6)))
  "One time out of six you get a six"
  "Five times out of six you get something else")
```

由于 Clojure 中没有语句，`if` 相当于你在 C 和许多其他语言中找到的三元 `if` 表达式：
```c
test ? true-expression : false-expression
```

### 真理的探索
再次，在 Clojure 中我们使用求值为值的表达式。当检查分支时，所有值要么是真值，要么是假值。事实上，几乎所有值都是真值：
```clojure
(if true :truthy :falsy) => :truthy
(if :foo :truthy :falsy)
(if '() :truthy :falsy)
(if 0 :truthy :falsy)
(if " " :truthy :falsy)
```

唯一的假值是 `false` 和 `nil`：
```clojure
(if false :truthy :falsy) => :falsy
(if nil :truthy :falsy)
(when false :truthy)
```

关于最后一个：当条件为假时，`when` 求值为 `nil`。由于 `nil` 是假值，上面的 `when` 表达式会使 `if` 的"else"分支被评估：
```clojure
(if (when false :truthy) :true :falsy) 
```

（超糟糕的代码，但无论如何）

当只有布尔真或假可以为你解决问题时，有 `true?` 函数：
```clojure
(true? true) => true
(true? 0)  => false
(true? '()) => false
(true? nil) => false
(true? false) => false
```

因此：
```clojure
(if (true? 0) :true :false)
```

### `when`
正如之前提到的，`when` 是一个单分支 `if`，仅用于真分支，并为你包装在 `do` 中。尝试这个，然后尝试用 `if` 替换 `when`：
```clojure
(when :truthy
  (println "That sounds true to me")
  :truthy-for-you)
```

如果 `when` 条件不是真值，将返回 `nil`：
```clojure
(when nil :true-enough?)
```

### `cond`
由于深度嵌套的 if/else 结构可能难以编写、阅读和维护，Clojure 核心提供了几种更多的流程控制结构，一个非常常见的是 `cond` 宏。它接受条件/结果形式对，测试每个条件，如果为真，则求值结果形式并"返回"它，短路以便不再测试更多条件：
```clojure
(let [dice-roll (inc (rand-int 6))]
  (cond
    (= 6 dice-roll)   "Six is as high as it gets"
    (odd? dice-roll) (str "An odd roll " dice-roll " is")
    :else            (str "Not six, nor odd, instead: " dice-roll)))
```

`:else` 只是关键字 `:else`，它求值为自身并且是真值。它是为你的 cond 形式提供默认值的常规方式。如果没有默认子句`:else`，这个form会对任何非六非奇数求值为 `nil`。通过在 `:else` 关键字前放置两个忽略标记 (`#_ #_`) 来尝试。

### `case`
有点类似于其他语言中的 `switch/case` 结构，Clojure 核心有 `case` 宏，它接受一个测试表达式，后跟零个或多个测试常量/表达式对，后跟一个可选表达式。（然而，测试表达式后的主体不能为空。）
```clojure
(let [test-str "foo bar"]
  (case test-str
    "foo bar" (str "That's very " :foo-bar)
    "baz"     :baz
    (count test-str)))
```

尾随表达式（如果有）作为默认值"返回"。

如果没有子句匹配且没有默认值，会发生运行时错误。

注意！测试常量必须是编译时常量，编译器不会帮你找到像这样的错误：
```clojure
(let [test-int 2
      two 2]
  (case test-int
    1     :one
    two   (str "That's not a literal 2")
    (str test-int ": Probably not expected")))
```

### 减少分支
核心库中有丰富的函数，可以帮助你避免编写分支代码。相反，你提供条件作为谓词。一个常用的谓词函数是 `filter`：
```clojure
(filter even? [0 1 2 3 4 5 6 7 8 9 10 11 12])
```
和它的"兄弟" `remove`：
```clojure
(remove odd? [0 1 2 3 4 5 6 7 8 9 10 11 12])
```

过滤值序列是常见任务，你的编程时间可以用来决定_如何_过滤，通过编写谓词。有时你甚至不需要这样做，Clojure 核心中有很多谓词：
```clojure
(zero? 0)
(even? 0)
(neg? 0)
(pos? 0)
(nat-int? 0)
(empty? " ")
(empty? [])
(empty? (take 0 [1 2 3]))
(integer? -2/1)
(indexed? [1 2 3])
(indexed? '(1 2 3))
```

什么是谓词？就本指南而言，谓词是一个测试事物真值的函数。按照约定，这些函数以 `?` 结尾。许多只接受一个参数。

一个方便的谓词是 `some?`，它测试"某物性"，如果不是 `nil`，就是某物：
```clojure
(some? nil)
(some? false)
(some? '())
```

你可以用它来测试某物是否为 `nil`，通过将其包装在对 `not` 函数的调用中：
```clojure
(not (some? nil))
(not (some? false))
```

你会有冲动定义一个名为 `nil?` 的函数，对吧？你不需要：
```clojure
(nil? nil)
(nil? false)
```

Clojure 核心还包含接受谓词加集合以应用它的谓词。例如 `every?`：
```clojure
(every? nat-int? [0 1 2])
(every? nat-int? [-1 0 1 2])
```

这种带函数作为参数的函数模式在 Clojure 中很常见。它超出了谓词。接受函数作为参数的函数被称为"高阶函数"。
https://en.wikipedia.org/wiki/Higher-order_function

## 10. 函数 (Functions) - 深入

在深入高阶函数之前，让我们看一下函数。函数是 Clojure 的一等公民，是解决业务问题的主要构建块。

我们已经看到了几种创建函数的方法。这是一个匿名函数，返回给它的整数，除非它能被 15 整除，此时它返回 `"fizz buzz"`。（这不是完整的 Fizz Buzz 问题。）
```clojure
(fn [n]
  (if (zero? (mod n 15))
    "fizz buzz"
    n))
```

让我们定义它（将其绑定到我们可以使用的符号）：
```clojure
(def fizz-buzz-1 (fn [n]
                   (if (zero? (mod n 15))
                     "fizz buzz"
                     n)))
(fizz-buzz-1 2)
(fizz-buzz-1 15)
```

There's a macro that lets us define and create the function in one call：
```clojure
(defn fizz-buzz-2 [n]
  (if (zero? (mod n 15))
    "fizz buzz"
    n))

(fizz-buzz-2 4)
```

`defn` 允许我们为函数提供文档：
```clojure
(defn fizz-buzz-3
  "Says 'fizz buzz' if `n` is divisible by 15,
  otherwise says `n`"
  [n]
  (if (zero? (mod n 15))
    "fizz buzz"
    n))

(doc fizz-buzz-3) ; (或悬停 `fizz-buzz-3`)
```

很容易放错文档字符串的位置，特别是因为通常像上面的 `fizz-buzz-2` 一样编写 `defn` 形式：
```clojure
(defn fizz-buzz-4
  [n]
  "Says 'fizz buzz' if `n` is divisible by 15,
  otherwise says `n`"
  (if (zero? (mod n 15))
    "fizz buzz"
    n))
```

这指定了一个完全有效的函数体，所以 Clojure 不会对此抱怨。但是：
```clojure
(doc fizz-buzz-4) => nil 
```
> 这说明文档字符串放置的位置不对，导致文档系统无法识别这个字符串作为文档

clj-kondo 的默认配置会帮你发现这些错误。然而，它无法帮你解决这个问题：
> clj-kondo 是一个静态代码检测工具
```clojure
(defn only-the-last-eval-returns [x]
  [1 x]
  [2 x])

(only-the-last-eval-returns "foo")
```

像这样很容易发现，你可能会想知道为什么你会这样写一个函数。然而，你可能会犯这个错误，特别是如果你曾经写过一些 Hiccup，这是一种用 Clojure 数据结构编写 HTML 的超好方法。它被流行的 Reagent 库使用 https://purelyfunctional.tv/guide/reagent/#hiccup

当你犯这个错误并完成你的一小时 bug 追踪时，你会听到本指南低语"告诉过你！"

`fn`（ `defn`也是 ）的参数绑定向量按顺序将每个参数绑定到一个名称：
```clojure
(defn coords->str [x y]
  (str "x: " x ", y: " y))
```

### 可变参数函数 (Variadic Functions)
你可以定义接受任意数量参数的函数，方法是在最后一个参数名前放置 `&`。这将名称绑定到一个包含所有剩余参数的序列：
```clojure
(defn lead+members [lead & members]
  {:lead lead
   :members members})

(lead+members "Dave Mustain"
              "Marty Friedman"
              "Nick Menza"
              "David Ellefson")
```

### 多重元数 (Multi-arity)
Clojure 支持基于参数数量的函数签名。`defn` 宏允许你将每个元数定义为一个单独的列表。这通常用于提供默认值：
```clojure
(defn hello
  ([] (hello "World"))
  ([s] (str "Hello " s "!")))

(hello)
(hello "Clojure Friend")
```

或者为函数创建一个"标识"值（一个起始值，操作的其余部分使用）。假设你想添加两个 x-y 坐标：
```clojure
(defn add-coords-1 [coord-1 coord-2]
  {:x (+ (:x coord-1)
         (:x coord-2))
   :y (+ (:y coord-1)
         (:y coord-2))})

(add-coords-1 {:x -2 :y 10}
               {:x 4 :y 6})
```

如果要求是，如果函数被调用时只有一个参数，它应该将其添加到原点？（看到我做了什么吗？函数的标识是函数应该开始的地方，所以从原点开始。😎）我们可以看到 `add-coords-1` 在这里失败：
```clojure
(add-coords-1 {:x -2 :y 10})
```

我们需要添加一个一元数：
```clojure
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
```

现在，如果用没有参数调用，它应该返回原点，因为如果你不添加任何坐标，你就停留在起点。编写一个函数 `add-coords-3`，当像这样调用时返回原点：
```clojure
(add-coords-3)
```

它仍然应该能够像这样调用：
```clojure
(add-coords-3 {:x 3 :y 4})
(add-coords-3 {:x 2 :y 4}
              {:x -4 :y -4})
```

这与让函数与其他函数组合有关。例如，高阶函数 `apply`，它在一个序列上"应用"一个函数。现在我们可以像这样应用我们的 `add-coords-2` 函数：
```clojure
(apply add-coords-2 [{:x 1 :y 1} {:x 4 :y 4}])
```
和这样：
```clojure
(apply add-coords-2 [{:x 1 :y 1}])
```
但不能这样：
```clojure
(apply add-coords-2 [])
```

但你创建的 `add-coords-3` 函数可以：
```clojure
(apply add-coords-3 [])
```

它无法处理任意长的坐标序列。为此，我们需要再添加一个元数：
```clojure
(defn add-coords-4
  ;; 在这里添加你的 `add-coords-3` 的零元数
  ;; 在这里添加你的 `add-coords-3` 的一元数
  ([coord-1 coord-2]
   {:x (+ (:x coord-1)
          (:x coord-2))
    :y (+ (:y coord-1)
          (:y coord-2))})
  ([coord-1 coord-2 & more-coords]
   ;; 当你了解高阶函数 `reduce` 后实现这个元数
   ))
```

听 Eric Normand 详细解释为什么函数的标识很重要：
https://lispcast.com/what-is-a-functions-identity/

### 闭包 (Closures)
当你即时创建函数，lambda，如果你喜欢，你可以直接使用 `fn` 特殊形式，或通过 `#()` 语法代理。这创建了一个闭包，就像在 JavaScript 和其他语言中一样。也就是说，这些函数可以访问在创建函数时变量的快照值：
```clojure
(defn named-coords-factory [name]
  (fn [x y] {:name name
             :coords {:x x
                      :y y}}))

(def bob-coords-fn (named-coords-factory "Bob"))
(def fred-coords-fn (named-coords-factory "Fred"))

(bob-coords-fn 0 0)
(fred-coords-fn 5 5)
(bob-coords-fn 7 7)
```

闭包对于在 let 绑定框内创建低元数函数很有用，函数体可以使用：
```clojure
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
      " " (whisper)
      "! " (yell)
      "? " (ask)
      (default))))
```

在 let 绑定框中创建的所有函数都"闭合"了 `command` 和 `sentence`，所以 `case` 可以保持简洁易读：
```clojure
(whisper-or-yell-or-ask " " "How wOnDerFuLLY NIce To seE")
(whisper-or-yell-or-ask "! " "Hello tHERE")
(whisper-or-yell-or-ask "? " "How are you doing")
(whisper-or-yell-or-ask ": " "Oh well")
```

### 属性映射 (Attributes Map)
`defn` 宏允许你以映射形式向函数添加属性。这作为元数据（稍后会更多关于这一点）添加到保存函数的变量。映射放在函数名之后，任何文档之后，参数向量（或任何元数）之前：
```clojure
(defn i-have-attributes
  {:doc "Docs can be added like this too"
   :foo "Any attributes you fancy"}
  []
  "Good for you")

(doc i-have-attributes)
(meta #'i-have-attributes)
```

你可以添加的一个方便属性是测试函数。测试运行器会获取这个：
```clojure
(defn fizz-buzz-5
  "That limited fizz-buzz function again"
  {:test (fn []
           (is (= "fizz-buzz" (fizz-buzz-5 15)))
           (is (= 3 (fizz-buzz-5 3))))}
  [n]
  (if (pos? (rem 15 n))
    "fizz-buzz"
    n))

(clojure.test/test-var #'fizz-buzz-5)
```

需要修复 bug。😀

如何实现完整的 Fizz Buzz？
https://en.wikipedia.org/wiki/Fizz_buzz
```clojure
(defn fizz-buzz
  "My Fizz Buzz solution"
  {:test (fn []
           (are [arg expected]
                (= expected (fizz-buzz arg))
             1  1
             3   "Fizz"
             4  4
             5   "Buzz"
             7  7
             15  "Fizz Buzz"
             20  "Buzz"))}
  [n])

(clojure.test/test-var #'fizz-buzz)
(map fizz-buzz (range 1 40))
```

对编译器和各种 Clojure 核心设施有特殊意义的元数据列在这里：
https://clojure.org/reference/special_forms

现在，进入高阶函数！

## 11. 高阶函数 (Higher order functions)

Clojure 之所以如此强大的一个重大贡献是函数是"一等公民"
-  https://en.wikipedia.org/wiki/First-class_function。

它们可以是集合中的值（也可以是映射中的键），可以作为参数传递给其他函数，并作为求值结果"返回"。你可能从像 JavaScript 这样的语言中熟悉这个概念。

让我们看一下 Clojure 核心中的某些高阶函数。`some` 对集合的元素逐个调用函数，并返回第一个真值结果，如果在某个元素产生真值结果之前列表耗尽，则返回 `nil`：
```clojure
(some even? [1 1 2 3 5 8 13 21])
```

不要与 `some?` 混淆，它不是高阶函数：
```clojure
(some some? [nil false])
(some some? [nil nil])
```

Clojure 中的一个常见惯用法是使用 `set` 作为谓词在集合中查找事物。是的，集合是函数。用作函数时，它们会在自身中查找给它们的参数：
```clojure
(#{ "foo" "bar" } "bar")
```

因此：
```clojure
(some #{ "foo" } [ "foo" "bar" "baz" ])
(some #{ "fubar" } [ "foo" "bar" "baz" ])
```

`apply` 接受一个函数和一个集合，然后在集合上"应用"该函数。假设你有一个数字集合，想要将它们相加。这行不通：
```clojure
(+ [1 1 2 3 5 8 13 21])
```

`apply` 来救援：
```clojure
(apply + [1 1 2 3 5 8 13 21])
```

将数字连接为字符串：
```clojure
(apply str [1 1 2 3 5 8 13 21])
```

对比：
```clojure
(str [1 1 2 3 5 8 13 21])
```

我们也已经在上面看到了 `filter` 和 `remove`，这是两个非常常用的高阶函数。它们与 `map` 和 `reduce` 处于同一行列。继续阅读。😎

## 12. `map` 和 `reduce`

在具有头等函数的其他语言中，你可能使用过高阶函数 `map` 和 `reduce`。它们值得详细研究和练习，这里有一个超赞的预告片：
https://purelyfunctional.tv/courses/3-functional-tools/

让我们也在这里简要检查一下。`map` 从头到尾对一个或多个集合的元素调用函数，并返回（惰性的，稍后会更多）结果序列，顺序相同。假设我们想将列表中的每个元素减一：
```clojure
(map dec '(1 1 2 3 5 8 13 21))
```

假设我们想再次将它们减一：
```clojure
(->> '(1 1 2 3 5 8 13 21)
     (map dec)
     (map dec))
```

嗯，也许最好减去二：
```clojure
(map (fn [n] (- n 2)) '(1 1 2 3 5 8 13 21))
```

如果你给 `map` 更多集合处理，它会重复：
1. 从每个集合中挑选下一个项目
2. 将它们作为参数给映射函数
3. 将结果添加到返回序列中
直到最短的集合耗尽：
```clojure
(map + [1 2 3] '(0 2 4 6 8))
(map (fn [n1 s n2] (str n1 ": " s "-" n2))
     (range)
     [ "foo" "bar" "baz" ]
     (range 2 -1 -1))
```

（我们还没有过多讨论 `range`，它是一个生成数字序列的函数。没有参数时，它生成一个无限的，小心 😀，整数序列 0, 0+1, 0+2, 0+3, 0+4, 0+5, 0.6 ... 好在其他序列耗尽了！）

在其他语言中，你可能用 `for` 循环解决的许多任务，在 Clojure 中用 `map` 解决。

对于其他这样的"for循环式"任务，你将使用 `reduce`。与 `map` 不同，它不限于产生与输入集合相同长度或形状的结果。相反，它累积任何形状的结果。例如，它可以从未数字集合创建一个字符串：
```clojure
(reduce (fn [acc n]
          (str acc ": " n))
        [1 1 2 3 5 8 13 21])
```

`reduce` 将使用两个参数调用函数：上次函数调用的结果和列表中的下一个数字。过程的开始是特殊的，因为那时还没有结果。`reduce` 有两种处理方式，具体来说有两种元数。用两个参数调用时，它使用列表中的前两个元素进行第一次函数调用。这是使用 `reduce` 的两个元数版本减少 `+` 函数：
```clojure
(reduce + [1 1 2 3 5 8 13 21])
```

过程从这样调用 `+` 开始：
```clojure
(+ 1 1)
```

给 `reduce` 三个参数使其使用第二个参数作为起始"结果"：
```clojure
(reduce + 100 [1 1 2 3 5 8 13 21])
```

你可能注意到 `+` 函数接受多于（和少于）2个参数：
```clojure
(+)
(+ 1)
(+ 1 1)
(+ 1 1 2 3 5 8 13 21)
```

`+` 将取第一个参数（如果有），并将其添加到"当前"值（为零），然后下一个参数并将其添加到新当前值，依此类推，直到有结果。这个过程听起来有点像我刚才描述的 reduce，对吧？事实上，就是这样。

如果我们实现 `+` 函数，我们如何做？我们可以先实现一个将两个数字相加的东西，然后用它作为减少函数与 `reduce` 一起使用。当然，现在我们有将两个数字相加的任务，而不使用现有的 `+` 函数... 🤔 SICP 有这个，使用 Peano 算术 https://youtu.be/V_7mmwpgJHU?t=814 https://en.wikipedia.org/wiki/Peano_axioms#Addition

```clojure
(defn plus [x y]
  (if (zero? x)
    y
    (plus (dec x) (inc y))))
```

但这里没有处理太多情况... 嗯... 让我们保持简单，只做整数数学。然后我们可以使用 Java 的 `Integer.sum(x, y)` 方法：
```clojure
(Integer/sum 1 1)
```

太棒了，有了这个，我们可以创建一个 `add-two` 函数：
```clojure
(defn add-two [x y]
  (Integer/sum x y))
(add-two 1 1)
```

与 `+` 不同，这个不能完全与高阶函数如 apply 组合：
```clojure
(apply add-two [])
(apply add-two [1])
(apply add-two [1 1])
(apply add-two [1 1 2 3 5 8 13 21])
```

我们需要 `add-many`。使用 `reduce` 和我们的 `add-two`，我们可以这样定义 `add-many`：
```clojure
(defn add-many [& numbers]
  (reduce add-two numbers))
```
这样就行，对吧？
```clojure
(apply add-many [1])
(apply add-many [1 1])
(apply add-many [1 1 2 3 5 8 13 21])
```

零元数版本的 `+` 呢，你问？正确，那会爆炸：
```clojure
(add-many)
```

内置的 `+` 函数有一个默认的"当前"值为零，记得吗？我们可以将它添加到 `add-many` 中，有两种方式：要么添加一个零元数签名，要么使用三个元数的 `reduce`。让我们选择后一种选项，因为我们正在学习关于 reduce：
```clojure
(defn add* [& numbers]
  (reduce add-two 0 numbers))
(add*)
(add* 1)
(add* 1 1)
(add* 1 1 2 3)
```

BOOM.

我们也可以用 `apply` 使用它：
```clojure
(apply add* [])
(apply add* [1])
(apply add* [1 1])
(apply add* [1 1 2 3 5 8 13 21])
```

或者 `reduce`：
```clojure
(reduce add* [])
(reduce add* [1])
(reduce add* [1 1])
(reduce add* [1 1 2 3 5 8 13 21])
```

除了我们只处理整数外，我们的 `add*` 非常像 Clojure 核心中 `+` 的实现方式。检查一下（在输出窗口中）：
```clojure
(source +)
```

嗯，好吧，他们似乎在低元数情况下使用多元数函数签名，可能是因为类型转换，但无论如何，😀

关于 `reduce` 还有一件事要提。编写减少函数时，你可以在输入序列耗尽之前停止该过程，使用 `reduced` 函数。假设我们想要输入序列作为一个由 `:` 分隔的字符串，但在看到 `nil` 项时停止。这是最后一个版本作为比较：
```clojure
(reduce (fn [acc n]
          (str acc ": " n))
        [1 1 2 3 5 8 nil 13 21])
```

我们可以通过在遇到 `nil` 项时用累积值调用 `reduced` 来短路这个过程：
```clojure
(reduce (fn [acc n]
          (if (nil? n)
            (reduced acc)
            (str acc ": " n)))
        [1 1 2 3 5 8 nil 13 21])
```

这是发生的事情：
```clojure
(doc reduced)
```

减少是 Clojure 中一个非常重要的概念，因为它是一种"函数优先"的语言。或者正如 Functional Design 节目中所说的 https://clojuredesign.club/episode/058-reducing-it-down/ "减少函数是函数式编程的支柱，因为我们没有变异。"

事实上，在 Clojure 中减少是如此重要，Rich Hickey 添加了一个包含更多功能的 reducers 库 https://clojure.org/reference/reducers

Functional Design 二人组，Nate Jones 和 Christoph Neumann 也检查了这个库：https://clojuredesign.club/episode/060-reduce-done-quick/

该集中的精彩引用："序列抽象，它相当惰性。"

我们不会深入 reducers 库的兔子洞...

## 13. 不可变性 (Immutability)

相当疯狂的是，我们已经讨论 Clojure 这么久了，还没有讨论它如何鼓励我们在处理数据时不改变数据。Clojurians 从不停止谈论不可变性，对吧？我们有时听起来像 Rothbardians，定义自己为 State 的敌人 😄 https://www.youtube.com/watch?v=qe60zwUAOqE

在某种程度上这是真的，作为 Clojurians，我们经常尝试在操作持续期间保持在数据转换模式中，只在"边界"处理不纯世界，开始时我们可能在读取一些输入，结束时我们可能在更新数据库，将结果打印到文件（或屏幕），或改变网页的 DOM。

Clojure 通过多种方式鼓励我们走在不可变的道路上，我将在这里提到其中两种：
* 持久数据结构 (Persistent Data Structures)
* 纯函数 (Pure Functions)

### 持久数据结构 (Persistent Data Structures)
Clojure 通过为我们提供不可变数据结构来帮助我们保持在不可变领域。这些的实现称为持久数据结构：https://en.wikipedia.org/wiki/Persistent_data_structure 实际上，这意味着数据结构永远不会改变，我们用于转换它们的函数实际上创建了副本。（以非常聪明的方式，所以现在不要开始担心。）

假设我们定义了一个包含一些数字的向量：
```clojure
(def eighteen [1 0 0 1 0])
eighteen
```

现在我们想将最后一个 `0` 更改为 `1`。我们可以使用 `assoc` 函数。在向量上使用时，它接受一个索引和新值：
```clojure
(def nineteen (assoc eighteen 4 1))
nineteen
```

再次检查 `eighteen`...
```clojure
eighteen
```
...我们看到它仍然忠于它的名字。在索引4处将 `1` 关联到它创建了一个副本，然后定义为 `nineteen`。

这在局部绑定中也是正确的：
```clojure
(let [origin {:x 0
              :y 0}
      x-travel (assoc origin :x 100)]
  [origin x-travel])
```

这提供了非常确定性的程序流程。数据不会在我们脚下随意改变。不改变状态的转换过程更容易并行化，其他线程无法改变你正在转换的数据。一整类 bug 永远没有机会孵化！

不可变性带来的另一个好处是 Clojure 可以有效地提供值相等性。值根据定义是不可变的。在 Clojure 中，即使是最深的数据结构也可以在不到一瞬间内进行比较。

让我们用一个不太深的结构展示这一点（除了名称）：
```clojure
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
```

`update-in` 是一个用于转换数据结构的高阶函数，给定一个"地址"和一个函数。我们可以用它来让两个-Bob 找到两个-Alice，就像一-Bob 和一-Alice 已经找到彼此一样：
```clojure
(def unified-universa
  (update-in universa [:two "Bob" :z] inc))
unified-universa

(= (:one unified-universa)
   (:two unified-universa))

(= universa unified-universa)
```

你永远不必再编写 `equals()` 方法！😄

不可变性也使我们的程序与你可以随意改变变量值时不同。可能需要一段时间来习惯这一点。（我仍然处于更容易看到许多问题的变异解决方案的阶段。这种情况越来越少，但无论如何。可能你会比我更快掌握它。）

坚持理解它是绝对值得的。回报是巨大的。如果你只打算查看我在本指南中推荐的资源之一，我建议是 Rafal Dittwald 关于以 Clojure 方式解决问题的这个：https://www.youtube.com/watch?v=vK1DazRK_a0 剧透：他在视频中没有使用 Clojure

当然，在谈话中，Rafal 不仅假装数据是不可变的。他还使用了函数纯度。

### 纯函数 (Pure Functions)
Clojure 不像某些语言那样强制纯度（看着你 Haskell），但它使养成编写纯函数的习惯变得容易，从而将副作用推向程序的"边缘"。

如果函数遵守以下规则，则被认为是纯的：
1. 对于相同的输入，始终返回相同的值
2. 不影响其环境中的任何内容。因此，不改变任何东西，包括不打印任何内容到任何地方，或命中改变 API 端点。

纯函数是确定性的，你可以安全地调用它，不用担心它会更新应用程序状态或做任何其他事情，只计算基于你给它的输入的返回值，仅此而已。

## 14. 转换数据结构 (Transforming Data Structures)

Clojure 有一个核心库，使"深入"数据结构并操作它变得容易，有趣且可读，创建一个带有结果的副本。

我们已经看到了 `assoc`，它创建了数据结构的副本，在索引（对于 `vector`）或键（对于映射）处有新值：
```clojure
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
```

`assoc` 可以向映射添加新键：
```clojure
(def colt-express-w-age
  (assoc colt-express :age-from 10))
```

对于向量，你只能在最后一项后面添加新项，不能超出：
```clojure
(def board-games-empty
  [])

(def board-games-w-c-e
  (assoc board-games-empty 0 colt-express))
```

`board-games-empty` 仍然是空的。因此：
```clojure
(def board-games-w-c-e-and-exit-fail
  (assoc board-games-empty 1 exit-haunted))

(def board-games-w-c-e-and-exit
  (assoc board-games-w-c-e 1 exit-haunted))
```

使用 `assoc` 向向量添加内容并不常见。对于这个，`conj` 通常更有意义：
```clojure
(conj board-games-empty colt-express exit-haunted)
```

映射上的 `assoc` 可以替换现有值（在副本中）：
```clojure
(def colt-express-w-age-and-adjusted-playtime
  (assoc colt-express-w-age :play-time 45))
```

向量上的 `assoc` 也可以做到这一点：
```clojure
(def board-games-w-adjusted-c-e
  (assoc board-games-w-c-e
         0
         colt-express-w-age-and-adjusted-playtime))
```

你可以在一次调用中 `assoc` 多个内容：
```clojure
(assoc colt-express
       :play-time 50
       :age-from 10)

(assoc board-games-empty
       0 colt-express
       1 exit-haunted)
```

（再次，有 `conj` 用于此。）

对于映射，还有 `merge`，让你合并两个或多个映射：
```clojure
(merge colt-express
       {:play-time 45
        :age-from 10})
```

注意：它是"浅"合并，所以像这样添加家庭成员评级不会起作用：
```clojure
(merge exit-haunted
       {:play-time 90
        :ratings {:lun 5}
        :age-from 10})
```

`assoc` 做同样的事情：
```clojure
(assoc exit-haunted :ratings {:lun 5})
```

Clojure 核心中没有深合并，但有 `assoc-in` 用于深入访问。它不接受键（或索引），而是接受一个"路径"：
```clojure
(assoc-in exit-haunted [:ratings :lun] 5)

(assoc-in colt-express [:categories 2] "Planning")
;;（但...不要这样做，参见下面的 `update`，了解如何 `conj` 类别。）
```

与 `assoc` 不同，你一次只能用 `assoc-in` 添加一个东西。

使用 `dissoc` 从映射中删除内容：
```clojure
(dissoc colt-express :play-time :ratings :categories)
```

你可能会经常在 REPL（像你在这个文件中这样）中使用 `dissoc`，来检查可能包含一些大数据结构的数据结构，比如日志或其他：
```clojure
(dissoc colt-express :log)
```

（这个数据结构没有任何日志，所以它保持不变，但无论如何。）

Clojure 核心中没有 `dissoc-in`，但在访问 `update` 和 `update-in` 之后，让我们回到这个话题。

`update` 和 `update-in` 与它们的 `assoc` 对应物类似，但不是值，它们接受一个用于操作值的函数：
```clojure
(update exit-haunted :name string/upper-case)
```

练习：将 `colt-express` 条目的 `:play-time` 更新为 5 或左右。

你添加到函数后的参数会传递给函数：
```clojure
(update colt-express :categories conj "Planning")
```

练习：使你对 `:play-time` 的更新将 `5`（或左右）作为参数。

练习：从 `exit-haunted` 的 `:ratings` 中移除 `:pez` 和 `:wiv` 条目。

`update-in` 是 `assoc-in` 的对应物，就像 `update` 是 `assoc` 的对应物：
```clojure
(update-in colt-express [:ratings :lun] inc)

(update-in colt-express [:ratings :lun] + 9000)
;; https://www.youtube.com/watch?v=PCHxU7witPA
```

练习：没有 `dissoc-in`，但它看起来你可以使用 `update-in` 来做到这一点，不是吗？

奖励是你在缺少 `dissoc-in` 时可以少访问一次 StackOverflow 😄
https://stackoverflow.com/a/21942548/44639

我们之前已经使用关键字作为映射查找函数。这很好，但有时你可能更喜欢 `get` 函数：
```clojure
(get colt-express :ratings)

(= (:ratings colt-express)
   (get colt-express :ratings))
```

`get` 接受第三个参数，如果条目缺失，将用作默认值：
```clojure
(get exit-haunted :play-time 0)
```

关键字作为查找函数也支持这一点：
```clojure
(:play-time exit-haunted 0)
```

没有默认值，将返回 `nil`。这可能会爆炸，取决于你用这个值做什么：
```clojure
(* (get colt-express :play-time) 2)
(* (get exit-haunted :play-time) 2)
```

在这种情况下，安全总比后悔好：
```clojure
(* (get colt-express :play-time 0) 2)
(* (get exit-haunted :play-time 0) 2)
```

是的，也有 `get-in`。练习：使用 `get-in` 获取我对这两个精彩家庭游戏的评级。

你可能已经注意到，本节中的所有函数都将集合作为第一个参数。这使得它们很容易与线程第一 `->` 宏一起使用。这是设计使然，是高度惯用的 Clojure。

通常会看到像这样的数据转换管道：
```clojure
(-> exit-haunted
    (assoc :play-time 90)
    (update :categories conj "Scary")
    (assoc-in [:ratings :lun] 5)
    (update-in [:ratings :vig] + 1)
    (dissoc :name)
    (update :log vec)
    (update :log conj "Name redacted")
    (update :log conj "(Because scary)"))
```

（尽管，可能比这更有意义。）

我推荐在 ClojureDocs 上"另请参阅"浏览一些从这里开始的内容：
https://clojuredocs.org/clojure.core/update-in
并将很多示例粘贴到这里进行实验。


## 15. 操作集合 (Manipulating Sets)

(下面的部分由Qwen llm续写生成)

集合（Sets）是 Clojure 中四种核心集合类型之一，具有两个关键特性：**唯一性**（没有重复元素）和**无序性**（不保证元素顺序）。

集合字面量使用 `#{}` 语法：
```clojure
#{1 2 3}
#{:a :b :c}
#{}
```

### 集合的基本操作

#### 创建集合
```clojure
;; 从字面量
#{1 2 3}

;; 从其他集合转换
(set [1 2 3 2 1])  ; => #{1 2 3}
(set "hello")       ; => #{\h \e \l \o}

;; 创建空集合
#{}
(hash-set)
(sorted-set)
```

### 添加和删除元素
```clojure
;; 添加元素 - conj 用于添加
(conj #{1 2 3} 4)   ; => #{1 2 3 4}
(conj #{1 2 3} 2)   ; => #{1 2 3} (重复元素被忽略)

;; 删除元素 - disj 用于删除
(disj #{1 2 3} 2)   ; => #{1 3}
(disj #{1 2 3} 4)   ; => #{1 2 3} (不存在的元素被忽略)
```

### 检查成员关系
集合在 Clojure 中同时也是函数，这使得成员检查非常简洁：
```clojure
;; 作为函数调用
(#{1 2 3} 2)   ; => 2 (如果元素存在，返回该元素)
(#{1 2 3} 4)   ; => nil (如果元素不存在，返回 nil)

;; 使用 contains? 函数
(contains? #{1 2 3} 2)  ; => true
(contains? #{1 2 3} 4)  ; => false

;; 使用 some 函数检查多个值
(some #{1 2 3} [4 5 2 6])  ; => 2 (返回第一个在集合中存在的元素)
```

## 集合运算

Clojure 提供了完整的集合运算支持，让集合操作变得直观而强大：

```clojure
(def odds #{1 3 5 7 9})
(def evens #{0 2 4 6 8})
(def primes #{2 3 5 7 11})

;; 并集 (union)
(clojure.set/union odds evens)  ; => #{0 1 2 3 4 5 6 7 8 9}

;; 交集 (intersection)
(clojure.set/intersection odds primes)  ; => #{3 5 7}

;; 差集 (difference)
(clojure.set/difference odds primes)  ; => #{1 9}
(clojure.set/difference primes odds) ; => #{2 11}

;; 对称差集 (可以用 union 和 difference 组合)
(clojure.set/union 
  (clojure.set/difference odds primes)
  (clojure.set/difference primes odds))  ; => #{1 2 9 11}

;; 子集关系
(clojure.set/subset? #{1 3} #{1 2 3 4})  ; => true
(clojure.set/superset? #{1 2 3 4} #{1 3})  ; => true
```

## 集合与高阶函数

集合可以与所有 Clojure 高阶函数配合使用，让数据处理更加灵活：

```clojure
;; 使用 map 转换集合
(map inc #{1 2 3})  ; => (2 3 4) 注意: 返回的是序列，不是集合

;; 但可以转换回集合
(set (map inc #{1 2 3}))  ; => #{2 3 4}

;; 使用 filter 过滤
(set (filter even? #{1 2 3 4 5}))  ; => #{2 4}

;; 使用 reduce 聚合
(reduce + #{1 2 3 4 5})  ; => 15
```

## 集合的实际用例

在实际程序中，集合有多种常见用途：

```clojure
;; 1. 去重
(def users ["Alice" "Bob" "Charlie" "Alice" "Diana"])
(distinct users)  ; => ("Alice" "Bob" "Charlie" "Diana")
(set users)       ; => #{"Diana" "Charlie" "Bob" "Alice"}

;; 2. 快速查找
(def valid-codes #{:active :inactive :suspended :deleted})
(def user-status :active)

(if (valid-codes user-status)
  (println "Valid status") 
  (println "Invalid status"))

;; 3. 多选标签处理
(def document-tags #{"clojure" "functional" "immutable"})
(def search-tags #{"clojure" "data"})

;; 查找同时包含所有搜索标签的文档
(if (clojure.set/subset? search-tags document-tags)
  (println "Document matches all tags")
  (println "Document does not match all tags"))

;; 4. 图算法中的节点集合
(def graph {:a #{:b :c}
            :b #{:a :d}
            :c #{:a}
            :d #{:b}})
```

## 集合类型

Clojure 有两种主要的集合实现：

1. **哈希集合 (Hash Sets)**
   - 默认的集合类型
   - 无序
   - 基于哈希表实现
   - O(1) 的查找、添加和删除操作

2. **排序集合 (Sorted Sets)**
   - 保持元素排序
   - 基于红黑树实现
   - O(log n) 的查找、添加和删除操作
   - 需要元素实现 Comparable 接口

```clojure
(def hash-set-example #{3 1 4 2 5})
(def sorted-set-example (sorted-set 3 1 4 2 5))

hash-set-example     ; => #{1 4 3 2 5} (顺序不确定)
sorted-set-example   ; => #{1 2 3 4 5} (按顺序)
```

## 选择正确的集合类型

当选择使用哪种集合类型时，考虑以下因素：

- **需要唯一性但不关心顺序**：使用标准集合 `#{}` 
- **需要唯一性且关心顺序**：使用排序集合 `(sorted-set)`
- **需要保留重复元素**：使用向量 `[]` 或列表 `'()`
- **需要键值对**：使用映射 `{}`
- **需要快速在末尾添加元素**：使用向量
- **需要快速在开头添加元素**：使用列表

集合是 Clojure 处理唯一元素集合的理想选择，配合 Clojure 强大的函数式工具，能让数据处理代码变得简洁而富有表达力。

## 练习

1. 给定两个集合 `#{1 2 3 4}` 和 `#{3 4 5 6}`，找出它们的并集、交集和差集。

2. 编写一个函数，接收一个整数集合，返回其中的所有偶数。

3. 创建一个包含 1 到 100 中所有素数的集合。

4. 给定一个字符串集合，找出所有长度大于 5 的字符串。

5. 编写一个函数，检查一个集合是否是另一个集合的子集。

接下来，让我们探索 Clojure 的元数据（Metadata）系统，这是 Clojure 中一个强大但常被忽视的特性。

# 16. 元数据 (Metadata)

元数据是附加到对象上的额外信息，不影响对象的值相等性，但可以在运行时访问。这类似于对象的注释或标签，不会改变对象的核心行为，但可以为工具、编译器或运行时系统提供额外信息。

## 什么是元数据？

在 Clojure 中，元数据是存储在对象上的键值对映射。它不会影响对象的值：

```clojure
(= [1 2 3] ^{:debug true} [1 2 3])  ; => true
```

这里 `^{:debug true}` 是附加到向量的元数据，但两个向量在值上仍然相等。

## 附加元数据

有两种方式附加元数据：

1. **使用 `with-meta` 函数**：
```clojure
(with-meta [:a :b :c] {:source "database" :version 1})
```

2. **使用元数据读取器宏**（`^` 前缀）：
```clojure
^{:debug true} [1 2 3]
^:debug [1 2 3]  ; 短语法，等同于 ^{:debug true}
^{:doc "函数文档"} (defn my-func [])  ; 附加到函数
```

## 读取元数据

使用 `meta` 函数读取对象的元数据：

```clojure
(def data ^{:debug true} [1 2 3])
(meta data)  ; => {:debug true}
```

## 常见元数据用途

### 1. 函数文档
```clojure
(defn ^{:doc "添加两个数字"} add [a b] (+ a b))
(doc add)  ; 显示文档
```

### 2. 类型提示 (Type Hints)
Clojure 是动态类型语言，但有时提供类型提示可以避免反射，提高性能：

```clojure
(defn ^String capitalize [^String s] 
  (.toUpperCase s))
```

### 3. 标记函数特性
```clojure
(defn ^:private helper-function [x] (* x 2))  ; 标记为私有
(defn ^:dynamic *context* [] (get-context))   ; 标记为动态变量
(defn ^:const pi [] 3.14159)                  ; 标记为常量
```

### 4. 测试元数据
正如之前在 FizzBuzz 示例中看到的，可以将测试附加到函数：

```clojure
(defn ^{:test (fn [] (assert (= (inc 1) 2)))} increment [x] (+ x 1))
```

## 元数据的实际应用

元数据让 Clojure 代码更加自描述，同时不牺牲性能和灵活性。让我们看一个实际例子：

```clojure
;; 定义一个带有元数据的函数
(defn ^{:doc "安全获取映射中的值，如果键不存在则返回默认值"
         :added "1.0"
         :static true}
  safe-get
  [m k default]
  (get m k default))

;; 在REPL中查看文档
(doc safe-get)

;; 使用 clojure.repl/source 查看源码和元数据
(source safe-get)
```

元数据也广泛用于 Web 框架和库中，例如 Ring 中间件使用元数据标记处理函数的特性：

```clojure
(defn ^{:ring/middleware true} wrap-logging [handler]
  (fn [request]
    (let [response (handler request)]
      (log request response)
      response)))
```

## 练习

1. 为一个函数添加文档元数据，然后使用 `(doc your-function)` 查看。

2. 为一个集合添加自定义元数据（如 `{:author "your-name"}`），然后使用 `(meta your-collection)` 读取它。

3. 创建一个标记为 `^:private` 的函数，尝试从另一个命名空间调用它，观察结果。

4. 了解 `^:dynamic` 元数据的作用，创建一个动态变量并使用 `binding` 临时重新绑定它。

元数据是 Clojure 强大表达能力的重要组成部分，它让你的代码不仅仅是执行逻辑，还能携带丰富的上下文信息，使工具、库和框架能够更好地理解和使用你的代码。

# 17. 解构 (Destructuring)

解构是 Clojure 中一个强大的特性，允许你在函数参数或 `let` 绑定中，直接从数据结构中提取值并绑定到局部变量。这使得代码更加简洁、清晰，减少了对辅助函数的需求。

## 基本解构语法

解构使用与数据结构相似的模式来提取值：

### 向量解构
```clojure
;; 基本向量解构
(let [[a b c] [1 2 3]]
  (+ a b c))  ; => 6

;; 从向量提取前几个元素，其余放入剩余参数
(let [[x y & rest] [1 2 3 4 5]]
  [x y rest]) ; => [1 2 (3 4 5)]

;; 使用 :as 保留原始结构
(let [[x y :as all] [1 2 3 4 5]]
  [x y all])  ; => [1 2 [1 2 3 4 5]]
```

### 映射解构
```clojure
;; 基本映射解构
(let [{a :a b :b} {:a 1 :b 2 :c 3}]
  (+ a b))  ; => 3

;; 使用 :keys 简化关键字映射解构
(let [{:keys [a b]} {:a 1 :b 2 :c 3}]
  (+ a b))  ; => 3

;; 使用 :strs 处理字符串键
(let [{:strs [a b]} {"a" 1 "b" 2 "c" 3}]
  (+ a b))  ; => 3

;; 使用 :syms 处理符号键
(let [{:syms [a b]} {'a 1 'b 2 'c 3}]
  (+ a b))  ; => 3

;; 提供默认值
(let [{:keys [a b c] :or {c 0}} {:a 1 :b 2}]
  (+ a b c))  ; => 3

;; 使用 :as 保留原始映射
(let [{:keys [a b] :as m} {:a 1 :b 2 :c 3}]
  [a b m])  ; => [1 2 {:a 1 :b 2 :c 3}]
```

## 函数参数解构

解构在函数参数中特别有用：

```clojure
;; 向量解构在函数参数中
(defn first-two-sum [[x y]]
  (+ x y))

(first-two-sum [1 2 3])  ; => 3

;; 映射解构在函数参数中
(defn create-person [{:keys [name age city] :or {city "Unknown"}}]
  (str name " is " age " years old, lives in " city))

(create-person {:name "Alice" :age 30}) 
; => "Alice is 30 years old, lives in Unknown"

(create-person {:name "Bob" :age 25 :city "New York"})
; => "Bob is 25 years old, lives in New York"
```

## 嵌套解构

Clojure 支持嵌套解构，让你可以直接从复杂数据结构中提取值：

```clojure
;; 嵌套向量解构
(let [[[a b] [c d]] [[1 2] [3 4]]]
  (+ a b c d))  ; => 10

;; 嵌套映射解构
(let [{:keys [name address] 
       :or {address {:city "Unknown"}}} 
      {:name "Charlie" 
       :address {:street "123 Main St" 
                 :city "Boston"}}]
  (str name " lives in " (:city address)))
; => "Charlie lives in Boston"

;; 混合解构
(let [{:keys [name {:keys [street city]}] :as person}
      {:name "Diana" 
       :address {:street "456 Elm St" 
                 :city "Chicago"}}]
  (str name " lives at " street ", " city))
; => "Diana lives at 456 Elm St, Chicago"
```

## 实际用例

### 1. 处理 API 响应
```clojure
(defn process-user-response [{:keys [data errors] :as response}]
  (if errors
    (println "Errors:" errors)
    (println "User data:" data)))
```

### 2. 状态管理
在 Reagent (ClojureScript 的 React 封装) 中，通常这样使用解构管理组件状态：
```clojure
(defn user-profile [{:keys [user settings]}]
  [:div 
   [:h1 (:name user)]
   [:p "Theme: " (:theme settings)]])
```

### 3. 数据库查询结果
```clojure
(defn format-search-results [{:keys [results metadata]}]
  (str "Found " (:count metadata) " results: " results))
```

## 练习

1. 编写一个函数，使用解构从向量 `[1 2 3]` 中提取第一个和最后一个元素。

2. 为一个接受映射参数的函数添加解构，提取 `:name`、`:age` 和 `:occupation`，并为 `:occupation` 提供默认值 `"Unknown"`。

3. 使用嵌套解构，从这个数据结构中提取城市和邮编：
```clojure
{:name "Eve", :address {:street "789 Pine St", :city "Seattle", :zip "98101"}}
```

4. 编写一个函数，使用 `:as` 保留原始映射，同时提取 `:id` 和 `:name`。

解构是 Clojure 函数式编程风格的重要部分，它使代码更加声明式，减少中间变量，提高可读性。掌握解构后，你会发现处理复杂数据结构变得异常简洁和直观。

# 18. 可变状态与引用类型 (Mutable State and Reference Types)

虽然 Clojure 强调不可变性，但现实世界的应用经常需要管理可变状态。Clojure 提供了几种引用类型，它们在保持 Clojure 核心原则的同时，安全地管理可变状态。

## 核心原则

在 Clojure 中，状态管理遵循这些原则：
1. **不可变数据**：数据结构本身永远不会改变
2. **状态是标识+值+时间**：标识（identity）随时间变化，持有不同的不可变值
3. **显式状态**：状态变化是显式的，而不是隐含在变量赋值中

## 引用类型

Clojure 提供四种主要的引用类型，每种用于不同场景：

### 1. Atoms - 独立的同步状态

Atoms 提供无协调的同步状态变更。它们适合单个值的独立更新：

```clojure
;; 创建 atom
(def counter (atom 0))

;; 读取值
@counter  ; => 0
(deref counter)  ; 等价于 @counter

;; 更新值
(swap! counter inc)  ; 原子地递增
(swap! counter + 5)  ; 原子地加5
(reset! counter 100)  ; 设置为新值（不考虑旧值）

;; 在函数中使用
(defn next-id []
  (swap! counter inc))

(next-id)  ; => 1
(next-id)  ; => 2
```

Atoms 保证对单个值的更新是原子的、一致的、隔离的，但不提供多个值之间的协调。

### 2. Refs - 协调的同步状态

Refs 用于需要协调多个引用之间变更的场景，使用软件事务内存 (STM)：

```clojure
;; 创建 refs
(def account-a (ref 1000))
(def account-b (ref 500))

;; 读取值
@account-a  ; => 1000

;; 在事务中更新多个 refs
(defn transfer [from to amount]
  (dosync  ; 开始事务
   (alter from - amount)
   (alter to + amount)))

(transfer account-a account-b 200)
@account-a  ; => 800
@account-b  ; => 700
```

Refs 保证多个引用的更新要么全部成功，要么全部失败，保持数据一致性。

### 3. Agents - 异步独立状态

Agents 用于异步、独立的状态变更，不阻塞调用线程：

```clojure
;; 创建 agent
(def logger (agent []))

;; 发送异步更新
(send logger conj "Log entry 1")
(send logger conj "Log entry 2")

;; 等待所有操作完成
(await logger)

;; 读取值
@logger  ; => ["Log entry 1" "Log entry 2"]

;; 错误处理
(agent-error logger)  ; 检查错误
(restart-agent logger [])  ; 重启 agent
```

Agents 适合后台任务，比如日志记录、缓存更新、批处理操作等。

### 4. Vars - 动态作用域

Vars 与 `def` 创建，通常用于全局配置，但也可以动态重新绑定：

```clojure
;; 创建 var
(def ^:dynamic *db-connection* nil)

;; 动态重新绑定
(binding [*db-connection* "test-db"]
  (println "Using database:" *db-connection*))  ; => "Using database: test-db"

;; 外部访问
(println *db-connection*)  ; => nil
```

Vars 应该谨慎使用，主要用于配置和可选特性开关，而不是业务状态。

## 状态管理的最佳实践

1. **最小化状态**：只在必要时使用可变状态
2. **封装状态**：将状态管理封装在少数函数中
3. **使用不可变数据**：状态容器中存储不可变数据
4. **优先选择 Atoms**：对于简单场景，Atoms 通常足够
5. **避免全局状态**：尽可能将状态作为参数传递
6. **使用 STM 保证一致性**：当多个值需要保持一致时，使用 Refs

## 实际例子：购物车

```clojure
;; 使用 atom 管理购物车状态
(def cart (atom {:items [] :total 0}))

(defn add-item [item]
  (swap! cart (fn [current-cart]
                (let [new-items (conj (:items current-cart) item)
                      new-total (+ (:total current-cart) (:price item))]
                  {:items new-items :total new-total}))))

;; 添加商品
(add-item {:name "Book" :price 20})
(add-item {:name "Pen" :price 2})

;; 查看购物车
@cart  ; => {:items [{:name "Book" :price 20} {:name "Pen" :price 2}], :total 22}
```

## 练习

1. 创建一个 atom 作为计数器，编写函数实现递增、递减和重置功能。

2. 使用 refs 实现一个银行账户系统，支持转账操作。

3. 使用 agent 实现一个简单的消息队列，支持添加消息和处理消息。

4. 创建一个动态 var 作为日志级别，使用 binding 临时改变它。

Clojure 的引用类型设计精妙，让开发者可以在需要时安全地管理状态，同时不牺牲函数式编程的核心优势。通过理解每种引用类型的适用场景，你可以构建既强大又可维护的应用程序。

# 19. 惰性序列 (Lazy Sequences)

惰性序列是 Clojure 的核心特性之一，它允许创建理论上无限的序列，只在需要时计算元素。这不仅节省内存，还能简化代码，让开发者以声明式方式处理数据流。

## 什么是惰性序列？

惰性序列是"按需计算"的序列。序列中的元素只在被访问时计算，而不是在序列创建时计算所有元素。

```clojure
;; range 创建惰性序列
(def naturals (range))  ; 无限序列 0, 1, 2, 3...

;; 只计算需要的部分
(take 10 naturals)  ; => (0 1 2 3 4 5 6 7 8 9)

;; 不会阻塞或耗尽内存
(take 5 (drop 10000 naturals))  ; => (10000 10001 10002 10003 10004)
```

## 创建惰性序列

Clojure 提供多种方式创建惰性序列：

### 1. 使用内置函数

```clojure
;; range - 数字序列
(range 5)          ; => (0 1 2 3 4)
(range 1 10 2)     ; => (1 3 5 7 9)

;; repeat - 重复元素
(take 5 (repeat "hello"))  ; => ("hello" "hello" "hello" "hello" "hello")

;; repeatedly - 重复调用函数
(take 5 (repeatedly #(rand-int 10)))  ; => 随机整数序列

;; iterate - 迭代应用函数
(take 10 (iterate inc 1))  ; => (1 2 3 4 5 6 7 8 9 10)
(take 10 (iterate #(* 2 %) 1))  ; => (1 2 4 8 16 32 64 128 256 512)
```

### 2. 使用 `lazy-seq` 宏

`lazy-seq` 允许创建自定义惰性序列：

```clojure
;; 斐波那契数列
(defn fib-seq
  ([] (fib-seq 0 1))
  ([a b] (lazy-seq (cons a (fib-seq b (+ a b))))))

(take 10 (fib-seq))  ; => (0 1 1 2 3 5 8 13 21 34)
```

### 3. 使用 `for` 和 `map` 等函数

这些函数返回惰性序列：

```clojure
;; for 表达式
(take 10 (for [x (range)
               y (range x)]
           [x y]))

;; map 函数
(take 10 (map #(* % %) (range)))  ; 平方数
```

## 惰性序列的特性

### 1. 一次性计算

每个元素只计算一次，结果会被缓存：

```clojure
(def slow-seq (map #(do (Thread/sleep 1000) %) (range 5)))

;; 第一次访问会慢
(time (first slow-seq))  ; 约 1 秒

;; 后续访问缓存的结果
(time (first slow-seq))  ; 几乎瞬间
```

### 2. 无限序列

可以创建理论上无限的序列：

```clojure
;; 无限斐波那契序列
(def fibs (lazy-cat [0 1] (map + fibs (rest fibs))))
(take 15 fibs)  ; => (0 1 1 2 3 5 8 13 21 34 55 89 144 233 377)

;; 无限随机数
(def random-nums (repeatedly #(rand-int 100)))
(take 10 random-nums)
```

### 3. 链式转换

惰性序列可以链式转换，只在需要时计算：

```clojure
(def processed
  (->> (range)
       (filter even?)    ; 只保留偶数
       (map #(* % %))    ; 平方
       (map str)         ; 转成字符串
       (take 10)))       ; 只取前10个

;; 此时还未计算
processed

;; 现在计算
(doall processed)  ; => ("0" "4" "16" "36" "64" "100" "169"...)

;; 使用 doall 强制计算整个序列
```

## 惰性序列的实际应用

### 1. 处理大数据集

```clojure
;; 惰性读取大文件
(defn lazy-lines [filename]
  (let [rdr (clojure.java.io/reader filename)]
    (reify
      clojure.lang.Seqable
      (seq [_] (map clojure.string/trim-newline
                    (line-seq rdr)))
      java.io.Closeable
      (close [_] (.close rdr)))))

;; 只处理需要的行
(with-open [lines (lazy-lines "large-file.txt")]
  (doseq [line (take 100 lines)]  ; 只读取前100行
    (println line)))
```

### 2. 事件流处理

```clojure
;; 模拟事件流
(def event-stream
  (map (fn [id] {:id id :type (rand-nth [:click :scroll :hover]) :time (System/currentTimeMillis)})
       (range)))

;; 处理点击事件
(def click-events
  (filter #(= (:type %) :click) event-stream))

;; 只获取前5个点击事件
(take 5 click-events)
```

### 3. 无限数据结构

```clojure
;; 生成所有素数
(def primes
  (letfn [(prime? [n primes-so-far]
            (not (some #(zero? (rem n %)) (take-while #(<= (* % %) n) primes-so-far))))]
    (lazy-seq
     (cons 2 (filter #(prime? % primes) (iterate #(+ 2 %) 3))))))

(take 20 primes)  ; => (2 3 5 7 11 13 17 19 23 29 31 37 41 43 47 53 59 61 67 71)
```

## 惰性序列的陷阱

### 1. 意外的具体化

```clojure
;; 危险：这会尝试具体化整个无限序列！
(count (range))  ; 不要这样做！

;; 安全：限制范围
(count (take 1000 (range)))  ; => 1000
```

### 2. 资源泄漏

惰性序列可能持有资源引用，需要小心处理：

```clojure
;; 危险：文件描述符可能不会及时关闭
(def lines (line-seq (clojure.java.io/reader "file.txt")))

;; 安全：使用 with-open 确保资源释放
(with-open [rdr (clojure.java.io/reader "file.txt")]
  (doseq [line (take 10 (line-seq rdr))]  ; 只处理前10行
    (println line)))
```

### 3. 性能考虑

过度链式转换可能导致性能问题：

```clojure
;; 多次遍历
(def result
  (->> (range 1000000)
       (filter even?)
       (map inc)
       (filter #(< % 100))
       (map str)))

;; 优化：减少遍历次数
(def optimized-result
  (->> (range 1000000)
       (eduction
         (filter even?)
         (map inc)
         (filter #(< % 100))
         (map str))
       (into [])))
```

## 练习

1. 创建一个生成所有3的倍数的惰性序列。

2. 使用惰性序列实现一个无限循环，每秒打印当前时间（提示：使用 `repeatedly` 和 `Thread/sleep`）。

3. 创建一个函数，接收一个序列，返回只包含连续重复元素的惰性序列。例如，`(1 1 2 2 2 3 3)` 变成 `(1 2 3)`。

4. 使用惰性序列实现一个简单的分页系统，每次获取下一页数据。

惰性序列是 Clojure 强大表达能力的关键部分，它让开发者能够以声明式方式处理潜在无限的数据流。理解惰性和及早求值的平衡，是编写高效 Clojure 代码的重要技能。

# 20. 总结与下一步

恭喜你完成了这份 Clojure 入门指南！通过学习基础知识，你已经掌握了这门强大语言的核心概念。让我们回顾一下重要的知识点，并讨论如何继续你的 Clojure 之旅。

## 核心概念回顾

1. **一切皆表达式**：Clojure 中没有语句，只有表达式，每个表达式都有返回值
2. **数据结构为核心**：列表、向量、映射和集合是 Clojure 的基础构建块
3. **不可变性**：数据结构默认不可变，转换产生新值而不是修改原值
4. **函数是一等公民**：函数可以作为参数传递、作为返回值、存储在数据结构中
5. **宏系统**：Clojure 代码是数据，宏允许你扩展语言本身
6. **管理状态**：Atoms、Refs、Agents 和 Vars 提供安全的可变状态管理
7. **惰性求值**：序列默认是惰性的，允许处理无限数据流

## 最佳实践

- **默认不可变**：尽可能使用不可变数据和纯函数
- **小而专注的函数**：编写单一职责的函数，它们易于组合
- **数据转换管道**：使用 threading 宏创建清晰的数据转换流程
- **避免副作用**：将副作用推向程序边界
- **使用解构**：让代码更简洁，减少中间变量
- **拥抱 REPL**：交互式开发是 Clojure 的核心工作流

## 学习路径

### 1. 基础巩固
- 完成 [4Clojure](https://4clojure.8thlight.com/) 挑战
- 阅读 [Clojure for the Brave and True](https://www.braveclojure.com/)
- 完成 [Clojure Koans](https://github.com/functional-koans/clojure-koans)

### 2. 深入核心概念
- 阅读 [The Joy of Clojure](https://www.manning.com/books/the-joy-of-clojure)
- 学习 [Reducers](https://clojure.org/reference/reducers) 和 [Transducers](https://clojure.org/reference/transducers)
- 了解 [core.async](https://github.com/clojure/core.async) 用于异步编程

### 3. 实战项目
- **Web 开发**：使用 [Ring](https://github.com/ring-clojure/ring) + [Compojure](https://github.com/weavejester/compojure) 或 [Pedestal](https://github.com/pedestal/pedestal)
- **前端开发**：使用 [ClojureScript](https://clojurescript.org/) + [Reagent](https://reagent-project.github.io/) 或 [Om](https://github.com/omcljs/om)
- **数据处理**：使用 [Incanter](http://incanter.org/) 或 [Tablecloth](https://github.com/scicloj/tablecloth)
- **系统工具**：编写自动化脚本或命令行工具

### 4. 社区参与
- 加入 [Clojurians Slack](https://clojurians.slack.com/) 或 [Zulip](https://clojurians.zulipchat.com/)
- 关注 [Clojure Subreddit](https://www.reddit.com/r/Clojure/)
- 参与 [ClojureVerse](https://clojureverse.org/) 论坛
- 参加本地或线上 [Clojure Meetups](https://www.meetup.com/topics/clojure/)

## 推荐资源

### 书籍
- [Clojure for the Brave and True](https://www.braveclojure.com/) (免费在线版)
- [The Joy of Clojure](https://www.manning.com/books/the-joy-of-clojure)
- [Practical Clojure](https://www.apress.com/gp/book/9781430272311)

### 视频
- [Stuart Halloway's Clojure Concurrency](https://www.infoq.com/presentations/clojure-concurrency/)
- [Rich Hickey's talks](https://github.com/matthiasn/talk-transcripts/blob/master/Hickey_Rich) (Clojure 作者)
- [Functional Design in Clojure podcast](https://clojuredesign.club/)

### 工具
- [Calva](https://calva.io/) - VS Code 的 Clojure 插件
- [CIDER](https://docs.cider.mx/) - Emacs 的 Clojure 环境
- [REBL](https://github.com/cognitect-labs/REBL-distro) - 数据浏览器
- [Figwheel](https://figwheel.org/) - ClojureScript 热重载

## 鼓励

学习 Clojure 是一段值得的旅程。虽然函数式编程和不可变数据的概念可能起初有些陌生，但坚持下去会带来丰厚的回报。Clojure 社区以友好和支持著称，不要犹豫寻求帮助。

正如 Rich Hickey (Clojure 作者) 所说：
> "简单不是容易的。简单是通过深思熟虑的设计和对复杂性的拒绝达到的。"

继续练习，构建项目，向社区学习。Clojure 不仅仅是一门语言，它是一种思考软件的方式。祝你在 Clojure 之旅中取得成功！ ♥️

## 附录：速查表

### 基础语法
```clojure
;; 函数调用
(function arg1 arg2)

;; 定义变量
(def x 42)

;; 定义函数
(defn add [a b] (+ a b))

;; 条件判断
(if condition true-expr false-expr)
(when condition expr1 expr2)

;; 循环
(loop [x 0]
  (when (< x 10)
    (println x)
    (recur (inc x))))

;; 解构
(let [[a b] [1 2]] (+ a b))
(let [{:keys [name age]} {:name "Alice" :age 30}]
  (str name " is " age))
```

### 集合操作
```clojure
;; 向量
[1 2 3]
(conj [1 2] 3)  ; => [1 2 3]
(assoc [1 2 3] 1 4)  ; => [1 4 3]
(nth [1 2 3] 1)  ; => 2

;; 列表
'(1 2 3)
(conj '(2 3) 1)  ; => (1 2 3)

;; 映射
{:a 1 :b 2}
(assoc {:a 1} :b 2)  ; => {:a 1 :b 2}
(dissoc {:a 1 :b 2} :a)  ; => {:b 2}
(get {:a 1} :a)  ; => 1
(:a {:a 1})  ; => 1

;; 集合
#{1 2 3}
(conj #{1 2} 3)  ; => #{1 2 3}
(disj #{1 2 3} 2)  ; => #{1 3}
(#{1 2 3} 2)  ; => 2
```

### 函数式工具
```clojure
(map inc [1 2 3])  ; => (2 3 4)
(filter even? [1 2 3 4])  ; => (2 4)
(reduce + [1 2 3 4])  ; => 10
(take 3 (range))  ; => (0 1 2)
(drop 2 [1 2 3 4])  ; => (3 4)
```

### 线程宏
```clojure
(-> {:a 1 :b 2}
    (assoc :c 3)
    (update :a inc))  ; => {:a 2 :b 2 :c 3}

(->> [1 2 3 4]
     (map inc)
     (filter even?))  ; => (2 4)
```

现在，打开你的 REPL，开始 Clojure 之旅吧！