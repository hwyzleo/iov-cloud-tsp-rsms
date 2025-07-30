DROP TABLE IF EXISTS `db_rsms`.`tb_server_platform`;
CREATE TABLE `db_rsms`.`tb_server_platform`
(
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `code`            VARCHAR(20)  NOT NULL COMMENT '平台代码',
    `name`            VARCHAR(255) NOT NULL COMMENT '平台名称',
    `type`            SMALLINT     NOT NULL COMMENT '平台类型：1-国标，2-地标，3-企标',
    `url`             VARCHAR(255) NOT NULL COMMENT '平台地址',
    `port`            SMALLINT     NOT NULL COMMENT '平台端口',
    `protocol`        VARCHAR(50)  NOT NULL COMMENT '平台协议',
    `read_write_sync` TINYINT      NOT NULL COMMENT '是否读写同步',
    `heartbeat`       TINYINT      NOT NULL COMMENT '是否维持心跳',
    `description`     VARCHAR(255)          DEFAULT NULL COMMENT '备注',
    `create_time`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`       VARCHAR(64)           DEFAULT NULL COMMENT '创建者',
    `modify_time`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`       VARCHAR(64)           DEFAULT NULL COMMENT '修改者',
    `row_version`     INT                   DEFAULT 1 COMMENT '记录版本',
    `row_valid`       TINYINT               DEFAULT 1 COMMENT '记录是否有效',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='服务端平台';

DROP TABLE IF EXISTS `db_rsms`.`tb_client_platform`;
CREATE TABLE `db_rsms`.`tb_client_platform`
(
    `id`                   BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `server_platform_code` VARCHAR(20)  NOT NULL COMMENT '服务端平台代码',
    `collect_frequency`    SMALLINT     NOT NULL COMMENT '采集频率',
    `report_frequency`     SMALLINT     NOT NULL COMMENT '上报频率',
    `unique_code`          VARCHAR(255) NOT NULL COMMENT '企业唯一识别码',
    `encrypt_type`         SMALLINT     NOT NULL COMMENT '数据加密方式',
    `encrypt_key`          VARCHAR(255)          DEFAULT NULL COMMENT '数据加密KEY',
    `enable`               TINYINT      NOT NULL COMMENT '是否启用',
    `hostname`             VARCHAR(255)          DEFAULT NULL COMMENT '绑定主机名',
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

DROP TABLE IF EXISTS `db_rsms`.`tb_client_platform_account`;
CREATE TABLE `db_rsms`.`tb_client_platform_account`
(
    `id`                 BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `client_platform_id` BIGINT       NOT NULL COMMENT '客户端平台ID',
    `username`           VARCHAR(255) NOT NULL COMMENT '用户名',
    `password`           VARCHAR(255) NOT NULL COMMENT '密码',
    `use_limit`          SMALLINT     NOT NULL COMMENT '使用上限：0-无限制',
    `enable`             TINYINT      NOT NULL COMMENT '是否启用',
    `sort`               INT                   DEFAULT 99 COMMENT '排序',
    `description`        VARCHAR(255)          DEFAULT NULL COMMENT '备注',
    `create_time`        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`          VARCHAR(64)           DEFAULT NULL COMMENT '创建者',
    `modify_time`        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`          VARCHAR(64)           DEFAULT NULL COMMENT '修改者',
    `row_version`        INT                   DEFAULT 1 COMMENT '记录版本',
    `row_valid`          TINYINT               DEFAULT 1 COMMENT '记录是否有效',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='客户端平台账号';

DROP TABLE IF EXISTS `db_rsms`.`tb_client_platform_login_history`;
CREATE TABLE `db_rsms`.`tb_client_platform_login_history`
(
    `id`                 BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `client_platform_id` BIGINT       NOT NULL COMMENT '客户端平台主键',
    `username`           VARCHAR(255) NOT NULL COMMENT '用户名',
    `hostname`           VARCHAR(255)          DEFAULT NULL COMMENT '主机名',
    `login_time`         TIMESTAMP             DEFAULT NULL COMMENT '登录时间',
    `login_sn`           INT                   DEFAULT NULL COMMENT '登录流水号',
    `login_result`       TINYINT               DEFAULT NULL COMMENT '登录结果',
    `failure_reason`     SMALLINT              DEFAULT NULL COMMENT '登录失败原因：1-无应答，2-错误应答',
    `failure_count`      SMALLINT              DEFAULT NULL COMMENT '连续登录失败次数',
    `logout_time`        TIMESTAMP             DEFAULT NULL COMMENT '登出时间',
    `description`        VARCHAR(255)          DEFAULT NULL COMMENT '备注',
    `create_time`        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`          VARCHAR(64)           DEFAULT NULL COMMENT '创建者',
    `modify_time`        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`          VARCHAR(64)           DEFAULT NULL COMMENT '修改者',
    `row_version`        INT                   DEFAULT 1 COMMENT '记录版本',
    `row_valid`          TINYINT               DEFAULT 1 COMMENT '记录是否有效',
    PRIMARY KEY (`id`),
    INDEX `idx_client_platform` (`client_platform_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='客户端平台登录历史';

DROP TABLE IF EXISTS `db_rsms`.`tb_report_vehicle`;
CREATE TABLE `db_rsms`.`tb_report_vehicle`
(
    `id`                BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `vin`               VARCHAR(20) NOT NULL COMMENT '车架号',
    `report_state`      SMALLINT    NOT NULL DEFAULT 0 COMMENT '上报状态：0-正常上报，1-维修保养',
    `iccid`             VARCHAR(50)          DEFAULT NULL COMMENT '车载终端所使用SIM卡ICCID编号',
    `model`             VARCHAR(255)         DEFAULT NULL COMMENT '车辆备案时所用车辆型号',
    `drive_motor_type`  VARCHAR(255)         DEFAULT NULL COMMENT '驱动电机在整车中的布置型式及位置，如轮边电机、轮毂电机、前后双电机等',
    `max_speed`         SMALLINT             DEFAULT NULL COMMENT '整车最高车速',
    `ev_range`          SMALLINT             DEFAULT NULL COMMENT '在纯电行驶状态下的续驶里程（工况法）',
    `gear_ratio`        VARCHAR(255)         DEFAULT NULL COMMENT '各挡位下的传动比，CVT无此项',
    `battery_param`     VARCHAR(255)         DEFAULT NULL COMMENT '电池相关参数',
    `drive_motor_param` VARCHAR(255)         DEFAULT NULL COMMENT '驱动电机相关参数',
    `alarm_default`     VARCHAR(255)         DEFAULT NULL COMMENT '通用报警预值',
    `engine_sn`         VARCHAR(255)         DEFAULT NULL COMMENT '发动机编号',
    `fuel_type`         VARCHAR(255)         DEFAULT NULL COMMENT '燃油类型',
    `fuel_mark`         VARCHAR(255)         DEFAULT NULL COMMENT '燃油标号',
    `engine_max_power`  VARCHAR(255)         DEFAULT NULL COMMENT '最大输出功率',
    `engine_max_torque` VARCHAR(255)         DEFAULT NULL COMMENT '最大输出转矩',
    `description`       VARCHAR(255)         DEFAULT NULL COMMENT '备注',
    `create_time`       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`         VARCHAR(64)          DEFAULT NULL COMMENT '创建者',
    `modify_time`       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`         VARCHAR(64)          DEFAULT NULL COMMENT '修改者',
    `row_version`       INT                  DEFAULT 1 COMMENT '记录版本',
    `row_valid`         TINYINT              DEFAULT 1 COMMENT '记录是否有效',
    PRIMARY KEY (`id`),
    UNIQUE KEY (`vin`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='上报车辆';

DROP TABLE IF EXISTS `db_rsms`.`tb_registered_vehicle`;
CREATE TABLE `db_rsms`.`tb_registered_vehicle`
(
    `id`                 BIGINT      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `client_platform_id` BIGINT      NOT NULL COMMENT '客户端平台ID',
    `vin`                VARCHAR(20) NOT NULL COMMENT '车架号',
    `description`        VARCHAR(255)         DEFAULT NULL COMMENT '备注',
    `create_time`        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`          VARCHAR(64)          DEFAULT NULL COMMENT '创建者',
    `modify_time`        TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`          VARCHAR(64)          DEFAULT NULL COMMENT '修改者',
    `row_version`        INT                  DEFAULT 1 COMMENT '记录版本',
    `row_valid`          TINYINT              DEFAULT 1 COMMENT '记录是否有效',
    PRIMARY KEY (`id`),
    INDEX `idx_client_platform` (`client_platform_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='客户端平台已注册车辆';

DROP TABLE IF EXISTS `db_rsms`.`tb_vehicle_gb_message`;
CREATE TABLE `db_rsms`.`tb_vehicle_gb_message`
(
    `id`           BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `vin`          VARCHAR(20)   NOT NULL COMMENT '车架号',
    `parse_time`   TIMESTAMP     NOT NULL COMMENT '解析时间',
    `message_time` TIMESTAMP     NOT NULL COMMENT '消息时间',
    `command_flag` VARCHAR(50)   NOT NULL COMMENT '命令标识',
    `message_data` VARCHAR(2048) NOT NULL COMMENT '消息数据',
    `description`  VARCHAR(255)           DEFAULT NULL COMMENT '备注',
    `create_time`  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`    VARCHAR(64)            DEFAULT NULL COMMENT '创建者',
    `modify_time`  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`    VARCHAR(64)            DEFAULT NULL COMMENT '修改者',
    `row_version`  INT                    DEFAULT 1 COMMENT '记录版本',
    `row_valid`    TINYINT                DEFAULT 1 COMMENT '记录是否有效',
    PRIMARY KEY (`id`),
    INDEX `idx_vin` (`vin`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='车辆国标消息';

DROP TABLE IF EXISTS `db_rsms`.`tb_reissue_time_period`;
CREATE TABLE `db_rsms`.`tb_reissue_time_period`
(
    `id`                 BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `client_platform_id` BIGINT       NOT NULL COMMENT '客户端平台ID',
    `username`           VARCHAR(255) NOT NULL COMMENT '用户名',
    `hostname`           VARCHAR(255)          DEFAULT NULL COMMENT '主机名',
    `start_time`         TIMESTAMP    NOT NULL COMMENT '平台中断开始时间',
    `end_time`           TIMESTAMP             DEFAULT NULL COMMENT '平台中断结束时间',
    `reissue_time`       TIMESTAMP             DEFAULT NULL COMMENT '补发开始时间',
    `reissue_state`      SMALLINT     NOT NULL COMMENT '补发状态：0-未补发，1-正在补发，2-补发完成',
    `description`        VARCHAR(255)          DEFAULT NULL COMMENT '备注',
    `create_time`        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`          VARCHAR(64)           DEFAULT NULL COMMENT '创建者',
    `modify_time`        TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`          VARCHAR(64)           DEFAULT NULL COMMENT '修改者',
    `row_version`        INT                   DEFAULT 1 COMMENT '记录版本',
    `row_valid`          TINYINT               DEFAULT 1 COMMENT '记录是否有效',
    PRIMARY KEY (`id`),
    INDEX `idx_client_platform` (`client_platform_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='平台补发时间段';

DROP TABLE IF EXISTS `db_rsms`.`tb_vehicle_gb_alarm`;
CREATE TABLE `db_rsms`.`tb_vehicle_gb_alarm`
(
    `id`               BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `vin`              VARCHAR(20)   NOT NULL COMMENT '车架号',
    `alarm_time`       TIMESTAMP     NOT NULL COMMENT '报警时间',
    `restoration_time` TIMESTAMP              DEFAULT NULL COMMENT '报警恢复时间',
    `alarm_flag`       SMALLINT      NOT NULL COMMENT '报警标志位',
    `alarm_level`      SMALLINT      NOT NULL COMMENT '报警级别',
    `message_data`     VARCHAR(2048) NOT NULL COMMENT '消息数据',
    `description`      VARCHAR(255)           DEFAULT NULL COMMENT '备注',
    `create_time`      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`        VARCHAR(64)            DEFAULT NULL COMMENT '创建者',
    `modify_time`      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`        VARCHAR(64)            DEFAULT NULL COMMENT '修改者',
    `row_version`      INT                    DEFAULT 1 COMMENT '记录版本',
    `row_valid`        TINYINT                DEFAULT 1 COMMENT '记录是否有效',
    PRIMARY KEY (`id`),
    INDEX `idx_vin` (`vin`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='车辆国标报警';

DROP TABLE IF EXISTS `db_rsms`.`tb_gb_inspection_report`;
CREATE TABLE `db_rsms`.`tb_gb_inspection_report`
(
    `id`                       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `start_time`               TIMESTAMP    NOT NULL COMMENT '检测开始时间',
    `end_time`                 TIMESTAMP    NOT NULL COMMENT '检测结束时间',
    `report_type`              SMALLINT     NOT NULL COMMENT '报告类型：1-单车报告，2-车型报告',
    `report_state`             SMALLINT     NOT NULL COMMENT '报告状态：1-处理中，2-处理结束',
    `vehicle`                  VARCHAR(255) NOT NULL COMMENT '车型或车架号',
    `vehicle_start_time`       TIMESTAMP             DEFAULT NULL COMMENT '单车开始时间',
    `vehicle_end_time`         TIMESTAMP             DEFAULT NULL COMMENT '单车结束时间',
    `vehicle_count`            INT                   DEFAULT NULL COMMENT '车辆数',
    `vehicle_data_count`       INT                   DEFAULT NULL COMMENT '有数据车辆数',
    `vehicle_nodata_count`     INT                   DEFAULT NULL COMMENT '无数据车辆数',
    `vehicle_error_count`      INT                   DEFAULT NULL COMMENT '错误车辆数',
    `scene`                    SMALLINT     NOT NULL COMMENT '场景：0-不指定，1-车辆行驶，2-车辆充电，3-车辆报警，4-车辆补发，5-平台补发',
    `scene_start_time`         TIMESTAMP             DEFAULT NULL COMMENT '单车场景开始时间',
    `scene_end_time`           TIMESTAMP             DEFAULT NULL COMMENT '单车场景结束时间',
    `message_count`            BIGINT                DEFAULT NULL COMMENT '数据总条数',
    `message_error_count`      BIGINT                DEFAULT NULL COMMENT '错误数据条数',
    `data_count`               BIGINT                DEFAULT NULL COMMENT '数据总条数',
    `data_error_count`         BIGINT                DEFAULT NULL COMMENT '错误数据条数',
    `description`              VARCHAR(255)          DEFAULT NULL COMMENT '备注',
    `create_time`              TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`                VARCHAR(64)           DEFAULT NULL COMMENT '创建者',
    `modify_time`              TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`                VARCHAR(64)           DEFAULT NULL COMMENT '修改者',
    `row_version`              INT                   DEFAULT 1 COMMENT '记录版本',
    `row_valid`                TINYINT               DEFAULT 1 COMMENT '记录是否有效',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='国标数据质量检测报告';

DROP TABLE IF EXISTS `db_rsms`.`tb_gb_inspection_item`;
CREATE TABLE `db_rsms`.`tb_gb_inspection_item`
(
    `id`                       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `report_id`                BIGINT       NOT NULL COMMENT '报告主键',
    `item_category`            VARCHAR(255) NOT NULL COMMENT '检查项大类',
    `item_type`                VARCHAR(255)          DEFAULT NULL COMMENT '检查项小类',
    `item_code`                VARCHAR(255) NOT NULL COMMENT '检查项',
    `total_data_count`         BIGINT                DEFAULT NULL COMMENT '总数量',
    `error_data_count`         BIGINT                DEFAULT NULL COMMENT '错误数量',
    `total_vehicle_count`      BIGINT                DEFAULT NULL COMMENT '总车辆数',
    `error_vehicle_count`      BIGINT                DEFAULT NULL COMMENT '错误车辆数',
    `description`              VARCHAR(255)          DEFAULT NULL COMMENT '备注',
    `create_time`              TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by`                VARCHAR(64)           DEFAULT NULL COMMENT '创建者',
    `modify_time`              TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `modify_by`                VARCHAR(64)           DEFAULT NULL COMMENT '修改者',
    `row_version`              INT                   DEFAULT 1 COMMENT '记录版本',
    `row_valid`                TINYINT               DEFAULT 1 COMMENT '记录是否有效',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='国标数据质量检测项';