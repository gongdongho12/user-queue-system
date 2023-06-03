CREATE TABLE `tickets`
(
    `id`                      bigint       NOT NULL AUTO_INCREMENT,
    `title`                   varchar(255) NOT NULL comment '티켓 이름',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '티켓';
