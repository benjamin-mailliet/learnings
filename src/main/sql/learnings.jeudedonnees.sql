DELETE FROM utilisateur;
INSERT INTO `utilisateur`(`id`,`nom`, `prenom`, `email`,`motdepasse`,`admin`, `groupe`) VALUES(1, 'ADMIN', 'Admin', 'admin@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',1, null);
INSERT INTO `utilisateur`(`id`,`nom`, `prenom`, `email`,`motdepasse`,`admin`, `groupe`) VALUES(2, 'BONNEAU', 'Jean', 'jean.bonneau@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0, 'GROUPE_1');
INSERT INTO `utilisateur`(`id`,`nom`, `prenom`, `email`,`motdepasse`,`admin`, `groupe`) VALUES(3, 'ONYME', 'Anne','anne.onyme@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0, 'GROUPE_1');
INSERT INTO `utilisateur`(`id`,`nom`, `prenom`, `email`,`motdepasse`,`admin`, `groupe`) VALUES(4, 'ATTAN', 'Charles','charles.attan@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0, 'GROUPE_2');
INSERT INTO `utilisateur`(`id`,`nom`, `prenom`, `email`,`motdepasse`,`admin`, `groupe`) VALUES(5, 'BERE', 'Hubert','hubert.bere@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0, 'GROUPE_2');

DELETE FROM ressource;
DELETE FROM seance;
INSERT INTO seance (id, titre, description, isnote, datelimiterendu, date, type) VALUES(3, 'TP2 : Bootstrap', 'TP permettant de découvrir le framework CSS Bootstrap', true, '2014-09-21 23:59:59', '2014-09-19', 'TP');
INSERT INTO seance (id, titre, description, isnote, datelimiterendu, date, type) VALUES(2, 'TP1 : HTML - Les animaux laids', 'Tp mettant en oeuvre les acquis sur HTML et CSS avec une liste d''animaux moches', false, NULL, '2014-09-12', 'TP');
INSERT INTO seance (id, titre, description, isnote, datelimiterendu, date, type) VALUES(1, 'HTML et CSS', 'Cours sur les base du HTML et du CSS.<ul><li>Chapitre 1 : HTML, rappel</li><li>Chapitre 2 : HTML 5</li><li>Chapitre 3 : CSS, rappel</li><li>Chapitre 4 : CSS 3</li></ul>', false, NULL, '2014-09-12', 'COURS');

INSERT INTO ressource (id, chemin, titre, seance_id) VALUES(3, 'ressources/seances/ea35b8d6-TP2.pptx', 'TP2 : Bootstrap', 3);
INSERT INTO ressource (id, chemin, titre, seance_id) VALUES(2, 'ressources/seances/91ca9088-Cours1.zip', 'Cours 1 : HTML / CSS', 1);
INSERT INTO ressource (id, chemin, titre, seance_id) VALUES(1, 'ressources/seances/f0465006-tp1.zip', 'TP1 : les animaux laids', 2);

DELETE FROM projet;
INSERT INTO projet VALUES (1, 'Projet de ouf', 'Desription du projet de ouf', '2014-11-16 23:59:00', '2015-01-18 23:59:00');