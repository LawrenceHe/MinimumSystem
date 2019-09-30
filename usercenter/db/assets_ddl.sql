--  用户资产表  --
DROP TABLE IF EXISTS assets;
CREATE TABLE assets
(
    id                      BIGINT PRIMARY KEY COMMENT '用户id',
    fortune                 BIGINT COMMENT '财富',
    loan                    BIGINT COMMENT '贷款'
) COMMENT '用户资产表';

INSERT INTO assets VALUES(364104831335804928, 1000000, 0);