ALTER table a_game_transfer ADD COLUMN `origamount` decimal(10,2) COMMENT '转账前钱包余额';
ALTER table a_game_transfer ADD COLUMN `otherbefore` varchar(32) COMMENT '转账前第三方余额';
ALTER table a_game_transfer ADD COLUMN `otherafter` varchar(32) COMMENT '转账后第三方余额';

ALTER table a_pay_platform ADD COLUMN `sequence` int(11) COMMENT '排序 值越小优先级越高';
-- add by sunshine 2016.02.25
alter table a_user_info add COLUMN weekrake char(2) DEFAULT '1' COMMENT '周返水开关 0:关闭 1:打开';