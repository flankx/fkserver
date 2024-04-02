
CREATE TABLE IF NOT EXISTS `fk_user`
(
    `id`                 bigint      NOT NULL AUTO_INCREMENT,
    `login`              varchar(50) NOT NULL,
    `password_hash`      varchar(60) NOT NULL,
    `first_name`         varchar(50)      DEFAULT NULL,
    `last_name`          varchar(50)      DEFAULT NULL,
    `email`              varchar(191)     DEFAULT NULL,
    `image_url`          varchar(256)     DEFAULT NULL,
    `activated`          tinyint     NOT NULL,
    `lang_key`           varchar(10)      DEFAULT NULL,
    `activation_key`     varchar(20)      DEFAULT NULL,
    `reset_key`          varchar(20)      DEFAULT NULL,
    `created_by`         varchar(50) NOT NULL,
    `created_date`       timestamp   NULL DEFAULT NULL,
    `reset_date`         timestamp   NULL DEFAULT NULL,
    `last_modified_by`   varchar(50)      DEFAULT NULL,
    `last_modified_date` timestamp   NULL DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `ux_user_login` (`login`),
    UNIQUE KEY `ux_user_email` (`email`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `fk_authority`
(
    `name` varchar(50) NOT NULL,
    PRIMARY KEY (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE IF NOT EXISTS `fk_user_authority`
(
    `user_id`        bigint      NOT NULL,
    `authority_name` varchar(50) NOT NULL,
    PRIMARY KEY (`user_id`, `authority_name`),
    KEY `fk_authority_name` (`authority_name`),
    CONSTRAINT `fk_authority_name` FOREIGN KEY (`authority_name`) REFERENCES `fk_authority` (`name`),
    CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `fk_user` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

