ALTER TABLE `learnings`.`travail` 
ADD COLUMN `urlRepository` VARCHAR(1000) NULL DEFAULT NULL AFTER `commentaire`;

ALTER TABLE `learnings`.`travail` 
CHANGE COLUMN `chemin` `chemin` VARCHAR(1000) NULL DEFAULT NULL;
