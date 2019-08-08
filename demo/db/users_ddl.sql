--  用户表
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
  id                      BIGINT PRIMARY KEY COMMENT '用户id',
  username                VARCHAR(100) COMMENT '用户名',
  password                VARCHAR(100) COMMENT '用户密码密文',
  mobile                  VARCHAR(20) COMMENT '用户手机',
  enabled                 BOOLEAN COMMENT '是否有效用户',
  created_time            DATETIME     NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time            DATETIME     NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by              VARCHAR(100) NOT NULL COMMENT '创建人',
  updated_by              VARCHAR(100) NOT NULL COMMENT '更新人'
) COMMENT '用户表';
CREATE UNIQUE INDEX ux_users_username
  ON users (username);
CREATE UNIQUE INDEX ux_users_mobile
  ON users (mobile);
