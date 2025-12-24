(ns get-started.hello-repl-cn)

"欢迎来到入门 REPL！💜"

;; == 需要一些 VS Code 基础知识 ==
;; 本教程假设你对 VS Code 有一定的了解。
;; 如果你是这个编辑器的新手，请查看此页面：https://code.visualstudio.com/docs

;; == 本教程中使用的键盘快捷键符号 ==
;; 我们使用一种特殊的符号来表示快捷键：
;; `+` 意味着同时按下这些键。
;; ` ` (空格) 意味着按顺序按下按键序列。
;; `Ctrl+Alt/Option+C Enter` 意味着：
;; 同时按下 Ctrl, Alt (Mac 上是 Option), 和 C，
;; 然后松开这些键，再按下 Enter。
;; (Alt 键在某些机器上称为 Option 或 Opt)
;; 当某个按钮在不同平台上不同时，我们会用斜杠分隔。

;; == 评估 (Evaluation) 结果 ==
;; 评估代码时，结果的第一行会直接显示在代码行内 (Inline)。
;; 完整的结果会显示在两个地方：
;; 1. Calva Output “终端”窗口，你可以在 VS Code 的终端视图中找到。
;; 2. Calva Inspector (检查器)，这是一个侧边栏视图，
;;    允许你更详细地检查结果。

;; 本项目配置为自动启动 REPL 并将其连接到编辑器 (Jack-in)。
;; 如果没有发生这种情况，你可能使用的是旧版本的 Calva。
;; 请更新到最新版本（或至少 v2.0.460）。
;;
;; 一旦 REPL 连接成功（状态栏底部的 REPL 按钮变亮/变色），
;; 你应该在 REPL 中加载此文件。使用命令：
;;   Calva: Load/Evaluate Current File and its Requires/Dependencies
;;   (快捷键：Ctrl+Alt+C Enter)
;;
;; 然后你可以评估上面那行写着 "欢迎来到入门 REPL！💜" 的代码。
;; 将光标放在那一行，然后按：
;; `Alt/Option+Enter`

;; 你做了吗？太棒了！
;; 看到行尾的 `=> "欢迎 ...` 了吗？
;; 那就是你刚刚执行的评估的结果。你刚刚使用了 Clojure REPL！
;; 🎉 恭喜！ 🎂
;; 你可以通过按 `Esc` 键来清除行内显示的结果。

(comment
  ;; 你可以用同样的方法评估下面的字符串

  "Hello World!"

  ;; 你现在处于一个 'Rich Comment Form' (富注释形式) 中，
  ;; 这是我们 Clojure 程序员最常开发新代码的地方。
  ;; https://calva.io/rich-comments/
  ;; 它通常缩写为 RCF。

  ;; 评估下面的形式（你可以把光标放在这两行的任意位置）：

  (repeat 7
          "I am using the REPL! 💪")

  ;; 只有 `=> ("I am using the REPL! 💪"` 会显示在行内。
  ;; 你可以在三个地方看到完整的结果：
  ;; 1. Calva Output “终端”
  ;; 2. Calva Inspector。它为每个结果提供了一个 _Inspect_ 按钮，
  ;;    让你展开结果并以树状结构探索它。
  ;; 3. 将鼠标悬停在行内结果显示上时，底部会显示。

  ;; 让我们真正进入 REPL 的状态。😂
  ;; 将光标放在下面五行代码的任意位置：
  ;; 按 `Alt+Enter`，然后按 `Ctrl/Cmd+K Ctrl/Cmd+I` (显示悬停信息)。



  (map (fn [s]
         (if (< (count s) 5)
           (str "Give me " s "! ~•~ " (last s) "!")
           s))
       ["an R" "an E" "a  P" "an L" "What do you get?" "REPL!"])

  ;; 用 `Esc` 清除行内显示。当你编辑文件时，
  ;; 行内结果也会自动清除。

  ;; 这引出了一个非常重要的事情：
  ;; 默认情况下，Calva 是“括号的守护者”(Guardian of the Parens)。
  ;; 这意味着 Backspace (退格) 和 Delete 键不会删除
  ;; 配对的括号。请试着删除上面表达式中的一个括号。
  ;; 看到了吗？删不掉。

  ;; 如何强制删除配对的括号：
  ;;   按 `alt/option+backspace` 或 `alt/option+delete`

  :rcf) ; <- 这是一个方便的方法，可以防止在格式化代码时，
        ;    富注释形式的右括号被折叠到上面去。




;;
;; = 本指南如何工作 =
;;

;; 这里有三个文件供你探索。对于 Calva 新手来说：
;; 1. hello_repl.clj (本文件)
;; 2. welcome_to_clojure.clj (Clojure 入门指南)
;; 3. hello_paredit.clj (Calva 结构化编辑简介)

;; 如果你不理解本文件和 paredit 文件中的代码，请不要担心。
;; 它们很短，旨在让你跟着操作，而不必理解代码的实际含义。
;; 一旦你深入学习 Clojure 指南，事情就会变得清晰。

;; 关于命令和快捷键：
;; 请阅读 https://calva.io/finding-commands/
;; (非常短。)
;; 当我们通过名称引用命令时，如果你不知道快捷键，
;; 请使用 VS Code 命令面板 (Ctrl/Cmd+Shift+P) 搜索它们。
;; 所有 Calva 命令都以 “Calva” 为前缀。

;; == 评估定义 (Evaluating definitions) ==
;; Alt+Enter 是 Calva 评估当前 “顶层 (top level)” 形式的默认快捷键。
;; 顶层意味着最外层的形式“容器”，通常直接位于文件中。
;; 这个函数定义就在顶层。请评估它！

(defn greet
  "I'll greet you"
  [s]
  (str "Hello " s "!"))

;; 在 `(comment ...)` 内部的形式也被认为是顶层形式。
;; 这使得实验代码变得非常容易。

(comment
  (greet "World")
  :rcf)

;; 任何打印到 stdout (标准输出) 的内容都不会显示在行内。
;; (而是显示在 Output 窗口)

(comment
  (println (greet "World"))
  :rcf)

;; 你应该会看到评估结果 nil 显示在行内，
;; 而 “Hello World!” 以及结果会被打印到输出终端中。

;; 也许你会好奇什么是 “form” (形式)？粗略定义的话，
;; 它大概等同于 S-expression (S表达式)：
;; https://en.wikipedia.org/wiki/S-expression
;; 也就是说，要么是一个“单词”，要么是被某种括号包围的东西，
;; 圆括号 ()，方括号 []，花括号 {}，或引号 ""。
;; 下面这整个东西就是一个 form：

(str 23 (apply + [2 3]) (:foo {:foo "foo"}))


;; 同样，`str`, `23`, "foo", `(apply + [2 3])`,
;; `{:foo "foo"}`, `+`, `[2 3]`, `apply`, 以及
;; `(:foo {:foo "foo"})` 也都是 form。

;; Calva 有一个 “当前 form” (current form) 的概念，
;; 让你评估那些不在顶层的 form。“当前 form” 取决于光标的位置。
;; Calva 有两个命令让你可以轻松实验哪个 form 被视为当前 form：
;; * Calva: Select Current Form (选择当前形式)
;; * Calva: Expand Selection (扩展选择)


;; == 评估当前形式 (Evaluating the Current Form) ==
;; Ctrl+Enter 评估 “当前” 形式。
;; 试着把光标放在这段代码片段的不同位置来测试它：

(comment

  (str 23 (apply + [2 3]) (:foo {:foo "foo"}))

  ;; 你可能会发现 Calva 把字符串里的单词也视为 form。
  ;; 如果 `foo` 导致评估错误，不要惊慌。它没有被定义，
  ;; 因为它本就不该被定义。当然，你可以定义它，
  ;; 纯粹为了好玩和学习：对这些定义进行顶层评估。
  ;; (使用 `Alt/Option+Enter`)

  (def foo
    [1 2 "three four"])
  (def three 3)
  (def four "four")

  :rcf)

;; 然后在上面的字符串内部评估当前 form。
;; 无论你要求 Calva 发送什么给 REPL，Calva 都会发送。


;; == 富注释支持 (Rich Comments Support) ==
;; 重复一个重要的概念：在 `(comment ...)` 内部的 form
;; 也被 Calva 视为顶层形式。
;; 在下面的不同位置按 Alt/Option+Enter 来感受一下。

(comment
  "I ♥️ Clojure"

  (greet "World")

  foo

  (range 10)

  ;; https://calva.io/rich-comments/
  :rcf)


;; 同时也试试这些命令：
;; *Show Hover* (显示悬停)
;; *Show Definition Preview Hover* (显示定义预览悬停)
;; *Go to Definition* (转到定义)

(comment
  (println (greet "side effect"))
  (+ (* 2 2)
     2)

  ;; 这里也一样，如果你安装了 Java 源码
  (Math/abs -1)
  :rcf)

(import 'java.util.Random)
(def rnd (Random.))
(.nextInt rnd 100)

;; == 你控制评估的内容 ==
;; 请注意，Calva 永远不会评估你的代码，
;; 除非你明确要求。所以，除了这个文件，
;; 你必须自己加载你打开的文件。养成这个习惯，
;; 因为如果你的文件没有被加载，有时候东西会跑不通，
;; 并且会以奇怪的方式失败。

;; 用这个文件试试：`Ctrl+Alt+C Enter`。
;; 加载文件的结果是该文件中最后一个顶层 form 的结果。

;; == 编辑代码 ==
;; 关于在 Calva 中编辑 Clojure 的提示：
;; 如果你编辑并实验这些例子，你会注意到 Calva 会自动缩进你的代码。
;; 你可以使用 `Tab` 键随意重新缩进和格式化代码。
;; 它会格式化当前包围的 form。
;; 从 `; 1` 开始，在这段代码的标号处试试看：

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

;; === Paredit `strict` 模式已开启 ===
;; Calva 支持结构化编辑（基于 form 而不是基于行的编辑），
;; 使用一种叫 Paredit 的系统。默认情况下，Paredit 试图保护
;; 你不小心删除括号而破坏 form 的结构平衡。
;; 要覆盖这种保护，请使用 `Alt+Backspace` 或 `Alt+delete`。

(comment
  (defn strict-greet
    "尝试使用 Backspace 或 Delete 删除括号和引号。
     然后再按住 Alt 键尝试同样的操作。"
    [name]
    (str "Strictly yours, " name "!"))

  (strict-greet "dear Paredit fan")
  :rcf)

;; (如果需要，使用 *Undo* 撤销操作。)
;; 查看 `hello_paredit.clj` 了解更多。以及：
;; https://calva.io/paredit

;;;;;;;;;;;;;;;;;;; 检查点 (CHECKPOINT) ;;;;;;;;;;;;;;;;;;;

;; 你现在已经足够了解 Calva，可以去 `welcome_to_clojure.clj`
;; 中实验代码了。本文件后面还有一些很棒的 Calva 功能，
;; 所以在你学了一些 Clojure 知识后，一定要回到这里。
;; 比如，什么是 Threaded Expression (宏/管道表达式)。

;; == 在 Threaded Expressions 中评估 ==
;; Ctrl+Alt+Enter 将评估当前包围的 form 直到光标处。
;; 在 Threaded expressions (如 -> 或 ->>) 中非常有用。
;; 假设你想计算下面数据的平均评分：

(comment
  ;; 首先你需要顶层评估这个
  (def colt-express
    {:name "Colt Express"
     :categories ["Family"
                  "Strategy"]
     :play-time 40
     :ratings {:pez 5.0
               :kat 5.0
               :wiw 5.0
               :vig 3.0
               :rex 5.0
               :lun 4.0}})

  ;; 还有这个
  (defn average [coll]
    (/ (apply + coll) (count coll)))

  ;; 如果你喜欢，这个也评估一下
  (->> colt-express
       :ratings
       vals
       average)

  ;; 要查看 thread 中每一步的结果，
  ;; 你可以在每个 form 后面按 Ctrl+Alt+Enter。
  ;; 把光标放在 `(->> colt-express` 后面试试。
  ;; 然后放在 `:ratings` 后面，再放在 `vals` 后面。
  :rcf)

;; == 评估从顶层 Form 开始到光标处的内容 ==
;; Shift+Alt+Enter 将评估从当前顶层 form 开始，
;; 直到光标处的所有代码，并自动闭合所有打开的括号。
;; 重复上面的例子来尝试一下，但是这次把光标放在，
;; 比如说，`:wiw 5.0` 的正后方，然后使用此命令。

(comment
  ;; 当你想评估一大块代码中的某一部分时，这个命令很有用。
  ;; 例如，你可能把一些代码包在 `(do ...)` 中，然后使用
  ;; *Evaluate From Start of Top Level Form to Cursor*
  ;; 命令在不同位置检查代码。
  ;; 在下面的编号行注释处试试。

  ; 但首先顶层评估这个
  (defn average [coll]
    (/ (apply + coll) (count coll)))

  (do
    (def bar-express
      {:name "Bar Express"
       :categories ["Family"
                    "Strategy"]
       :play-time 40
       :ratings {:pez 5.0
                 :kat 5.0
                 :wiw 5.0   ; 1, 然后 eval `bar-express`
                 :vig 3.0
                 :rex 5.0
                 :lun 4.0}})

    (let [foo-express (-> bar-express
                          (assoc :name "Foo Express")
                          (assoc-in [:ratings :lyr] 5.0)
                          (update-in [:ratings :vig] inc))]
      (->> foo-express   ; 2
           :ratings      ; 3
           vals          ; 4
           (average)     ; 5 (如果这里报错，你需要
                         ;   顶层评估 `average` 函数的定义。)
           )))
  :rcf)

;; == Calva 调试器 (Debugger) ==
;; https://calva.io/debugger/
;; 使用它的最简单方法是对函数进行“调试插桩”(Instrument)。
;; 方法是将光标放在函数内部，然后使用命令：
;; *Instrument Current Top Level Form for Debugging*
;; 然后你调用该函数。这将导致调试器在插桩函数
;; 的第一个可断点处停止。

(comment
  ;; 让我们先试试不插桩的情况。这个函数有一个 bug。
  ;; 先用通常的方式 (`Alt+Enter`) 评估它，然后调用它。

  (defn bar
    [n]
    (cond (> n 40) (+ n 20)
          (> n 20) (- (first n) 20)
          :else 0))

  (bar 2)  ; 正常工作
  (bar 24) ; 抛出异常，发生了什么？

  ;; 这是一个奇怪的错误信息（也许你会这么觉得，
  ;; 取决于你对 Clojure 的熟悉程度）。
  ;; 现在按照上面的描述对函数进行插桩 (Instrument)。
  ;; Calva 会标记已插桩用于调试的代码。
  ;; 现在评估那个有问题的函数调用 `(bar 24)`。
  ;; 调试器将启动并等待你逐步执行函数。
  ;;
  ;; 要取消函数的插桩，只需用正常方式重新评估它
  ;; (顶层评估) 即可。
  ;; 调试器文档在这里：https://calva.io/debugger/

  ;; 注意：如果你是 Clojure 新手，发现 Calva 有调试器
  ;; 可能会让你感到熟悉。
  ;; 但是，请尝试先探索“交互式编程”(Interactive Programming)，
  ;; 也就是先使用 REPL。这才是 Clojure 之道。
  ;; 这一节只是为了让你知道调试器的存在，
  ;; 以备在极少数真正需要它的场合使用。
  :rcf)

;; == 停止无限循环 ==
;; 由于评估 Clojure 表达式如此简单有趣，
;; 有时你会不小心评估一些永远不会结束的东西，
;; 或者需要很长时间才能完成的东西。
;; 对此，Calva 有一个命令叫：
;; *Interrupt Running Evaluations* (中断运行中的评估)。
;; 如果你顶层评估下面这个，你就需要它：

(comment
  (def tmp1 (dorun (range)))
  :rcf)

;; 完成了吗？太棒了。请考虑使用交互式指南
;; 更多地熟悉 Paredit。如果你是 Clojure 新手，
;; 请继续阅读 `welcome_to_clojure.clj`，
;; 这是一个 Clojure 语言的交互式指南。

;; 有 Clojure 项目想要连接 Calva 吗？请看：https://calva.io/connect/

;; 在 https://calva.io 了解更多关于 Calva 的信息

;; 这个字符串是文件中的最后一个表达式
"hello_repl.clj is loaded, and ready with some things for you to try."
;; 当你加载文件时，这是你在 Output 窗口中会看到打印出来的内容。

;; 本指南下载自：
;; https://github.com/BetterThanTomorrow/dram
;; 请考虑贡献代码。