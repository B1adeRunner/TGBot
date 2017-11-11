CREATE TABLE IF NOT EXISTS users
(
  userId          BIGINT PRIMARY KEY NOT NULL,
  firstName       VARCHAR (20),
  lastName        VARCHAR (20),
  userName        VARCHAR (20),
  realFirstName   VARCHAR (20),
  realMiddleName  VARCHAR (20),
  realLastName    VARCHAR (20),
  birthDate       DATE
);

CREATE TABLE IF NOT EXISTS usersDataForBot
(
  id            BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  userId        BIGINT REFERENCES users(userId),
  chatId        BIGINT
);

CREATE TABLE IF NOT EXISTS languageCodes
(
  code          BIGINT PRIMARY KEY NOT NULL,
  language      VARCHAR(40),
  languageType  VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS messages
(
  id            BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  userId        BIGINT,
  chatId        BIGINT,
  languageCode  BIGINT,
  messageText   LONGTEXT
);
