CREATE TABLE log
(
    id        INT AUTO_INCREMENT PRIMARY KEY,
    severity  VARCHAR(250) NOT NULL,
    message   VARCHAR(250) NOT NULL,
    timestamp TIMESTAMP   NOT NULL
);