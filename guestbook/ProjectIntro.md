# 你的第一个应用程序

## 目录

- 留言簿应用程序  
- 快速入门：GitHub Codespaces  
- 安装 JDK  
- 安装构建工具  
- 创建新应用  
- Kit 应用程序结构解析  
  - 源码目录（The Source Directory）  
  - 环境目录（The Env Directory）  
  - 测试目录（The Test Directory）  
  - 资源目录（The Resources Directory）  
- 启动服务器  
- 系统（System）  
- Kit 模块（Kit Modules）  
- 访问数据库  
- 在路由组件中暴露数据库查询  
- 为留言簿创建控制器  
- 创建页面并处理表单输入  
- 添加测试  
- 打包应用程序  

---

## 留言簿应用程序

本教程将指导你使用 Kit 构建一个简单的留言簿应用。该留言簿允许用户留言，并查看其他人留下的消息列表。本应用将演示 HTML 模板、数据库访问和项目架构等基础知识。

如果你尚未选定偏好的 Clojure 编辑器，建议使用 **Calva** 跟随本教程操作。

---

## 快速入门：GitHub Codespaces

你可以通过以下仓库启动一个基于浏览器的开发环境，进行本教程的学习：  
https://github.com/kit-clj/playground/

---

## 安装 JDK

Clojure 运行于 JVM 之上，因此需先安装 JDK。若系统尚未安装 JDK，推荐安装 [OpenJDK](https://openjdk.org/)。注意：Kit 默认设置要求 JDK 11 或更高版本。你也可以根据操作系统说明安装相应软件包。

---

## 安装构建工具

Kit 支持使用 **Clojure Deps + CLI** 构建与运行项目。注意：Kit 要求 `tools.build` 版本为 `1.10.3.933` 或更高。

### 安装 Clojure CLI

根据你所用的操作系统，按下列步骤安装：

#### macOS

```bash
brew install clojure/tools/clojure
```

#### Linux

```bash
curl -L -O https://github.com/clojure/brew-install/releases/latest/download/posix-install.sh
chmod +x posix-install.sh
sudo ./posix-install.sh
```

对于 macOS 和 Linux，还需安装 `clj-new`：

```bash
clojure -Ttools install com.github.seancorfield/clj-new '{:git/tag "v1.2.404"}' :as clj-new
```

如需了解自定义选项（如更改安装路径），请参阅 [官方文档](https://github.com/clojure/brew-install)。

---

## 创建新应用

安装好 Clojure CLI 后，可在终端中运行以下命令初始化应用：

```bash
clojure -Tclj-new create :template io.github.kit-clj :name kit/guestbook
cd guestbook
```

上述命令将基于 `kit-clj` 模板创建一个名为 `kit/guestbook` 的新项目。

---

## Kit 应用程序结构解析

新建应用具有如下结构：

```
├── Dockerfile
├── README.md
├── build.clj
├── deps.edn
├── env
│   ├── dev
│   │   ├── clj
│   │   │   ├── user.clj
│   │   │   └── kit
│   │   │       └── guestbook
│   │   │           ├── dev_middleware.clj
│   │   │           └── env.clj
│   │   └── resources
│   │       └── logback.xml
│   └── prod
│       ├── clj
│       │   └── kit
│       │       └── guestbook
│       │           └── env.clj
│       └── resources
│           └── logback.xml
├── kit.edn
├── kit.git-config.edn
├── project.clj
├── resources
│   └── system.edn
├── src
│   └── clj
│       └── kit
│           └── guestbook
│               ├── config.clj
│               ├── core.clj
│               └── web
│                   ├── controllers
│                   │   └── health.clj
│                   ├── handler.clj
│                   ├── middleware
│                   │   ├── core.clj
│                   │   ├── exception.clj
│                   │   └── formats.clj
│                   └── routes
│                       ├── api.clj
│                       └── utils.clj
└── test
    └── clj
        └── kit
            └── guestbook
                └── test_utils.clj
```

我们来看看根目录下各文件的作用：

- `deps.edn`：Clojure Deps 使用的项目配置与依赖管理文件  
- `build.clj`：Clojure CLI tools 使用的构建脚本  
- `Dockerfile`：用于构建 Docker 镜像，支持容器部署  
- `README.md`：惯例存放项目文档  
- `resources/system.edn`：系统资源配置文件  
- `.gitignore`：Git 忽略文件列表（如构建产物等）

---

### 源码目录（The Source Directory）

所有业务代码位于 `src/clj` 目录下。由于项目名为 `kit/guestbook`，因此根命名空间即为 `kit.guestbook`。我们依次说明默认生成的命名空间：

#### `guestbook`

- `config.clj`：读取 `system.edn`，生成 Integrant 配置映射  
- `core.clj`：应用入口点，包含启动/停止服务器的逻辑

#### `guestbook.web`

该命名空间定义了应用中与服务器通信的边界逻辑：接收 HTTP 请求，返回响应。

- `handler.clj`：定义路由与请求处理的入口点

#### `guestbook.web.controllers`

控制器（controller）存放于此。默认已生成一个健康检查控制器；添加新控制器时，请在此目录下新建命名空间。

- `health.clj`：默认健康检查控制器，返回服务器基础统计信息

#### `guestbook.web.middleware`

中间件（middleware）命名空间：实现横切关注点功能（如会话管理、数据转换等）。这些函数可包裹一组路由，提供统一功能。

- `core.clj`：默认中间件与环境特化中间件的聚合  
- `exception.clj`：控制器内异常分类与对应 HTTP 响应的返回逻辑  
- `formats.clj`：负责请求数据 → Clojure 数据结构、响应数据 → 字符串的转换

#### `guestbook.web.routes`

HTTP 路由定义于此。默认已生成 `/api` 路由；添加新路由时，请在此建立新命名空间。

- `api.clj`：路由定义（默认为 `/api`），集成 Swagger UI  
- `utils.clj`：从请求中提取数据的通用辅助函数

---

### 环境目录（The Env Directory）

环境相关的代码与资源配置位于 `env/dev`、`env/test` 与 `env/prod` 下。开发与测试使用 `dev` 配置，测试使用 `test` 配置，构建生产包则使用 `prod` 配置。

#### 开发目录（Dev Directory）

开发时专用的代码位于 `env/dev/clj`：

- `user.clj`：REPL 开发期间运行任意代码的工具命名空间；你可从此处启动/停止服务器  
- `guestbook/env.clj`：开发环境默认配置  
- `guestbook/dev_middleware.clj`：仅开发时启用的中间件（生产环境不应包含）

开发用资源（如配置文件）置于 `env/dev/resources`，默认含：

- `logback.xml`：开发日志配置

测试环境同理，配置位于 `test/resources`：

- `logback.xml`：测试日志配置

#### 生产目录（Prod Directory）

与 `dev` 目录对应，含生产构建时使用的代码与资源：

- `guestbook/env.clj`：生产环境配置  
- `logback.xml`：生产日志配置

---

### 测试目录（The Test Directory）

应用测试代码存放于此。已内置部分测试工具。

---

### 资源目录（The Resources Directory）

所有随应用打包的非代码资源均存放于此。`resources/public` 目录下的所有文件，将由服务器直接对外提供服务。

---

## 启动服务器

REPL 是 Clojure 开发中的最佳伙伴。让我们通过以下命令启动本地开发 REPL：

```bash
clj -M:dev
```

你也可依据编辑器选择不同 REPL 启动方式：

- Emacs + CIDER：`clj -M:dev:cider`  
- VS Code + Calva：`clj -M:dev:nrepl`（仅启动 nREPL，不含 CIDER 中间件）

详见 **留言簿示例 README** 中关于编辑器连接 REPL 的说明。

进入 REPL 后，即可调用 `env/dev/user.clj` 提供的函数启动系统：

```clojure
(go)     ; 启动系统
(halt)   ; 停止系统
(reset)  ; 代码修改后重载系统
```

服务器启动后，访问 `http://localhost:3000/api/health` 以确认其正常运行。

---

## 系统（System）

系统级资源（如 HTTP 服务端口、数据库连接）定义在 `resources/system.edn` 中。例如，以下键定义了 HTTP 服务配置——主机、端口与请求处理器：

```edn
:server/http
 {:port #long #or [#env PORT 3000]
  :host #or [#env HTTP_HOST "0.0.0.0"]
  :handler #ig/ref :handler/ring}
```

了解完默认项目结构后，我们来学习如何通过模块扩展功能。

---

## Kit 模块（Kit Modules）

Kit 模块是一组模板，用于向 Kit 项目注入代码与资源。模块定义在 `kit.edn` 文件中。默认配置指向官方模块仓库：

```edn
:modules {:root         "modules"
          :repositories [{:url  "git@github.com:kit-clj/modules.git"
                          :tag  "master"
                          :name "kit-modules"}]}
```

由于本应用需渲染 HTML 内容，我们添加官方 `HTML` 模块。在 REPL 中执行：

```clojure
;; 下载官方 Kit 模块仓库
(kit/sync-modules)

;; 列出可用模块
(kit/list-modules)
;; =>
;; :kit/html - 添加 Selmer HTML 模板支持  
;; :kit/sql  - 添加 SQL 支持。可选配置 [ :postgres :sqlite ]，默认 :sqlite  
;; :kit/cljs - 添加 shadow-cljs 支持的 ClojureScript  
;; nil

;; 安装 HTML 模块以支持渲染页面
(kit/install-module :kit/html)
;; =>
;; updating file: resources/system.edn
;; injecting
;; path: [:reitit.routes/pages]
;; value: {:base-path "", :env #ig/ref :system/env}
;; updating file: deps.edn
;; injecting
;; path: [:deps selmer/selmer]
;; value: #:mvn{:version "1.12.44"}
;; injecting
;; path: [:deps ring/ring-defaults]
;; value: #:mvn{:version "0.3.3"}
;; injecting
;; path: [:deps luminus/ring-ttl-session]
;; value: #:mvn{:version "0.3.3"}
;; updating file: src/clj/kit/guestbook/core.clj
;; applying
;; action: :append-requires
;; value: [[kit.guestbook.web.routes.pages]]
;; html installed successfully!
;; restart required!
;; nil
```

输出提示需重启 REPL —— 请照做。重启后，再次执行 `(go)` 并访问 `localhost:3000`，即可验证模块是否安装成功。

### HTML 模板

该模块在 `resources/html` 下生成了以下文件：

- `home.html`：主页模板  
- `error.html`：错误页模板  

此目录专用于存放应用页面的 HTML 模板。

模块同时生成命名空间 `kit.guestbook.web.pages.layout`，帮助你使用 **Selmer 模板引擎** 渲染页面。

### 路由

模块还自动生成了 `kit.guestbook.web.routes.pages` 中的页面路由。详见 [路由文档](https://cljdoc.org/d/metosin/reitit/)。

---

## 添加数据库

与安装 HTML 模块类似，我们可添加 SQLite 支持的 `:kit/sql` 模块（默认即为 SQLite；若需显式指定：`(kit/install-module :kit/sql {:feature-flag :sqlite})`）：

```clojure
(kit/install-module :kit/sql)
;; updating file: resources/system.edn
;; injecting
;;  path: [:db.sql/connection]
;;  value: #profile {:dev {:jdbc-url "jdbc:sqlite:_dev.db"}, :test {:jdbc-url "jdbc:sqlite:_test.db"}, :prod {:jdbc-url #env JDBC_URL}}
;; injecting
;;  path: [:db.sql/query-fn]
;;  value: {:conn #ig/ref :db.sql/connection, :options {}, :filename "sql/queries.sql"}
;; injecting
;;  path: [:db.sql/migrations]
;;  value: {:store :database, :db {:datasource #ig/ref :db.sql/connection}, :migrate-on-init? true}
;; updating file: deps.edn
;; injecting
;;  path: [:deps io.github.kit-clj/kit-sql]
;;  value: #:mvn{:version "0.1.0"}
;; injecting
;;  path: [:deps org.xerial/sqlite-jdbc]
;;  value: #:mvn{:version "3.34.0"}
;; updating file: src/clj/kit/guestbook/core.clj
;; applying
;;  action: :append-requires
;;  value: [[kit.edge.db.sql]]
;; sql installed successfully!
;; restart required!
```

再次重启后，创建第一条数据库迁移：

```clojure
(migratus.core/create
  (:db.sql/migrations state/system)
  "add-guestbook-table")
```

此命令将在 `resources/migrations` 下生成两个文件，形如：

```
20211109173842-add-guestbook-table.up.sql
20211109173842-add-guestbook-table.down.sql
```

Kit 使用 **Migratus** 管理迁移，迁移由 `up` 与 `down` 的 SQL 文件按创建时间顺序执行。

打开 `<date>-add-guestbook-table.up.sql`，添加建表语句：

```sql
CREATE TABLE guestbook (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  name VARCHAR(30),
  message VARCHAR(200),
  timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

该 `guestbook` 表将存储留言者姓名、留言内容及时间戳。

相应地，替换 `<date>-add-guestbook-table.down.sql` 的内容：

```sql
DROP TABLE IF EXISTS guestbook;
```

迁移将在启动时自动执行，依据 `system.edn` 中的配置：

```edn
:db.sql/migrations {:store :database,
                     :db {:datasource #ig/ref :db.sql/connection},
                     :migrate-on-init? true}
```

---

## 访问数据库

SQL 查询定义位于 `resources/sql/queries.sql`：

- `queries.sql`：定义 SQL 查询及其对应函数名

打开该模板文件，其初始内容为：

```sql
-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)

-- :name update-user! :! :n
-- :doc update an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT * FROM users
WHERE id = :id
```

每条查询以 `-- :name <函数名>` 开头，下一行 `-- :doc` 为文档字符串，随后是标准 SQL 语句。参数使用 `:param` 形式。

我们替换为留言簿专属查询：

```sql
-- :name save-message! :! :n
-- :doc creates a new message
INSERT INTO guestbook
(name, message)
VALUES (:name, :message)

-- :name get-messages :? :*
-- :doc selects all available messages
SELECT * FROM guestbook
```

模型配置完成后，重载应用并在 REPL 中测试：

```clojure
(reset)

(def query-fn (:db.sql/query-fn state/system))

(query-fn :save-message! {:name "m1" :message "hello world"})
;; => 1

(query-fn :get-messages {})
;; => [{:id 1, :name "m1", :message "hello world", :timestamp 1636480432353}]
```

新定义的 `query-fn` 函数可用于执行 `queries.sql` 中声明的 SQL 函数，其实现依赖于 `kit-sql` 提供的 `:db.sql/query-fn` 组件。

该函数接受两个参数：SQL 函数名、参数映射。

更多组件细节见：[访问组件](#accessing-components)。

---

## 在路由组件中暴露数据库查询

为使页面路由能使用查询，需修改 `resources/system.edn`，为页面路由组件添加 `:query-fn` 键：

```edn
:reitit.routes/pages
{:base-path "",
 :query-fn #ig/ref :db.sql/query-fn
 :env      #ig/ref :system/env}
```

该键引用 `:db.sql/query-fn` 组件，其负责根据 `resources/sql/queries.sql` 生成查询函数：

```edn
:db.sql/query-fn
{:conn #ig/ref :db.sql/connection,
 :options {},
 :filename "sql/queries.sql"}
```

与 REPL 示例不同之处在于：
- 显式传入数据库连接  
- 仅开放指定 SQL 文件中的查询函数  

详见：[访问组件](#accessing-components)

---

## 为留言簿创建控制器

我们将创建一个新控制器，负责保存新留言至数据库。  
在 `src/clj/kit/guestbook/web/controllers/` 下新建文件 `guestbook.clj`，内容如下：

```clojure
(ns kit.guestbook.web.controllers.guestbook
  (:require
   [clojure.tools.logging :as log]   
   [ring.util.http-response :as http-response]))

(defn save-message!
  [{:keys [query-fn]} {{:strs [name message]} :form-params :as request}]
  (log/debug "saving message" name message)
  (try
    (if (or (empty? name) (empty? message))
      (cond-> (http-response/found "/")
        (empty? name)
        (assoc-in [:flash :errors :name] "name is required")
        (empty? message)
        (assoc-in [:flash :errors :message] "message is required"))
      (do
        (query-fn :save-message! {:name name :message message})
        (http-response/found "/")))
    (catch Exception e
      (log/error e "failed to save message!")
      (-> (http-response/found "/")
          (assoc :flash {:errors {:unknown (.getMessage e)}})))))
```

该命名空间含 `save-message!` 函数，用于执行插入留言的 SQL 查询。  
查询函数通过第一个参数——Integrant 系统映射传入，`query-fn` 键中即为查询函数映射（函数名由 `queries.sql` 中的 `-- :name` 自动推导）。

函数从请求的 `form-params` 中提取表单数据，并尝试保存；最终重定向至首页，并将错误以 flash session 形式附于响应。

---

## 创建页面并处理表单输入

HTML 页面路由定义于 `kit.guestbook.web.routes.pages`。我们先在 `ns` 声明中引入控制器与工具命名空间：

```clojure
(ns kit.guestbook.web.routes.pages
  (:require
    ...
    [kit.guestbook.web.controllers.guestbook :as guestbook]))
```

接着，修改 `home-page` 处理函数，使其从数据库读取消息并传入模板：

```clojure
(defn home [{:keys [query-fn]} {:keys [flash] :as request}]
  (layout/render request "home.html" {:messages (query-fn :get-messages {})
                                      :errors (:errors flash)}))
```

函数渲染 `home.html`，并传入：
- `:messages`：数据库中的所有留言  
- `:errors`：可能存在的错误（来自 flash session）

最后，在 `page-routes` 函数中添加 `/save-message` POST 路由：

```clojure
(defn page-routes [opts]
  [["/" {:get (partial home opts)}]
   ["/save-message" {:post (partial guestbook/save-message! opts)}]])
```

当表单提交时，请求将转交至 `guestbook/save-message!` 处理。

---

### 修改 `home.html` 模板

当前模板仅含静态内容。我们更新 `div.content`，循环渲染留言列表：

```html
<div class="content container">
  <div class="columns">
    <div class="column">
      <h3>Messages</h3>
      <ul class="messages">
        {% for item in messages %}
        <li>
          <time>{{item.timestamp}}</time>
          <p>{{item.message}}</p>
          <p> - {{item.name}}</p>
        </li>
        {% endfor %}
      </ul>
    </div>
  </div>
</div>
```

这里使用 Selmer 的 `{% for %}` 遍历消息列表；每条消息是含 `name`、`message`、`timestamp` 键的映射，可直接引用。

再添加留言表单，并支持错误提示与 CSRF 保护（`{% csrf-field %}` 必须存在）：

```html
<div class="columns">
  <div class="column">
      {% if errors.unknown %}
      <div class="notification is-danger">{{errors.unknown}}</div>
      {% endif %}
      <form method="POST" action="/save-message">
          {% csrf-field %}
          <p>
              <label>
              Name:
              <input class="input" type="text" name="name" value="{{name}}" />
              </label>
          </p>
          {% if errors.name %}
          <div class="notification is-danger">{{errors.name}}</div>
          {% endif %}
          <p>
              <label>
              Message:
              <textarea class="textarea" name="message">{{message}}</textarea>
              </label>
          </p>
          {% if errors.message %}
          <div class="notification is-danger">{{errors.message}}</div>
          {% endif %}
          <input type="submit" class="button is-primary is-outlined has-text-dark" value="comment" />
      </form>
  </div>
</div>
```

完整 `div.content` 如下：

```html
<div class="content container">
  <div class="columns">
      <div class="column">
          <h3>Messages</h3>
          <ul class="messages">
              {% for item in messages %}
              <li>
                  <time>{{item.timestamp}}</time>
                  <p>{{item.message}}</p>
                  <p> - {{item.name}}</p>
              </li>
              {% endfor %}
          </ul>
      </div>
  </div>
  <div class="columns">
      <div class="column">
          {% if errors.unknown %}
          <div class="notification is-danger">{{errors.unknown}}</div>
          {% endif %}
          <form method="POST" action="/save-message">
              {% csrf-field %}
              <p>
                  <label>
                  Name:
                  <input class="input" type="text" name="name" value="{{name}}" />
                  </label>
              </p>
              {% if errors.name %}
              <div class="notification is-danger">{{errors.name}}</div>
              {% endif %}
              <p>
                  <label>
                  Message:
                  <textarea class="textarea" name="message">{{message}}</textarea>
                  </label>
              </p>
              {% if errors.message %}
              <div class="notification is-danger">{{errors.message}}</div>
              {% endif %}
              <input type="submit" class="button is-primary is-outlined has-text-dark" value="comment" />
          </form>
      </div>
  </div>
</div>
```

为提升视觉效果，我们在 `<head>` 中引入 [Bulma CSS 框架](https://bulma.io/)：

```html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bulma@0.9.3/css/bulma.min.css">
```

再编辑 `resources/public/css/screen.css`，优化表单样式：

```css
ul {
	list-style: none;
}

ul.messages li {
	position: relative;
	font-size: 16px;
	padding: 5px;
	border-bottom: 1px dotted #ccc;
}

li:last-child {
	border-bottom: none;
}

li time {
	font-size: 12px;
	padding-bottom: 20px;
}

form, .error {
	padding: 30px;
	margin-bottom: 50px;
	position: relative;
}
```

刷新浏览器，留言簿页面应可正常工作。你可在表单中提交留言测试功能。

> 更多 HTML 模板选项见：[HTML Templating](#html-templating)

---

## 添加测试

测试代码位于 `test` 源码路径下。

可在终端运行 `clj -M:test` 验证数据库交互是否如期工作。

---

## 打包应用程序

执行以下命令，将应用打包为独立可运行 JAR：

```bash
clj -Sforce -T:build all
```

构建产物位于 `target/guestbook-standalone.jar`，可通过如下方式运行：

```bash
export JDBC_URL="jdbc:sqlite:guestbook_dev.db"
java -jar target/guestbook-standalone.jar
```

⚠️ 注意：运行 JAR 时需手动提供 `JDBC_URL` 环境变量——它不会被打包进 JAR。

本教程完整源码见：[GitHub 示例仓库](https://github.com/kit-clj/guestbook-tutorial)

---

## 主题索引

- 你的第一个应用程序  
- REPL 驱动开发  
- 应用配置环境（Profiles）  
- 应用模块（Modules）  
- Integrant  
- HTML 模板  
- 静态资源  
- ClojureScript  
- 路由  
- WebSocket  
- 请求与响应  
- 中间件  
- 会话（Sessions）  
- 数据库访问  
- 缓存  
- 任务调度  
- 日志  
- 测试  
- 服务器调优  
- 环境变量  
- 部署  
- 有用类库  
- 示例应用  
- 升级指南  
- 编辑器配置  
- Clojure 资源  

## 库（Libs）

- `kit-core`  
- `kit-hato`  
- `kit-metrics`  
- `kit-mysql`  
- `kit-nrepl`  
- `kit-postgres`  
- `kit-quartz`  
- `kit-redis`  
- `kit-repl`  
- `kit-sql`  
- `kit-sql-conman`  
- `kit-sql-hikari`  
- `kit-sql-migratus`  
- `kit-undertow`  
- `kit-xtdb`