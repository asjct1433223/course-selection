-- =============================================
-- 学生选课管理系统 - 数据库建表脚本
-- 兼容 H2 和 MySQL
-- =============================================

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    password    VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    real_name   VARCHAR(50)  NOT NULL COMMENT '真实姓名',
    role        VARCHAR(20)  NOT NULL DEFAULT 'STUDENT' COMMENT '角色: ADMIN/TEACHER/STUDENT',
    phone       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    email       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    enabled     TINYINT      NOT NULL DEFAULT 1 COMMENT '是否启用 1-是 0-否',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 教师表
CREATE TABLE IF NOT EXISTS teacher (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT       NOT NULL COMMENT '关联用户ID',
    teacher_no  VARCHAR(20)  NOT NULL UNIQUE COMMENT '工号',
    name        VARCHAR(50)  NOT NULL COMMENT '姓名',
    gender      VARCHAR(5)   DEFAULT '男' COMMENT '性别',
    title       VARCHAR(50)  DEFAULT '讲师' COMMENT '职称: 教授/副教授/讲师/助教',
    department  VARCHAR(100) DEFAULT NULL COMMENT '所属院系',
    phone       VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     DEFAULT CURRENT_TIMESTAMP
);

-- 学生表
CREATE TABLE IF NOT EXISTS student (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT       NOT NULL COMMENT '关联用户ID',
    student_no    VARCHAR(20)  NOT NULL UNIQUE COMMENT '学号',
    name          VARCHAR(50)  NOT NULL COMMENT '姓名',
    gender        VARCHAR(5)   DEFAULT '男' COMMENT '性别',
    class_name    VARCHAR(100) DEFAULT NULL COMMENT '班级',
    grade         VARCHAR(10)  DEFAULT NULL COMMENT '年级（入学年份）',
    phone         VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    create_time   DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     DEFAULT CURRENT_TIMESTAMP
);

-- 课程表
CREATE TABLE IF NOT EXISTS course (
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    course_no    VARCHAR(20)  NOT NULL UNIQUE COMMENT '课程编号',
    name         VARCHAR(100) NOT NULL COMMENT '课程名称',
    credit       DECIMAL(3,1) NOT NULL DEFAULT 2.0 COMMENT '学分',
    teacher_id   BIGINT       NOT NULL COMMENT '授课教师ID',
    capacity     INT          NOT NULL DEFAULT 60 COMMENT '课容量',
    selected_num INT          NOT NULL DEFAULT 0 COMMENT '已选人数',
    semester     VARCHAR(20)  NOT NULL COMMENT '学期(如: 2025-2026-2)',
    class_hours  INT          DEFAULT 48 COMMENT '总学时',
    description  TEXT         DEFAULT NULL COMMENT '课程简介',
    create_time  DATETIME     DEFAULT CURRENT_TIMESTAMP,
    update_time  DATETIME     DEFAULT CURRENT_TIMESTAMP
);

-- 选课记录表
CREATE TABLE IF NOT EXISTS sc (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id  BIGINT       NOT NULL COMMENT '学生ID',
    course_id   BIGINT       NOT NULL COMMENT '课程ID',
    score       DECIMAL(5,2) DEFAULT NULL COMMENT '成绩',
    create_time DATETIME     DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    UNIQUE KEY uk_student_course (student_id, course_id)
);
