-- 数据库初始化脚本

-- 创建数据库
CREATE DATABASE seckill;

-- 使用数据库
use seckill;

-- 创建秒杀库存表

CREATE TABLE seckill(
  `seckill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `name` VARCHAR(120) NOT NULL COMMENT '商品的名称',
  `number` int NOT NULL COMMENT '库存的数量',
  `create_time` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `start_time` TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
  `end_time` TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
  PRIMARY KEY (seckill_id),
  KEY idx_start_time(start_time),
  KEY idx_end_time(end_time),
  KEY idx_create_time(create_time)

)ENGINE = InnoDB AUTO_INCREMENT = 1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';

-- 初始化数据

INSERT INTO
  seckill(name, number, start_time, end_time)
VALUES
  ('1000元秒杀iphone7',100,'2017-07-07 00:00:00','2017-07-08 00:00:00'),
  ('800元秒杀ipad2',100,'2017-07-07 00:00:00','2017-07-08 00:00:00'),
  ('500元秒杀小米4',100,'2017-07-07 00:00:00','2017-07-08 00:00:00'),
  ('200元秒杀红米4',100,'2017-07-07 00:00:00','2017-07-08 00:00:00');


-- 秒杀成功明细表
CREATE TABLE success_killed(
  `seckill_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商品库存ID',
  `user_phone` BIGINT NOT NULL COMMENT '用户的手机号',
  `state` TINYINT NOT NULL DEFAULT -1 COMMENT '-1:无效  0:成功',
  `create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
  PRIMARY KEY (seckill_id,user_phone),
  KEY idx_create_time(create_time)
)ENGINE = InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表'
