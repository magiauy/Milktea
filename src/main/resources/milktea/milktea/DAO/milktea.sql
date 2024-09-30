-- --------------------------------------------------------
-- Host:                         localhost
-- Server version:               10.4.32-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for milktea
CREATE DATABASE IF NOT EXISTS `milktea` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `milktea`;

-- Dumping structure for table milktea.category
CREATE TABLE IF NOT EXISTS `category` (
  `id` varchar(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.category: ~0 rows (approximately)

-- Dumping structure for table milktea.customer
CREATE TABLE IF NOT EXISTS `customer` (
  `id` varchar(10) NOT NULL,
  `point` decimal(20,6) DEFAULT NULL,
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.customer: ~0 rows (approximately)

-- Dumping structure for table milktea.employee
CREATE TABLE IF NOT EXISTS `employee` (
  `id` varchar(10) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `role` enum('ADMIN','MANAGER','CUSTOMER') DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.employee: ~1 rows (approximately)
INSERT INTO `employee` (`id`, `username`, `password`, `role`, `status`) VALUES
	('NV000', 'admin', '1', 'ADMIN', 'ACTIVE');

-- Dumping structure for table milktea.goodsreceipt
CREATE TABLE IF NOT EXISTS `goodsreceipt` (
  `id` varchar(10) NOT NULL,
  `providerId` varchar(10) DEFAULT NULL,
  `employeeId` varchar(10) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `total` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `providerId` (`providerId`),
  KEY `employeeId` (`employeeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.goodsreceipt: ~0 rows (approximately)

-- Dumping structure for table milktea.goodsreceiptdetail
CREATE TABLE IF NOT EXISTS `goodsreceiptdetail` (
  `goodsReceiptId` varchar(10) DEFAULT NULL,
  `ingredientId` varchar(10) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `price` decimal(20,6) DEFAULT NULL,
  `total` decimal(20,6) DEFAULT NULL,
  KEY `goodsReceiptId` (`goodsReceiptId`),
  KEY `ingredientId` (`ingredientId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.goodsreceiptdetail: ~0 rows (approximately)

-- Dumping structure for table milktea.ingredient
CREATE TABLE IF NOT EXISTS `ingredient` (
  `id` varchar(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `unit` enum('GRAM','KILOGRAM','LITER','MILLILITER') DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.ingredient: ~0 rows (approximately)

-- Dumping structure for table milktea.inventory
CREATE TABLE IF NOT EXISTS `inventory` (
  `inventoryId` varchar(10) NOT NULL,
  `ingredientId` varchar(10) DEFAULT NULL,
  `quantityInStock` double DEFAULT NULL,
  `lastUpdatedDate` date DEFAULT NULL,
  `expirationDate` date DEFAULT NULL,
  `price` decimal(20,6) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE','DISPOSED','OUTDATED') DEFAULT NULL,
  PRIMARY KEY (`inventoryId`),
  KEY `ingredientId` (`ingredientId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.inventory: ~0 rows (approximately)

-- Dumping structure for table milktea.inventoryreport
CREATE TABLE IF NOT EXISTS `inventoryreport` (
  `id` varchar(10) NOT NULL,
  `employeeId` varchar(10) DEFAULT NULL,
  `date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employeeId` (`employeeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.inventoryreport: ~0 rows (approximately)

-- Dumping structure for table milktea.inventoryreportdetail
CREATE TABLE IF NOT EXISTS `inventoryreportdetail` (
  `id` varchar(10) DEFAULT NULL,
  `inventoryId` varchar(10) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  KEY `id` (`id`),
  KEY `inventoryId` (`inventoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.inventoryreportdetail: ~0 rows (approximately)

-- Dumping structure for table milktea.invoice
CREATE TABLE IF NOT EXISTS `invoice` (
  `invoiceId` varchar(10) NOT NULL,
  `employeeId` varchar(10) DEFAULT NULL,
  `customerId` varchar(10) DEFAULT NULL,
  `promotionId` varchar(20) DEFAULT NULL,
  `issueDate` date DEFAULT NULL,
  `discount` decimal(20,6) DEFAULT NULL,
  `totalAmount` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`invoiceId`),
  KEY `employeeId` (`employeeId`),
  KEY `customerId` (`customerId`),
  KEY `promotionId` (`promotionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.invoice: ~0 rows (approximately)

-- Dumping structure for table milktea.invoicedetail
CREATE TABLE IF NOT EXISTS `invoicedetail` (
  `invoiceId` varchar(10) DEFAULT NULL,
  `productId` varchar(10) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unitPrice` decimal(20,6) DEFAULT NULL,
  `totalPrice` decimal(20,6) DEFAULT NULL,
  KEY `invoiceId` (`invoiceId`),
  KEY `productId` (`productId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.invoicedetail: ~0 rows (approximately)

-- Dumping structure for table milktea.person
CREATE TABLE IF NOT EXISTS `person` (
  `id` varchar(10) NOT NULL,
  `firstName` varchar(50) DEFAULT NULL,
  `lastName` varchar(50) DEFAULT NULL,
  `gender` enum('MALE','FEMALE','OTHER') DEFAULT NULL,
  `phoneNumber` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.person: ~1 rows (approximately)
INSERT INTO `person` (`id`, `firstName`, `lastName`, `gender`, `phoneNumber`) VALUES
	('NV000', 'admin', 'admin', 'MALE', '0339702533');

-- Dumping structure for table milktea.product
CREATE TABLE IF NOT EXISTS `product` (
  `productId` varchar(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `categoryId` varchar(10) DEFAULT NULL,
  `price` decimal(20,6) DEFAULT NULL,
  `Status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`productId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.product: ~0 rows (approximately)

-- Dumping structure for table milktea.promotion
CREATE TABLE IF NOT EXISTS `promotion` (
  `promotionProgramId` varchar(10) DEFAULT NULL,
  `promotionId` varchar(20) NOT NULL,
  `discountAmount` decimal(20,6) DEFAULT NULL,
  `minimumAmount` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`promotionId`),
  KEY `promotionProgramId` (`promotionProgramId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.promotion: ~0 rows (approximately)

-- Dumping structure for table milktea.promotionprogram
CREATE TABLE IF NOT EXISTS `promotionprogram` (
  `promotionProgramId` varchar(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  PRIMARY KEY (`promotionProgramId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.promotionprogram: ~0 rows (approximately)

-- Dumping structure for table milktea.provider
CREATE TABLE IF NOT EXISTS `provider` (
  `id` varchar(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `address` varchar(50) DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.provider: ~0 rows (approximately)

-- Dumping structure for table milktea.wastereport
CREATE TABLE IF NOT EXISTS `wastereport` (
  `id` varchar(10) NOT NULL,
  `employeeId` varchar(10) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `total` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `employeeId` (`employeeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.wastereport: ~0 rows (approximately)

-- Dumping structure for table milktea.wastereportdetail
CREATE TABLE IF NOT EXISTS `wastereportdetail` (
  `id` varchar(10) NOT NULL,
  `inventoryId` varchar(10) DEFAULT NULL,
  `quantity` double DEFAULT NULL,
  `price` decimal(20,6) DEFAULT NULL,
  `total` decimal(20,6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `inventoryId` (`inventoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.wastereportdetail: ~0 rows (approximately)

ALTER TABLE `customer`
    DROP FOREIGN KEY IF EXISTS `FK_customer_person`;
ALTER TABLE `employee`
    DROP FOREIGN KEY IF EXISTS `FK_employee_person`;
ALTER TABLE `goodsreceipt`
    DROP FOREIGN KEY IF EXISTS `FK_goodsreceipt_employee`,
    DROP FOREIGN KEY IF EXISTS `FK_goodsreceipt_provider`;
ALTER TABLE `goodsreceiptdetail`
    DROP FOREIGN KEY IF EXISTS `FK_goodsreceiptdetail_goodsreceipt`,
    DROP FOREIGN KEY IF EXISTS `FK_goodsreceiptdetail_ingredient`;
ALTER TABLE `inventory`
    DROP FOREIGN KEY IF EXISTS `FK_inventory_ingredient`;
ALTER TABLE `inventoryreport`
    DROP FOREIGN KEY IF EXISTS `FK_inventoryreport_employee`;
ALTER TABLE `inventoryreportdetail`
    DROP FOREIGN KEY IF EXISTS `FK_inventoryreportdetail_inventoryreport`,
    DROP FOREIGN KEY IF EXISTS `FK_inventoryreportdetail_inventory`;
ALTER TABLE `invoice`
    DROP FOREIGN KEY IF EXISTS `FK_invoice_customer`,
    DROP FOREIGN KEY IF EXISTS `FK_invoice_employee`,
    DROP FOREIGN KEY IF EXISTS `FK_invoice_promotion`;
ALTER TABLE `invoicedetail`
    DROP FOREIGN KEY IF EXISTS `FK_invoicedetail_invoice`,
    DROP FOREIGN KEY IF EXISTS `FK_invoicedetail_product`;
ALTER TABLE `promotion`
    DROP FOREIGN KEY IF EXISTS `FK_promotion_promotionprogram`;
ALTER TABLE `wastereport`
    DROP FOREIGN KEY IF EXISTS `FK_wastereport_employee`;
ALTER TABLE `wastereportdetail`
    DROP FOREIGN KEY IF EXISTS `FK_wastereportdetail_inventory`;

ALTER TABLE `customer`
    ADD CONSTRAINT `FK_customer_person` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `employee`
    ADD CONSTRAINT `FK_employee_person` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `goodsreceipt`
    ADD CONSTRAINT `FK_goodsreceipt_employee` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    ADD CONSTRAINT `FK_goodsreceipt_provider` FOREIGN KEY (`providerId`) REFERENCES `provider` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `goodsreceiptdetail`
    ADD CONSTRAINT `FK_goodsreceiptdetail_goodsreceipt` FOREIGN KEY (`goodsReceiptId`) REFERENCES `goodsreceipt` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    ADD CONSTRAINT `FK_goodsreceiptdetail_ingredient` FOREIGN KEY (`ingredientId`) REFERENCES `ingredient` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `inventory`
    ADD CONSTRAINT `FK_inventory_ingredient` FOREIGN KEY (`ingredientId`) REFERENCES `ingredient` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `inventoryreport`
    ADD CONSTRAINT `FK_inventoryreport_employee` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `inventoryreportdetail`
    ADD CONSTRAINT `FK_inventoryreportdetail_inventoryreport` FOREIGN KEY (`id`) REFERENCES `inventoryreport` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    ADD CONSTRAINT `FK_inventoryreportdetail_inventory` FOREIGN KEY (`inventoryId`) REFERENCES `inventory` (`inventoryId`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `invoice`
    ADD CONSTRAINT `FK_invoice_customer` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    ADD CONSTRAINT `FK_invoice_employee` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    ADD CONSTRAINT `FK_invoice_promotion` FOREIGN KEY (`promotionId`) REFERENCES `promotion` (`promotionProgramId`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `invoicedetail`
    ADD CONSTRAINT `FK_invoicedetail_invoice` FOREIGN KEY (`invoiceId`) REFERENCES `invoice` (`invoiceId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    ADD CONSTRAINT `FK_invoicedetail_product` FOREIGN KEY (`productId`) REFERENCES `product` (`productId`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `promotion`
    ADD CONSTRAINT `FK_promotion_promotionprogram` FOREIGN KEY (`promotionProgramId`) REFERENCES `promotionprogram` (`promotionProgramId`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `wastereport`
    ADD CONSTRAINT `FK_wastereport_employee` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `wastereportdetail`
    ADD CONSTRAINT `FK_wastereportdetail_inventory` FOREIGN KEY (`inventoryId`) REFERENCES `inventory` (`inventoryId`) ON DELETE NO ACTION ON UPDATE NO ACTION;


/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
