DROP DATABASE IF EXISTS milou;
CREATE DATABASE milou;
USE milou;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE emails (
    id INT AUTO_INCREMENT PRIMARY KEY,
    subject VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    code VARCHAR(6) NOT NULL UNIQUE,
    sender_id INT NOT NULL,
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('SENT','DRAFT','TRASHED') NOT NULL DEFAULT 'SENT',
    parent_email_id INT NULL,
    FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_email_id) REFERENCES emails(id) ON DELETE SET NULL,
    INDEX (sender_id),
    INDEX (creation_date)
);

CREATE TABLE recipients (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email_id INT NOT NULL,
    recipient_id INT NOT NULL,
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (email_id) REFERENCES emails(id) ON DELETE CASCADE,
    FOREIGN KEY (recipient_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX (recipient_id)
);
