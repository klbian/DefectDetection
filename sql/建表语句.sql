drop database if exists defect_detection;
create database defect_detection;
use defect_detection;

-- 管理人员表
DROP TABLE IF EXISTS manager;
CREATE TABLE manager
(
    id           SMALLINT(5) PRIMARY KEY AUTO_INCREMENT,
    account      VARCHAR(11) UNIQUE NOT NULL,
    password     VARCHAR(32)        NOT NULL,
    phone_number CHAR(11),
    name         VARCHAR(20),
    email        VARCHAR(100),
    is_Online    boolean default (0),
    warnings_open boolean default 0,
    warnings_level tinyint default 0,
    phone_Way boolean default 0,
    email_way boolean default 0
);

-- 设备表
DROP TABLE IF EXISTS device;
CREATE TABLE device
(
    id          SMALLINT(5) PRIMARY KEY AUTO_INCREMENT,
    type        TINYINT NOT NULL,
    mac         CHAR(64),
    ip          CHAR(64),
    name        VARCHAR(255),
    remark      VARCHAR(255),
    is_deleted  NUMERIC(1) DEFAULT (0),
    create_time DATETIME,
    create_id   INTEGER,
    update_time DATETIME,
    update_id   INTEGER,
    is_Connect boolean default false
);

-- 警告信息表
DROP TABLE IF EXISTS warnings;
CREATE TABLE warnings
(
    id          INTEGER PRIMARY KEY AUTO_INCREMENT,
    create_time DATETIME,
    level       TINYINT,
    type        CHAR(6),
    content     VARCHAR(255)
);

-- API 表
DROP TABLE IF EXISTS api;
CREATE TABLE api
(
    id               INTEGER PRIMARY KEY AUTO_INCREMENT,
    is_deleted       NUMERIC(1) DEFAULT (0),
    create_time      DATETIME,
    create_id        INTEGER,
    create_name      VARCHAR(20),
    update_time      DATETIME,
    update_id        INTEGER,
    validity_period  INTEGER    DEFAULT (-1),
    validity_times   INTEGER    DEFAULT (-1),
    permission_level TINYINT    DEFAULT (0),
    api_key          VARCHAR(128) UNIQUE NOT NULL,
    status           NUMERIC(1) DEFAULT (1),
    remark           VARCHAR(255),
    counts  int default 0
);

-- 系统日志表
DROP TABLE IF EXISTS sys_log;
CREATE TABLE sys_log
(
    id            INTEGER PRIMARY KEY AUTO_INCREMENT,
    time          DATETIME,
    operation     VARCHAR(32),
    operator      SMALLINT,
    operator_type NUMERIC(1) NOT NULL,
    target varchar(32)
);

-- 模型表
DROP TABLE IF EXISTS model;
CREATE TABLE model
(
    id                 SMALLINT PRIMARY KEY AUTO_INCREMENT,
    name               VARCHAR(32),
    param_storage_path VARCHAR(128) NOT NULL,
    is_deleted         NUMERIC(1) DEFAULT (0),
    create_time        DATETIME,
    create_id          INTEGER,
    update_time        DATETIME,
    update_id          INTEGER,
    remark             VARCHAR(255)
);

-- 操作员表
DROP TABLE IF EXISTS operator;
CREATE TABLE operator
(
    id            SMALLINT PRIMARY KEY AUTO_INCREMENT,
    job_id        INTEGER unique ,
    login_pwd     CHAR(32),
    op_pwd        CHAR(32),
    is_deleted    NUMERIC(1) DEFAULT (0),
    create_time   DATETIME,
    create_id     INTEGER,
    update_time   DATETIME,
    update_id     INTEGER,
    create_name   VARCHAR(20),
    name          VARCHAR(20),
    work_order_id SMALLINT,
    remark        VARCHAR(255)
);

-- 缺陷表
DROP TABLE IF EXISTS defection;
CREATE TABLE defection
(
    id          INTEGER(12) PRIMARY KEY AUTO_INCREMENT,
    l           DECIMAL(10, 4),
    h           DECIMAL(10, 4),
    x           DECIMAL(10, 4),
    y           DECIMAL(10, 4),
    score       DECIMAL(6, 3),
    detect_id   INTEGER(12),
    category    VARCHAR(50),
    category_id TINYINT(3)
);

-- 检测单表
DROP TABLE IF EXISTS detect_log;
CREATE TABLE detect_log
(
    id             INTEGER(12) PRIMARY KEY AUTO_INCREMENT,
    name           VARCHAR(128) UNIQUE,
    defections_sum TINYINT(3) DEFAULT (0),
    time DATETIME,
    work_order_id  INTEGER(12),
    storage_path   VARCHAR(255)
);

-- 工单表
DROP TABLE IF EXISTS work_order;
CREATE TABLE work_order
(
    id          SMALLINT(5) PRIMARY KEY AUTO_INCREMENT,
    is_deleted  NUMERIC(1) DEFAULT (0),
    create_time DATETIME,
    create_id   INTEGER,
    current_num integer,
    detect_sum  INTEGER,
    is_over     NUMERIC(1) DEFAULT (0),
    finish_time DATETIME,
    update_time DATETIME,
    update_id   INTEGER,
    remark      VARCHAR(255)
);

-- 缺陷种类表
drop table if exists defection_category;
create table defection_category
(
    id    integer primary key auto_increment,
    name  VARCHAR(20),
    count integer,
    create_time datetime
)