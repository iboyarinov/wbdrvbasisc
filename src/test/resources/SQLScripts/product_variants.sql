CREATE TABLE PRODUCT_VARIANTS(
ID INT NOT NULL,
PRODUCT_ID INT,
variation1_type CHAR(50),
variation1_value CHAR(50),
variation2_type CHAR(50),
variation2_value CHAR(50),
variation3_type CHAR(50),
variation3_value CHAR(50),
price DOUBLE,
old_price DOUBLE,
stock INT,
available CHAR(1),
FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(ID)
);