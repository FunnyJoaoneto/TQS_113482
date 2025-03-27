CREATE TABLE employee (
  id SERIAL PRIMARY KEY,
  name varchar(255) not null
);

INSERT INTO employee (name) VALUES 
('Alice Johnson'),
('Bob Smith'),
('Charlie Brown');