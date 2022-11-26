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
    `create_time`     datetime                                                               DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     datetime                                                               DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
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
    `registration_time` datetime                                                              DEFAULT NULL COMMENT '任务注册时间',
    `request_info`      json                                                                  DEFAULT NULL COMMENT '请求信息',
    `response_info`     json                                                                  DEFAULT NULL COMMENT '响应信息',
    `request_time`      datetime                                                              DEFAULT NULL COMMENT '请求发起时间',
    `timing`            bigint                                                                DEFAULT NULL COMMENT '执行耗时(ms)',
    `create_time`       datetime                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
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
    `create_time`  datetime             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`  datetime             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
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
    PRIMARY KEY (`user_id`) USING BTREE,
    UNIQUE KEY `uidx_user_useraccount` (`user_account`) USING BTREE
) ENGINE=InnoDB COMMENT='用户表';