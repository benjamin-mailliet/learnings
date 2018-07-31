CREATE TABLE utilisateur (
  id int(11) NOT NULL AUTO_INCREMENT,
  nom varchar(50) NOT NULL,
  prenom varchar(50) NOT NULL,
  email varchar(100) NOT NULL,
  groupe varchar(10) DEFAULT NULL,
  motdepasse varchar(100) NOT NULL,
  admin bit(1) DEFAULT b'0',
  PRIMARY KEY (id),
  UNIQUE KEY email_UNIQUE (email)
);

CREATE TABLE seance (
  id int(11) NOT NULL AUTO_INCREMENT,
  titre varchar(100) NOT NULL,
  description varchar(5000) DEFAULT NULL,
  isnote bit(1) NOT NULL DEFAULT b'0',
  datelimiterendu datetime DEFAULT NULL,
  date date NOT NULL,
  type varchar(50) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE appel (
  idseance int(11) NOT NULL,
  ideleve int(11) NOT NULL,
  statut varchar(10) NOT NULL,
  PRIMARY KEY (idseance,ideleve),
  KEY fk_appel_eleve_idx (ideleve),
  CONSTRAINT fk_appel_eleve FOREIGN KEY (ideleve) REFERENCES utilisateur (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT fk_appel_seance FOREIGN KEY (idseance) REFERENCES seance (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE binome (
  id int(11) NOT NULL AUTO_INCREMENT,
  eleve1_id int(11) NOT NULL,
  eleve2_id int(11) DEFAULT NULL,
  seance_id int(11) NOT NULL,
  PRIMARY KEY (id),
  KEY binome_eleve1_idx (eleve1_id),
  KEY binome_eleve2_idx (eleve2_id),
  KEY binome_seance_idx (seance_id),
  CONSTRAINT binome_eleve1 FOREIGN KEY (eleve1_id) REFERENCES utilisateur (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT binome_eleve2 FOREIGN KEY (eleve2_id) REFERENCES utilisateur (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT binome_seance FOREIGN KEY (seance_id) REFERENCES seance (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE rendu_tp (
  id int(11) NOT NULL AUTO_INCREMENT,
  chemin varchar(1000) NOT NULL,
  note decimal(4,2) DEFAULT NULL,
  dateRendu datetime NOT NULL,
  commentaire varchar(5000) DEFAULT NULL,
  commentaireNote varchar(5000) DEFAULT NULL,
  binome_id int(11) NOT NULL,
  PRIMARY KEY (id),
  KEY rendu_tp_binome_idx (binome_id),
  CONSTRAINT rendu_tp_binome FOREIGN KEY (binome_id) REFERENCES binome (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE ressource (
  id int(11) NOT NULL AUTO_INCREMENT,
  chemin varchar(1000) NOT NULL,
  titre varchar(100) NOT NULL,
  seance_id int(11) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY seance_id (seance_id),
  CONSTRAINT ressource_ibfk_1 FOREIGN KEY (seance_id) REFERENCES seance (id)
);

CREATE TABLE note (
  id int(11) NOT NULL AUTO_INCREMENT,
  eleve_id int(11) NOT NULL,
  seance_id int(11) DEFAULT NULL,
  valeur decimal(4,2) DEFAULT NULL,
  commentaire mediumtext,
  PRIMARY KEY (id),
  KEY note_eleve_fk_idx (eleve_id),
  KEY note_seance_fk_idx (seance_id),
  CONSTRAINT note_eleve_fk FOREIGN KEY (eleve_id) REFERENCES utilisateur (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT note_seance_fk FOREIGN KEY (seance_id) REFERENCES seance (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);
