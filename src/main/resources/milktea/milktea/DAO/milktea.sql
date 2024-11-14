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

-- Dumping data for table milktea.category: ~4 rows (approximately)
INSERT IGNORE INTO `category` (`id`, `name`) VALUES
	('C001', 'Trà sữa'),
	('C002', 'Trà trái cây'),
	('C003', 'Cafe'),
	('C004', 'Đá xây'),
	('C005', 'Topping');

-- Dumping structure for table milktea.customer
CREATE TABLE IF NOT EXISTS `customer` (
  `id` varchar(10) NOT NULL,
  `point` decimal(20,0) DEFAULT NULL,
  KEY `id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- Dumping data for table milktea.customer: ~10 rows (approximately)
INSERT IGNORE INTO `customer` (`id`, `point`) VALUES
	('KH000', 2000),
	('KH001', 4000),
	('KH003', 0),
	('KH004', 0),
	('KH005', 0),
	('KH006', 0),
	('KH007', 0),
	('KH008', 0),
	('KH002', 2000),
	('KH009', 0);

-- Dumping structure for table milktea.employee
CREATE TABLE IF NOT EXISTS `employee` (
  `id` varchar(10) NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `roleId` varchar(10) DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  KEY `id` (`id`),
  KEY `roleId` (`roleId`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.employee: ~4 rows (approximately)
INSERT IGNORE INTO `employee` (`id`, `username`, `password`, `roleId`, `status`) VALUES
	('NV000', 'owner', '1', 'R01', 'ACTIVE'),
	('NV001', 'admin', '1', 'R02', 'ACTIVE'),
	('NV002', 'nhanvien', '1', 'R03', 'ACTIVE'),
	('NV003', 'NV003', '123456', 'R02', 'ACTIVE');

-- Dumping structure for table milktea.goodsreceipt
CREATE TABLE IF NOT EXISTS `goodsreceipt` (
  `id` varchar(10) NOT NULL,
  `providerId` varchar(10) DEFAULT NULL,
  `employeeId` varchar(10) DEFAULT NULL,
  `date` date DEFAULT NULL,
  `total` decimal(20,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `providerId` (`providerId`),
  KEY `employeeId` (`employeeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.goodsreceipt: ~1 rows (approximately)
INSERT IGNORE INTO `goodsreceipt` (`id`, `providerId`, `employeeId`, `date`, `total`) VALUES
	('GRN0001', 'PR002', 'NV002', '2024-11-12', 320000),
	('GRN002', 'PR037', 'NV002', '2024-11-12', 2000000);

-- Dumping structure for table milktea.goodsreceiptdetail
CREATE TABLE IF NOT EXISTS `goodsreceiptdetail` (
  `goodsReceiptId` varchar(10) DEFAULT NULL,
  `ingredientId` varchar(10) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `unit` enum('GRAM','KILOGRAM','LITER','MILLILITER') DEFAULT NULL,
  `price` decimal(20,0) DEFAULT NULL,
  `total` decimal(20,0) DEFAULT NULL,
  KEY `goodsReceiptId` (`goodsReceiptId`),
  KEY `ingredientId` (`ingredientId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.goodsreceiptdetail: ~2 rows (approximately)
INSERT IGNORE INTO `goodsreceiptdetail` (`goodsReceiptId`, `ingredientId`, `quantity`, `unit`, `price`, `total`) VALUES
	('GRN0001', 'I001', 10, 'KILOGRAM', 32000, 320000),
	('GRN002', 'I020', 100, 'KILOGRAM', 20000, 2000000);

-- Dumping structure for table milktea.ingredient
CREATE TABLE IF NOT EXISTS `ingredient` (
  `id` varchar(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `quantity` float DEFAULT NULL,
  `unit` enum('GRAM','KILOGRAM','LITER','MILLILITER') DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.ingredient: ~74 rows (approximately)
INSERT IGNORE INTO `ingredient` (`id`, `name`, `quantity`, `unit`, `status`) VALUES
	('I001', 'Đường trắng', 605, 'GRAM', 'ACTIVE'),
	('I002', 'Trà đen', 410, 'GRAM', 'ACTIVE'),
	('I003', 'Trà xanh', 440, 'GRAM', 'ACTIVE'),
	('I004', 'Sữa tươi', 17.75, 'LITER', 'ACTIVE'),
	('I005', 'Sữa đặc', 15, 'LITER', 'ACTIVE'),
	('I006', 'Bột matcha', 180, 'GRAM', 'ACTIVE'),
	('I007', 'Bột cacao', 142, 'GRAM', 'ACTIVE'),
	('I008', 'Trân châu đen', 2350, 'GRAM', 'ACTIVE'),
	('I009', 'Trân châu trắng', 3000, 'GRAM', 'INACTIVE'),
	('I010', 'Thạch dừa', 4000, 'GRAM', 'ACTIVE'),
	('I011', 'Thạch cà phê', 3000, 'GRAM', 'INACTIVE'),
	('I012', 'Syrup dâu', 2, 'LITER', 'ACTIVE'),
	('I013', 'Syrup chanh dây', 1.5, 'LITER', 'ACTIVE'),
	('I014', 'Syrup táo xanh', 1.2, 'LITER', 'ACTIVE'),
	('I015', 'Syrup xoài', 1.7, 'LITER', 'ACTIVE'),
	('I016', 'Syrup vải', 2, 'LITER', 'ACTIVE'),
	('I017', 'Syrup kiwi', 1, 'LITER', 'ACTIVE'),
	('I018', 'Syrup nho', 2, 'LITER', 'INACTIVE'),
	('I019', 'Bột sữa béo', 1000, 'GRAM', 'ACTIVE'),
	('I020', 'Lá bạc hà', 100, 'GRAM', 'ACTIVE'),
	('I021', 'Chanh tươi', 4.95, 'KILOGRAM', 'ACTIVE'),
	('I022', 'Đá viên', 48.65, 'KILOGRAM', 'ACTIVE'),
	('I023', 'Siro caramel', 1, 'LITER', 'ACTIVE'),
	('I024', 'Bột trà xanh Nhật', 500, 'GRAM', 'ACTIVE'),
	('I025', 'Đậu đỏ', 2, 'KILOGRAM', 'ACTIVE'),
	('I026', 'Trân châu hoàng kim', 2000, 'GRAM', 'ACTIVE'),
	('I027', 'Bột pudding trứng', 800, 'GRAM', 'ACTIVE'),
	('I028', 'Bột pudding dừa', 800, 'GRAM', 'INACTIVE'),
	('I029', 'Bột pudding khoai môn', 800, 'GRAM', 'ACTIVE'),
	('I030', 'Lá trà oolong', 295, 'GRAM', 'ACTIVE'),
	('I031', 'Syrup đào', 1.5, 'LITER', 'ACTIVE'),
	('I032', 'Syrup dừa', 0.9, 'LITER', 'INACTIVE'),
	('I033', 'Bột trà sữa', 1.95, 'KILOGRAM', 'ACTIVE'),
	('I034', 'Bột bạc hà', 100, 'GRAM', 'ACTIVE'),
	('I035', 'Thạch lựu', 2450, 'GRAM', 'ACTIVE'),
	('I036', 'Thạch chanh dây', 2000, 'GRAM', 'ACTIVE'),
	('I037', 'Hạt thủy tinh dâu', 3000, 'GRAM', 'ACTIVE'),
	('I038', 'Hạt thủy tinh xoài', 3000, 'GRAM', 'INACTIVE'),
	('I039', 'Hạt thủy tinh việt quất', 3000, 'GRAM', 'ACTIVE'),
	('I040', 'Bột ngũ cốc', 1, 'KILOGRAM', 'ACTIVE'),
	('I041', 'Kem sữa mặn', 1.5, 'KILOGRAM', 'ACTIVE'),
	('I042', 'Kem sữa ngọt', 1.5, 'KILOGRAM', 'INACTIVE'),
	('I043', 'Bột dừa', 500, 'GRAM', 'ACTIVE'),
	('I044', 'Nước cốt dừa', 3, 'LITER', 'ACTIVE'),
	('I045', 'Bột năng', 1.2, 'KILOGRAM', 'ACTIVE'),
	('I046', 'Tinh dầu dừa', 0.5, 'LITER', 'ACTIVE'),
	('I047', 'Tinh dầu bạc hà', 0.2, 'LITER', 'INACTIVE'),
	('I048', 'Syrup sữa chua', 2, 'LITER', 'ACTIVE'),
	('I049', 'Syrup nho đen', 1, 'LITER', 'ACTIVE'),
	('I050', 'Bột trà ô long', 700, 'GRAM', 'ACTIVE'),
	('I051', 'Hạt cà phê Arabica', 5, 'KILOGRAM', 'ACTIVE'),
	('I052', 'Hạt cà phê Robusta', 5, 'KILOGRAM', 'ACTIVE'),
	('I053', 'Cà phê hòa tan', 2, 'KILOGRAM', 'ACTIVE'),
	('I054', 'Syrup hương cà phê', 1, 'LITER', 'ACTIVE'),
	('I055', 'Syrup mocha', 1, 'LITER', 'ACTIVE'),
	('I056', 'Bột vani', 1, 'KILOGRAM', 'ACTIVE'),
	('I057', 'Bột quế', 1, 'KILOGRAM', 'ACTIVE'),
	('I058', 'Syrup hạt phỉ', 1, 'LITER', 'ACTIVE'),
	('I059', 'Bột kem béo cà phê', 2, 'KILOGRAM', 'ACTIVE'),
	('I060', 'Hạt chia', 500, 'GRAM', 'ACTIVE'),
	('I061', 'Hạt quinoa', 300, 'GRAM', 'ACTIVE'),
	('I062', 'Sữa hạnh nhân', 1.5, 'LITER', 'ACTIVE'),
	('I063', 'Sữa yến mạch', 1, 'LITER', 'ACTIVE'),
	('I064', 'Bột dừa', 200, 'GRAM', 'ACTIVE'),
	('I065', 'Syrup chanh leo', 1, 'LITER', 'ACTIVE'),
	('I066', 'Bột chuối', 300, 'GRAM', 'ACTIVE'),
	('I067', 'Syrup bạc hà', 1, 'LITER', 'ACTIVE'),
	('I068', 'Hạt chia đen', 100, 'GRAM', 'ACTIVE'),
	('I069', 'Bột cacao nguyên chất', 200, 'GRAM', 'ACTIVE'),
	('I070', 'Bột yến mạch', 1, 'KILOGRAM', 'ACTIVE'),
	('I071', 'Hạt hướng dương', 500, 'GRAM', 'ACTIVE'),
	('I072', 'Syrup việt quất', 1, 'LITER', 'ACTIVE'),
	('I073', 'Syrup dừa nướng', 1, 'LITER', 'ACTIVE'),
	('I074', 'Nước ép dứa', 1, 'LITER', 'ACTIVE');

-- Dumping structure for table milktea.invoice
CREATE TABLE IF NOT EXISTS `invoice` (
  `invoiceId` varchar(10) NOT NULL,
  `employeeId` varchar(10) DEFAULT NULL,
  `customerId` varchar(10) DEFAULT NULL,
  `promotionId` varchar(20) DEFAULT NULL,
  `issueDate` date DEFAULT NULL,
  `discount` decimal(20,0) DEFAULT NULL,
  `total` decimal(20,0) DEFAULT NULL,
  PRIMARY KEY (`invoiceId`),
  KEY `employeeId` (`employeeId`),
  KEY `customerId` (`customerId`),
  KEY `promotionId` (`promotionId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.invoice: ~1 rows (approximately)
INSERT IGNORE INTO `invoice` (`invoiceId`, `employeeId`, `customerId`, `promotionId`, `issueDate`, `discount`, `total`) VALUES
	('HD0001', 'NV001', 'KH009', 'KM001', '2024-11-08', 0, 100000);

-- Dumping structure for table milktea.invoicedetail
CREATE TABLE IF NOT EXISTS `invoicedetail` (
  `invoiceId` varchar(10) DEFAULT NULL,
  `productId` varchar(10) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unitPrice` decimal(20,0) DEFAULT NULL,
  `totalPrice` decimal(20,0) DEFAULT NULL,
  KEY `invoiceId` (`invoiceId`),
  KEY `productId` (`productId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.invoicedetail: ~1 rows (approximately)
INSERT IGNORE INTO `invoicedetail` (`invoiceId`, `productId`, `quantity`, `unitPrice`, `totalPrice`) VALUES
	('HD0001', 'P021', 10, 1000, 100000);

-- Dumping structure for table milktea.person
CREATE TABLE IF NOT EXISTS `person` (
  `id` varchar(10) NOT NULL,
  `firstName` varchar(50) DEFAULT NULL,
  `lastName` varchar(50) DEFAULT NULL,
  `gender` enum('MALE','FEMALE','OTHER') DEFAULT NULL,
  `phoneNumber` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.person: ~14 rows (approximately)
INSERT IGNORE INTO `person` (`id`, `firstName`, `lastName`, `gender`, `phoneNumber`) VALUES
	('KH000', '', 'Vãng Lai', 'MALE', '0333902351'),
	('KH001', 'Uy', 'UyD', 'MALE', '0339702531'),
	('KH002', 'Uy', 'Uy', 'FEMALE', '0339806231'),
	('KH003', 'Hello', 'Hello', 'FEMALE', '0986578543'),
	('KH004', 'oiii', 'ol', 'FEMALE', '0339876543'),
	('KH005', 'Test', 'Test', 'FEMALE', '0339875472'),
	('KH006', 'Test', 'Test', 'FEMALE', '0986356811'),
	('KH007', 'Uy', 'Uy', 'MALE', '0338787652'),
	('KH008', 'Ma', 'Gia Uy', 'MALE', '0339702532'),
	('KH009', 'Ma', 'Gia Uy', 'MALE', '0318237162'),
	('NV000', 'owner', 'owner', 'MALE', '0339702533'),
	('NV001', 'admin', 'admin', 'MALE', '0338098321'),
	('NV002', 'nhanvien', 'nhanvien', 'MALE', '0339862531'),
	('NV003', 'Mã Gia', 'Uy', 'FEMALE', '0339702531');

-- Dumping structure for table milktea.product
CREATE TABLE IF NOT EXISTS `product` (
  `productId` varchar(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `categoryId` varchar(10) DEFAULT NULL,
  `price` decimal(20,0) DEFAULT NULL,
  `Status` enum('ACTIVE','INACTIVE') DEFAULT 'ACTIVE',
  PRIMARY KEY (`productId`),
  KEY `categoryId` (`categoryId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.product: ~52 rows (approximately)
INSERT IGNORE INTO `product` (`productId`, `name`, `categoryId`, `price`, `Status`) VALUES
	('P001', 'Trà sữa trân châu đường đen', 'C001', 40000, 'ACTIVE'),
	('P002', 'Trà sữa matcha', 'C001', 48000, 'ACTIVE'),
	('P003', 'Trà sữa socola', 'C002', 47000, 'ACTIVE'),
	('P004', 'Trà sữa ô long', 'C001', 50000, 'ACTIVE'),
	('P005', 'Trà sữa khoai môn', 'C001', 49000, 'ACTIVE'),
	('P006', 'Trà sữa dâu tây', 'C001', 45000, 'ACTIVE'),
	('P007', 'Trà sữa caramel', 'C001', 46000, 'INACTIVE'),
	('P008', 'Trà sữa sầu riêng', 'C001', 52000, 'ACTIVE'),
	('P009', 'Trà sữa bạc hà', 'C001', 47000, 'ACTIVE'),
	('P010', 'Trà sữa hạt dẻ', 'C001', 48000, 'ACTIVE'),
	('P011', 'Trà đào cam sả', 'C002', 55000, 'ACTIVE'),
	('P012', 'Trà chanh dây', 'C002', 52000, 'ACTIVE'),
	('P013', 'Trà táo xanh', 'C002', 53000, 'ACTIVE'),
	('P014', 'Trà vải', 'C002', 50000, 'ACTIVE'),
	('P015', 'Trà xoài', 'C002', 51000, 'INACTIVE'),
	('P016', 'Trà chanh tươi', 'C002', 45000, 'ACTIVE'),
	('P017', 'Trà kiwi', 'C002', 54000, 'ACTIVE'),
	('P018', 'Trà nho', 'C002', 52000, 'ACTIVE'),
	('P019', 'Trà việt quất', 'C002', 53000, 'ACTIVE'),
	('P020', 'Trà chanh mật ong', 'C002', 48000, 'ACTIVE'),
	('P021', 'Cafe sữa đá', 'C003', 30000, 'ACTIVE'),
	('P022', 'Cafe đen đá', 'C003', 25000, 'ACTIVE'),
	('P023', 'Cafe bạc xỉu', 'C003', 32000, 'ACTIVE'),
	('P024', 'Espresso', 'C003', 35000, 'ACTIVE'),
	('P025', 'Americano', 'C003', 34000, 'ACTIVE'),
	('P026', 'Cappuccino', 'C003', 40000, 'INACTIVE'),
	('P027', 'Latte', 'C003', 42000, 'ACTIVE'),
	('P028', 'Mocha', 'C003', 45000, 'ACTIVE'),
	('P029', 'Caramel Macchiato', 'C003', 46000, 'ACTIVE'),
	('P030', 'Cafe trứng', 'C003', 50000, 'ACTIVE'),
	('P031', 'Smoothie dâu', 'C004', 55000, 'ACTIVE'),
	('P032', 'Smoothie xoài', 'C004', 56000, 'ACTIVE'),
	('P033', 'Smoothie việt quất', 'C004', 58000, 'ACTIVE'),
	('P034', 'Smoothie chuối', 'C004', 57000, 'ACTIVE'),
	('P035', 'Smoothie bơ', 'C004', 59000, 'INACTIVE'),
	('P036', 'Smoothie kiwi', 'C004', 60000, 'ACTIVE'),
	('P037', 'Smoothie chanh dây', 'C004', 58000, 'ACTIVE'),
	('P038', 'Smoothie cam', 'C004', 57000, 'ACTIVE'),
	('P039', 'Smoothie táo', 'C004', 60000, 'ACTIVE'),
	('P040', 'Smoothie đào', 'C004', 59000, 'ACTIVE'),
	('P041', 'Trân châu đen', 'C005', 5000, 'ACTIVE'),
	('P042', 'Trân châu trắng', 'C005', 5000, 'INACTIVE'),
	('P043', 'Thạch dừa', 'C005', 4000, 'ACTIVE'),
	('P044', 'Thạch cà phê', 'C005', 4000, 'ACTIVE'),
	('P045', 'Hạt thủy tinh dâu', 'C005', 6000, 'ACTIVE'),
	('P046', 'Hạt thủy tinh xoài', 'C005', 6000, 'INACTIVE'),
	('P047', 'Hạt thủy tinh việt quất', 'C005', 6000, 'ACTIVE'),
	('P048', 'Pudding trứng', 'C005', 7000, 'ACTIVE'),
	('P049', 'Pudding dừa', 'C005', 7000, 'INACTIVE'),
	('P050', 'Pudding khoai môn', 'C005', 7000, 'ACTIVE'),
	('P051', 'Matcha sữa gấu', 'C001', 55000, 'INACTIVE'),
	('P052', 'Khoai môn latte', 'C001', 10000, 'ACTIVE');

-- Dumping structure for table milktea.promotion
CREATE TABLE IF NOT EXISTS `promotion` (
  `promotionProgramId` varchar(10) DEFAULT NULL,
  `promotionId` varchar(20) NOT NULL,
  `discount` decimal(20,0) DEFAULT NULL,
  `minimumPrice` decimal(20,0) DEFAULT NULL,
  PRIMARY KEY (`promotionId`),
  KEY `promotionProgramId` (`promotionProgramId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.promotion: ~4 rows (approximately)
INSERT IGNORE INTO `promotion` (`promotionProgramId`, `promotionId`, `discount`, `minimumPrice`) VALUES
	('CTKM0002', 'KM00002', 10000, 222),
	('CTKM0001', 'KM001', 10000, 0),
	('CTKM0002', 'KM0011', 10000, 12),
	('CTKM0000', 'NoPromotion', 0, 0);

-- Dumping structure for table milktea.promotionprogram
CREATE TABLE IF NOT EXISTS `promotionprogram` (
  `promotionProgramId` varchar(10) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `startDate` date DEFAULT NULL,
  `endDate` date DEFAULT NULL,
  PRIMARY KEY (`promotionProgramId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.promotionprogram: ~3 rows (approximately)
INSERT IGNORE INTO `promotionprogram` (`promotionProgramId`, `name`, `startDate`, `endDate`) VALUES
	('CTKM0000', 'Mặc định', '0001-02-24', '9999-12-06'),
	('CTKM0001', 'Sale Thu Đông', '2024-07-01', '2024-12-31'),
	('CTKM0002', 'ADmin', '2024-11-15', '2024-11-15');

-- Dumping structure for table milktea.provider
CREATE TABLE IF NOT EXISTS `provider` (
  `id` varchar(10) NOT NULL,
  `name` char(50) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `phone` varchar(15) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.provider: ~50 rows (approximately)
INSERT IGNORE INTO `provider` (`id`, `name`, `address`, `phone`, `email`) VALUES
	('PR001', 'Công Ty TNHH Nguyên Liệu Sữa', 'Số 15, Đường Lý Thái Tổ, Quận 10, TP. Hồ Chí Minh', '0901234567', 'contact@nlmilk.vn'),
	('PR002', 'Công Ty TNHH Đường Việt', 'Số 25, Đường Trần Hưng Đạo, Quận 5, TP. Hồ Chí Minh', '0912345678', 'info@duongviet.com.vn'),
	('PR003', 'Công Ty TNHH Thạch Rau Câu', 'Số 45, Đường Hoàng Văn Thụ, Quận Phú Nhuận, TP. Hồ Chí Minh', '0933456789', 'sales@thachraucau.com.vn'),
	('PR004', 'Công Ty TNHH Hương Liệu An Việt', 'Số 12, Đường Võ Văn Tần, Quận 3, TP. Hồ Chí Minh', '0975678901', 'contact@huonglieuanviet.vn'),
	('PR005', 'Công Ty Cổ Phần Sữa Nguyên Chất', 'Số 98, Đường Nguyễn Trãi, Quận 1, TP. Hồ Chí Minh', '0909876543', 'contact@suanguyenchat.vn'),
	('PR006', 'Công Ty TNHH Bột Trà Xanh', 'Số 34, Đường Cách Mạng Tháng 8, Quận Tân Bình, TP. Hồ Chí Minh', '0932789012', 'sales@bottraxanh.vn'),
	('PR007', 'Công Ty TNHH Đường Mía', 'Số 67, Đường Nguyễn Đình Chiểu, Quận 1, TP. Hồ Chí Minh', '0908765432', 'info@duongmia.com'),
	('PR008', 'Công Ty TNHH Cacao Việt', 'Số 23, Đường Lê Lợi, Quận 1, TP. Hồ Chí Minh', '0933456123', 'sales@cacaovietnam.com.vn'),
	('PR009', 'Công Ty TNHH Trái Cây Tươi', 'Số 56, Đường Nguyễn Thị Minh Khai, Quận 3, TP. Hồ Chí Minh', '0912341234', 'info@traicaytuoi.vn'),
	('PR010', 'Công Ty TNHH Sữa Ong Chúa', 'Số 78, Đường Bà Huyện Thanh Quan, Quận 3, TP. Hồ Chí Minh', '0923456789', 'contact@suaongchua.vn'),
	('PR011', 'Công Ty TNHH Hạt Dinh Dưỡng', 'Số 10, Đường Lê Văn Sỹ, Quận 3, TP. Hồ Chí Minh', '0901236789', 'info@hatdinhduong.vn'),
	('PR012', 'Công Ty TNHH Đá Gel', 'Số 67, Đường Nguyễn Thị Minh Khai, Quận 1, TP. Hồ Chí Minh', '0932457891', 'support@dagel.vn'),
	('PR013', 'Công Ty TNHH Trái Cây Đông Lạnh', 'Số 90, Đường Nguyễn Trãi, Quận 5, TP. Hồ Chí Minh', '0909871234', 'contact@traicaydonglanh.vn'),
	('PR014', 'Công Ty TNHH Cà Phê Hòa Tan', 'Số 12, Đường Lê Duẩn, Quận 1, TP. Hồ Chí Minh', '0945671234', 'sales@caphehoatan.vn'),
	('PR015', 'Công Ty TNHH Đường Cát', 'Số 45, Đường Phan Đình Phùng, Quận Phú Nhuận, TP. Hồ Chí Minh', '0932456123', 'info@duongcat.vn'),
	('PR016', 'Công Ty TNHH Trân Châu', 'Số 33, Đường Điện Biên Phủ, Quận Bình Thạnh, TP. Hồ Chí Minh', '0971234567', 'support@tranchau.vn'),
	('PR017', 'Công Ty TNHH Bột Khoai Môn', 'Số 23, Đường Hai Bà Trưng, Quận 1, TP. Hồ Chí Minh', '0905678901', 'info@botkhoaimon.vn'),
	('PR018', 'Công Ty TNHH Trà Sữa Đài Loan', 'Số 88, Đường Trần Phú, Quận 5, TP. Hồ Chí Minh', '0913456789', 'contact@trasuadaiwan.vn'),
	('PR019', 'Công Ty TNHH Topping Tươi', 'Số 14, Đường Lê Hồng Phong, Quận 10, TP. Hồ Chí Minh', '0943214567', 'info@toppingtuoi.vn'),
	('PR020', 'Công Ty TNHH Dụng Cụ Pha Chế', 'Số 77, Đường Ngô Gia Tự, Quận 10, TP. Hồ Chí Minh', '0932345678', 'sales@dungcuphache.vn'),
	('PR021', 'Công Ty TNHH Sữa Tươi Nguyên Chất', 'Số 21, Đường Lý Thường Kiệt, Quận 10, TP. Hồ Chí Minh', '0909876543', 'info@suatuyet.vn'),
	('PR022', 'Công Ty TNHH Thạch Trân Châu', 'Số 33, Đường Phạm Văn Đồng, Quận Gò Vấp, TP. Hồ Chí Minh', '0908765432', 'sales@thachtranchau.vn'),
	('PR023', 'Công Ty TNHH Đường Cát Việt', 'Số 56, Đường Võ Văn Kiệt, Quận 1, TP. Hồ Chí Minh', '0934567890', 'info@duongcatviet.vn'),
	('PR024', 'Công Ty TNHH Trà Đen', 'Số 19, Đường Lý Thái Tổ, Quận 10, TP. Hồ Chí Minh', '0912348765', 'contact@traden.vn'),
	('PR025', 'Công Ty TNHH Tinh Dầu Tự Nhiên', 'Số 77, Đường Phan Xích Long, Quận Phú Nhuận, TP. Hồ Chí Minh', '0923456123', 'sales@tinhdautunhien.vn'),
	('PR026', 'Công Ty TNHH Bột Nguyên Liệu', 'Số 67, Đường Trường Sơn, Quận Tân Bình, TP. Hồ Chí Minh', '0902345678', 'info@botnguyenlieu.vn'),
	('PR027', 'Công Ty TNHH Đá Viên', 'Số 34, Đường Cách Mạng Tháng 8, Quận Tân Bình, TP. Hồ Chí Minh', '0932134567', 'sales@davien.vn'),
	('PR028', 'Công Ty TNHH Kem Sữa', 'Số 78, Đường Lê Văn Sỹ, Quận 3, TP. Hồ Chí Minh', '0912456789', 'info@kemsua.vn'),
	('PR029', 'Công Ty TNHH Nguyên Liệu Pha Chế', 'Số 56, Đường Nguyễn Oanh, Quận Gò Vấp, TP. Hồ Chí Minh', '0932123456', 'contact@nguyenlieuphache.vn'),
	('PR030', 'Công Ty TNHH Sữa Đặc', 'Số 12, Đường Nguyễn Văn Cừ, Quận 5, TP. Hồ Chí Minh', '0923456789', 'sales@suadac.vn'),
	('PR031', 'Công Ty TNHH Cốt Dừa', 'Số 45, Đường Nguyễn Văn Trỗi, Quận Phú Nhuận, TP. Hồ Chí Minh', '0912349876', 'contact@cotdua.vn'),
	('PR032', 'Công Ty TNHH Hạnh Nhân', 'Số 19, Đường Phan Văn Trị, Quận Bình Thạnh, TP. Hồ Chí Minh', '0934678901', 'info@hanhnhan.vn'),
	('PR033', 'Công Ty TNHH Trái Cây Tươi Việt', 'Số 54, Đường Nguyễn Thái Sơn, Quận Gò Vấp, TP. Hồ Chí Minh', '0902345671', 'sales@traicaytuoiviet.vn'),
	('PR034', 'Công Ty TNHH Thạch Rau Câu Tự Nhiên', 'Số 77, Đường Trường Sơn, Quận Tân Bình, TP. Hồ Chí Minh', '0934123456', 'info@thachraucau.vn'),
	('PR035', 'Công Ty TNHH Kem Pháp', 'Số 90, Đường Nguyễn Trãi, Quận 5, TP. Hồ Chí Minh', '0912234567', 'sales@kemphap.vn'),
	('PR036', 'Công Ty TNHH Bột Bắp', 'Số 13, Đường Cách Mạng Tháng 8, Quận 10, TP. Hồ Chí Minh', '0932124678', 'contact@botbap.vn'),
	('PR037', 'Công Ty TNHH Tinh Dầu Trà', 'Số 22, Đường Nguyễn Huệ, Quận 1, TP. Hồ Chí Minh', '0903123456', 'info@tinhdautra.vn'),
	('PR038', 'Công Ty TNHH Trà Xanh', 'Số 34, Đường Hai Bà Trưng, Quận 1, TP. Hồ Chí Minh', '0912435678', 'sales@traxanh.vn'),
	('PR039', 'Công Ty TNHH Bột Thơm', 'Số 78, Đường Nguyễn Thị Minh Khai, Quận 1, TP. Hồ Chí Minh', '0903456789', 'contact@botthom.vn'),
	('PR040', 'Công Ty TNHH Đường Đen', 'Số 45, Đường Lê Duẩn, Quận 1, TP. Hồ Chí Minh', '0931234567', 'info@duongden.vn'),
	('PR041', 'Công Ty TNHH Trà Hòa Tan', 'Số 23, Đường Trần Hưng Đạo, Quận 1, TP. Hồ Chí Minh', '0902345673', 'sales@trahoatan.vn'),
	('PR042', 'Công Ty TNHH Đá Xay', 'Số 67, Đường Nguyễn Oanh, Quận Gò Vấp, TP. Hồ Chí Minh', '0912341235', 'contact@daxay.vn'),
	('PR043', 'Công Ty TNHH Sữa Bột', 'Số 12, Đường Lê Văn Lương, Quận 7, TP. Hồ Chí Minh', '0934567123', 'info@suabot.vn'),
	('PR044', 'Công Ty TNHH Đường Bột', 'Số 33, Đường Nguyễn Hữu Cảnh, Quận Bình Thạnh, TP. Hồ Chí Minh', '0913456789', 'sales@duongbot.vn'),
	('PR045', 'Công Ty TNHH Trà Thái', 'Số 90, Đường Nguyễn Văn Quá, Quận 12, TP. Hồ Chí Minh', '0932451234', 'contact@trathai.vn'),
	('PR046', 'Công Ty TNHH Nước Cốt Chanh', 'Số 54, Đường Nguyễn Sơn, Quận Tân Phú, TP. Hồ Chí Minh', '0909876512', 'info@nuoccotchanh.vn'),
	('PR047', 'Công Ty TNHH Đường Nâu', 'Số 22, Đường Phan Đình Phùng, Quận Phú Nhuận, TP. Hồ Chí Minh', '0912344321', 'sales@duongnau.vn'),
	('PR048', 'Công Ty TNHH Bột Kem', 'Số 13, Đường Lý Chính Thắng, Quận 3, TP. Hồ Chí Minh', '0932123458', 'contact@botkem.vn'),
	('PR049', 'Công Ty TNHH Nguyên Liệu Đài Loan', 'Số 56, Đường Nguyễn Thị Minh Khai, Quận 1, TP. Hồ Chí Minh', '0923456788', 'info@nguyenlieudaiwan.vn'),
	('PR050', 'Công Ty TNHH Trân Châu Tự Nhiên', 'Số 19, Đường Lê Văn Sỹ, Quận 3, TP. Hồ Chí Minh', '0901234567', 'sales@tranchautunhien.vn');

-- Dumping structure for table milktea.recipe
CREATE TABLE IF NOT EXISTS `recipe` (
  `productId` varchar(15) NOT NULL,
  `ingredientId` varchar(10) NOT NULL,
  `quantity` double DEFAULT NULL,
  `unit` enum('GRAM','KILOGRAM','LITER','MILLILITER') DEFAULT NULL,
  PRIMARY KEY (`productId`,`ingredientId`) USING BTREE,
  KEY `productId` (`productId`),
  KEY `ingredientId` (`ingredientId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.recipe: ~143 rows (approximately)
INSERT IGNORE INTO `recipe` (`productId`, `ingredientId`, `quantity`, `unit`) VALUES
	('P001', 'I001', 30, 'GRAM'),
	('P001', 'I002', 10, 'GRAM'),
	('P001', 'I003', 20, 'GRAM'),
	('P001', 'I008', 50, 'GRAM'),
	('P002', 'I001', 20, 'GRAM'),
	('P002', 'I004', 150, 'MILLILITER'),
	('P002', 'I006', 5, 'GRAM'),
	('P003', 'I001', 25, 'GRAM'),
	('P003', 'I004', 150, 'MILLILITER'),
	('P003', 'I007', 8, 'GRAM'),
	('P004', 'I001', 20, 'GRAM'),
	('P004', 'I004', 150, 'MILLILITER'),
	('P004', 'I030', 5, 'GRAM'),
	('P005', 'I001', 30, 'GRAM'),
	('P005', 'I004', 150, 'MILLILITER'),
	('P005', 'I029', 10, 'GRAM'),
	('P006', 'I001', 30, 'GRAM'),
	('P006', 'I004', 150, 'MILLILITER'),
	('P006', 'I012', 5, 'MILLILITER'),
	('P007', 'I001', 25, 'GRAM'),
	('P007', 'I004', 150, 'MILLILITER'),
	('P007', 'I023', 5, 'MILLILITER'),
	('P008', 'I001', 20, 'GRAM'),
	('P008', 'I004', 150, 'MILLILITER'),
	('P008', 'I005', 5, 'MILLILITER'),
	('P009', 'I001', 25, 'GRAM'),
	('P009', 'I004', 150, 'MILLILITER'),
	('P009', 'I034', 5, 'GRAM'),
	('P010', 'I001', 30, 'GRAM'),
	('P010', 'I004', 150, 'MILLILITER'),
	('P010', 'I019', 10, 'GRAM'),
	('P011', 'I013', 10, 'MILLILITER'),
	('P011', 'I021', 50, 'GRAM'),
	('P011', 'I022', 150, 'GRAM'),
	('P012', 'I013', 10, 'MILLILITER'),
	('P012', 'I021', 30, 'GRAM'),
	('P012', 'I022', 150, 'GRAM'),
	('P013', 'I014', 10, 'MILLILITER'),
	('P013', 'I021', 30, 'GRAM'),
	('P013', 'I022', 150, 'GRAM'),
	('P014', 'I016', 10, 'MILLILITER'),
	('P014', 'I021', 30, 'GRAM'),
	('P014', 'I022', 150, 'GRAM'),
	('P015', 'I015', 10, 'MILLILITER'),
	('P015', 'I021', 30, 'GRAM'),
	('P015', 'I022', 150, 'GRAM'),
	('P016', 'I021', 10, 'GRAM'),
	('P016', 'I022', 150, 'GRAM'),
	('P017', 'I017', 10, 'MILLILITER'),
	('P017', 'I021', 30, 'GRAM'),
	('P017', 'I022', 150, 'GRAM'),
	('P018', 'I018', 10, 'MILLILITER'),
	('P018', 'I021', 30, 'GRAM'),
	('P018', 'I022', 150, 'GRAM'),
	('P019', 'I021', 30, 'GRAM'),
	('P019', 'I022', 150, 'GRAM'),
	('P019', 'I039', 10, 'MILLILITER'),
	('P020', 'I021', 10, 'GRAM'),
	('P020', 'I022', 150, 'GRAM'),
	('P021', 'I022', 150, 'GRAM'),
	('P021', 'I051', 10, 'GRAM'),
	('P021', 'I054', 5, 'MILLILITER'),
	('P022', 'I022', 150, 'GRAM'),
	('P022', 'I052', 10, 'GRAM'),
	('P022', 'I054', 5, 'MILLILITER'),
	('P023', 'I022', 150, 'GRAM'),
	('P023', 'I053', 15, 'GRAM'),
	('P023', 'I054', 5, 'MILLILITER'),
	('P024', 'I022', 150, 'GRAM'),
	('P024', 'I051', 10, 'GRAM'),
	('P024', 'I054', 5, 'MILLILITER'),
	('P025', 'I022', 150, 'GRAM'),
	('P025', 'I051', 15, 'GRAM'),
	('P025', 'I054', 5, 'MILLILITER'),
	('P026', 'I022', 150, 'GRAM'),
	('P026', 'I041', 10, 'MILLILITER'),
	('P026', 'I051', 15, 'GRAM'),
	('P027', 'I022', 150, 'GRAM'),
	('P027', 'I041', 10, 'MILLILITER'),
	('P027', 'I051', 15, 'GRAM'),
	('P028', 'I022', 150, 'GRAM'),
	('P028', 'I041', 10, 'MILLILITER'),
	('P028', 'I051', 15, 'GRAM'),
	('P029', 'I022', 150, 'GRAM'),
	('P029', 'I041', 10, 'MILLILITER'),
	('P029', 'I051', 15, 'GRAM'),
	('P030', 'I022', 150, 'GRAM'),
	('P030', 'I027', 10, 'GRAM'),
	('P030', 'I051', 15, 'GRAM'),
	('P031', 'I012', 10, 'MILLILITER'),
	('P031', 'I022', 150, 'GRAM'),
	('P031', 'I041', 10, 'GRAM'),
	('P032', 'I015', 10, 'MILLILITER'),
	('P032', 'I022', 150, 'GRAM'),
	('P032', 'I041', 10, 'GRAM'),
	('P033', 'I022', 150, 'GRAM'),
	('P033', 'I041', 10, 'GRAM'),
	('P033', 'I072', 10, 'MILLILITER'),
	('P034', 'I022', 150, 'GRAM'),
	('P034', 'I041', 10, 'GRAM'),
	('P034', 'I066', 10, 'GRAM'),
	('P035', 'I022', 150, 'GRAM'),
	('P035', 'I037', 10, 'GRAM'),
	('P035', 'I041', 10, 'GRAM'),
	('P036', 'I022', 150, 'GRAM'),
	('P036', 'I041', 10, 'GRAM'),
	('P036', 'I067', 10, 'MILLILITER'),
	('P037', 'I013', 10, 'MILLILITER'),
	('P037', 'I022', 150, 'GRAM'),
	('P037', 'I041', 10, 'GRAM'),
	('P038', 'I010', 10, 'MILLILITER'),
	('P038', 'I022', 150, 'GRAM'),
	('P038', 'I041', 10, 'GRAM'),
	('P039', 'I014', 10, 'MILLILITER'),
	('P039', 'I022', 150, 'GRAM'),
	('P039', 'I041', 10, 'GRAM'),
	('P040', 'I022', 150, 'GRAM'),
	('P040', 'I031', 10, 'MILLILITER'),
	('P040', 'I041', 10, 'GRAM'),
	('P041', 'I008', 50, 'GRAM'),
	('P041', 'I022', 150, 'GRAM'),
	('P042', 'I009', 50, 'GRAM'),
	('P042', 'I022', 150, 'GRAM'),
	('P043', 'I022', 150, 'GRAM'),
	('P043', 'I032', 50, 'GRAM'),
	('P044', 'I021', 50, 'GRAM'),
	('P044', 'I022', 150, 'GRAM'),
	('P045', 'I022', 150, 'GRAM'),
	('P045', 'I033', 50, 'GRAM'),
	('P046', 'I022', 150, 'GRAM'),
	('P046', 'I036', 50, 'GRAM'),
	('P047', 'I022', 150, 'GRAM'),
	('P047', 'I034', 50, 'GRAM'),
	('P048', 'I022', 150, 'GRAM'),
	('P048', 'I035', 50, 'GRAM'),
	('P049', 'I022', 150, 'GRAM'),
	('P049', 'I037', 50, 'GRAM'),
	('P050', 'I004', 100, 'MILLILITER'),
	('P050', 'I029', 40, 'GRAM'),
	('P051', 'I004', 300, 'MILLILITER'),
	('P051', 'I005', 0.5, 'LITER'),
	('P051', 'I006', 500, 'GRAM'),
	('P052', 'I002', 10, 'GRAM');

-- Dumping structure for table milktea.role
CREATE TABLE IF NOT EXISTS `role` (
  `roleId` varchar(10) NOT NULL,
  `roleName` varchar(50) DEFAULT NULL,
  `access` int(11) DEFAULT NULL,
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Dumping data for table milktea.role: ~2 rows (approximately)
INSERT IGNORE INTO `role` (`roleId`, `roleName`, `access`) VALUES
	('R01', 'Owner', 70016),
	('R02', 'Admin', 45055),
	('R03', 'Employee', 17023);

ALTER TABLE `customer`
    ADD CONSTRAINT `FK_customer_person` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `employee`
    ADD CONSTRAINT `FK_employee_person` FOREIGN KEY (`id`) REFERENCES `person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    ADD CONSTRAINT `FK_employee_role` FOREIGN KEY (`roleId`) REFERENCES `role` (`roleId`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `goodsreceipt`
    ADD CONSTRAINT `FK_goodsreceipt_employee` FOREIGN KEY (`employeeId`) REFERENCES `employee` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    ADD CONSTRAINT `FK_goodsreceipt_provider` FOREIGN KEY (`providerId`) REFERENCES `provider` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `goodsreceiptdetail`
    ADD CONSTRAINT `FK_goodsreceiptdetail_goodsreceipt` FOREIGN KEY (`goodsReceiptId`) REFERENCES `goodsreceipt` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    ADD CONSTRAINT `FK_goodsreceiptdetail_ingredient` FOREIGN KEY (`ingredientId`) REFERENCES `ingredient` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `invoice`
    ADD CONSTRAINT `FK_invoice_person` FOREIGN KEY (`employeeId`) REFERENCES `person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    ADD CONSTRAINT `FK_invoice_person_2` FOREIGN KEY (`customerId`) REFERENCES `person` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    ADD CONSTRAINT `FK_invoice_promotion` FOREIGN KEY (`promotionId`) REFERENCES `promotion` (`promotionId`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `invoicedetail`
    ADD CONSTRAINT `FK_invoicedetail_invoice` FOREIGN KEY (`invoiceId`) REFERENCES `invoice` (`invoiceId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    ADD CONSTRAINT `FK_invoicedetail_product` FOREIGN KEY (`productId`) REFERENCES `product` (`productId`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `product`
    ADD CONSTRAINT `FK_product_category` FOREIGN KEY (`categoryId`) REFERENCES `category` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `promotion`
    ADD CONSTRAINT `FK_promotion_promotionprogram` FOREIGN KEY (`promotionProgramId`) REFERENCES `promotionprogram` (`promotionProgramId`) ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE `recipe`
    ADD CONSTRAINT `FK_recipe_ingredient` FOREIGN KEY (`ingredientId`) REFERENCES `ingredient` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    ADD CONSTRAINT `FK_recipe_product` FOREIGN KEY (`productId`) REFERENCES `product` (`productId`) ON DELETE NO ACTION ON UPDATE NO ACTION;








/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
