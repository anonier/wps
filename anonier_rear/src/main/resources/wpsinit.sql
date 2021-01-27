SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wps_file
-- ----------------------------
DROP TABLE IF EXISTS `wps_file`;
CREATE TABLE `wps_file`
(
    `id`           int(10)                                                       NOT NULL AUTO_INCREMENT,
    `parnter_key`  varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `file_id`       varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `name`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `version`      tinyint(4)                                                    NULL     DEFAULT NULL,
    `size`         varchar(20)                                                   NULL     DEFAULT NULL,
    `download_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_time`  timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP NULL DEFAULT NULL,
    `update_name`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `update_time`  timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for wps_user
-- ----------------------------
DROP TABLE IF EXISTS `wps_user`;
CREATE TABLE `wps_user`
(
    `id`          int(10)                                                       NOT NULL AUTO_INCREMENT,
    `wps_file_id` varchar(32)                                                   NULL DEFAULT NULL,
    `user_id`     varchar(32)                                                   NULL DEFAULT NULL,
    `name`        varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL DEFAULT NULL,
    `upload_url`  varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Compact;

-- ----------------------------
-- Records of w_user_t
-- ----------------------------
INSERT INTO `wps_user`
VALUES ('1', null, null, '张三', 'http://47.103.207.193:8081/v1/3rd/file/save');
INSERT INTO `wps_user`
VALUES ('2', null, null, '李四', 'http://47.103.207.193:8081/v1/3rd/file/save');
INSERT INTO `wps_user`
VALUES ('3', null, null, '赵五', 'http://47.103.207.193:8081/v1/3rd/file/save');

-- ----------------------------
-- Table structure for wps_partner
-- ----------------------------
DROP TABLE IF EXISTS `wps_partner`;
CREATE TABLE `wps_partner`
(
    `id`          int(10)                                                       NOT NULL AUTO_INCREMENT,
    `parnter_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `name`        varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `secret`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `api_url`     varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `api_key`     varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `api_secret`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `create_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP NULL DEFAULT NULL,
    `update_time` timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP NULL DEFAULT NULL,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;