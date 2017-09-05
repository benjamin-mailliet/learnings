# Héberger un site web Java EE #

## Introduction ##
Le but de ce document est de décrire les différentes étapes afin d'héberger un site web. Le site web est considéré comme du type de ceux vus dans le cours de développement web :

* JDK 8
* Projet Maven
* Serveur Tomcat 8
* Base de données MySQL

L'hébergement d'applications Java est moins aisée que celle d'applications PHP pour lesquelles beaucoup de solutions clé en main existent.

## Openshift ##
[Openshift](www.openshift.com) est la solution _Platform as a Service_ (PaaS) proposée par RedHat. C'est sur cette plateforme que le site Learnings est hébergé et donc c'est la plateforme que nous conseillions jusqu'à présent. Mais il n'est plus possible de créer de compte sur la version "online" d'Openshift depuis Août 2016. Donc il va falloir trouver autre chose tant que la nouvelle version d'Openshift Online n'est pas disponible..

## Heroku ##
[Heroku](www.heroku.com) est une autre solution PaaS. Elle est moins facile d'utilisation qu'Openshift car sans support natif de MySQL et d'upload de fichiers. **La version gratuite est à réserver à l'hébergement de prototypes d'application web. Pour des applications web de production, il faudra passer sur une solution payante.**

La procédure ci-dessous décrit comment déployer un site sur la plateforme Heroku. Ce n'est pas une documentation complète d'utilisation de Heroku mais plus un _quick start_. Pour plus d'information, le site de Heroku est très fourni. La gestion d'upload de fichier ne sera notamment pas décrite.

### Initialisation de l'application 

* Créer un compte sur le site Heroku
* Créer une nouvelle application en allant sur le dashboard. Pour le reste de l'exemple, l'application s'appellera _learnings-devwebhei_.
* Télécharger et installer [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli). Il s'agit d'un utilitaire en ligne de commande permettant d'intéragir la plateforme Heroku.
* En local, ouvrir une ligne de commande et aller là où sont les sources de votre application. Il est nécessaire d'avoir un repository Git donc il faut en initier un si nécessaire. Si le code de l'application est déjà stocké sur un git (par exemple GitHub), il est conseillé de recloner votre application dans un autre dossier pour ne pas mélanger la partie "code de l'application" de celle de "déploiement sur Heroku" :  
  `git clone https://github.com/benjamin-mailliet/learnings.git learnings-heroku`
* Utiliser Heroku CLI pour créer un nouveau repository remote Git nommé "heroku". Il sera lié à l'application Heroku créée précédemment (ici _learnings-devwebhei_).  
  `heroku git:remote -a learnings-devwebhei`  
  

### Base de données
Comme dit précédemment, Heroku n'a pas de support natif de MySQL, il faut donc passer par ce que la plateforme appelle un _add-on_. Le plus gros problème avec cet add-on est qu'il demande de renseigner sa carte bancaire dans Heroku (même si son utilisation basique est gratuite). Si cela n'est pas problématique, la procédure suivante peut être suivie. 

>Sinon, il est possible d'utiliser [PostgreSQL](https://devcenter.heroku.com/articles/heroku-postgresql) sans donner sa carte bancaire mais cela demandera quelques modifications dans l'application Java (notamment utiliser le [driver PostgreSQL](http://search.maven.org/#artifactdetails%7Corg.postgresql%7Cpostgresql%7C9.4.1212%7Cbundle) plutôt que celui pour MySQL).

* Ajouter sa carte bancaire dans la configuration du compte Heroku.
* Ajouter l'addon Mysql  [JawsDB](https://devcenter.heroku.com/articles/jawsdb) à votre application. (Il existe aussi l'add-on [ClearDB](https://devcenter.heroku.com/articles/cleardb) mais il bien fallait faire un choix.)   
  `heroku addons:create jawsdb`
* Récupérer les infos de connexion.  
  `heroku config:get JAWSDB_URL`  
  L'URL renvoyée est de la forme : `mysql://[login]:[password]@[host]:3306/[database_name]`. Renseigner ces informations dans votre projet Java.
* Utiliser les informations de connexion pour se connecter à la base avec votre client MySQL pour créer les tables et le jeu de donnée initial.

### Déploiement

* Nous allons déployer notre application en utilisant le [plugin maven Heroku](https://devcenter.heroku.com/articles/deploying-java-applications-with-the-heroku-maven-plugin). Il faut donc ajouter les lignes suivantes dans le fichier pom.xml.  
``` <build>   
    <plugins>  
      <plugin>   
        <groupId>com.heroku.sdk</groupId>   
        <artifactId>heroku-maven-plugin</artifactId>   
        <version>1.1.3</version>   
      </plugin>   
    </plugins>   
  </build>
```
* Commiter et pusher les modifications faites dans le code  
  `git add -A`  
  `git commit -m "conf heroku"`  
  `git push heroku master`
* Exécuter le plugin maven Heroku   
 `mvn clean heroku:deploy-war`
 > Dans le cas où apparaît l'erreur : "'mvn' n’est pas reconnu en tant que commande interne", il faut ajouter maven au path de windows. Pour cela, [télécharger Maven](https://maven.apache.org/), le dézipper et ajouter le sous-répertoire bin du répertoire d'installation au [path de windows](https://www.java.com/fr/download/help/path.xml).

* Activer l'application. Voir la [documentation Heroku](https://devcenter.heroku.com/articles/getting-started-with-java#scale-the-app) pour plus d'informations et notamment les limitations de la version gratuite. 
  `heroku ps:scale web=1`
* Se connecter à l'URL de l'application

## Autres PaaS

Il existe d'autres offres PaaS, proposés notamment par les grosses entreprises du monde l'informatique : [Google App Engine](https://cloud.google.com), [Microsoft Windows Azure](https://azure.microsoft.com) ou [Amazon Web Services](https://aws.amazon.com) par exemple.

## Serveur virtuel

Une autre solution est d'héberger son site sur un serveur virtuel dédié. On trouve de telle solution chez la plupart des hébergeurs professionnel, par exemple les [VPS chez OVH](https://www.ovh.com/fr/vps/). C'est néanmoins payant même si les prix d'appel peuvent être raisonnables.

L'avantage de cette solution est la liberté car une machine linux est fournie et l'utilisateur peut alors installer tout ce dont il a besoin. Les inconvénients sont le coût et la complexité car il va falloir administrer le serveur Linux et cela demande des connaissances.

