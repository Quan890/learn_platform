CREATE DATABASE IF NOT EXISTS `xd_learn_platform` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `xd_learn_platform`;

/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80026 (8.0.26)
 Source Host           : localhost:3306
 Source Schema         : xd_learn_platform

 Target Server Type    : MySQL
 Target Server Version : 80026 (8.0.26)
 File Encoding         : 65001

 Date: 22/12/2025 20:04:58
*/

-- 新增：创建并使用数据库
CREATE DATABASE IF NOT EXISTS `xd_learn_platform` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `xd_learn_platform`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_id`(`user_id` ASC) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  CONSTRAINT `comment_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `comment_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, '这门课程讲得很清楚，适合初学者学习。', 2, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (2, '老师的教学方式很生动，推荐给大家。', 3, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (3, '内容很实用，学完后对工作有很大帮助。', 4, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (4, '希望能增加更多的实战案例。', 2, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (5, '讲解详细，通俗易懂。', 3, 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (6, '课程质量很高，值得购买。', 4, 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (7, '课程内容很丰富，讲解清晰易懂，推荐给初学者。', 5, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (8, '老师讲得很细致，每个知识点都有实际案例支撑。', 6, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (9, '通过这门课程，我对Java有了更深入的理解。', 7, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (10, '课程结构安排合理，循序渐进，非常适合自学。', 8, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (11, '实践项目很有帮助，能够巩固所学的知识点。', 9, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (12, '数据库设计部分讲得特别好，受益匪浅。', 10, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (13, '索引优化的技巧非常实用，工作中马上就能用上。', 11, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (14, '老师的教学方式很生动，不会觉得枯燥。', 12, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (15, '课程内容紧跟技术发展趋势，很有前瞻性。', 13, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (16, '通过学习这门课程，我的数据库技能提升了很多。', 14, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (17, 'Vue的组件化思想讲解得很透彻，容易理解。', 15, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (18, 'Vuex状态管理部分对我帮助很大，解决了项目中的痛点。', 16, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (19, '课程中的实战案例非常贴近实际开发场景。', 17, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (20, '老师对Vue原理的剖析让我豁然开朗。', 18, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `comment` VALUES (21, '1', 1, 1, '2025-12-20 16:20:07', '2025-12-20 16:20:07');
INSERT INTO `comment` VALUES (22, '3', 1, 1, '2025-12-20 16:22:58', '2025-12-20 16:22:58');
INSERT INTO `comment` VALUES (23, 'good', 1, 1, '2025-12-20 18:51:54', '2025-12-20 18:51:54');

-- ----------------------------
-- Table structure for course
-- ----------------------------
DROP TABLE IF EXISTS `course`;
CREATE TABLE `course`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课程ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '课程标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '课程描述',
  `cover_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '封面图片URL',
  `teacher_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '教师姓名',
  `price` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '价格',
  `rating` decimal(2, 1) NULL DEFAULT 0.0 COMMENT '评分',
  `student_count` bigint NULL DEFAULT 0 COMMENT '学生人数',
  `type_id` bigint NOT NULL COMMENT '课程类型ID',
  `creator_id` bigint NOT NULL COMMENT '创建者ID',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `type_id`(`type_id` ASC) USING BTREE,
  INDEX `creator_id`(`creator_id` ASC) USING BTREE,
  CONSTRAINT `course_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `course_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `course_ibfk_2` FOREIGN KEY (`creator_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course
-- ----------------------------
INSERT INTO `course` VALUES (1, 'Java基础教程', 'Java是一门面向对象编程语言，不仅吸收了C++语言的各种优点，还摒弃了C++里难以理解的多继承、指针等概念，因此Java语言具有功能强大和简单易用两个特征。', 'https://example.com/java-cover.jpg', '张老师', 99.00, 4.8, 1200, 1, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course` VALUES (2, 'MySQL数据库实战', 'MySQL是一个关系型数据库管理系统，由瑞典MySQL AB公司开发，目前属于Oracle旗下产品。', 'https://example.com/mysql-cover.jpg', '李老师', 199.00, 4.6, 800, 2, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course` VALUES (3, 'Vue.js从入门到精通', 'Vue.js是一套用于构建用户界面的渐进式JavaScript框架。与其它大型框架不同的是，Vue被设计为可以自底向上逐层应用。', 'https://example.com/vue-cover.jpg', '王老师', 149.00, 4.7, 1500, 3, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course` VALUES (4, 'Spring Boot实战', 'Spring Boot是由Pivotal团队提供的全新框架，其设计目的是用来简化新Spring应用的初始搭建以及开发过程。', 'https://example.com/springboot-cover.jpg', '赵老师', 299.00, 4.9, 900, 4, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course` VALUES (5, 'Android开发详解', 'Android是一种基于Linux的自由及开放源代码的操作系统，主要使用于移动设备，如智能手机和平板电脑。', 'https://example.com/android-cover.jpg', '孙老师', 249.00, 4.5, 700, 5, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course` VALUES (6, '机器学习入门', '机器学习是人工智能的一个分支。人工智能的研究历史有着一条从以"推理"为重点，到以"知识"为重点，再到以"学习"为重点的自然、清晰的脉络。', 'https://example.com/ml-cover.jpg', '周老师', 399.00, 4.8, 1100, 6, 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course` VALUES (7, 'Python高级编程', '深入学习Python高级特性，包括装饰器、生成器、元类、并发编程等', 'https://example.com/python-advanced-cover.jpg', '陈老师', 159.00, 4.7, 1300, 1, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course` VALUES (8, 'PostgreSQL数据库优化', 'PostgreSQL数据库的高级使用技巧和性能优化方法', 'https://example.com/postgresql-cover.jpg', '刘老师', 189.00, 4.6, 750, 2, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course` VALUES (9, 'React全家桶实战', 'React核心概念及周边生态系统的综合应用', 'https://example.com/react-cover.jpg', '杨老师', 179.00, 4.8, 1600, 3, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course` VALUES (10, '微服务架构设计', '微服务架构的设计原则、实现方式和最佳实践', 'https://example.com/microservices-cover.jpg', '黄老师', 269.00, 4.9, 950, 4, 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');

-- ----------------------------
-- Table structure for course_chapter
-- ----------------------------
DROP TABLE IF EXISTS `course_chapter`;
CREATE TABLE `course_chapter`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '章集ID',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '章集标题',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '章集描述',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序字段',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  CONSTRAINT `course_chapter_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '章集表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_chapter
-- ----------------------------
INSERT INTO `course_chapter` VALUES (1, 'Java环境搭建', 'Java开发环境的安装和配置', 1, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (2, 'Java基础语法', 'Java基本语法结构和数据类型', 1, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (3, '面向对象编程', 'Java面向对象编程的概念和实现', 1, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (4, '集合框架', 'Java集合框架的使用和原理', 1, 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (5, '异常处理', 'Java异常处理机制和最佳实践', 1, 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (6, 'MySQL安装配置', 'MySQL数据库的安装和基本配置', 2, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (7, 'SQL基础语法', 'SQL语言的基础语法和常用操作', 2, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (8, '数据库设计', '数据库设计原则和范式理论', 2, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (9, '索引优化', 'MySQL索引的原理和优化技巧', 2, 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (10, '事务处理', 'MySQL事务处理机制和ACID特性', 2, 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (11, 'Vue基础概念', 'Vue.js框架的基本概念和核心特性', 3, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (12, '模板语法', 'Vue模板语法和指令的使用', 3, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (13, '组件化开发', 'Vue组件化开发的思想和实践', 3, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (14, '状态管理', 'Vuex状态管理模式和最佳实践', 3, 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (15, '路由管理', 'Vue Router路由管理的使用方法', 3, 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (16, 'Spring Boot入门', 'Spring Boot框架的基本概念和快速搭建', 4, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (17, '自动配置原理', 'Spring Boot自动配置机制的原理分析', 4, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (18, '数据访问', 'Spring Boot中数据访问层的实现', 4, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (19, '安全配置', 'Spring Security安全框架的集成和配置', 4, 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (20, '监控管理', 'Spring Boot Actuator监控管理功能', 4, 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (21, 'Android基础', 'Android开发环境搭建和基础知识', 5, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (22, '四大组件', 'Activity、Service、BroadcastReceiver、ContentProvider详解', 5, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (23, 'UI设计', 'Android用户界面设计和布局管理', 5, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (24, '数据存储', 'Android数据存储方案和最佳实践', 5, 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (25, '性能优化', 'Android应用性能优化技巧和工具', 5, 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (26, '机器学习基础', '机器学习的基本概念和发展历程', 6, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (27, '监督学习', '监督学习算法原理和应用场景', 6, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (28, '无监督学习', '无监督学习算法原理和应用场景', 6, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (29, '深度学习', '深度学习基础和神经网络原理', 6, 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (30, '实战案例', '机器学习在实际项目中的应用案例', 6, 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (31, '装饰器详解', 'Python装饰器的原理和高级用法', 7, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (32, '生成器和迭代器', '生成器和迭代器的概念及应用', 7, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (33, '上下文管理器', 'with语句和上下文管理器的实现', 7, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (34, '元类编程', 'Python元类的概念和使用场景', 7, 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (35, '并发编程', '多线程、多进程和异步编程', 7, 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (36, 'PostgreSQL基础', 'PostgreSQL数据库的基本特性和优势', 8, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (37, '高级查询', '复杂查询和性能优化技巧', 8, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (38, '索引策略', 'PostgreSQL索引类型和选择策略', 8, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (39, '分区表', '大表分区技术和最佳实践', 8, 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (40, '复制和高可用', '主从复制和高可用方案', 8, 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (41, 'React核心概念', 'React组件、状态和生命周期', 9, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (42, 'Hooks详解', 'React Hooks的使用和自定义Hooks', 9, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (43, '状态管理', 'Redux状态管理库的使用', 9, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (44, '路由管理', 'React Router的使用和配置', 9, 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (45, '性能优化', 'React应用性能优化技巧', 9, 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (46, '微服务概念', '微服务架构的基本概念和优势', 10, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (47, '服务拆分', '微服务拆分原则和实践', 10, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (48, '服务通信', '服务间通信方式和协议选择', 10, 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (49, '服务治理', '服务注册发现和负载均衡', 10, 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_chapter` VALUES (50, '分布式事务', '分布式事务处理方案和实现', 10, 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');

-- ----------------------------
-- Table structure for course_resource
-- ----------------------------
DROP TABLE IF EXISTS `course_resource`;
CREATE TABLE `course_resource`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `resource_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源名称',
  `resource_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '资源URL',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `chapter_id` bigint NULL DEFAULT NULL COMMENT '章集ID',
  `sort_order` int NOT NULL DEFAULT 0 COMMENT '排序字段',
  `type_id` bigint NOT NULL COMMENT '资源类型ID',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  INDEX `chapter_id`(`chapter_id` ASC) USING BTREE,
  INDEX `type_id`(`type_id` ASC) USING BTREE,
  CONSTRAINT `course_resource_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `course_resource_ibfk_2` FOREIGN KEY (`chapter_id`) REFERENCES `course_chapter` (`id`) ON DELETE SET NULL ON UPDATE RESTRICT,
  CONSTRAINT `course_resource_ibfk_3` FOREIGN KEY (`type_id`) REFERENCES `resource_type` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_resource
-- ----------------------------
INSERT INTO `course_resource` VALUES (1, 'Java基础语法讲解视频', 'https://example.com/java-basic.mp4', 1, 2, 1, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (2, 'Java面向对象编程文档', 'https://example.com/java-oop.pdf', 1, 3, 1, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (3, 'MySQL安装与配置视频', 'https://example.com/mysql-install.mp4', 2, 6, 1, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (4, 'MySQL索引优化文档', 'https://example.com/mysql-index.pdf', 2, 9, 1, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (5, 'Vue组件化开发视频', 'https://example.com/vue-component.mp4', 3, 13, 1, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (6, 'Vue状态管理文档', 'https://example.com/vue-state.pdf', 3, 14, 1, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (7, 'Spring Boot自动配置视频', 'https://example.com/springboot-autoconfig.mp4', 4, 17, 1, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (8, 'Spring Boot安全配置文档', 'https://example.com/springboot-security.pdf', 4, 19, 1, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (9, 'Android四大组件详解视频', 'https://example.com/android-components.mp4', 5, 22, 1, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (10, 'Android性能优化文档', 'https://example.com/android-performance.pdf', 5, 25, 1, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (11, '机器学习算法介绍视频', 'https://example.com/ml-algorithms.mp4', 6, 27, 1, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (12, '机器学习实战案例文档', 'https://example.com/ml-case.pdf', 6, 30, 1, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (13, 'Python装饰器视频教程', 'https://example.com/python-decorators-video.mp4', 7, 31, 1, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (14, 'PostgreSQL高级查询文档', 'https://example.com/postgresql-advanced-query.pdf', 8, 37, 1, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (15, 'React Hooks使用指南', 'https://example.com/react-hooks-guide.pdf', 9, 42, 1, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (16, '微服务拆分实践案例', 'https://example.com/microservices-decomposition-case.pdf', 10, 47, 1, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (17, 'Java环境搭建视频教程', 'https://example.com/java-setup-video.mp4', 1, 1, 1, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (18, 'JDK安装图文指南', 'https://example.com/jdk-install-guide.pdf', 1, 1, 2, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (19, 'SQL基础语法速查表', 'https://example.com/sql-cheat-sheet.pdf', 2, 7, 1, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (20, 'Vue模板语法练习项目', 'https://example.com/vue-template-exercise.zip', 3, 12, 1, 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (22, 'Java环境搭建视频教程2', 'https://example.com/java-setup-video.mp4', 1, 1, 2, 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_resource` VALUES (23, 'JDK安装图文指南2', 'https://example.com/jdk-install-guide.pdf', 1, 1, 3, 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');

-- ----------------------------
-- Table structure for course_type
-- ----------------------------
DROP TABLE IF EXISTS `course_type`;
CREATE TABLE `course_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '课程类型ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类型名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型描述',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '课程类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of course_type
-- ----------------------------
INSERT INTO `course_type` VALUES (1, '编程语言', '编程语言相关课程', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_type` VALUES (2, '数据库', '数据库相关课程', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_type` VALUES (3, '前端开发', '前端开发相关课程', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_type` VALUES (4, '后端开发', '后端开发相关课程', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_type` VALUES (5, '移动开发', '移动开发相关课程', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_type` VALUES (6, '人工智能', '人工智能相关课程', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_type` VALUES (7, '云计算', '云计算相关课程，包括AWS、Azure、阿里云等云服务平台的使用和架构设计', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_type` VALUES (8, 'DevOps', 'DevOps相关课程，包括CI/CD、容器化技术、自动化部署等', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_type` VALUES (9, '网络安全', '网络安全相关课程，包括信息安全、渗透测试、加密技术等', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `course_type` VALUES (10, '数据分析', '数据分析相关课程，包括大数据处理、数据挖掘、商业智能等', '2025-12-19 20:23:32', '2025-12-19 20:23:32');

-- ----------------------------
-- Table structure for favorite
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_user_course`(`user_id` ASC, `course_id` ASC) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  CONSTRAINT `favorite_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `favorite_ibfk_2` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of favorite
-- ----------------------------
INSERT INTO `favorite` VALUES (1, 2, 1, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (2, 2, 3, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (3, 3, 2, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (4, 3, 4, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (5, 4, 5, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (6, 4, 6, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (7, 5, 1, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (8, 5, 2, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (9, 5, 3, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (10, 5, 4, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (11, 5, 5, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (12, 5, 6, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (13, 5, 7, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (14, 5, 8, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (15, 5, 9, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (16, 5, 10, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (17, 6, 1, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (18, 6, 3, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (19, 6, 5, '2025-12-19 20:23:32');
INSERT INTO `favorite` VALUES (20, 6, 7, '2025-12-19 20:23:32');

-- ----------------------------
-- Table structure for learning_objective
-- ----------------------------
DROP TABLE IF EXISTS `learning_objective`;
CREATE TABLE `learning_objective`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '学习目标ID',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '学习目标内容',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  CONSTRAINT `learning_objective_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '学习目标表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of learning_objective
-- ----------------------------
INSERT INTO `learning_objective` VALUES (1, '掌握Java开发环境的搭建和配置', 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (2, '理解Java基本语法结构和数据类型', 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (3, '熟练运用Java面向对象编程思想', 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (4, '掌握Java集合框架的使用方法', 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (5, '理解Java异常处理机制', 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (6, '能够独立安装和配置MySQL数据库', 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (7, '熟练编写SQL语句进行数据操作', 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (8, '掌握数据库设计的基本原则', 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (9, '理解索引原理并能进行优化', 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (10, '掌握事务处理机制和ACID特性', 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (11, '理解Vue.js框架的核心概念', 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (12, '熟练使用Vue模板语法和指令', 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (13, '掌握组件化开发的思想和实践', 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (14, '能够使用Vuex进行状态管理', 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (15, '熟练配置和使用Vue Router', 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (16, '能够快速搭建Spring Boot项目', 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (17, '理解Spring Boot自动配置原理', 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (18, '掌握数据访问层的实现方式', 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (19, '能够集成Spring Security安全框架', 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `learning_objective` VALUES (20, '掌握应用监控和管理功能', 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');

-- ----------------------------
-- Table structure for resource_type
-- ----------------------------
DROP TABLE IF EXISTS `resource_type`;
CREATE TABLE `resource_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '资源类型ID',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源类型名称（视频、文档等）',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型描述',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '资源类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resource_type
-- ----------------------------
INSERT INTO `resource_type` VALUES (1, '视频', '课程视频资源', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `resource_type` VALUES (2, '文档', '课程文档资源', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `resource_type` VALUES (5, '代码示例', '课程中的代码示例和演示项目', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `resource_type` VALUES (6, '图表', '课程中的图表、流程图等可视化材料', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `resource_type` VALUES (7, '参考文献', '课程推荐阅读的书籍、文章等参考资料', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `resource_type` VALUES (8, '工具软件', '课程推荐使用的工具软件和插件', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `resource_type` VALUES (9, '模板', '项目模板、代码模板等可复用资源', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `resource_type` VALUES (10, '案例研究', '实际项目案例分析和研究报告', '2025-12-19 20:23:32', '2025-12-19 20:23:32');

-- ----------------------------
-- Table structure for target_audience
-- ----------------------------
DROP TABLE IF EXISTS `target_audience`;
CREATE TABLE `target_audience`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '适合人群ID',
  `content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '适合人群描述',
  `course_id` bigint NOT NULL COMMENT '课程ID',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `course_id`(`course_id` ASC) USING BTREE,
  CONSTRAINT `target_audience_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `course` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '适合人群表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of target_audience
-- ----------------------------
INSERT INTO `target_audience` VALUES (1, '零基础想要学习Java编程的初学者', 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (2, '有一定编程基础想要转学Java的开发者', 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (3, '准备参加Java相关面试的求职者', 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (4, '想要巩固Java基础知识的开发者', 1, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (5, '希望掌握数据库技能的开发者', 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (6, '准备从事数据库相关工作的求职者', 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (7, '想要提升数据库优化能力的DBA', 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (8, '对数据库设计感兴趣的架构师', 2, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (9, '前端开发工程师', 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (10, '想要学习Vue框架的开发者', 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (11, '准备从事前端开发的求职者', 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (12, '希望提升前端技能的产品经理', 3, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (13, 'Java后端开发工程师', 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (14, '想要学习微服务架构的开发者', 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (15, '准备从事企业级开发的求职者', 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (16, '对Spring生态系统感兴趣的技术人员', 4, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (17, '移动端开发工程师', 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (18, '想要进入移动开发领域的开发者', 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (19, '准备从事Android开发的求职者', 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `target_audience` VALUES (20, '对移动应用开发感兴趣的产品经理', 5, '2025-12-19 20:23:32', '2025-12-19 20:23:32');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `nickname` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `avatar` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '头像URL',
  `created_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE,
  UNIQUE INDEX `email`(`email` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', '管理员2', '358062a42d1dfa8c927092019ef02458', 'admin2@example.com', NULL, '2025-12-19 20:23:32', '2025-12-22 18:53:15');
INSERT INTO `user` VALUES (2, 'zhangsan', '张三', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'zhangsan@example.com', 'https://example.com/zhangsan-avatar.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (3, 'lisi', '李四', 'f45de51a19327b0bf4e55bc73809da5918a65f0e4d2e2d0b6e6d8e2f5a1d0a1e', 'lisi@example.com', 'https://example.com/lisi-avatar.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (4, 'wangwu', '王五', 'e1a2b3c4d5e6f789012345678901234567890123456789012345678901234567', 'wangwu@example.com', 'https://example.com/wangwu-avatar.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (5, 'user001', '用户001', 'e10adc3949ba59abbe56e057f20f883e', 'user001@example.com', 'https://example.com/avatar001.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (6, 'user002', '用户002', 'e10adc3949ba59abbe56e057f20f883e', 'user002@example.com', 'https://example.com/avatar002.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (7, 'user003', '用户003', 'e10adc3949ba59abbe56e057f20f883e', 'user003@example.com', 'https://example.com/avatar003.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (8, 'user004', '用户004', 'e10adc3949ba59abbe56e057f20f883e', 'user004@example.com', 'https://example.com/avatar004.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (9, 'user005', '用户005', 'e10adc3949ba59abbe56e057f20f883e', 'user005@example.com', 'https://example.com/avatar005.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (10, 'user006', '用户006', 'e10adc3949ba59abbe56e057f20f883e', 'user006@example.com', 'https://example.com/avatar006.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (11, 'user007', '用户007', 'e10adc3949ba59abbe56e057f20f883e', 'user007@example.com', 'https://example.com/avatar007.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (12, 'user008', '用户008', 'e10adc3949ba59abbe56e057f20f883e', 'user008@example.com', 'https://example.com/avatar008.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (13, 'user009', '用户009', 'e10adc3949ba59abbe56e057f20f883e', 'user009@example.com', 'https://example.com/avatar009.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (14, 'user010', '用户010', 'e10adc3949ba59abbe56e057f20f883e', 'user010@example.com', 'https://example.com/avatar010.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (15, 'user011', '用户011', 'e10adc3949ba59abbe56e057f20f883e', 'user011@example.com', 'https://example.com/avatar011.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (16, 'user012', '用户012', 'e10adc3949ba59abbe56e057f20f883e', 'user012@example.com', 'https://example.com/avatar012.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (17, 'user013', '用户013', 'e10adc3949ba59abbe56e057f20f883e', 'user013@example.com', 'https://example.com/avatar013.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (18, 'user014', '用户014', 'e10adc3949ba59abbe56e057f20f883e', 'user014@example.com', 'https://example.com/avatar014.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (19, 'user015', '用户015', 'e10adc3949ba59abbe56e057f20f883e', 'user015@example.com', 'https://example.com/avatar015.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');
INSERT INTO `user` VALUES (20, 'user016', '用户016', 'e10adc3949ba59abbe56e057f20f883e', 'user016@example.com', 'https://example.com/avatar016.jpg', '2025-12-19 20:23:32', '2025-12-19 20:23:32');

SET FOREIGN_KEY_CHECKS = 1;