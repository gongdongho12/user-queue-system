CREATE TABLE `tickets`
(
    `id`                      bigint       NOT NULL AUTO_INCREMENT,
    `title`                   varchar(255) NOT NULL COMMENT '티켓 이름',
    `date_created`            datetime(6) NOT NULL COMMENT '등록일시',
    `date_updated`            datetime(6) NOT NULL COMMENT '수정일시',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '티켓';
