CREATE  TABLE `utilisateur` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `email` VARCHAR(100) NOT NULL ,
  `motdepasse` VARCHAR(100) NOT NULL ,
  `admin` BIT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) );