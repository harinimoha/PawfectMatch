DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS adopters;
DROP TABLE IF EXISTS providers;
DROP TABLE IF EXISTS users;


CREATE TABLE users (
    id          INT             PRIMARY KEY AUTO_INCREMENT,
    email       VARCHAR(100)    NOT NULL UNIQUE,
    password    VARCHAR(255)    NOT NULL,
    role        VARCHAR(20)     NOT NULL,
    created_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,  --"user joined for ... years" add credentials

    CHECK (role IN ('ADOPTER', 'PROVIDER'))
);


CREATE TABLE providers (
    id          INT             PRIMARY KEY AUTO_INCREMENT,
    user_id     INT             NOT NULL UNIQUE,
    org_name    VARCHAR(150)    NOT NULL,
    phone       VARCHAR(20)     NOT NULL,
    address     VARCHAR(255),
    description VARCHAR(500),
    founded_year INT,  

    FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE adopters (
    id                  INT             PRIMARY KEY AUTO_INCREMENT,
    user_id             INT             NOT NULL UNIQUE,
    first_name          VARCHAR(100)    NOT NULL,
    last_name           VARCHAR(100)    NOT NULL,
    phone               VARCHAR(20)     NOT NULL,
    living_environment  VARCHAR(50),
    household_size      INT,
    has_children        BOOLEAN         DEFAULT FALSE,
    has_other_pets      BOOLEAN         DEFAULT FALSE,
    preferred_species   VARCHAR(50),

    FOREIGN KEY (user_id) REFERENCES users(id)
);


CREATE TABLE pets (
    id          INT             PRIMARY KEY AUTO_INCREMENT,
    provider_id INT             NOT NULL,
    name        VARCHAR(100)    NOT NULL,
    species     VARCHAR(50)     NOT NULL,
    breed       VARCHAR(100),
    age         INT,
    temperament VARCHAR(255),
    description VARCHAR(500),
    photo_url   VARCHAR(500),
    status      VARCHAR(20)     NOT NULL DEFAULT 'AVAILABLE',   -- available or adopted
    created_at  TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (provider_id) REFERENCES providers(id),
    CHECK (status IN ('AVAILABLE', 'ON_HOLD', 'ADOPTED'))
);


CREATE TABLE messages (
    id          INT             PRIMARY KEY AUTO_INCREMENT,
    sender_id   INT             NOT NULL,
    receiver_id INT             NOT NULL,
    pet_id      INT             NOT NULL,
    content     VARCHAR(1000)   NOT NULL,
    is_read     BOOLEAN         DEFAULT FALSE,
    sent_at     TIMESTAMP       DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (sender_id)   REFERENCES users(id),
    FOREIGN KEY (receiver_id) REFERENCES users(id),
    FOREIGN KEY (pet_id)      REFERENCES pets(id)
);