/*
 Navicat Premium Data Transfer

 Source Server         : mysqsl-2020
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : localhost:3306
 Source Schema         : ClientCenterService

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 12/01/2021 08:26:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for BATCH_JOB_EXECUTION
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION`;
CREATE TABLE `BATCH_JOB_EXECUTION` (
                                       `JOB_EXECUTION_ID` bigint NOT NULL,
                                       `VERSION` bigint DEFAULT NULL,
                                       `JOB_INSTANCE_ID` bigint NOT NULL,
                                       `CREATE_TIME` datetime NOT NULL,
                                       `START_TIME` datetime DEFAULT NULL,
                                       `END_TIME` datetime DEFAULT NULL,
                                       `STATUS` varchar(10) DEFAULT NULL,
                                       `EXIT_CODE` varchar(2500) DEFAULT NULL,
                                       `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
                                       `LAST_UPDATED` datetime DEFAULT NULL,
                                       `JOB_CONFIGURATION_LOCATION` varchar(2500) DEFAULT NULL,
                                       PRIMARY KEY (`JOB_EXECUTION_ID`),
                                       KEY `JOB_INST_EXEC_FK` (`JOB_INSTANCE_ID`),
                                       CONSTRAINT `JOB_INST_EXEC_FK` FOREIGN KEY (`JOB_INSTANCE_ID`) REFERENCES `BATCH_JOB_INSTANCE` (`JOB_INSTANCE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for BATCH_JOB_EXECUTION_CONTEXT
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_CONTEXT`;
CREATE TABLE `BATCH_JOB_EXECUTION_CONTEXT` (
                                               `JOB_EXECUTION_ID` bigint NOT NULL,
                                               `SHORT_CONTEXT` varchar(2500) NOT NULL,
                                               `SERIALIZED_CONTEXT` text,
                                               PRIMARY KEY (`JOB_EXECUTION_ID`),
                                               CONSTRAINT `JOB_EXEC_CTX_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for BATCH_JOB_EXECUTION_PARAMS
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_PARAMS`;
CREATE TABLE `BATCH_JOB_EXECUTION_PARAMS` (
                                              `JOB_EXECUTION_ID` bigint NOT NULL,
                                              `TYPE_CD` varchar(6) NOT NULL,
                                              `KEY_NAME` varchar(100) NOT NULL,
                                              `STRING_VAL` varchar(250) DEFAULT NULL,
                                              `DATE_VAL` datetime DEFAULT NULL,
                                              `LONG_VAL` bigint DEFAULT NULL,
                                              `DOUBLE_VAL` double DEFAULT NULL,
                                              `IDENTIFYING` char(1) NOT NULL,
                                              KEY `JOB_EXEC_PARAMS_FK` (`JOB_EXECUTION_ID`),
                                              CONSTRAINT `JOB_EXEC_PARAMS_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for BATCH_JOB_EXECUTION_SEQ
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_JOB_EXECUTION_SEQ`;
CREATE TABLE `BATCH_JOB_EXECUTION_SEQ` (
                                           `ID` bigint NOT NULL,
                                           `UNIQUE_KEY` char(1) NOT NULL,
                                           UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for BATCH_JOB_INSTANCE
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_JOB_INSTANCE`;
CREATE TABLE `BATCH_JOB_INSTANCE` (
                                      `JOB_INSTANCE_ID` bigint NOT NULL,
                                      `VERSION` bigint DEFAULT NULL,
                                      `JOB_NAME` varchar(100) NOT NULL,
                                      `JOB_KEY` varchar(32) NOT NULL,
                                      PRIMARY KEY (`JOB_INSTANCE_ID`),
                                      UNIQUE KEY `JOB_INST_UN` (`JOB_NAME`,`JOB_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for BATCH_JOB_SEQ
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_JOB_SEQ`;
CREATE TABLE `BATCH_JOB_SEQ` (
                                 `ID` bigint NOT NULL,
                                 `UNIQUE_KEY` char(1) NOT NULL,
                                 UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for BATCH_STEP_EXECUTION
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION`;
CREATE TABLE `BATCH_STEP_EXECUTION` (
                                        `STEP_EXECUTION_ID` bigint NOT NULL,
                                        `VERSION` bigint NOT NULL,
                                        `STEP_NAME` varchar(100) NOT NULL,
                                        `JOB_EXECUTION_ID` bigint NOT NULL,
                                        `START_TIME` datetime NOT NULL,
                                        `END_TIME` datetime DEFAULT NULL,
                                        `STATUS` varchar(10) DEFAULT NULL,
                                        `COMMIT_COUNT` bigint DEFAULT NULL,
                                        `READ_COUNT` bigint DEFAULT NULL,
                                        `FILTER_COUNT` bigint DEFAULT NULL,
                                        `WRITE_COUNT` bigint DEFAULT NULL,
                                        `READ_SKIP_COUNT` bigint DEFAULT NULL,
                                        `WRITE_SKIP_COUNT` bigint DEFAULT NULL,
                                        `PROCESS_SKIP_COUNT` bigint DEFAULT NULL,
                                        `ROLLBACK_COUNT` bigint DEFAULT NULL,
                                        `EXIT_CODE` varchar(2500) DEFAULT NULL,
                                        `EXIT_MESSAGE` varchar(2500) DEFAULT NULL,
                                        `LAST_UPDATED` datetime DEFAULT NULL,
                                        PRIMARY KEY (`STEP_EXECUTION_ID`),
                                        KEY `JOB_EXEC_STEP_FK` (`JOB_EXECUTION_ID`),
                                        CONSTRAINT `JOB_EXEC_STEP_FK` FOREIGN KEY (`JOB_EXECUTION_ID`) REFERENCES `BATCH_JOB_EXECUTION` (`JOB_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for BATCH_STEP_EXECUTION_CONTEXT
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION_CONTEXT`;
CREATE TABLE `BATCH_STEP_EXECUTION_CONTEXT` (
                                                `STEP_EXECUTION_ID` bigint NOT NULL,
                                                `SHORT_CONTEXT` varchar(2500) NOT NULL,
                                                `SERIALIZED_CONTEXT` text,
                                                PRIMARY KEY (`STEP_EXECUTION_ID`),
                                                CONSTRAINT `STEP_EXEC_CTX_FK` FOREIGN KEY (`STEP_EXECUTION_ID`) REFERENCES `BATCH_STEP_EXECUTION` (`STEP_EXECUTION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for BATCH_STEP_EXECUTION_SEQ
-- ----------------------------
DROP TABLE IF EXISTS `BATCH_STEP_EXECUTION_SEQ`;
CREATE TABLE `BATCH_STEP_EXECUTION_SEQ` (
                                            `ID` bigint NOT NULL,
                                            `UNIQUE_KEY` char(1) NOT NULL,
                                            UNIQUE KEY `UNIQUE_KEY_UN` (`UNIQUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for DATABASECHANGELOG
-- ----------------------------
DROP TABLE IF EXISTS `DATABASECHANGELOG`;
CREATE TABLE `DATABASECHANGELOG` (
                                     `ID` varchar(255) NOT NULL,
                                     `AUTHOR` varchar(255) NOT NULL,
                                     `FILENAME` varchar(255) NOT NULL,
                                     `DATEEXECUTED` datetime NOT NULL,
                                     `ORDEREXECUTED` int NOT NULL,
                                     `EXECTYPE` varchar(10) NOT NULL,
                                     `MD5SUM` varchar(35) DEFAULT NULL,
                                     `DESCRIPTION` varchar(255) DEFAULT NULL,
                                     `COMMENTS` varchar(255) DEFAULT NULL,
                                     `TAG` varchar(255) DEFAULT NULL,
                                     `LIQUIBASE` varchar(20) DEFAULT NULL,
                                     `CONTEXTS` varchar(255) DEFAULT NULL,
                                     `LABELS` varchar(255) DEFAULT NULL,
                                     `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for DATABASECHANGELOGLOCK
-- ----------------------------
DROP TABLE IF EXISTS `DATABASECHANGELOGLOCK`;
CREATE TABLE `DATABASECHANGELOGLOCK` (
                                         `ID` int NOT NULL,
                                         `LOCKED` bit(1) NOT NULL,
                                         `LOCKGRANTED` datetime DEFAULT NULL,
                                         `LOCKEDBY` varchar(255) DEFAULT NULL,
                                         PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for employees
-- ----------------------------
DROP TABLE IF EXISTS `employees`;
CREATE TABLE `employees` (
                             `id` bigint NOT NULL AUTO_INCREMENT,
                             `source_id` varchar(100) NOT NULL,
                             `first_name` varchar(100) NOT NULL,
                             `middle_initial` varchar(1) NOT NULL,
                             `last_name` varchar(100) NOT NULL,
                             `email_address` varchar(200) NOT NULL,
                             `phone_number` varchar(50) NOT NULL,
                             `street` varchar(255) NOT NULL,
                             `city` varchar(255) NOT NULL,
                             `state` varchar(100) NOT NULL,
                             `zip_code` varchar(20) NOT NULL,
                             `birth_date` date NOT NULL,
                             `social_security_number` varchar(20) NOT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for jhi_authority
-- ----------------------------
DROP TABLE IF EXISTS `jhi_authority`;
CREATE TABLE `jhi_authority` (
                                 `name` varchar(50) NOT NULL,
                                 PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for jhi_persistent_audit_event
-- ----------------------------
DROP TABLE IF EXISTS `jhi_persistent_audit_event`;
CREATE TABLE `jhi_persistent_audit_event` (
                                              `event_id` bigint NOT NULL AUTO_INCREMENT,
                                              `principal` varchar(50) NOT NULL,
                                              `event_date` timestamp NULL DEFAULT NULL,
                                              `event_type` varchar(255) DEFAULT NULL,
                                              PRIMARY KEY (`event_id`),
                                              KEY `idx_persistent_audit_event` (`principal`,`event_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for jhi_persistent_audit_evt_data
-- ----------------------------
DROP TABLE IF EXISTS `jhi_persistent_audit_evt_data`;
CREATE TABLE `jhi_persistent_audit_evt_data` (
                                                 `event_id` bigint NOT NULL,
                                                 `name` varchar(150) NOT NULL,
                                                 `value` varchar(255) DEFAULT NULL,
                                                 PRIMARY KEY (`event_id`,`name`),
                                                 KEY `idx_persistent_audit_evt_data` (`event_id`),
                                                 CONSTRAINT `fk_evt_pers_audit_evt_data` FOREIGN KEY (`event_id`) REFERENCES `jhi_persistent_audit_event` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for jhi_user
-- ----------------------------
DROP TABLE IF EXISTS `jhi_user`;
CREATE TABLE `jhi_user` (
                            `id` varchar(100) NOT NULL,
                            `login` varchar(50) NOT NULL,
                            `first_name` varchar(50) DEFAULT NULL,
                            `last_name` varchar(50) DEFAULT NULL,
                            `email` varchar(191) DEFAULT NULL,
                            `image_url` varchar(256) DEFAULT NULL,
                            `activated` bit(1) NOT NULL,
                            `lang_key` varchar(10) DEFAULT NULL,
                            `created_by` varchar(50) NOT NULL,
                            `created_date` timestamp NULL DEFAULT NULL,
                            `last_modified_by` varchar(50) DEFAULT NULL,
                            `last_modified_date` timestamp NULL DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            UNIQUE KEY `ux_user_login` (`login`),
                            UNIQUE KEY `ux_user_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for jhi_user_authority
-- ----------------------------
DROP TABLE IF EXISTS `jhi_user_authority`;
CREATE TABLE `jhi_user_authority` (
                                      `user_id` varchar(100) NOT NULL,
                                      `authority_name` varchar(50) NOT NULL,
                                      PRIMARY KEY (`user_id`,`authority_name`),
                                      KEY `fk_authority_name` (`authority_name`),
                                      CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `jhi_authority` (`name`),
                                      CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `jhi_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;


-- ----------------------------
-- Table duplicate entry 0 batch tables
-- ----------------------------
INSERT INTO BATCH_STEP_EXECUTION_SEQ (ID, UNIQUE_KEY) SELECT
    *
FROM
    (
        SELECT
            0 AS ID,
            '0' AS UNIQUE_KEY
    ) AS tmp
WHERE
    NOT EXISTS (
            SELECT
                *
            FROM
                BATCH_STEP_EXECUTION_SEQ
        );

INSERT INTO BATCH_JOB_EXECUTION_SEQ (ID, UNIQUE_KEY) SELECT
    *
FROM
    (
        SELECT
            0 AS ID,
            '0' AS UNIQUE_KEY
    ) AS tmp
WHERE
    NOT EXISTS (
            SELECT
                *
            FROM
                BATCH_JOB_EXECUTION_SEQ
        );

INSERT INTO BATCH_JOB_SEQ (ID, UNIQUE_KEY) SELECT
    *
FROM
    (
        SELECT
            0 AS ID,
            '0' AS UNIQUE_KEY
    ) AS tmp
WHERE
    NOT EXISTS (SELECT * FROM BATCH_JOB_SEQ);


