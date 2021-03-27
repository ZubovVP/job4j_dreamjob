CREATE TABLE post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   date VARCHAR (15)
);

CREATE TABLE candidate (
   id SERIAL PRIMARY KEY,
   name TEXT
);