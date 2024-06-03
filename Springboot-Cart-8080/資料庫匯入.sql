-- 建立 cart 資料庫, 若資料庫不存在
CREATE DATABASE if not exists cart CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

use cart;
drop table if exists `Item`;
drop table if exists `Order`;
drop table if exists `Product`;
drop table if exists `Customer`;

CREATE TABLE if not exists `Customer` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `role` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE if not exists `Product` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `cost` INT NOT NULL,
  `price` INT NOT NULL,
  `qty` INT NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE if not exists `Order` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `date` VARCHAR(50) NOT NULL,
  `customerId` INT NOT NULL,
  `checkout` INT NOT NULL default 0,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`customerId`) REFERENCES `Customer`(`id`)
);

CREATE TABLE if not exists `Item` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `orderId` INT NOT NULL,
  `productId` INT NOT NULL,
  `amount` INT NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`orderId`) REFERENCES `Order`(`id`),
  FOREIGN KEY (`productId`) REFERENCES `Product`(`id`)
);

-- Insert Customer
-- Password: 1234 = $2a$10$W2EPedlAMHFk.WAGtAqRq.ieN78uPnZtHYITpsbgxmm9hW2NSbaVS
-- $2a$ 是 bcrypt 密碼哈希的一個版本標識符，$2a$ 表示使用的是 bcrypt 算法。
-- 10 是工作因子, 這是一個指定加密強度的整數，範圍從 4 到 31。工作因子的默認值是 10。密強度越大，加密時間越長。
-- W2EPedlAMHFk.WAGtAqRq. 是 salt
-- ieN78uPnZtHYITpsbgxmm9hW2NSbaVS 是密碼哈希
INSERT INTO `Customer` (`username`, `password`, `role`) VALUES ('admin', '$2a$10$W2EPedlAMHFk.WAGtAqRq.ieN78uPnZtHYITpsbgxmm9hW2NSbaVS', 'ADMIN');
INSERT INTO `Customer` (`username`, `password`, `role`) VALUES ('john', '$2a$10$W2EPedlAMHFk.WAGtAqRq.ieN78uPnZtHYITpsbgxmm9hW2NSbaVS', 'ADMIN');
INSERT INTO `Customer` (`username`, `password`, `role`) VALUES ('mary', '$2a$10$W2EPedlAMHFk.WAGtAqRq.ieN78uPnZtHYITpsbgxmm9hW2NSbaVS', 'USER');

-- Insert Product
INSERT INTO `Product` (`name`, `cost`, `price`, `qty`) VALUES ('Apple', 10, 20, 100);
INSERT INTO `Product` (`name`, `cost`, `price`, `qty`) VALUES ('Banana', 5, 10, 100);
INSERT INTO `Product` (`name`, `cost`, `price`, `qty`) VALUES ('Orange', 8, 15, 100);
INSERT INTO `Product` (`name`, `cost`, `price`, `qty`) VALUES ('Pineapple', 15, 30, 100);
INSERT INTO `Product` (`name`, `cost`, `price`, `qty`) VALUES ('Watermelon', 20, 40, 100);


