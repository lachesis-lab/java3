DROP TABLE Client;

CREATE TABLE Client(
clientId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
name TEXT NOT NULL,
login TEXT NOT NULL,
password TEXT NOT NULL);

INSERT INTO Client(name,login,password)
values('Vasily','Vasya','1111'),
('Petr','Petya','1112'),
('Nina','Nina','1113');

INSERT INTO Client(name,login,password)
values('Sasha','Sasha','1111');
SELECT last_insert_rowid();

delete from Client where clientId =4;

select * from Client