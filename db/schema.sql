CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   date VARCHAR (15)
);

CREATE TABLE cites(
 id SERIAL PRIMARY KEY,
 name TEXT
);

CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   photo TEXT,
   id_city int references cites(id)
);

CREATE TABLE user (
id SERIAL PRIMARY KEY,
name VARCHAR (30) UNIQUE,
email VARCHAR (50),
password VARCHAR (64)
);