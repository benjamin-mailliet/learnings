CREATE  TABLE `utilisateur` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `nom` VARCHAR(50) NOT NULL,
  `prenom` VARCHAR(50) NOT NULL,
  `email` VARCHAR(100) NOT NULL ,
  `groupe` VARCHAR(10) NULL,
  `motdepasse` VARCHAR(100) NOT NULL ,
  `admin` BIT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) );
  
CREATE TABLE `seance` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `titre` VARCHAR(100) NOT NULL,
  `description` VARCHAR(5000),
  `isnote` BIT NOT NULL DEFAULT 0,
  `datelimiterendu` DATETIME,
  `date` DATE NOT NULL,
  `type` VARCHAR(50) NOT NULL,
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
  `seance_id` INT,
  `projettransversal_id` INT,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`seance_id`) REFERENCES seance(`id`),
  FOREIGN KEY (`projettransversal_id`) REFERENCES projettransversal(`id`)
);


CREATE TABLE `travail` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `chemin` VARCHAR(1000),
  `urlRepository` VARCHAR(1000),
  `note` decimal(4,2) DEFAULT NULL,
  `dateRendu` DATETIME NOT NULL,
  `seance_id` INT,
  `projettransversal_id` INT,
  `commentaire` VARCHAR(5000) NULL,
  commentaireNote TEXT DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`seance_id`) REFERENCES seance(`id`),
  FOREIGN KEY (`projettransversal_id`) REFERENCES projettransversal(`id`)
);


CREATE TABLE `travailutilisateur` (
  `idtravail` int(11) NOT NULL,
  `idutilisateur` int(11) NOT NULL,
  PRIMARY KEY (`idtravail`,`idutilisateur`),
  KEY `idutilisateur_fk` (`idutilisateur`),
  KEY `idtravail_fk` (`idtravail`),
  CONSTRAINT `idutilisateur_fk` FOREIGN KEY (`idutilisateur`) REFERENCES `utilisateur` (`id`),
  CONSTRAINT `idtravail_fk` FOREIGN KEY (`idtravail`) REFERENCES `travail` (`id`)
);


CREATE TABLE `appel` (
  `idseance` INT(11) NOT NULL,
  `ideleve` INT(11) NOT NULL,
  `statut` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`idseance`, `ideleve`),
  INDEX `fk_appel_eleve_idx` (`ideleve` ASC),
  CONSTRAINT `fk_appel_seance` FOREIGN KEY (`idseance`) REFERENCES `seance` (`id`),
  CONSTRAINT `fk_appel_eleve` FOREIGN KEY (`ideleve`) REFERENCES `utilisateur` (`id`)
);
