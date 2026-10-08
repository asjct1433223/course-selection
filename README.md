# 学生选课管理系统（course-selection）

面向校园场景的选课系统：管理员维护学生 / 教师 / 课程，教师录入成绩，学生选课与退课。
后端 Spring Boot + Spring Security(JWT) + MyBatis-Plus，前端 Vue 3 + Vite + Element Plus；
并配了一套 **Postman 接口自动化用例（46 个接口、106 条断言）**，由 GitHub Actions 在每次提交时自动回归。

## 功能

- **三角色权限**：`ADMIN` / `TEACHER` / `STUDENT`，由 Spring Security 按角色控制接口访问
- **登录鉴权**：JWT 签发与校验；未带 Token 访问受保护接口返回 `401`，越权访问返回 `403`
- **学生管理**（管理员）：分页、按学号检索、增删改；删除学生时级联清理其账号
- **教师管理**（管理员）：分页、按工号检索、增删改
- **课程管理**（管理员）：分页、按编号检索，课程关联授课教师
- **选课与成绩**：学生选课 / 退课（**容量上限**与**重复选课**校验），教师录入成绩
- **API 文档**：Knife4j（启动后访问 `/doc.html`）

## 技术栈

| 层 | 技术 |
| --- | --- |
| 后端 | Java 8、Spring Boot 2.7、Spring Security + JWT、MyBatis-Plus 3.5、Knife4j |
| 数据库 | 开发用 H2（文件模式，开箱即跑）；生产用 MySQL（`application-mysql.yml`） |
| 前端 | Vue 3、Vite 5、Element Plus、Pinia、Vue Router、Axios |
| 测试 | Postman（Tests 断言脚本 + Collection 变量链）、newman、GitHub Actions |

## 目录结构

```
course-selection/
├── backend/                 Spring Boot 后端（默认 dev profile = H2）
│   └── src/main/resources/
│       ├── application.yml          dev 配置（H2 + JWT）
│       ├── application-mysql.yml    MySQL 配置
│       └── sql/                     schema.sql + data.sql（含种子数据）
├── frontend/                Vue 3 前端（7 个页面：登录 / 首页 / 学生 / 教师 / 课程 / 选课 / 成绩）
├── api-tests/               Postman 接口自动化集合
├── db/student.sql           MySQL 建表与数据导出（8.4 导出，备用）
└── .github/workflows/       CI：接口回归 + 前端构建
```

## 本地运行

**后端**（默认 H2 文件库，不需要装 MySQL，首次启动自动执行建表与种子数据）：

```bash
cd backend
./mvnw spring-boot:run          # Windows: mvnw.cmd spring-boot:run
```

启动后：接口在 `http://localhost:8080`，API 文档在 `http://localhost:8080/doc.html`。

**前端**：

```bash
cd frontend
npm install
npm run dev                     # http://localhost:5173，/api 已代理到 8080
```

**测试账号**（种子数据，密码统一 `123456`，BCrypt 存储）：

| 角色 | 用户名 |
| --- | --- |
| 管理员 | `admin` |
| 教师 | `t001` |
| 学生 | `s2024001` |

## 接口自动化测试

集合：`api-tests/course-selection-api.postman_collection.json`，共 6 个模块、46 个请求、106 条断言。

```bash
# 需要服务已在 8080 运行
npx newman run api-tests/course-selection-api.postman_collection.json \
  --env-var baseUrl=http://localhost:8080
```

也可以在 Postman 里直接导入这个集合 + 用 Collection Runner 批量执行。

### 用例是怎么设计的

- **正反用例成对**：每个 CRUD 都走「新增 → 查询 → 修改 → 改后校验 → 删除 → 删后校验」，
  并且专门有一条「删除后查询应为空」的断言，避免只看接口返回码就以为通过。
- **异常与边界**：重复学号、重复选课、密码错误、无效 Token（401）、
  学生越权访问教师管理接口（403）、选课后课程超容量。
- **权限分开验证**：401（没登录）和 403（登录了但没权限）是两件事，各有用例覆盖。
- **可重复执行**：用时间戳生成唯一学号与账号（`PS` + `Date.now()`），
  用例末尾删除本次建的课程做清理，所以集合可以连续跑多次而不互相干扰。
- **数据链路**：三种角色分别登录取 Token 并用 Collection 变量回写，
  后续请求通过 `{{adminToken}}`、`{{createdCourseId}}` 这类变量串联，
  不手写死 ID——这样用例跑的是「真实的业务流程」而不是孤立接口。
- **断言分层**：HTTP 状态码（`pm.response.to.have.status`）+ 业务返回码（`code`）
  + 关键字段值（角色、Token 类型与长度、成绩是否写入）。

### CI

`.github/workflows/api-tests.yml` 在每次 push / PR 时：

1. 用 JDK 8 构建后端；
2. 以 H2 内嵌库启动服务（CI 里不需要外部数据库）；
3. 探测到端口就绪后，用 newman 跑整套集合，**断言失败即 CI 失败**；
4. 另有一个 job 构建前端，保证前端也能编译通过。

## 回归中发现的问题（第一轮接 CI 时）

第一次把集合接进 CI，106 条断言里有 4 条失败。逐条定位后：**2 条是用例本身的问题、1 条是被测接口的语义问题、1 条是前者的连带**。

| 用例 | 现象 | 定位 | 处理 |
| --- | --- | --- | --- |
| 30-6 修改课程 | 业务码 500「请选择授课教师」 | 按用例的请求体重放（只传 id / name / capacity），再读 `CourseServiceImpl.updateCourse`：它先 `checkTeacherExists` 再 `updateById`，属于**整体更新**语义；前端编辑时提交的也是完整对象 | **改用例**：提交完整课程对象，与前端保持一致 |
| 30-7 修改后校验 | 名称没变 | 上一条返回 500，数据没写进去 | 随 30-6 修复 |
| 40-3 学生选课 | 断言 message 含「选课成功」，实际返回「操作成功」 | 断言写死在提示文案上，接口只有通用成功文案 | **改用例**：断业务码，业务结果交给 40-4 校验选课记录 |
| 40-8 学生退课 | 同上 | 同上 | 同上 |

修复后本地与 CI 都是 **106 / 106 通过**。

**顺带记一个待改进项**：参数校验失败（例如未选授课教师）返回的业务码是 `500`，
语义上更接近 `400`（客户端请求有误），目前统一被全局异常处理兜成 500。
因为集合里已有若干用例按 `500` 断言（重复学号、重复选课、密码错误），这次**只记录、不改动**，避免为了跑绿而让测试与实现互相迁就。

**另一处修掉的环境问题**：仓库里的 `mvnw.cmd` 只有 14 字节，内容竟是 `404: Not Found`（下载失败被写进了文件）；
`mvnw` 也缺少 `-Dmaven.multiModuleProjectDirectory`，两者都跑不起来。已重写并实测可用。

## 已知限制

- 目前只有**接口层自动化**（Postman + newman），还没有接入 JUnit 单元测试与前端 E2E；
- 选课并发只做了「容量校验」，没有做行级锁或乐观锁，高并发下仍可能超卖；
- H2 与 MySQL 双配置下部分 SQL 函数行为存在差异，生产环境以 `application-mysql.yml` 为准。

## 说明

本项目为个人学习项目：需求拆解、接口设计与测试用例设计由本人完成，编码过程借助 AI 辅助，逐版验收。