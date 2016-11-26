CREATE DATABASE foodfun;

USE foodfun;

CREATE TABLE `Good` (
  `id` int(11) NOT NULL auto_increment,
  `Image` varchar(200) NOT NULL,
  `Name` varchar(200) NOT NULL,
  `Price` float NOT NULL,
  `Inventory` int NOT NULL,
  `content` mediumtext NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO `Good` VALUES ('1', 'xudianchi', '蓄电池',450.00,132,'蓄电池蓄电池蓄电池');
INSERT INTO `Good` VALUES ('2', 'qichejiyou1', '汽车机油',120.00,452,'汽车机油汽车机油');
INSERT INTO `Good` VALUES ('3', 'yushua', '雨刷',50.00,322,'雨刷雨刷');

CREATE TABLE `orders` (
  `id` int(11) NOT NULL auto_increment,
  `GoodId` int(11) NOT NULL,
  `Money` Double NOT NULL,
  `Number` int NOT NULL,
  `Time` varchar(20) NOT NULL,
  `Zhuangtai` char(20) NOT NULL,
  `username` varchar(200) NOT NULL,
  `address` mediumtext NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `orders` VALUES('1','2',240.00,2,'2016-11-03','已支付','test','河北省石家庄市河北师范大学');

CREATE TABLE `user` (
  `id` int(11) NOT NULL auto_increment,
  `username` char(20) NOT NULL,
  `password` char(20) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `user` VALUES('1','test','123456');

CREATE TABLE `address` (
  `id` int(11) NOT NULL auto_increment,
  `username` char(20) NOT NULL,
  `address` mediumtext NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `address` VALUES('1','test','河北省石家庄市河北师范大学');