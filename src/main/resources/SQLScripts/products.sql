CREATE TABLE PRODUCTS(
ID INT PRIMARY KEY,
NAME CHAR(50),
BRAND_ID INT,
FOREIGN KEY (BRAND_ID) REFERENCES BRAND(ID)
)