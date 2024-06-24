-- 刪除已存在資料表
drop table if exists stock;
drop table if exists log;
drop table if exists book;
drop table if exists wallet;

/* book(書籍)
+---------+-----------+------------+
| book_id | book_name | book_price |
+---------+-----------+------------+
|       1 | Java      |        100 |
|       2 | Python    |         70 |
+---------+-----------+------------+
*/
create table if not exists book(
	book_id int primary key, -- 書號
    book_name varchar(50), -- 書名
    book_price int -- 價格
);

/* stock(庫存)
+---------+-------------+
| book_id | book_amount |
+---------+-------------+
|       1 |          10 |
|       2 |          10 |
+---------+-------------+
*/
create table if not exists stock(
	book_id int, -- 書號
    book_amount int, -- 存量
    foreign key(book_id) references book(book_id)
);

/* wallet(錢包)
+----------+---------+
| username | balance |
+----------+---------+
| John     |     200 |
| Mary     |     200 |
+----------+---------+
*/
create table if not exists wallet(
	username varchar(50) primary key, -- 客戶
    balance int -- 餘額
);

/* log(交易紀錄)
+--------+-----------------+----------+---------+--------+---------------------+
| log_id | transaction_type| username | book_id | amount | transaction_date    |
+--------+-----------------+----------+---------+--------+---------------------+
|      1 | purchase        | John     |       1 |    100 | <timestamp>         |
|      2 | purchase        | Mary     |       2 |     70 | <timestamp>         |
+--------+-----------------+----------+---------+--------+---------------------+
*/
create table if not exists log(
	log_id int auto_increment primary key, -- 交易紀錄 ID
    tx_type varchar(50), -- 交易類型(purchase, refund)
    username varchar(50), -- 客戶
    book_id int, -- 書號
    amount int, -- 交易金額
    tx_date timestamp default current_timestamp, -- 交易時間
    foreign key(book_id) references book(book_id),
    foreign key(username) references wallet(username)
);

-- 建立 sample 資料
insert into book(book_id, book_name, book_price) values(1, 'Java', 100);
insert into book(book_id, book_name, book_price) values(2, 'Python', 70);
insert into stock(book_id, book_amount) values(1, 10);
insert into stock(book_id, book_amount) values(2, 10);
insert into wallet(username, balance) values('John', 200);
insert into wallet(username, balance) values('Mary', 200);

