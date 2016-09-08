CREATE TABLE `appel` (
  `idseance` INT(11) NOT NULL,
  `ideleve` INT(11) NOT NULL,
  `statut` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`idseance`, `ideleve`),
  INDEX `fk_appel_eleve_idx` (`ideleve` ASC),
  CONSTRAINT `fk_appel_seance` FOREIGN KEY (`idseance`) REFERENCES `learnings`.`seance` (`id`),
  CONSTRAINT `fk_appel_eleve` FOREIGN KEY (`ideleve`) REFERENCES `learnings`.`utilisateur` (`id`)
);


ALTER TABLE `utilisateur`
  ADD COLUMN `nom` VARCHAR(50) NOT NULL AFTER `id`,
  ADD COLUMN `prenom` VARCHAR(50) NOT NULL AFTER `nom`,
  ADD COLUMN `groupe` VARCHAR(10) NULL AFTER `email`;