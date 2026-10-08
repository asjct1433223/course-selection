# 学生选课管理系统（course-selection-backend）开发流程

## 一、项目概览

| 项目 | 说明 |
|------|------|
| **项目名称** | course-selection（学生选课管理系统） |
| **类型** | 教学项目 / Spring Boot 后端 |
| **JDK 版本** | 1.8 |
| **Spring Boot** | 2.7.18 |
| **构建工具** | Maven |
| **包路径** | `com.example.course` |

---

## 二、技术栈

```
┌─────────────────────────────────────────────────────┐
│                   前端（待对接）                       │
├─────────────────────────────────────────────────────┤
│  Controller 层    RESTful API + Knife4j 文档          │
│  Security 层      Spring Security + JWT 无状态认证     │
│  Service 层       业务逻辑 + @Transactional 事务       │
│  Mapper 层        MyBatis-Plus 3.5.3.1               │
│  Database 层      H2 (开发) / MySQL (生产)             │
└─────────────────────────────────────────────────────┘
```

**核心依赖**：

- **Spring Boot Web** — RESTful API
- **Spring Security** — 认证与授权
- **Spring Validation** — 参数校验
- **MyBatis-Plus** — ORM + 分页
- **H2** — 开发环境内嵌数据库
- **MySQL** — 生产环境数据库
- **JWT (jjwt 0.9.1)** — 无状态 Token 认证
- **Knife4j 4.1.0** — API 文档（Swagger 增强版）
- **Lombok** — 代码简化
- **FastJSON 2.0.43** — JSON 序列化

---

## 三、项目目录结构

```
course-selection-backend/
├── pom.xml                          # Maven 构建配置
├── mvnw / mvnw.cmd                  # Maven Wrapper（无需安装 Maven）
├── sql/
│   ├── schema.sql                   # 建表脚本（启动时自动执行）
│   └── data.sql                     # 初始测试数据
├── data/                            # H2 数据库文件存储目录
└── src/main/
    ├── java/com/example/course/
    │   ├── CourseApplication.java           # 启动入口
    │   ├── common/
    │   │   ├── Result.java                  # 统一响应体 {code, message, data}
    │   │   ├── GlobalExceptionHandler.java  # 全局异常处理
    │   │   └── JwtUtils.java                # JWT 生成/校验工具
    │   ├── config/
    │   │   ├── SecurityConfig.java          # Spring Security 配置
    │   │   ├── CorsConfig.java              # 跨域配置
    │   │   └── MyBatisPlusConfig.java       # MyBatis-Plus 分页/自动填充
    │   ├── security/
    │   │   └── JwtAuthenticationFilter.java # JWT 请求拦截过滤器
    │   ├── entity/
    │   │   ├── User.java                    # 用户实体 (sys_user)
    │   │   ├── Student.java                 # 学生实体 (student)
    │   │   ├── Teacher.java                 # 教师实体 (teacher)
    │   │   ├── Course.java                  # 课程实体 (course)
    │   │   └── Sc.java                      # 选课记录 (sc)
    │   ├── dto/
    │   │   ├── LoginDTO.java                # 登录请求
    │   │   ├── LoginResultDTO.java          # 登录响应
    │   │   ├── PasswordDTO.java             # 密码修改
    │   │   └── ScQueryDTO.java              # 选课查询
    │   ├── mapper/
    │   │   ├── UserMapper.java              # 用户 Mapper
    │   │   ├── StudentMapper.java           # 学生 Mapper
    │   │   ├── TeacherMapper.java           # 教师 Mapper
    │   │   ├── CourseMapper.java            # 课程 Mapper
    │   │   └── ScMapper.java                # 选课 Mapper
    │   ├── service/
    │   │   ├── AuthService.java             # 认证服务接口
    │   │   ├── StudentService.java          # 学生服务接口
    │   │   ├── TeacherService.java          # 教师服务接口
    │   │   ├── CourseService.java           # 课程服务接口
    │   │   ├── ScService.java               # 选课服务接口
    │   │   └── impl/
    │   │       ├── AuthServiceImpl.java     # 登录认证实现
    │   │       ├── StudentServiceImpl.java  # 学生管理实现
    │   │       ├── TeacherServiceImpl.java  # 教师管理实现
    │   │       ├── CourseServiceImpl.java   # 课程管理 + 选课/退课
    │   │       └── ScServiceImpl.java       # 成绩管理实现
    │   └── controller/
    │       ├── AuthController.java          # /api/auth/* 认证接口
    │       ├── StudentController.java       # /api/student/* 学生CRUD
    │       ├── TeacherController.java       # /api/teacher/* 教师CRUD
    │       ├── CourseController.java        # /api/course/* 课程CRUD + 选课/退课
    │       └── ScController.java            # /api/sc/* 成绩录入与查询
    └── resources/
        ├── application.yml                 # 主配置（dev 环境，H2）
        └── application-mysql.yml           # MySQL 生产环境配置
```

---

## 四、数据库设计（ER 关系）

```
┌──────────────┐     1:1     ┌──────────────┐
│   sys_user   │◄────────────│   teacher    │
│  (用户表)     │             │  (教师表)     │
│  id          │             │  user_id(FK) │
│  username    │             │  teacher_no  │
│  password    │             │  name/title  │
│  role        │             │  department  │
│  real_name   │             └──────┬───────┘
└──────┬───────┘                    │ 1:N
       │ 1:1                        │
       │            ┌───────────────┼───────┐
       │            │               ▼       │
       │            │        ┌──────────┐   │
       │            │        │  course  │   │
       │            │        │ (课程表)  │   │
       │            │        │ id       │◄──┘
       │            │        │ course_no│   N:M
       │            │        │ capacity │   │
       │            │        │ selected │   │
       │            │        └────┬─────┘   │
       │            │             │         │
       │            │             │ N:M     │
       │  ┌─────────┴──────────┐  │         │
       └─►│     student        │  │         │
          │    (学生表)         │  │         │
          │    user_id(FK)     │  │         │
          │    student_no      │  │         │
          │    class_name      │  │         │
          └────────┬───────────┘  │         │
                   │              │         │
                   │    ┌─────────┴──┐      │
                   └───►│     sc     │◄─────┘
                        │ (选课记录)  │
                        │ student_id │
                        │ course_id  │
                        │ score      │
                        └────────────┘
```

**5 张表，4 个业务域**：

| 表 | 说明 | 关键字段 |
|----|------|---------|
| `sys_user` | 统一用户认证 | username, password(BCrypt), role(ADMIN/TEACHER/STUDENT) |
| `teacher` | 教师信息 | user_id FK, teacher_no 工号, title 职称 |
| `student` | 学生信息 | user_id FK, student_no 学号, grade 年级 |
| `course` | 课程信息 | teacher_id FK, capacity 容量, selected_num 已选 |
| `sc` | 选课记录 | student_id FK, course_id FK, score 成绩, 联合唯一键 |

---

## 五、分层架构与调用链

```
 HTTP Request
     │
     ▼
┌──────────────────┐
│  CorsFilter      │  ← 跨域处理
│  SecurityFilter  │  ← JWT 认证过滤
└──────┬───────────┘
       ▼
┌──────────────────┐
│   Controller     │  ← @RestController, 参数校验 @Valid
│   (5 controllers)│     注解: @Api / @ApiOperation (Knife4j)
└──────┬───────────┘
       ▼
┌──────────────────┐
│  Service 接口    │  ← 业务接口定义
│  Service Impl    │  ← @Service, @Transactional 事务管理
└──────┬───────────┘
       ▼
┌──────────────────┐
│  Mapper 接口     │  ← MyBatis-Plus BaseMapper 自动CRUD
│  (5 mappers)     │     自定义SQL用 XML 或注解
└──────┬───────────┘
       ▼
┌──────────────────┐
│  Database        │  ← H2 (dev) / MySQL (prod)
└──────────────────┘
```

**统一返回格式** → `Result<T>`：

```json
{ "code": 200, "message": "操作成功", "data": {...} }
```

---

## 六、API 接口清单

| 模块 | 方法 | 路径 | 权限 | 说明 |
|------|------|------|------|------|
| **认证** | POST | `/api/auth/login` | 公开 | 用户登录，返回 JWT Token |
| **学生管理** | GET | `/api/student/page` | 认证 | 分页查询学生 |
| | GET | `/api/student/list` | 认证 | 所有学生列表 |
| | GET | `/api/student/{id}` | 认证 | 按ID查学生 |
| | POST | `/api/student` | 认证 | 新增学生 |
| | PUT | `/api/student` | 认证 | 更新学生 |
| | DELETE | `/api/student/{id}` | 认证 | 删除学生 |
| **教师管理** | GET | `/api/teacher/page` | 认证 | 分页查询教师 |
| | GET | `/api/teacher/list` | 认证 | 所有教师列表 |
| | GET | `/api/teacher/{id}` | 认证 | 按ID查教师 |
| | POST | `/api/teacher` | 认证 | 新增教师 |
| | PUT | `/api/teacher` | 认证 | 更新教师 |
| | DELETE | `/api/teacher/{id}` | 认证 | 删除教师 |
| **课程管理** | GET | `/api/course/page` | 认证 | 分页查询课程（含教师名） |
| | GET | `/api/course/list` | 认证 | 所有课程列表 |
| | GET | `/api/course/{id}` | 认证 | 按ID查课程 |
| | POST | `/api/course` | 认证 | 新增课程 |
| | PUT | `/api/course` | 认证 | 更新课程 |
| | DELETE | `/api/course/{id}` | 认证 | 删除课程 |
| | POST | `/api/course/select` | 认证 | **选课**（studentId + courseId） |
| | POST | `/api/course/drop` | 认证 | **退课**（studentId + courseId） |
| **成绩管理** | GET | `/api/sc/student/{id}` | 认证 | 查学生选课记录 |
| | GET | `/api/sc/course/{id}` | 认证 | 查课程选课学生 |
| | GET | `/api/sc/list` | 认证 | 所有选课记录 |
| | PUT | `/api/sc/score` | 认证 | 录入/修改成绩 |

**权限模型**（3 种角色）：

- `ADMIN` — 管理员，可访问 `/api/admin/**` + 教师接口 + 所有认证接口
- `TEACHER` — 教师，可访问 `/api/teacher/**`
- `STUDENT` — 学生，可访问所有认证接口（业务逻辑限制只能操作自己的数据）

---

## 七、开发环境配置

### 启动方式

```bash
# 方式1: 使用 Maven Wrapper（推荐，无需安装 Maven）
./mvnw spring-boot:run

# 方式2: 先打包再运行
./mvnw clean package -DskipTests
java -jar target/course-selection-1.0.0.jar

# 方式3: IDE 直接运行 CourseApplication.main()
```

### 环境切换

| 配置 | 文件 | 说明 |
|------|------|------|
| **dev（默认）** | `application.yml` | H2 内嵌数据库，启动自动建表+初始化数据，无需安装任何数据库 |
| **mysql（生产）** | `application-mysql.yml` | 连接 MySQL，需手动建库 |

切换方式：修改 `application.yml` 第 5 行 `spring.profiles.active` 为 `mysql`。

### 开发工具地址

| 工具 | URL | 说明 |
|------|-----|------|
| **API 文档 (Knife4j)** | `http://localhost:8080/doc.html` | Swagger 增强 UI，中文界面，可直接调试 |
| **H2 控制台** | `http://localhost:8080/h2-console` | 浏览器端数据库管理 |
| **JWT 密钥** | 配置在 `application.yml` → `jwt.secret` | Token 有效期 24h |

### 默认测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| `admin` | `123456` | 管理员 |
| `t001` ~ `t004` | `123456` | 教师 |
| `s2024001` ~ `s2024010` | `123456` | 学生 |

---

## 八、典型开发流程

```
┌──────────────────────────────────────────────────────┐
│ 1. 环境准备                                          │
│    JDK 8 + IDE (推荐 IntelliJ IDEA)                   │
│    git clone 项目 → IDE 打开 → 等待 Maven 依赖下载     │
├──────────────────────────────────────────────────────┤
│ 2. 启动开发服务                                       │
│    直接运行 CourseApplication.main()                  │
│    → 自动建表 (sql/schema.sql)                        │
│    → 自动插入测试数据 (sql/data.sql)                   │
│    → 访问 http://localhost:8080/doc.html 查看 API      │
├──────────────────────────────────────────────────────┤
│ 3. 新增功能步骤                                       │
│    a) 定义 Entity（若新表）                            │
│    b) 编写 Mapper 接口（继承 BaseMapper）               │
│    c) 编写 Service 接口 + Impl（继承 ServiceImpl）      │
│    d) 编写 Controller（@RestController）               │
│    e) 在 SecurityConfig 配置接口权限                   │
│    f) 通过 Knife4j 页面测试接口                         │
├──────────────────────────────────────────────────────┤
│ 4. 切到生产环境                                       │
│    修改 spring.profiles.active: mysql                 │
│    → 配置 MySQL 连接信息                               │
│    → 手动执行 sql/schema.sql                          │
│    → 启动应用                                         │
├──────────────────────────────────────────────────────┤
│ 5. 打包部署                                          │
│    ./mvnw clean package -DskipTests                   │
│    java -jar target/course-selection-1.0.0.jar        │
└──────────────────────────────────────────────────────┘
```

---

## 九、核心业务规则

1. **选课逻辑**（`CourseServiceImpl.java:45`）：
   - 课程不存在 → 报错
   - `selected_num >= capacity` → 课已满
   - 同一学生重复选同一课程 → 不允许
   - 选课成功 → `selected_num + 1` + 插入 `sc` 记录

2. **退课逻辑**（`CourseServiceImpl.java:69`）：
   - 未选此课 → 报错
   - 退课成功 → `selected_num - 1`（最小为 0）+ 删除 `sc` 记录

3. **成绩录入**：成绩范围 0-100，直接更新 `sc.score` 字段

4. **认证**：`POST /api/auth/login` → BCrypt 密码校验 → 返回 JWT，后续请求需带 `Authorization: Bearer <token>`

---

## 十、配置要点速查

| 配置项 | 位置 | 默认值 |
|--------|------|--------|
| 服务端口 | `application.yml` → `server.port` | 8080 |
| H2 数据库文件 | `application.yml` → `spring.datasource.url` | `./data/course_selection` |
| JWT 密钥 | `application.yml` → `jwt.secret` | Base64 编码字符串 |
| JWT 过期时间 | `application.yml` → `jwt.expiration` | 86400000ms (24h) |
| MyBatis SQL 日志 | `application.yml` → `mybatis-plus.configuration.log-impl` | StdOutImpl（控制台输出） |
| 逻辑删除字段 | `application.yml` → `mybatis-plus.global-config.db-config.logic-delete-field` | deleted |

---

## 十一、项目总结

核心要点：**Maven + Spring Boot 2.7 + MyBatis-Plus + H2/MySQL 双环境 + JWT 认证** 的标准三层架构。

- 开发时直接启动即可，使用 H2 内嵌数据库，无需安装任何外部依赖
- 生产环境切换到 MySQL
- Knife4j 提供可视化的 API 文档与调试界面（`/doc.html`）
