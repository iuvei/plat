alter table a_user_info  add COLUMN relaflag CHAR(1) DEFAULT '0' COMMENT '是否有关联账号  0:无  1:有';
alter table a_user_info  add COLUMN relaaccount VARCHAR(1024) DEFAULT NULL  COMMENT '关联账号';
alter table a_user_info  add COLUMN iparea VARCHAR(64) DEFAULT NULL  COMMENT 'IP区域';
alter table a_user_info  add COLUMN phonearea VARCHAR(64) DEFAULT NULL  COMMENT '电话区域';
alter table a_user_info  add COLUMN  regdevice varchar(1024) DEFAULT NULL COMMENT '注册客户端信息';

alter table user_login_log  add COLUMN iparea VARCHAR(64) COMMENT 'IP区域';

CREATE TABLE `a_pay_order_log` (
  `coid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uiid` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `orderid` varchar(64) DEFAULT NULL COMMENT '订单ID',
  `type` int(11) DEFAULT NULL COMMENT '类型(0:存款 1:提款 2:赠送优惠 3:扣款  9:转账)',
  `amount` int(11) DEFAULT NULL COMMENT '金额',
  `walletlog` varchar(64) DEFAULT NULL COMMENT '钱包日志',
  `gamelog` varchar(64) DEFAULT NULL COMMENT '第三方日志',
  `remark` varchar(256) DEFAULT NULL COMMENT '审核描述',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间（和原订单更新时间一致）',
  PRIMARY KEY (`coid`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;


-- 2016.02.01
UPDATE a_web_ad set adtitle ='老虎机20%次次存，次次送，奖金高达8888元' where adimages='2wan_1_18233e.jpg';

ALTER TABLE a_user_info MODIFY COLUMN relaaccount text COMMENT '关联账号';
