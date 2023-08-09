-- Mock 处理器表
CREATE TABLE `dm_mock_handler`
(
    `handler_Id`      int                                                           NOT NULL AUTO_INCREMENT,
    `project_id`      int                                                           NOT NULL COMMENT '项目id',
    `enable_status`   int                                                           NOT NULL DEFAULT '2' COMMENT '启用状态.1-启用,其他-禁用',
    `name`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '处理器名称',
    `http_methods` set('GET','POST','PUT','DELETE') CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'GET' COMMENT '支持的请求方式',
    `request_uri`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支持的请求URI',
    `label`           varchar(128)                                                           DEFAULT NULL COMMENT '标签',
    `delay_time`      int                                                           NOT NULL DEFAULT '0' COMMENT '延迟处理时间',
    `customize_space` json                                                                   DEFAULT NULL COMMENT '自定义空间',
    `responses`       json                                                                   DEFAULT NULL COMMENT '模拟响应信息集',
    `tasks`           json                                                                   DEFAULT NULL COMMENT '任务信息集',
    `author`          varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci           DEFAULT NULL COMMENT '负责人',
    `create_time`     DATETIME(3) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME(3) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`handler_Id`) USING BTREE,
    KEY               `idx_mockhandler_name` (`name`) USING BTREE COMMENT '处理器名称',
    KEY               `idx_mockhandler_projectid` (`project_id`) USING BTREE COMMENT '项目id'
) ENGINE=InnoDB COMMENT='Mock 处理器表';

-- Mock 处理器唯一表
CREATE TABLE `dm_mock_handler_unique`
(
    `id`          int                                                           NOT NULL AUTO_INCREMENT COMMENT '主键',
    `handler_id`  int                                                           NOT NULL COMMENT '处理器id',
    `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支持的请求URI',
    `http_method` varbinary(8) NOT NULL COMMENT '支持的请求方式',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uidx_mockhandlerunique_requesturi_httpmethod` (`request_uri`,`http_method`) USING BTREE
) ENGINE=InnoDB COMMENT='Mock 处理器唯一表';

-- Mock 任务日志表
CREATE TABLE `dm_mock_task_log`
(
    `log_id`            int                                                          NOT NULL AUTO_INCREMENT,
    `task_code`         varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务编码',
    `handler_id`        int                                                          NOT NULL COMMENT 'Mock处理器id',
    `name`              varchar(255)                                                 NOT NULL COMMENT '任务名称',
    `registration_time` DATETIME(3) DEFAULT NULL COMMENT '任务注册时间',
    `request_info`      json   DEFAULT NULL COMMENT '请求信息',
    `response_info`     json   DEFAULT NULL COMMENT '响应信息',
    `request_time`      DATETIME(3) DEFAULT NULL COMMENT '请求发起时间',
    `timing`            bigint DEFAULT NULL COMMENT '执行耗时(ms)',
    `create_time`       DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`log_id`) USING BTREE,
    KEY                 `idx_mocktasklog_handlerid` (`handler_id`) USING BTREE,
    KEY                 `idx_mocktasklog_name` (`name`) USING BTREE
) ENGINE=InnoDB COMMENT='Mock 任务日志表';

-- 项目表
CREATE TABLE `dm_project`
(
    `project_id`   int         NOT NULL AUTO_INCREMENT COMMENT '项目id',
    `project_name` varchar(64) NOT NULL COMMENT '项目名称',
    `base_uri`     varchar(64) NOT NULL DEFAULT '' COMMENT '基础路径',
    `create_time`  DATETIME(3) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME(3) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`project_id`)
) ENGINE=InnoDB COMMENT='项目表';

-- 用户表
CREATE TABLE `dm_user`
(
    `user_id`      int         NOT NULL AUTO_INCREMENT,
    `username`     varchar(32) NOT NULL COMMENT '用户昵称',
    `user_account` varchar(32) NOT NULL COMMENT '用户账号',
    `password`     varchar(64) NOT NULL COMMENT '用户密码',
    `role`         int         NOT NULL DEFAULT '2' COMMENT '角色.1-管理员,2-用户',
    `permission`   varchar(255)         DEFAULT NULL COMMENT '项目id,多个逗号拼接',
    `create_time`  DATETIME(3) DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  DATETIME(3) DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE KEY `uidx_user_useraccount` (`user_account`) USING BTREE
) ENGINE=InnoDB COMMENT='用户表';

CREATE TABLE `dm_text_file`
(
    `id`                 int          NOT NULL AUTO_INCREMENT,
    `file_name`          varchar(64)  NOT NULL COMMENT '文件名',
    `original_file_name` varchar(128) NOT NULL COMMENT '原始文件名',
    `content`            longtext COMMENT '文本内容',
    `create_time`        DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uidx_dmtextfile_filename` (`file_name`)
) ENGINE=InnoDB  COMMENT='文本文件内容表';

CREATE TABLE `dm_mock_handler_condition`
(
    `condition_id`   INT         NOT NULL AUTO_INCREMENT,
    `handler_id`     INT         NOT NULL COMMENT 'mock处理器ID',
    `component_id`   INT         NOT NULL COMMENT '组件id',
    `component_type` VARCHAR(64) NOT NULL COMMENT '组件类型.MockHandlerComponentType',
    `expression`     TEXT        NOT NULL COMMENT '条件表达式',
    `sort`           INT         NOT NULL COMMENT '排序',
    `create_time`    DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`condition_id`),
    KEY              `idx_dmmockhandlercondition_componentid_componenttype` (`component_id`,`component_type`) USING BTREE
) ENGINE=InnoDB COMMENT='条件信息表';

CREATE TABLE `dm_mock_handler_task`
(
    `task_id`           INT         NOT NULL AUTO_INCREMENT,
    `handler_id`        INT         NOT NULL COMMENT 'mock处理器ID',
    `name`              VARCHAR(64) NOT NULL COMMENT '任务名称',
    `async`             TINYINT(2) NOT NULL COMMENT '是否异步.1-是,0-否',
    `cron`              VARCHAR(32) NOT NULL COMMENT 'Cron表达式',
    `number_of_execute` INT         NOT NULL DEFAULT 1 COMMENT '执行次数',
    `sort`              INT         NOT NULL COMMENT '排序',
    `create_time`       DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`task_id`),
    KEY                 `idx_dmmockhandlertask_handlerid` (`handler_id`) USING BTREE,
    KEY                 `idx_dmmockhandlertask_taskid` (`task_id`) USING BTREE
) ENGINE=InnoDB  COMMENT='Mock处理器任务表';

CREATE TABLE `dm_mock_handler_task_request`
(
    `task_request_id` INT          NOT NULL AUTO_INCREMENT,
    `handler_id`      INT          NOT NULL COMMENT 'mock处理器ID',
    `task_id`         INT          NOT NULL COMMENT '任务ID',
    `request_url`     VARCHAR(256) NOT NULL COMMENT '请求地址',
    `http_method`     VARCHAR(8)   NOT NULL COMMENT '请求方式',
    `headers`         JSON COMMENT '请求头',
    `body`            TEXT DEFAULT NULL COMMENT '请求体',
    `uri_variables`   JSON COMMENT '请求体',
    `create_time`     DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`task_request_id`),
    KEY               `idx_dmmockhandlertaskrequest_handlerid` (`handler_id`) USING BTREE,
    UNIQUE KEY `dmmockhandlertaskrequest_taskid` (`task_id`) USING BTREE
) ENGINE=InnoDB  COMMENT='Mock处理器任务请求信息表';

CREATE TABLE `dm_mock_handler_response`
(
    `response_id` INT         NOT NULL AUTO_INCREMENT,
    `handler_id`  INT         NOT NULL COMMENT 'mock处理器ID',
    `name`        VARCHAR(64) NOT NULL COMMENT '响应名称',
    `delay_time`  INT         NOT NULL DEFAULT '0' COMMENT '延迟时间',
    `sort`        INT         NOT NULL COMMENT '排序',
    `create_time` DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`response_id`),
    KEY           `idx_dmmockhandlerresponse_handlerid` (`handler_id`) USING BTREE
) ENGINE=InnoDB  COMMENT='Mock处理器响应信息表';

CREATE TABLE `dm_mock_handler_response_item`
(
    `response_item_id` INT NOT NULL AUTO_INCREMENT,
    `response_id`      INT NOT NULL COMMENT 'mock 响应ID',
    `handler_id`       INT NOT NULL COMMENT 'mock处理器ID',
    `status`           INT NOT NULL COMMENT '响应码',
    `headers`          JSON COMMENT '响应头',
    `body`             TEXT COMMENT '延迟时间',
    `create_time`      DATETIME(3) NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`response_item_id`),
    KEY                `idx_dmmockhandlerresponseitem_handlerid_responseid` (`handler_id`,`response_id`) USING BTREE
) ENGINE=InnoDB  COMMENT='Mock处理器响应详情信息表';

-- 初始化用户信息
INSERT INTO dev_dynamic_mock.dm_user (username, user_account, password, role, permission)
VALUES ('root', 'root', '63a9f0ea7bb98050796b649e85481845', 1, null);
