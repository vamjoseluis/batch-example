DROP TABLE dog IF EXISTS

CREATE TABLE dog(
	id BIGINT IDENTITY NOT NULL PRIMARY KEY,
	name varchar(100),
	breed varchar(100),
	age int
);