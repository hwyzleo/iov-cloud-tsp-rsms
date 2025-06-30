DROP TABLE IF EXISTS `db_rsms`.`tb_server_platform`;
CREATE TABLE `db_rsms`.`tb_server_platform`
(
    `id`                BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`              VARCHAR(20)  NOT NULL COMMENT '平台代码',
    `name`              VARCHAR(255) NOT NULL COMMENT '平台名称',
    `type`              SMALLINT     NOT NULL COMMENT '平台类型：1-国标，2-地标，3-企标',
    `url`               VARCHAR(255) NOT NULL COMMENT '平台地址',
    `port`              SMALLINT     NOT NULL COMMENT '平台端口',
    `protocol`          VARCHAR(50)  NOT NULL COMMENT '平台协议',
    `collect_frequency` SMALLINT     NOT NULL COMMENT '采集频率',
    `report_frequency`  SMALLINT     NOT NULL COMMENT '上报频率',
    `read_write_sync`   TINYINT      NOT NULL COMMENT '是否读写同步',
    `heartbeat`         TINYINT      NOT NULL COMMENT '是否维持心跳',
    `encrypt_type`      SMALLINT     NOT NULL COMMENT '数据加密方式',
    `encrypt_key`       VARCHAR(255)          DEFAULT NULL COMMENT '数据加密KEY',
    `description`       VARCHAR(255)          DEFAULT NULL COMMENT '备注',
    `create_time`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`         VARCHAR(64)           DEFAULT NULL COMMENT '创建者',
    `modify_time`       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`         VARCHAR(64)           DEFAULT NULL COMMENT '修改者',
    `row_version`       INT                   DEFAULT 1 COMMENT '记录版本',
    `row_valid`         TINYINT               DEFAULT 1 COMMENT '记录是否有效',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='服务端平台';

DROP TABLE IF EXISTS `db_rsms`.`tb_client_platform`;
CREATE TABLE `db_rsms`.`tb_client_platform`
(
    `id`                   BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `server_platform_code` VARCHAR(20)  NOT NULL COMMENT '服务端平台代码',
    `unique_code`          VARCHAR(255) NOT NULL COMMENT '唯一识别码',
    `username`             VARCHAR(255) NOT NULL COMMENT '用户名',
    `password`             VARCHAR(255) NOT NULL COMMENT '密码',
    `hostname`             VARCHAR(255)          DEFAULT NULL COMMENT '绑定主机名',
    `enable`               TINYINT      NOT NULL COMMENT '是否启用',
    `description`          VARCHAR(255)          DEFAULT NULL COMMENT '备注',
    `create_time`          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`            VARCHAR(64)           DEFAULT NULL COMMENT '创建者',
    `modify_time`          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`            VARCHAR(64)           DEFAULT NULL COMMENT '修改者',
    `row_version`          INT                   DEFAULT 1 COMMENT '记录版本',
    `row_valid`            TINYINT               DEFAULT 1 COMMENT '记录是否有效',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='客户端平台';

DROP TABLE IF EXISTS `db_rsms`.`tb_client_platform_login_history`;
CREATE TABLE `db_rsms`.`tb_client_platform_login_history`
(
    `id`                 BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `client_platform_id` BIGINT    NOT NULL COMMENT '客户端平台主键',
    `login_time`         TIMESTAMP          DEFAULT NULL COMMENT '登录时间',
    `login_sn`           INT                DEFAULT NULL COMMENT '登录流水号',
    `login_result`       TINYINT            DEFAULT NULL COMMENT '登录结果',
    `failure_reason`     SMALLINT           DEFAULT NULL COMMENT '登录失败原因：1-无应答，2-错误应答',
    `failure_count`      SMALLINT           DEFAULT NULL COMMENT '连续登录失败次数',
    `logout_time`        TIMESTAMP          DEFAULT NULL COMMENT '登出时间',
    `description`        VARCHAR(255)       DEFAULT NULL COMMENT '备注',
    `create_time`        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`          VARCHAR(64)        DEFAULT NULL COMMENT '创建者',
    `modify_time`        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`          VARCHAR(64)        DEFAULT NULL COMMENT '修改者',
    `row_version`        INT                DEFAULT 1 COMMENT '记录版本',
    `row_valid`          TINYINT            DEFAULT 1 COMMENT '记录是否有效',
    PRIMARY KEY (`id`),
    INDEX `idx_client_platform` (`client_platform_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='客户端平台登录历史';