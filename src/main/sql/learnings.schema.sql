CREATE  TABLE `utilisateur` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `email` VARCHAR(100) NOT NULL ,
  `motdepasse` VARCHAR(100) NOT NULL ,
  `admin` BIT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) );
  
  CREATE TABLE `cours` (
  `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `titre` VARCHAR(100) NOT NULL,
  `description` VARCHAR(5000),
  `date` DATE NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `tp` (
  `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `titre` VARCHAR(100) NOT NULL,
  `description` VARCHAR(5000),
  `isnote` BIT NOT NULL,
  `datelimiterendu` DATETIME,
  `date` DATE NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `projettransversal` (
  `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `titre` VARCHAR(100) NOT NULL,
  `description` VARCHAR(5000) NOT NULL,
  `datelimiterendulot1` DATETIME NOT NULL,
  `datelimiterendulot2` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
);