-- MySQL Administrator dump 1.4
--
-- ------------------------------------------------------
-- Server version	5.5.5-10.0.17-MariaDB


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


--
-- Create schema wire
--

CREATE DATABASE IF NOT EXISTS wire;
USE wire;

--
-- Definition of table `account`
--

DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `account_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account_password` varchar(200) NOT NULL DEFAULT '',
  `account_block` tinyint(1) NOT NULL DEFAULT '0',
  `account_wifi_ssid` varchar(32) DEFAULT NULL,
  `account_wifi_password` varchar(64) DEFAULT NULL,
  `account_key` varchar(16) NOT NULL DEFAULT '',
  `account_email` varchar(200) NOT NULL DEFAULT '',
  `account_time` datetime NOT NULL,
  `account_use` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`account_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `account`
--

/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` (`account_id`,`account_password`,`account_block`,`account_wifi_ssid`,`account_wifi_password`,`account_key`,`account_email`,`account_time`,`account_use`) VALUES 
 (1,'mE6eq4lQmg6QRWT+Pt8C9VGVneYOkBW3xzP2RWT5gwG5T0JTKprvO0WkgnXS5CKJ',0,'c','ABCabc123ZXCzxc','','0aVjQWFx2txQpiGGhWQquboSH+oLjOWi2w45jI1BK4aCAFf7b0pcg4jXznpCq3lp','0000-00-00 00:00:00',0),
 (2,'a+B1wYx8bz6DdjMX8/2NTCaQE8m0nEeN4Nx9P/zfN2R2MJr2Qjx+iyv7wWdsaCgG6pqaj+2j+cbJVeKp5Tdwwc5ricRoqLIwbis3fRqp6FM=',0,'E3P7atrick is a very good name','8KcHZssPJ7q4abLmAjmo2CYvqYYSEHSYGLqtXi0JI7dpxpe075D5FnrhLnFQC3i','','8KcHZssPJ7q4abLmAjmo2CYvqYYSEHSYGLqtXi0JI7dpxpe075D5FnrhLnFQC3iXTawU4PjYyClLVyS8zRN4885ricRoqLIwbis3fRqp6FM=','0000-00-00 00:00:00',0);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;


--
-- Definition of table `authority`
--

DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority` (
  `grouping_id` int(10) unsigned NOT NULL DEFAULT '0',
  `account_id` int(10) unsigned NOT NULL,
  KEY `FK_authority_1` (`grouping_id`) USING BTREE,
  KEY `FK_authority_2` (`account_id`) USING BTREE,
  CONSTRAINT `FK_authority_1` FOREIGN KEY (`grouping_id`) REFERENCES `grouping` (`grouping_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_authority_2` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `authority`
--

/*!40000 ALTER TABLE `authority` DISABLE KEYS */;
/*!40000 ALTER TABLE `authority` ENABLE KEYS */;


--
-- Definition of table `confirmation`
--

DROP TABLE IF EXISTS `confirmation`;
CREATE TABLE `confirmation` (
  `confirmation_email` varchar(100) NOT NULL,
  `account_pass` varchar(32) NOT NULL DEFAULT '',
  PRIMARY KEY (`confirmation_email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `confirmation`
--

/*!40000 ALTER TABLE `confirmation` DISABLE KEYS */;
INSERT INTO `confirmation` (`confirmation_email`,`account_pass`) VALUES 
 ('ab','\"a'),
 ('patrick.soebiantoro@gmail.com','a'),
 ('psoebiantoro2@gmail.com','a');
/*!40000 ALTER TABLE `confirmation` ENABLE KEYS */;


--
-- Definition of table `device`
--

DROP TABLE IF EXISTS `device`;
CREATE TABLE `device` (
  `device_id` varchar(20) NOT NULL DEFAULT '',
  `device_state` tinyint(1) DEFAULT NULL,
  `device_name` varchar(45) DEFAULT NULL,
  `account_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`device_id`),
  KEY `FK_device_1` (`account_id`),
  CONSTRAINT `FK_device_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `device`
--

/*!40000 ALTER TABLE `device` DISABLE KEYS */;
INSERT INTO `device` (`device_id`,`device_state`,`device_name`,`account_id`) VALUES 
 ('CC:50:E3:C7:64:3C-0',0,'',2),
 ('CC:50:E3:C7:64:3C-5',0,'',2),
 ('DC:4F:22:4C:74:50-0',0,'',2),
 ('DC:4F:22:4C:74:50-12',0,'',2),
 ('DC:4F:22:4C:74:50-13',0,'',2),
 ('DC:4F:22:4C:74:50-14',0,'',2),
 ('DC:4F:22:4C:74:50-15',0,'',2),
 ('DC:4F:22:4C:74:50-16',0,'',2),
 ('DC:4F:22:4C:74:50-2',0,'',2),
 ('DC:4F:22:4C:74:50-4',0,'',2),
 ('DC:4F:22:4C:74:50-5',0,'',2),
 ('IDDEVICE',0,'NAMADEVICE',2),
 ('IDDEVICE2',0,'NAMADEVICE',2),
 ('IDDEVICE3',0,'NAMADEVICE',2);
/*!40000 ALTER TABLE `device` ENABLE KEYS */;


--
-- Definition of table `grouping`
--

DROP TABLE IF EXISTS `grouping`;
CREATE TABLE `grouping` (
  `grouping_name` varchar(45) DEFAULT NULL,
  `grouping_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `account_id` int(10) unsigned DEFAULT NULL,
  PRIMARY KEY (`grouping_id`),
  KEY `FK_grouping_1` (`account_id`),
  CONSTRAINT `FK_grouping_1` FOREIGN KEY (`account_id`) REFERENCES `account` (`account_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `grouping`
--

/*!40000 ALTER TABLE `grouping` DISABLE KEYS */;
INSERT INTO `grouping` (`grouping_name`,`grouping_id`,`account_id`) VALUES 
 ('a',1,NULL),
 ('b',2,NULL);
/*!40000 ALTER TABLE `grouping` ENABLE KEYS */;


--
-- Definition of table `member`
--

DROP TABLE IF EXISTS `member`;
CREATE TABLE `member` (
  `grouping_id` int(10) unsigned NOT NULL DEFAULT '0',
  `device_id` varchar(20) NOT NULL DEFAULT '',
  KEY `FK_member_2` (`device_id`),
  KEY `FK_member_1` (`grouping_id`) USING BTREE,
  CONSTRAINT `FK_member_1` FOREIGN KEY (`grouping_id`) REFERENCES `grouping` (`grouping_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_member_2` FOREIGN KEY (`device_id`) REFERENCES `device` (`device_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `member`
--

/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` (`grouping_id`,`device_id`) VALUES 
 (1,'IDDEVICE'),
 (2,'IDDEVICE2'),
 (1,'IDDEVICE3');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;


--
-- Definition of table `timer`
--

DROP TABLE IF EXISTS `timer`;
CREATE TABLE `timer` (
  `timer_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `timer_start` time DEFAULT NULL,
  `timer_state` tinyint(1) DEFAULT NULL,
  `timer_d0` tinyint(1) DEFAULT NULL,
  `timer_d1` tinyint(1) DEFAULT NULL,
  `timer_d2` tinyint(1) DEFAULT NULL,
  `timer_d3` tinyint(1) DEFAULT NULL,
  `timer_d4` tinyint(1) DEFAULT NULL,
  `timer_d5` tinyint(1) DEFAULT NULL,
  `timer_d6` tinyint(1) DEFAULT NULL,
  `grouping_id` int(10) unsigned NOT NULL DEFAULT '0',
  `timer_name` varchar(45) NOT NULL DEFAULT '',
  `timer_action` tinyint(3) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`timer_id`),
  KEY `FK_timer_1` (`grouping_id`) USING BTREE,
  CONSTRAINT `FK_timer_1` FOREIGN KEY (`grouping_id`) REFERENCES `grouping` (`grouping_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `timer`
--

/*!40000 ALTER TABLE `timer` DISABLE KEYS */;
INSERT INTO `timer` (`timer_id`,`timer_start`,`timer_state`,`timer_d0`,`timer_d1`,`timer_d2`,`timer_d3`,`timer_d4`,`timer_d5`,`timer_d6`,`grouping_id`,`timer_name`,`timer_action`) VALUES 
 (1,'22:32:00',1,1,0,0,0,0,0,0,1,'ta',0),
 (2,'22:32:00',1,1,0,0,0,0,0,0,1,'ta',0),
 (3,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,'tb',0);
/*!40000 ALTER TABLE `timer` ENABLE KEYS */;


--
-- Definition of table `traffic`
--

DROP TABLE IF EXISTS `traffic`;
CREATE TABLE `traffic` (
  `traffic_ip` varchar(15) NOT NULL DEFAULT '',
  `account_id` int(10) unsigned DEFAULT NULL,
  `traffic_block` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`traffic_ip`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `traffic`
--

/*!40000 ALTER TABLE `traffic` DISABLE KEYS */;
INSERT INTO `traffic` (`traffic_ip`,`account_id`,`traffic_block`) VALUES 
 ('\0ûºéUŽºý!)B…«Ç',1,0),
 ('....',0,0);
/*!40000 ALTER TABLE `traffic` ENABLE KEYS */;


--
-- Definition of table `traffic_detail`
--

DROP TABLE IF EXISTS `traffic_detail`;
CREATE TABLE `traffic_detail` (
  `traffic_ip` varchar(15) NOT NULL,
  `traffic_time` datetime NOT NULL,
  KEY `FK_traffic_detail_1` (`traffic_ip`),
  CONSTRAINT `FK_traffic_detail_1` FOREIGN KEY (`traffic_ip`) REFERENCES `traffic` (`traffic_ip`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `traffic_detail`
--

/*!40000 ALTER TABLE `traffic_detail` DISABLE KEYS */;
INSERT INTO `traffic_detail` (`traffic_ip`,`traffic_time`) VALUES 
 ('....','2019-02-22 18:44:11'),
 ('....','2019-02-22 18:44:14'),
 ('....','2019-02-22 18:44:15'),
 ('....','2019-02-22 18:44:15'),
 ('....','2019-02-22 18:44:15'),
 ('....','2019-02-22 18:44:15'),
 ('....','2019-02-22 18:44:16'),
 ('....','2019-02-22 18:44:16');
/*!40000 ALTER TABLE `traffic_detail` ENABLE KEYS */;




/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
