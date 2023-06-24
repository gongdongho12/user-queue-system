CREATE TABLE `tickets`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT,
    `title`        varchar(255) NOT NULL COMMENT '티켓 이름',
    `date_created` datetime(6) NOT NULL COMMENT '등록일시',
    `date_updated` datetime(6) NOT NULL COMMENT '수정일시',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '티켓';

CREATE TABLE `members`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT,
    `user_id`      varchar(255) NOT NULL COMMENT '유저 아이디',
    `password`     varchar(255) NOT NULL COMMENT '패스워드',
    `role`         varchar(10)  NOT NULL COMMENT '유저 권한',
    `date_created` datetime(6) NOT NULL COMMENT '등록일시',
    `date_updated` datetime(6) NOT NULL COMMENT '수정일시',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '멤버';

CREATE TABLE `tokens`
(
    `id`           bigint       NOT NULL AUTO_INCREMENT,
    `token`        varchar(255) NOT NULL COMMENT '토큰 키',
    `token_type`   varchar(255) NOT NULL COMMENT '토큰 타입',
    `user_id`      varchar(255) NOT NULL COMMENT '유저 아이디',
    `revoked`      bit(1)       NOT NULL COMMENT '취소 여부',
    `expired`      bit(1)       NOT NULL COMMENT '만료 여부',
    `date_created` datetime(6) NOT NULL COMMENT '등록일시',
    `date_updated` datetime(6) NOT NULL COMMENT '수정일시',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
    COMMENT '멤버';

