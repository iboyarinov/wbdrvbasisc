CREATE TABLE PRODUCT_VARIANTS(
ID INT NOT NULL,
PRODUCT_ID INT,
variation1_type CHAR(50),
variation1_value CHAR(50),
variation2_type CHAR(50),
variation2_value CHAR(50),
variation3_type CHAR(50),
variation3_value CHAR(50),
variation4_type CHAR(50),
variation4_value CHAR(50),
variation5_type CHAR(50),
variation5_value CHAR(50),
price DOUBLE,
old_price DOUBLE,
stock CHAR(50),
available CHAR(1),
FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT(ID)
)