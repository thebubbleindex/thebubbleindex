drop table `bubble_index_timeseries` if exists;
CREATE TABLE IF NOT EXISTS `bubble_index_timeseries` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `keywords` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `symbol` varchar(255) DEFAULT NULL,
  `dtype` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
