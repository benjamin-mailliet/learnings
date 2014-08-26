CREATE  TABLE `utilisateur` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `email` VARCHAR(100) NOT NULL ,
  `motdepasse` VARCHAR(100) NOT NULL ,
  `admin` BIT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) );
  
CREATE TABLE `cours` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `titre` VARCHAR(100) NOT NULL,
  `description` VARCHAR(5000),
  `date` DATE NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `tp` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `titre` VARCHAR(100) NOT NULL,
  `description` VARCHAR(5000),
  `isnote` BIT NOT NULL,
  `datelimiterendu` DATETIME,
  `date` DATE NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `projettransversal` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `titre` VARCHAR(100) NOT NULL,
  `description` VARCHAR(5000) NOT NULL,
  `datelimiterendulot1` DATETIME NOT NULL,
  `datelimiterendulot2` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `ressource` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `chemin` VARCHAR(1000) NOT NULL,
  `titre` VARCHAR(100) NOT NULL,
  `cours_id` INT,
  `tp_id` INT,
  `projettransversal_id` INT,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`cours_id`) REFERENCES cours(`id`),
  FOREIGN KEY (`tp_id`) REFERENCES tp(`id`),
  FOREIGN KEY (`projettransversal_id`) REFERENCES projettransversal(`id`)
);


CREATE TABLE `travail` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `chemin` VARCHAR(1000) NOT NULL,
  `note` decimal(4,2) DEFAULT NULL,
  `dateRendu` DATETIME NOT NULL,
  `tp_id` INT,
  `projettransversal_id` INT,
  `utilisateur_id1` INT NOT NULL,
  `utilisateur_id2` INT,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`tp_id`) REFERENCES tp(`id`),
  FOREIGN KEY (`projettransversal_id`) REFERENCES projettransversal(`id`),
  FOREIGN KEY (`utilisateur_id1`) REFERENCES utilisateur(`id`),
  FOREIGN KEY (`utilisateur_id2`) REFERENCES utilisateur(`id`)
);
