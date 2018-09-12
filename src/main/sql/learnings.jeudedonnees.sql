DELETE FROM utilisateur;
INSERT INTO `utilisateur`(`id`,`nom`, `prenom`, `email`,`motdepasse`,`admin`, `groupe`) VALUES(1, 'ADMIN', 'Admin', 'admin@learnings-devwebhei.fr','$argon2i$v=19$m=65536,t=2,p=1$aO9/0ITWAHgHwMgls5CYvw$HCpY/zbG4jEiF7q39o3MAfsrXcFXoLC6FI5CFJiN2Yw',1, null);
INSERT INTO `utilisateur`(`id`,`nom`, `prenom`, `email`,`motdepasse`,`admin`, `groupe`) VALUES(2, 'BONNEAU', 'Jean', 'jean.bonneau@learnings-devwebhei.fr','$argon2i$v=19$m=65536,t=2,p=1$aO9/0ITWAHgHwMgls5CYvw$HCpY/zbG4jEiF7q39o3MAfsrXcFXoLC6FI5CFJiN2Yw',0, 'GROUPE_1');
INSERT INTO `utilisateur`(`id`,`nom`, `prenom`, `email`,`motdepasse`,`admin`, `groupe`) VALUES(3, 'ONYME', 'Anne','anne.onyme@learnings-devwebhei.fr','$argon2i$v=19$m=65536,t=2,p=1$aO9/0ITWAHgHwMgls5CYvw$HCpY/zbG4jEiF7q39o3MAfsrXcFXoLC6FI5CFJiN2Yw',0, 'GROUPE_1');
INSERT INTO `utilisateur`(`id`,`nom`, `prenom`, `email`,`motdepasse`,`admin`, `groupe`) VALUES(4, 'ATTAN', 'Charles','charles.attan@learnings-devwebhei.fr','$argon2i$v=19$m=65536,t=2,p=1$aO9/0ITWAHgHwMgls5CYvw$HCpY/zbG4jEiF7q39o3MAfsrXcFXoLC6FI5CFJiN2Yw',0, 'GROUPE_2');
INSERT INTO `utilisateur`(`id`,`nom`, `prenom`, `email`,`motdepasse`,`admin`, `groupe`) VALUES(5, 'BERE', 'Hubert','hubert.bere@learnings-devwebhei.fr','$argon2i$v=19$m=65536,t=2,p=1$aO9/0ITWAHgHwMgls5CYvw$HCpY/zbG4jEiF7q39o3MAfsrXcFXoLC6FI5CFJiN2Yw',0, 'GROUPE_2');

DELETE FROM ressource;
DELETE FROM seance;
INSERT INTO seance (id, titre, description, isnote, datelimiterendu, date, type) VALUES(3, 'TP2 : Bootstrap', '<p>TP permettant de découvrir le framework CSS Bootstrap.</p>', true, '2014-09-21 23:59:59', '2014-09-19', 'TP');
INSERT INTO seance (id, titre, description, isnote, datelimiterendu, date, type) VALUES(2, 'TP1 : HTML - Les animaux laids', '<p>Tp mettant en oeuvre les acquis sur HTML et CSS avec une liste d''animaux moches.</p>', false, NULL, '2014-09-12', 'TP');
INSERT INTO seance (id, titre, description, isnote, datelimiterendu, date, type) VALUES(1, 'HTML et CSS', '<p>Cours sur les base du HTML et du CSS.</p><ul><li>Chapitre 1 : HTML, rappel</li><li>Chapitre 2 : HTML 5</li><li>Chapitre 3 : CSS, rappel</li><li>Chapitre 4 : CSS 3</li></ul>', false, NULL, '2014-09-12', 'COURS');

INSERT INTO ressource (id, chemin, titre, seance_id, categorie) VALUES(4, 'ressources/seances/ea35b8d6-correction.zip', 'Correction du TP (zip)', 3, 'CORRECTION');
INSERT INTO ressource (id, chemin, titre, seance_id, categorie) VALUES(3, 'ressources/seances/ea35b8d6-TP2.pptx', 'TP2 : Bootstrap', 3, 'SUPPORT');
INSERT INTO ressource (id, chemin, titre, seance_id, categorie) VALUES(2, 'ressources/seances/91ca9088-Cours1.zip', 'Cours 1 : HTML / CSS', 1, 'SUPPORT');
INSERT INTO ressource (id, chemin, titre, seance_id, categorie) VALUES(1, 'ressources/seances/f0465006-tp1.zip', 'TP1 : les animaux laids', 2, 'SUPPORT');