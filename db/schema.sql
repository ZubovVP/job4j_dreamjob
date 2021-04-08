CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   date VARCHAR (15)
);

CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   photo TEXT
);

CREATE TABLE user (
id SERIAL PRIMARY KEY,
name VARCHAR (30),
email VARCHAR (50),
password VARCHAR (64)
);