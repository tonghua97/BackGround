CREATE DATABASE foodfun;

USE foodfun;

CREATE TABLE `Test` (
  `id` int(11) NOT NULL auto_increment,
  `Name` varchar(200) NOT NULL,
  `content` mediumtext NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `Test` VALUES ('1', 'test1', 'test111');
INSERT INTO `Test` VALUES ('2', 'test2', 'test222');
INSERT INTO `Test` VALUES ('3', 'test3', 'test333');
