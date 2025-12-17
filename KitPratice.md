# what 's so great about clojure
- Poweful Macro system
- functional programing
    - mutable state
        - any piece of data  which is allowed to change is mutable state
            - if a variable can be reassigned new values that variable is mutable state
            -  a variable can be reassigned new values that variable is mutable state
            - an object's field can be reassigned new values
            - the elements of an array can be reassigned that array is mutable state
    - In functional programming we avoid using mutable state as much as possible
        -  if a function doesn't use any mutable state , it is referentially transparent
        -  by making modified copies in this manner we can avoid ever mutating any objects 
            -  copying things every time we want to modify them might be unreasonably expensive
                - to solve this problem clojure has persistent collections
                    - a persistent collection is a collection type where the instances are immutable, but implemented in such a way that allows for efficiently creating modified copies
                    - The new and old list share in memory the elementswhich they have in common.
                    ```clojure
                    [1,2,3,4,5]
                    -> [1,2,3,4,5,"charles]
                    ```
    - Clojure simply lets you create impure functions as you see fit to handle things like I/O that inherently mutable 
- Clojure provide  reference types, allow us to synchronize threads without using locks
    - threads of execution can easily mess each other up when they share im mutable data,because one thread might modify the data in a way that the other threads don't anticipate
    -  a closure reference is like a mutable collection that stores just one element and the operation   that access and replace the element of a reference  ensur some kind of coordination across threads

- Intimately connected to java
    - closure compile to java byte code so as to run on the jvm
    - every closure data type is defined in terms of java classes
    - code in either language can invoke code of the other
        - closure standard library simply defers to the java libraries for some core functionality,  for example reading and writing files in closure is done by simply using the existing java file

# prepare env

```
# 安装创建 Clojure 项目/库的模板的工具
clojure -Ttools install com.github.seancorfield/clj-new '{:git/tag "v1.2.404"}' :as clj-new

# create app using template kit-clj
clojure -Tclj-new create :template io.github.kit-clj :name kit/guestbook
cd guestbook
```