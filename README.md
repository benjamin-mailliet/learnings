learnings-devwebhei
===================

Learnings est une application pour la gestion du cours de développement Web pour les H4 d'HEI.

Principales fonctionnalités
---------------------------
* Authentification
* Changement de mot de passe

### Pour les élèves

* Visualisation des séances de cours / TP
* Téléchargement des supports de cours / TP
* Gestion du rendu des travaux

### Pour les enseignants

* Gestion des utilisateurs
* Gestion des séances
  * Chargement des supports de cours
  * Appel
* Visualisation des rendus des élèves
* Gestion des notes

Technologies utilisées
----------------------
Cette application est également un exemple de développement proposé aux élèves. Pour cette raison, elle a été effectuée en utilisant les technologies enseignées durant le cours :

* HTML5
* CSS 3 avec [Bootstrap](http://getbootstrap.com)
* Javascript avec [JQuery](http://jquery.com)
* Servlets
* [Thymeleaf](http://www.thymeleaf.org/)
* [JDBC](http://www.oracle.com/technetwork/java/javase/jdbc/index.html) (avec le driver [ConnectorJ](https://dev.mysql.com/downloads/connector/j/)  pour se connecter à une base MySQL)
* [JUnit](http://junit.org) et [AssertJ](http://joel-costigliola.github.io/assertj/)

### Dockerisation
#### Build
| Image | Commande |
| ------------- | -------------   |
| learnings-front   | docker build -f Dockerfile-httpd  -t learnings-front:latest . |
| learnings-middle  | docker build -f Dockerfile-tomcat -t learnings-middle:latest . |

#### Lancement via docker compose
docker-compose up
