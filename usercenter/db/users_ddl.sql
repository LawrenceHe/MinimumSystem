--  用户表
DROP TABLE IF EXISTS users;
CREATE TABLE users
(
  id                      BIGINT PRIMARY KEY COMMENT '用户id',
  username                VARCHAR(100) COMMENT '用户名（手机号）',
  password                VARCHAR(100) COMMENT '用户密码密文',
  enabled                 BOOLEAN DEFAULT TRUE COMMENT '是否有效用户',
  account_non_expired     BOOLEAN DEFAULT TRUE COMMENT '账号是否未过期',
  credentials_non_expired BOOLEAN DEFAULT TRUE COMMENT '密码是否未过期',
  account_non_locked      BOOLEAN DEFAULT TRUE COMMENT '是否未锁定',
  created_time            DATETIME     NOT NULL DEFAULT now() COMMENT '创建时间',
  updated_time            DATETIME     NOT NULL DEFAULT now() COMMENT '更新时间',
  created_by              VARCHAR(100) NOT NULL DEFAULT 'system' COMMENT '创建人',
  updated_by              VARCHAR(100) NOT NULL DEFAULT 'system' COMMENT '更新人'
) COMMENT '用户表';
CREATE UNIQUE INDEX ux_users_username
  ON users (username);
