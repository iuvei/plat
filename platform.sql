/*
Navicat MySQL Data Transfer

Source Server         : 192.168.0.219
Source Server Version : 50718
Source Host           : 192.168.0.219:3306
Source Database       : platform

Target Server Type    : MYSQL
Target Server Version : 50718
File Encoding         : 65001

Date: 2018-02-02 10:22:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for account_sync_record
-- ----------------------------
DROP TABLE IF EXISTS `account_sync_record`;
CREATE TABLE `account_sync_record` (
  `user_id` int(11) NOT NULL COMMENT '用户主键',
  `id` bigint(20) DEFAULT NULL COMMENT '流水编号',
  `sync_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '同步时间',
  KEY `id_Idx` (`id`) USING BTREE,
  KEY `time_Idx` (`sync_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台用户账单同步记录';

-- ----------------------------
-- Table structure for platform_sync_record
-- ----------------------------
DROP TABLE IF EXISTS `platform_sync_record`;
CREATE TABLE `platform_sync_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `start_time` char(14) DEFAULT NULL COMMENT '同步开始时间yyyyMMddHHmmss',
  `end_time` char(14) NOT NULL COMMENT '同步结束时间yyyyMMddHHmmss',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='同步记录';

-- ----------------------------
-- Table structure for platform_user_adapter
-- ----------------------------
DROP TABLE IF EXISTS `platform_user_adapter`;
CREATE TABLE `platform_user_adapter` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `type` tinyint(4) NOT NULL COMMENT '用户类型：1、线路商 2、商户 3、会员 4、子账号',
  `platform_user_id` varchar(36) NOT NULL COMMENT '平台用户ID',
  `live_user_id` bigint(11) NOT NULL COMMENT '真人用户ID',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后修改时间',
  `platform_user_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `platform_parent_id` varchar(36) DEFAULT NULL COMMENT '平台用户的上级ID',
  PRIMARY KEY (`id`),
  KEY `type` (`type`),
  KEY `platform_user_id` (`platform_user_id`),
  KEY `platform_parent_id` (`platform_parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=273 DEFAULT CHARSET=utf8mb4 COMMENT='平台用户适配表';

-- ----------------------------
-- Table structure for platform_user_login
-- ----------------------------
DROP TABLE IF EXISTS `platform_user_login`;
CREATE TABLE `platform_user_login` (
  `user_id` int(11) NOT NULL COMMENT '用户主键',
  `login_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台用户登录记录';

-- ----------------------------
-- Table structure for sync_betorder_fail_record
-- ----------------------------
DROP TABLE IF EXISTS `sync_betorder_fail_record`;
CREATE TABLE `sync_betorder_fail_record` (
  `round_id` bigint(30) unsigned NOT NULL COMMENT '局ID',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='同步订单失败记录';
