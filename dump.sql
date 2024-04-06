-- MySQL dump 10.13  Distrib 5.7.21, for Linux (x86_64)
--
-- Host: localhost    Database: spring_microservices
-- ------------------------------------------------------
-- Server version	5.7.21

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

--
-- Table structure for table `clients`
--

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients` (
  `client_cod` bigint(20) NOT NULL AUTO_INCREMENT,
  `client_name` varchar(100) DEFAULT NULL,
  `client_email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`client_cod`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Informacion de los clientes registrados para comprar';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `exchange_transactions`
--

DROP TABLE IF EXISTS `exchange_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exchange_transactions` (
  `exchange_transaction_cod` bigint(20) NOT NULL AUTO_INCREMENT,
  `transaction_type` enum('BUY','SELL') NOT NULL COMMENT 'Tipo de operacion transaccional para compra/venta',
  `date` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de insercion del registro',
  `client_cod` bigint(20) NOT NULL,
  `product_cod` bigint(20) DEFAULT NULL,
  `value` double NOT NULL,
  `quantity` int(11) DEFAULT NULL,
  PRIMARY KEY (`exchange_transaction_cod`),
  KEY `exchange_transactions_products_FK` (`product_cod`),
  KEY `exchange_transactions_clients_FK` (`client_cod`),
  CONSTRAINT `exchange_transactions_clients_FK` FOREIGN KEY (`client_cod`) REFERENCES `clients` (`client_cod`),
  CONSTRAINT `exchange_transactions_products_FK` FOREIGN KEY (`product_cod`) REFERENCES `products` (`product_cod`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Tabla que almacena las operaciones de compra/venta de productos';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `exchange_transactions`
--

LOCK TABLES `exchange_transactions` WRITE;
/*!40000 ALTER TABLE `exchange_transactions` DISABLE KEYS */;
/*!40000 ALTER TABLE `exchange_transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `product_cod` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(250) NOT NULL COMMENT 'Nombre del producto',
  `description` varchar(255) DEFAULT NULL COMMENT 'Descripcion del producto',
  `creation_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `price` double DEFAULT NULL,
  `stock` bigint(20) DEFAULT '0',
  PRIMARY KEY (`product_cod`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Tabla con informacion de los productos del stock';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-06 20:46:20
