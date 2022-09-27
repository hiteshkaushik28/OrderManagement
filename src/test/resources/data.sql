insert into product (id, name, price) values (1, 'Xiaomi POCO', 11000.10)
insert into product (id, name, price) values (2, 'Skybag 101', 210.10)
DROP SEQUENCE IF EXISTS PROD_SEQUENCE_ID
CREATE SEQUENCE PROD_SEQUENCE_ID START WITH (select max(ID) + 1 from Product)
DROP SEQUENCE IF EXISTS ORDER_SEQUENCE_ID
CREATE SEQUENCE ORDER_SEQUENCE_ID START WITH (select max(ID) + 1 from Order_)