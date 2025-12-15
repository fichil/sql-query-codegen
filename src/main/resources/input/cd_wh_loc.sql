/*
 Navicat Premium Dump SQL

 Source Server         : fks-测试
 Source Server Type    : MySQL
 Source Server Version : 50738 (5.7.38-log)
 Source Host           : 192.168.4.104:3306
 Source Schema         : fks_wmstms_dev

 Target Server Type    : MySQL
 Target Server Version : 50738 (5.7.38-log)
 File Encoding         : 65001

 Date: 15/12/2025 11:39:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cd_wh_loc
-- ----------------------------
DROP TABLE IF EXISTS `cd_wh_loc`;
CREATE TABLE `cd_wh_loc`  (
  `JOB_ID` int(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `PROJECT_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '项目代号',
  `LOC_CODE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '库位编码',
  `ZONE_CODE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库区编码',
  `STATUS` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态（正常、冻结）',
  `IS_ENABLE` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'Y' COMMENT '是否启用',
  `CATEGORY` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库位种类（如，重力式货架、驶入式货架）',
  `LOC_USE_TYPE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库位使用类型（RS、CS、EA、PC、QC、ST、SS、KT、CD）',
  `LOC_PK_TYPE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库位拣货类型（固定拣货位FPL、动态拣货位DPL）',
  `PA_SEQ` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上架顺序',
  `PK_SEQ` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '拣货顺序',
  `ABC` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库位ABC',
  `LENGTH` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '长',
  `WIDTH` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '宽',
  `HEIGHT` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '高',
  `LANE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '通道',
  `SEQ` decimal(65, 0) NULL DEFAULT NULL COMMENT '序号',
  `FLOOR` decimal(65, 0) NULL DEFAULT NULL COMMENT '层',
  `LOC_GROUP` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '库位组',
  `IS_MIX_SKU` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'Y' COMMENT '是否允许混商品',
  `MAX_MIX_SKU` decimal(65, 8) NULL DEFAULT NULL COMMENT '最大混商品数量',
  `IS_MIX_LOT` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'Y' COMMENT '是否允许混批次',
  `MAX_MIX_LOT` decimal(65, 8) NULL DEFAULT NULL COMMENT '最大混批次数量',
  `IS_LOSE_ID` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'Y' COMMENT '是否忽略ID',
  `MAX_WEIGHT` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '最大重量',
  `MAX_CUBIC` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '最大体积',
  `MAX_PL` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT '最大托盘数',
  `X` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT 'X坐标',
  `Y` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT 'Y坐标',
  `Z` decimal(18, 8) NOT NULL DEFAULT 0.00000000 COMMENT 'Z坐标',
  `IS_RESERVE` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'N' COMMENT '是否预定',
  `DEF1` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自定义1',
  `DEF2` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自定义2',
  `DEF3` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自定义3',
  `DEF4` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自定义4',
  `DEF5` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自定义5',
  `DEF6` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自定义6',
  `DEF7` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自定义7',
  `DEF8` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自定义8',
  `DEF9` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自定义9',
  `DEF10` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '自定义10',
  `REMARK` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '备注',
  `REC_VER` decimal(12, 0) NOT NULL COMMENT '版本号',
  `CREATOR` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '创建人',
  `CREATE_TIME` datetime NOT NULL COMMENT '创建时间',
  `MODIFIER` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '修改人',
  `MODIFY_TIME` datetime NOT NULL COMMENT '修改时间',
  `REC_STATUS` decimal(1, 0) NOT NULL DEFAULT 0 COMMENT '逻辑删除,0正常,1删除',
  `TIME_ZONE` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '时区',
  `ORG_ID` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分公司',
  `OWNER_CODE` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '货主编码',
  `OWNER_NAME` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '货主名称',
  PRIMARY KEY (`JOB_ID`) USING BTREE,
  INDEX `IDX_CWL_ZONE_CODE`(`ZONE_CODE`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4746 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '库位表' ROW_FORMAT = COMPACT;

SET FOREIGN_KEY_CHECKS = 1;
