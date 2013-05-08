-- MySQL dump 10.13  Distrib 5.5.31, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: auctionhouse
-- ------------------------------------------------------
-- Server version	5.5.31-0ubuntu0.12.10.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

CREATE DATABASE IF NOT EXISTS auctionhouse;
USE auctionhouse;
--
-- Table structure for table `Services`
--

DROP TABLE IF EXISTS `ServicesSellers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ServicesSellers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `service` varchar(255) DEFAULT NULL,
  `cost` int(11) DEFAULT NULL,
  `currency` varchar(255) DEFAULT 'CHF',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ServicesSellers`
--

LOCK TABLES `ServicesSellers` WRITE;
/*!40000 ALTER TABLE `ServicesSellers` DISABLE KEYS */;
INSERT INTO `ServicesSellers` VALUES (1,'mozzie','music',120,'CHF'),(2,'mozzie','travelling',150,'CHF'),(3,'mozzie','opera',200,'CHF'),(4,'neal','books',1020,'CHF'),(5,'neal','travelling',150,'CHF'),(6,'neal','opera',200,'CHF'),(7,'ross','music',180,'CHF'),(8,'ross','travelling',150,'CHF'),(9,'ross','opera',200,'CHF');
/*!40000 ALTER TABLE `ServicesSellers` ENABLE KEYS */;
UNLOCK TABLES;

DROP TABLE IF EXISTS `ServicesBuyers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ServicesBuyers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `service` varchar(255) DEFAULT NULL,
  `launch_offer` varchar(255) DEFAULT 'no',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
--
LOCK TABLES `ServicesBuyers` WRITE;
/*!40000 ALTER TABLE `ServicesBuyers` DISABLE KEYS */;
INSERT INTO `ServicesBuyers` VALUES (1,'claudiu','books','no'),(2,'claudiu','travelling','no'),(3,'claudiu','music','no'),(4,'claudiu','opera','no'),(5,'rares','music','no'),(6,'rares','phones','no'),(7,'rares','travelling','no'),(8,'ioana','music','no'),(9,'ioana','travelling','no'), (10,'rachel','music','no'),(11,'rachel','travelling','no'),(12,'rachel','opera','no');
/*!40000 ALTER TABLE `ServicesBuyers` ENABLE KEYS */;
UNLOCK TABLES;

-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `status` varchar(11) NOT NULL DEFAULT 'inactive',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES (1,'claudiu','q1w2e3','buyer','inactive'),(2,'rares','q1w2e3','buyer','inactive'),(3,'ioana','q1w2e3','buyer','inactive'),(4,'mozzie','q1w2e3','seller','inactive'),(5,'neal','q1w2e3','seller','inactive'),(6,'ross','q1w2e3','seller','inactive'),(7,'rachel','q1w2e3','buyer','inactive');
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-05-06 14:56:16
