/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80400
 Source Host           : localhost:3306
 Source Schema         : img_viewer

 Target Server Type    : MySQL
 Target Server Version : 80400
 File Encoding         : 65001

 Date: 18/09/2025 21:12:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for captcha_code
-- ----------------------------
DROP TABLE IF EXISTS `captcha_code`;
CREATE TABLE `captcha_code`  (
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `captcha_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`email`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of captcha_code
-- ----------------------------

-- ----------------------------
-- Table structure for category_id_category_name
-- ----------------------------
DROP TABLE IF EXISTS `category_id_category_name`;
CREATE TABLE `category_id_category_name`  (
  `category_id` int(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`category_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category_id_category_name
-- ----------------------------
INSERT INTO `category_id_category_name` VALUES (1, '自然');
INSERT INTO `category_id_category_name` VALUES (2, '城市');
INSERT INTO `category_id_category_name` VALUES (3, '建筑');
INSERT INTO `category_id_category_name` VALUES (4, '太空');
INSERT INTO `category_id_category_name` VALUES (5, '室内');
INSERT INTO `category_id_category_name` VALUES (6, '行业应用');
INSERT INTO `category_id_category_name` VALUES (7, '其他');

-- ----------------------------
-- Table structure for config_item
-- ----------------------------
DROP TABLE IF EXISTS `config_item`;
CREATE TABLE `config_item`  (
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of config_item
-- ----------------------------
INSERT INTO `config_item` VALUES ('image_delete_type', 'TRUE');
INSERT INTO `config_item` VALUES ('image_delete_type_desp', '此条目用于配置用户删除图像时是否真实删除数据库记录条目。当配置为`TRUE`时，会删除数据库图像记录及本地对应图像文件；当配置为`FALSE`时，仅将图像权限设置为私有，并删除用户与图像的关联记录。');
INSERT INTO `config_item` VALUES ('jpeg_quality', '100');
INSERT INTO `config_item` VALUES ('jpeg_quality_desp', '此条目用于配置处理上传图像时，输出图像的质量。若非追求高质量，默认值设置为`85`即可。');
INSERT INTO `config_item` VALUES ('jpeg_subsamp', '422');
INSERT INTO `config_item` VALUES ('jpeg_subsamp_desp', '此条目用于配置图像下采样的方式。可设置为`444`、`422`、`420`、`411`，质量依次递减。');
INSERT INTO `config_item` VALUES ('krpano_home', 'D:\\Program Files\\krpano-1.19-pr15\\');
INSERT INTO `config_item` VALUES ('krpano_home_desp', '此条目用于配置全景图处理软件krpano的根目录。请将值设置为krpano软件所在的绝对路径。');
INSERT INTO `config_item` VALUES ('platform_name', 'WIN64');
INSERT INTO `config_item` VALUES ('platform_name_desp', '此条目用于配置部署平台类型，可选值有`WIN32`、`WIN64`、`LINUX`。');
INSERT INTO `config_item` VALUES ('preserve_original_file', 'FALSE');
INSERT INTO `config_item` VALUES ('preserve_original_file_desp', '此条目用于配置是否保留原始图像文件，参数值可以为`TRUE`或`FALSE`。');
INSERT INTO `config_item` VALUES ('resource_path', 'D:/ImageViewer/resources/');
INSERT INTO `config_item` VALUES ('resource_path_desp', '已废弃！deprecated!此条目用于配置图像资源路径。请将值设置为全景图pano资源目录的绝对路径。该值通常与`upload_path`相同。请在启动项目时，在jar文件后添加指定目录参数。');
INSERT INTO `config_item` VALUES ('upload_path', 'D:/ImageViewer/resources/uploadfile/');
INSERT INTO `config_item` VALUES ('upload_path_desp', '此条目用于配置图像上传路径。请将值设置为全景图pano上传目录的绝对路径。该值通常为`resource_path`+uploadfile/。');

-- ----------------------------
-- Table structure for image
-- ----------------------------
DROP TABLE IF EXISTS `image`;
CREATE TABLE `image`  (
  `image_id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
  `image_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `type_id` int(0) NULL DEFAULT NULL,
  `public` int(0) NULL DEFAULT NULL,
  `category_id` int(0) UNSIGNED NULL DEFAULT NULL,
  `original_filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `gps` json NULL,
  `url` varchar(1023) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `oss` varchar(1023) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`image_id`) USING BTREE,
  INDEX `type_id`(`type_id`) USING BTREE,
  INDEX `category_id`(`category_id`) USING BTREE,
  CONSTRAINT `image_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `type_id_type_name` (`type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `image_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `category_id_category_name` (`category_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 507 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of image
-- ----------------------------
INSERT INTO `image` VALUES (1, '测试图片1', '测试图片1的描述', 1, 1, 2, 'demo.JPG', '{\"gpsInfoDms\": {\"altitude\": [\"500.00\"], \"latitude\": [\"29\", \"35\", \"50.00\"], \"longitude\": [\"106\", \"18\", \" 2.50\"]}, \"gpsInfoRaw\": {\"altitude\": [\"500.00\"], \"latitude\": [\"29/1\", \"35/1\", \"5000/100\"], \"longitude\": [\"106/1\", \"18/1\", \"250/100\"]}, \"gpsInfoDouble\": {\"altitude\": 500.00, \"latitude\": 29.60705888888889, \"longitude\": 106.90065993333334}}', NULL, NULL);
-- ----------------------------
-- Table structure for type_id_type_name
-- ----------------------------
DROP TABLE IF EXISTS `type_id_type_name`;
CREATE TABLE `type_id_type_name`  (
  `type_id` int(0) NOT NULL,
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of type_id_type_name
-- ----------------------------
INSERT INTO `type_id_type_name` VALUES (1, '全景图');
INSERT INTO `type_id_type_name` VALUES (2, '普通');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `role_id` int(0) NULL DEFAULT NULL,
  PRIMARY KEY (`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('admin', 'admin', 'test@test.test', 0);
INSERT INTO `user` VALUES ('test', 'test', 'test@test.test', 2);

-- ----------------------------
-- Table structure for username_image_id
-- ----------------------------
DROP TABLE IF EXISTS `username_image_id`;
CREATE TABLE `username_image_id`  (
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `image_id` bigint(0) UNSIGNED NOT NULL,
  PRIMARY KEY (`username`, `image_id`) USING BTREE,
  INDEX `image_id`(`image_id`) USING BTREE,
  CONSTRAINT `username_image_id_ibfk_1` FOREIGN KEY (`username`) REFERENCES `user` (`username`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `username_image_id_ibfk_2` FOREIGN KEY (`image_id`) REFERENCES `image` (`image_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of username_image_id
-- ----------------------------
INSERT INTO `username_image_id` VALUES ('admin', 1);

SET FOREIGN_KEY_CHECKS = 1;
