-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.4.8 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  12.17.0.7270
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 student 的数据库结构
CREATE DATABASE IF NOT EXISTS `student` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `student`;

-- 导出  表 student.course 结构
CREATE TABLE IF NOT EXISTS `course` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_no` varchar(20) NOT NULL COMMENT '课程编号',
  `name` varchar(100) NOT NULL COMMENT '课程名称',
  `credit` decimal(3,1) NOT NULL DEFAULT '2.0' COMMENT '学分',
  `teacher_id` bigint NOT NULL COMMENT '授课教师ID',
  `capacity` int NOT NULL DEFAULT '60' COMMENT '课容量',
  `selected_num` int NOT NULL DEFAULT '0' COMMENT '已选人数',
  `semester` varchar(20) NOT NULL COMMENT '学期(如: 2025-2026-2)',
  `class_hours` int DEFAULT '48' COMMENT '总学时',
  `description` text COMMENT '课程简介',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `course_no` (`course_no`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  student.course 的数据：~8 rows (大约)
INSERT INTO `course` (`id`, `course_no`, `name`, `credit`, `teacher_id`, `capacity`, `selected_num`, `semester`, `class_hours`, `description`, `create_time`, `update_time`) VALUES
	(1, 'CS101', 'Java程序设计', 4.0, 1, 60, 5, '2025-2026-2', 64, '学习Java语言基础、面向对象编程、集合框架、多线程及网络编程等内容。', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(2, 'CS102', '数据结构与算法', 3.5, 2, 50, 4, '2025-2026-2', 56, '系统地学习线性表、树、图等数据结构，以及排序、查找等经典算法。', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(3, 'CS103', '数据库原理与应用', 3.0, 3, 60, 3, '2025-2026-2', 48, '介绍关系数据库基本理论、SQL语言、数据库设计范式及事务管理。', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(4, 'CS104', '软件工程导论', 2.5, 4, 45, 5, '2025-2026-2', 40, '学习软件生命周期、需求分析、系统设计、软件测试及项目管理等知识。', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(5, 'CS105', '操作系统原理', 3.5, 1, 50, 4, '2025-2026-2', 56, '学习进程管理、内存管理、文件系统、设备管理等操作系统核心概念。', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(6, 'CS106', '计算机网络', 3.0, 2, 55, 3, '2025-2026-2', 48, '学习TCP/IP协议栈、网络层协议、传输层协议及应用层协议等。', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(7, 'CS107', 'Web前端开发技术', 2.5, 3, 50, 6, '2025-2026-2', 40, '学习HTML5、CSS3、JavaScript及Vue.js框架，掌握前端开发技能。', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(8, 'CS108', '人工智能导论', 2.0, 4, 60, 2, '2025-2026-2', 32, '介绍人工智能基本概念、搜索算法、机器学习基础及深度学习入门。', '2026-08-31 18:37:24', '2026-08-31 18:37:24');

-- 导出  表 student.sc 结构
CREATE TABLE IF NOT EXISTS `sc` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` bigint NOT NULL COMMENT '学生ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `score` decimal(5,2) DEFAULT NULL COMMENT '成绩',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_student_course` (`student_id`,`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  student.sc 的数据：~23 rows (大约)
INSERT INTO `sc` (`id`, `student_id`, `course_id`, `score`, `create_time`) VALUES
	(1, 1, 1, 85.50, '2026-08-31 18:37:24'),
	(2, 1, 2, 78.00, '2026-08-31 18:37:24'),
	(3, 1, 5, NULL, '2026-08-31 18:37:24'),
	(4, 2, 1, 92.00, '2026-08-31 18:37:24'),
	(5, 2, 2, 88.50, '2026-08-31 18:37:24'),
	(6, 2, 6, NULL, '2026-08-31 18:37:24'),
	(7, 3, 1, 75.00, '2026-08-31 18:37:24'),
	(8, 3, 7, NULL, '2026-08-31 18:37:24'),
	(9, 4, 2, 90.00, '2026-08-31 18:37:24'),
	(10, 4, 3, 82.00, '2026-08-31 18:37:24'),
	(11, 5, 1, NULL, '2026-08-31 18:37:24'),
	(12, 5, 3, NULL, '2026-08-31 18:37:24'),
	(13, 6, 4, NULL, '2026-08-31 18:37:24'),
	(14, 6, 7, NULL, '2026-08-31 18:37:24'),
	(15, 7, 4, NULL, '2026-08-31 18:37:24'),
	(16, 7, 7, NULL, '2026-08-31 18:37:24'),
	(17, 7, 8, NULL, '2026-08-31 18:37:24'),
	(18, 8, 5, NULL, '2026-08-31 18:37:24'),
	(19, 8, 6, NULL, '2026-08-31 18:37:24'),
	(20, 9, 4, NULL, '2026-08-31 18:37:24'),
	(21, 9, 7, NULL, '2026-08-31 18:37:24'),
	(22, 10, 3, NULL, '2026-08-31 18:37:24'),
	(23, 10, 8, NULL, '2026-08-31 18:37:24');

-- 导出  表 student.student 结构
CREATE TABLE IF NOT EXISTS `student` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '关联用户ID',
  `student_no` varchar(20) NOT NULL COMMENT '学号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `gender` varchar(5) DEFAULT '男' COMMENT '性别',
  `class_name` varchar(100) DEFAULT NULL COMMENT '班级',
  `grade` varchar(10) DEFAULT NULL COMMENT '年级（入学年份）',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `student_no` (`student_no`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  student.student 的数据：~10 rows (大约)
INSERT INTO `student` (`id`, `user_id`, `student_no`, `name`, `gender`, `class_name`, `grade`, `phone`, `create_time`, `update_time`) VALUES
	(1, 6, '2024001', '张三', '男', '软件工程2401班', '2024', '13900001001', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(2, 7, '2024002', '李四', '女', '软件工程2401班', '2024', '13900001002', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(3, 8, '2024003', '王五', '男', '软件工程2402班', '2024', '13900001003', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(4, 9, '2024004', '赵六', '女', '计算机科学2401班', '2024', '13900001004', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(5, 10, '2024005', '孙七', '男', '计算机科学2401班', '2024', '13900001005', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(6, 11, '2024006', '周八', '女', '软件工程2402班', '2024', '13900001006', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(7, 12, '2024007', '吴九', '男', '软件工程2401班', '2024', '13900001007', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(8, 13, '2024008', '郑十', '女', '计算机科学2402班', '2024', '13900001008', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(9, 14, '2024009', '陈一一', '女', '软件工程2402班', '2024', '13900001009', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(10, 15, '2024010', '刘一二', '男', '计算机科学2402班', '2024', '13900001010', '2026-08-31 18:37:24', '2026-08-31 18:37:24');

-- 导出  表 student.sys_user 结构
CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码（加密）',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `role` varchar(20) NOT NULL DEFAULT 'STUDENT' COMMENT '角色: ADMIN/TEACHER/STUDENT',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `enabled` tinyint NOT NULL DEFAULT '1' COMMENT '是否启用 1-是 0-否',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  student.sys_user 的数据：~15 rows (大约)
INSERT INTO `sys_user` (`id`, `username`, `password`, `real_name`, `role`, `phone`, `email`, `enabled`, `create_time`, `update_time`) VALUES
	(1, 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '系统管理员', 'ADMIN', '13800000001', 'admin@school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(2, 't001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张教授', 'TEACHER', '13800000002', 'zhang@school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(3, 't002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李副教授', 'TEACHER', '13800000003', 'li@school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(4, 't003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王讲师', 'TEACHER', '13800000004', 'wang@school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(5, 't004', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '赵教授', 'TEACHER', '13800000005', 'zhao@school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(6, 's2024001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张三', 'STUDENT', '13900001001', 'zhangsan@stu.school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(7, 's2024002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李四', 'STUDENT', '13900001002', 'lisi@stu.school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(8, 's2024003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王五', 'STUDENT', '13900001003', 'wangwu@stu.school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(9, 's2024004', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '赵六', 'STUDENT', '13900001004', 'zhaoliu@stu.school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(10, 's2024005', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '孙七', 'STUDENT', '13900001005', 'sunqi@stu.school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(11, 's2024006', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '周八', 'STUDENT', '13900001006', 'zhouba@stu.school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(12, 's2024007', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '吴九', 'STUDENT', '13900001007', 'wujiu@stu.school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(13, 's2024008', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '郑十', 'STUDENT', '13900001008', 'zhengshi@stu.school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(14, 's2024009', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '陈一一', 'STUDENT', '13900001009', 'chenyiyi@stu.school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(15, 's2024010', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '刘一二', 'STUDENT', '13900001010', 'liuyier@stu.school.edu.cn', 1, '2026-08-31 18:37:24', '2026-08-31 18:37:24');

-- 导出  表 student.teacher 结构
CREATE TABLE IF NOT EXISTS `teacher` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '关联用户ID',
  `teacher_no` varchar(20) NOT NULL COMMENT '工号',
  `name` varchar(50) NOT NULL COMMENT '姓名',
  `gender` varchar(5) DEFAULT '男' COMMENT '性别',
  `title` varchar(50) DEFAULT '讲师' COMMENT '职称: 教授/副教授/讲师/助教',
  `department` varchar(100) DEFAULT NULL COMMENT '所属院系',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `teacher_no` (`teacher_no`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  student.teacher 的数据：~4 rows (大约)
INSERT INTO `teacher` (`id`, `user_id`, `teacher_no`, `name`, `gender`, `title`, `department`, `phone`, `create_time`, `update_time`) VALUES
	(1, 2, 'T2020001', '张教授', '男', '教授', '计算机科学与技术学院', '13800000002', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(2, 3, 'T2020002', '李副教授', '女', '副教授', '软件学院', '13800000003', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(3, 4, 'T2021001', '王讲师', '男', '讲师', '计算机科学与技术学院', '13800000004', '2026-08-31 18:37:24', '2026-08-31 18:37:24'),
	(4, 5, 'T2019001', '赵教授', '女', '教授', '软件学院', '13800000005', '2026-08-31 18:37:24', '2026-08-31 18:37:24');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
