-- 清空管理人员表
TRUNCATE TABLE manager;

-- 清空设备表
TRUNCATE TABLE device;

-- 清空警告信息表
TRUNCATE TABLE warnings;

-- 清空API表
TRUNCATE TABLE api;

-- 清空系统日志表
TRUNCATE TABLE sys_log;

-- 清空模型表
TRUNCATE TABLE model;

-- 清空操作员表
TRUNCATE TABLE operator;

-- 清空缺陷表
TRUNCATE TABLE defection;

-- 清空检测单表
TRUNCATE TABLE detect_log;

-- 清空工单表
TRUNCATE TABLE work_order;

-- 清空缺陷种类表
TRUNCATE TABLE defection_category;
-- 管理人员表
INSERT INTO manager (account, password, phone_number, name, email)
VALUES ('admin1', 'e10adc3949ba59abbe56e057f20f883e', '12345678901', '管理员1', 'admin1@example.com');

INSERT INTO manager (account, password, phone_number, name, email)
VALUES ('admin2', 'e10adc3949ba59abbe56e057f20f883e', '98765432109', '管理员2', 'admin2@example.com');

-- 设备表
INSERT INTO device (type, mac, ip, name, remark, create_time, create_id, update_time, update_id)
VALUES (1, '00:11:22:33:44:55', '127.0.0.1', 'IP摄像机', '工业高精度摄像机', NOW(), 1, NOW(), 1);

INSERT INTO device (type, mac, ip, name, remark, create_time, create_id, update_time, update_id)
VALUES (2, 'AA:BB:CC:DD:EE:FF', '192.168.1.2', '设备2', '备注2', NOW(), 2, NOW(), 2);

-- 警告信息表
INSERT INTO warnings (create_time, level, type, content)
VALUES (NOW(), 1, 1, '警告信息1');

INSERT INTO warnings (create_time, level, type, content)
VALUES (NOW(), 2, 2, '警告信息2');

-- API 表
INSERT INTO api (is_deleted, create_time, create_id, create_name, update_time, update_id, validity_period, validity_times, permission_level, api_key, status)
VALUES (0, NOW(), 1, '管理员1', NOW(), 1, -1, -1, 0, 'APIKEY1', 1);

INSERT INTO api (is_deleted, create_time, create_id, create_name, update_time, update_id, validity_period, validity_times, permission_level, api_key, status)
VALUES (0, NOW(), 2, '管理员2', NOW(), 2, -1, -1, 0, 'APIKEY2', 1);

-- 系统日志表
INSERT INTO sys_log (time, operation, operator, operator_type)
VALUES (NOW(), '操作1', 1, 1);

INSERT INTO sys_log (time, operation, operator, operator_type)
VALUES (NOW(), '操作2', 2, 2);

-- 模型表
INSERT INTO model (name, param_storage_path, is_deleted, create_time, create_id, update_time, update_id, remark)
VALUES ('模型1', '/path/to/param1', 0, NOW(), 1, NOW(), 1, '备注1');

INSERT INTO model (name, param_storage_path, is_deleted, create_time, create_id, update_time, update_id, remark)
VALUES ('检测模型', '/path/to/param2', 0, NOW(), 2, NOW(), 2, '备注2');

-- 操作员表
INSERT INTO operator (login_pwd, op_pwd, is_deleted, create_time, create_id, update_time, update_id, create_name, name, work_order_id, remark)
VALUES ('123456', 'abcdef', 0, NOW(), 1, NOW(), 1, '管理员1', '操作员1', 1, '备注1');

INSERT INTO operator (login_pwd, op_pwd, is_deleted, create_time, create_id, update_time, update_id, create_name, name, work_order_id, remark)
VALUES ('abcdef', '123456', 0, NOW(), 2, NOW(), 2, '管理员2', '操作员2', 2, '备注2');

-- 缺陷表
INSERT INTO defection (l, h, x, y, score, detect_id, category, category_id)
VALUES (1.234, 5.678, 0.123, 0.456, 0.789, 1, '缺陷种类1', 1);

INSERT INTO defection (l, h, x, y, score, detect_id, category, category_id)
VALUES (3.456, 7.890, 0.789, 0.123,  0.789, 2, '缺陷种类2', 2);

-- 检测单表
INSERT INTO detect_log (name, defections_sum, time, work_order_id, storage_path)
VALUES ('检测单1', 2, NOW(), 1, '/path/to/storage1');

INSERT INTO detect_log (name, defections_sum, time, work_order_id, storage_path)
VALUES ('检测单2', 1, NOW(), 2, '/path/to/storage2');

-- 工单表
INSERT INTO work_order (is_deleted, create_time, create_id, current_num, detect_sum, is_over, finish_time, update_time, update_id, remark)
VALUES (0, NOW(), 1, 1, 10, 0, NULL, NOW(), 1, '备注1');

INSERT INTO work_order (is_deleted, create_time, create_id, current_num, detect_sum, is_over, finish_time, update_time, update_id, remark)
VALUES (0, NOW(), 2, 2, 20, 0, NULL, NOW(), 2, '备注2');

-- 缺陷种类表
INSERT INTO defection_category (name, count)
VALUES ('缺陷种类1', 5);

INSERT INTO defection_category (name, count)
VALUES ('缺陷种类2', 10);