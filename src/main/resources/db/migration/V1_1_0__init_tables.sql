CREATE TABLE IF NOT EXISTS `contact` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(20) NOT NULL,
  `email` char(100) NOT NULL,
  `unsubscribed` char(1) NOT NULL DEFAULT 'N',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE IF NOT EXISTS `email` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` TEXT NOT NULL,
  `content` TEXT NOT NULL,
  `created_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE IF NOT EXISTS `email_contact` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email_id` bigint(20) NULL,
  `contact_id` bigint(20) NULL,
  `status` char(10) NOT NULL,
  `created_date` datetime NOT NULL,
  `opened_date` datetime NULL,
  PRIMARY KEY (`id`),
  KEY `fk_email_id` (`email_id`),
  KEY `fk_contact_id` (`contact_id`),
  CONSTRAINT `fk_email_id` FOREIGN KEY (`email_id`) REFERENCES `email` (`id`),
  CONSTRAINT `fk_contact_id` FOREIGN KEY (`contact_id`) REFERENCES `contact` (`id`),
  UNIQUE KEY `uc_contact_email` (`email_id`,`contact_id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;