SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS category;
CREATE TABLE IF NOT EXISTS category (
    id INT(11) NOT NULL AUTO_INCREMENT,
    category_name VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS product;
CREATE TABLE IF NOT EXISTS product (
    id INT(11) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description VARCHAR(255) DEFAULT NULL,
   	image_url VARCHAR(255) NOT NULL,
   	units_in_stock INT(11) DEFAULT NULL,
   	category_id INT(11) DEFAULT NULL,
   	date_created DATETIME(6) DEFAULT NULL,
   	last_updated DATETIME(6) DEFAULT NULL,
  	PRIMARY KEY (id),
  	CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS users;
CREATE TABLE IF NOT EXISTS users (
    id INT(11) NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    phone INT(12) NOT NULL,
    address VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS basket;
CREATE TABLE IF NOT EXISTS basket (
    id INT(11) NOT NULL AUTO_INCREMENT,
    user_id INT(11) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_basket_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS basket_item;
CREATE TABLE IF NOT EXISTS basket_item (
    id INT(11) NOT NULL AUTO_INCREMENT,
    basket_id INT(11) DEFAULT NULL,
    product_id INT(11) DEFAULT NULL,
    quantity INT(11) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_basket_item_basket FOREIGN KEY (basket_id) REFERENCES basket(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_basket_item_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS payment;
CREATE TABLE IF NOT EXISTS payment (
    id INT(11) NOT NULL AUTO_INCREMENT,
    user_id INT(11) NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_date DATETIME,
    PRIMARY KEY (id),
    CONSTRAINT fk_payment_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS orders;
CREATE TABLE IF NOT EXISTS orders (
    id INT(11) NOT NULL AUTO_INCREMENT,
    user_id INT(11) NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    order_date DATETIME(6) DEFAULT NULL,
    payment_id INT(11) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_orders_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_orders_payment FOREIGN KEY (payment_id) REFERENCES payment(id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS order_detail;
CREATE TABLE IF NOT EXISTS order_detail (
    id INT(11) NOT NULL AUTO_INCREMENT,
    order_id INT(11) NOT NULL,
    product_id INT(11) NOT NULL,
    quantity INT(11) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_order_detail_orders FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_order_detail_product FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE CASCADE ON UPDATE CASCADE
);

SET FOREIGN_KEY_CHECKS=1;