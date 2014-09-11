DELETE FROM utilisateur;
INSERT INTO `utilisateur`(`id`,`email`,`motdepasse`,`admin`) VALUES(1,'admin@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',1);
INSERT INTO `utilisateur`(`id`,`email`,`motdepasse`,`admin`) VALUES(2,'jean.bonneau@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0);
INSERT INTO `utilisateur`(`id`,`email`,`motdepasse`,`admin`) VALUES(3,'anne.onyme@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0);
INSERT INTO `utilisateur`(`id`,`email`,`motdepasse`,`admin`) VALUES(4,'charles.attan@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0);
INSERT INTO `utilisateur`(`id`,`email`,`motdepasse`,`admin`) VALUES(5,'hubert.bere@learnings-devwebhei.fr','6b411d0bccf8723d8072f45cb1ffb4f8ca62abdf2bed7516:cd22a8e992bc0404efa4d2011f6041f0679b6dd2bf2d3b81',0);

DELETE FROM ressource;
DELETE FROM seance;
INSERT INTO seance
(id, titre, description, isnote, datelimiterendu, date, type)
VALUES(3, 'TP2 : Bootstrap', 'TP permettant de découvrir le framework CSS Bootstrap', true, '2014-09-21 23:59:59', '2014-09-19', 'TP');
INSERT INTO seance
(id, titre, description, isnote, datelimiterendu, date, type)
VALUES(2, 'TP1 : HTML - Les animaux laids', 'Tp mettant en oeuvre les acquis sur HTML et CSS avec une liste d''animaux moches', false, NULL, '2014-09-12', 'TP');
INSERT INTO seance
(id, titre, description, isnote, datelimiterendu, date, type)
VALUES(1, 'HTML et CSS', 'Cours sur les base du HTML et du CSS.<ul><li>Chapitre 1 : HTML, rappel</li><li>Chapitre 2 : HTML 5</li><li>Chapitre 3 : CSS, rappel</li><li>Chapitre 4 : CSS 3</li></ul>', false, NULL, '2014-09-12', 'COURS');

INSERT INTO ressource
(id, chemin, titre, seance_id)
VALUES(3, 'http://dropbox.com', 'TP2 : Bootstrap', 3);
INSERT INTO ressource
(id, chemin, titre, seance_id)
VALUES(2, 'http://www.yahoo.fr', 'Cours 1 : HTML / CSS', 1);
INSERT INTO ressource
(id, chemin, titre, seance_id)
VALUES(1, 'http://www.google.fr', 'TP1 : les animaux laids', 2);
