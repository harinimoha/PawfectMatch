DROP TABLE IF EXISTS pets;

CREATE TABLE pets (
                      id INT PRIMARY KEY,
                      name VARCHAR(100),
                      species VARCHAR(50),
                      breed VARCHAR(100),
                      age INT
);