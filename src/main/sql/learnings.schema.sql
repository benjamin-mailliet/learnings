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

CREATE TABLE `ressource` (
  `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `chemin` VARCHAR(1000) NOT NULL,
  `titre` VARCHAR(100) NOT NULL,
  `cours_id` INTEGER,
  `tp_id` INTEGER,
  `projettransversal_id` INTEGER,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`cours_id`) REFERENCES cours(`id`),
  FOREIGN KEY (`tp_id`) REFERENCES tp(`id`),
  FOREIGN KEY (`projettransversal_id`) REFERENCES projettransversal(`id`)
);


CREATE TABLE `travail` (
  `id` INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
  `chemin` VARCHAR(1000) NOT NULL,
  `note` INTEGER,
  `dateRendu` DATETIME NOT NULL,
  `tp_id` INTEGER,
  `projettransversal_id` INTEGER,
  `utilisateur_id1` INTEGER NOT NULL,
  `utilisateur_id2` INTEGER,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`tp_id`) REFERENCES tp(`id`),
  FOREIGN KEY (`projettransversal_id`) REFERENCES projettransversal(`id`),
  FOREIGN KEY (`utilisateur_id1`) REFERENCES utilisateur(`id`),
  FOREIGN KEY (`utilisateur_id2`) REFERENCES utilisateur(`id`)
);
