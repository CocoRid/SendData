#SendData

##Description
Ce dépôt concerne un mini-projet pour le cours de M2M de la formation Master 2 M2PGI à l'IM²AG pour l'année 2013/2014.

Le but principal était de comprendre le fonctionnement du protocole MQTT et ainsi de l'utiliser dans un système embarqué, tel que sur une carte Arduino Intel Galileo ou, dans notre cas, avec un téléphone Android.

##Objectifs
L’objectif de ce mini-projet est d’envoyer des données provenant de capteurs d’un téléphone, s’exécutant sous Android, via le protocole MQTT à un serveur et ainsi enregistrer les données dans des fichiers.

Notre projet peut se diviser en trois parties :

* Dans un premier temps, nous devons récupérer des valeurs, en temps réel, de différents capteurs (accéléromètre et magnétomètre) installés sur téléphone s’exécutant avec le système Android. Une fois ces valeurs récupérées, il faut mettre à jour les valeurs affichées et cela à chaque fois qu’un capteur a une nouvelle valeur. Voici une capture écran de l’application :

![Capture d'écran de l'application Android] (images/screenshot_appli.png)

* Parallèlement, nous avons développé une application basique permettant d’envoyer des messages via le protocole MQTT à un serveur hébergé en local sur notre ordinateur. Les messages envoyés à partir de notre application devaient ensuite passer par une fonction permettant le formatage des données avant qu'elles soient enregistrées dans des fichiers (les informations concernant l’accéléromètre devant être séparée des informations concernant le magnétomètre). Voici un exemple du contenu de ces deux fichiers (les données sont formatées de manière très basique) :

![Capture d'écran des deux fichiers de données] (images/screenshot_data.png)

* Dans une dernière étape, nous avons combiné ces deux programmes. Ainsi, dès que nous recevons de nouvelles données d’un capteur du téléphone, nous devions l’afficher à l’écran et l’envoyer via le protocole MQTT sur un serveur local pour que les données soient enregistrées dans des fichiers.

##Contexte
Pour réaliser ce projet, nous avons utilisé divers logiciels et matériaux :

* Eclipse, pour le développement de l’application.
* Librairie Paho : http://www.eclipse.org/paho/
* Mosquitto, pour le serveur MQTT local.
* Un téléphone sous Android 4.4.2 (version CyanogenMod 11 non officiel). Capteurs présents sur ce téléphone : accéléromètre, magnétomètre, proximité, lumière ambiante.
* Node-Red, pour mettre en relation notre serveur MQTT local avec l’enregistrement des données dans des fichiers.
Voici la configuration que nous avons du réaliser sous Node-Red :
![Capture d'écran de la configuration de notre serveur, réalisé avec Node-Red] (images/screenshot_node_red.png)

##Problèmes rencontrés et solutions apportés :

#####Utilisation de MQTT sur Android :

Nous avons utilisé la libraire Paho pour faciliter notre développement de la partie MQTT.
Notre application Java, lors de son exécution sur un ordinateur produit bien le résultat attendu. En effet, il suffisait d’importer cette librairie sous forme d’un fichier .jar aux dépendances.
Cependant, lorsqu’on a passé cette implémentation sur notre application Android, l’application plantait au démarrage, renvoyant une erreur (provenant de la connexion MQTT : il manquait une dépendance).

######Solution 1
La librairie Paho ne fonctionnant pas à cause d'une dépendance qui manquait dans le .jar, nous avons essayé d'autres librairies MQTT ainsi que d'autres scripts que le notre pour nous connecter à notre serveur MQTT. Nous n'avons cependant eu aucun succès avec cette méthode.

######Solution 2
Grâce aux recherches précédentes sur les librairies et sur notre bug avec la librairie Paho, nous sommes tombés sur un sujet de *stackoverflow* indiquant qu'il ne fallait pas utiliser la librairie Paho sous forme d'un .jar, mais plutôt en utilisant les sources de la librairie Java et en les incluant dans un dossier *lib* de notre projet.

Nous avons donc recherché les sources de la librairie Paho pour les importer dans le dossier *lib* de notre projet. Cependant, toutes les sources ne se trouvaient pas dans le même fichier! En effet, la dépendance au fichier *ClientComms.java* manquait à l'appel, comme pour le fichier .jar. Ce fichier était bizarrement séparé des autres fichiers. Après l'avoir trouvé dans un dossier annexe et l'avoir ajouté à la librairie, une autre dépendance manquait, qui était aussi séparé des autres et que nous avons donc rajouté à la librairie. 

Une fois ces dépendances ajoutées à notre projet, l’application a enfin pu démarrer et s'exécuter correctement.

##Performances
Nous affichons les données de nos capteurs en temps réel dans notre application. Cela ne pose aucun problème.

Cependant, nous envoyons aussi en temps réel les données des capteurs via MQTT et nous pouvons remarquer qu'une erreur survient de temps en temps. 

Notre première solution à ce problème fût d'essayer l'envois de données à la chaîne via une application java, pour vérifier si cela venait du serveur surchargé ou de notre application. Le problème n'étant pas réapparu, nous pouvons en déduire que cela vient de notre application.

Notre seconde solution n'a pas pu être testé. Voici tout de même l'idée :
Notre application Android envois des données en réalité plus rapidement que notre application java précédente. Nous avons pu remarquer qu'aucun problème survenais lorsque le téléphone ne bougeait pas et donc ne devais pas traiter trop de données. Lorsque le téléphone bouge, beaucoup plus de données arrivent et cela force notre application à paralléliser le traitement de données et ainsi à envoyer plusieurs messages simultanément à MQTT.

On peut donc en déduire que soit le serveur est surchargé, soit l'application Android n'arrive pas à paralléliser l'envois de messages à MQTT. Dans les deux cas, la solution est la même : mettre un timer pour chaque capteur et ne permettre l'envois de messages à notre serveur MQTT que tous les X ms (il faudra faire varier ce délais suivant le nombre de données que nous voulons absolument recevoir, avec un risque de surcharge et donc de revoir apparaître l'erreur). Ainsi, au lieu de perdre beaucoup de données, nous en perdons toujours, mais nous pouvons tout de même en récupérer un certain nombre et de façon cohérente (il est préférable de recevoir toutes les X ms plutôt que de potentiellement ne rien recevoir pendant 10*X ms au moment critique).

##Perspectives
Ce mini-projet a été réalisé pour servir de base et d'apprentissage à un futur projet potentiel. En effet, des étudiants de Polytech ont pour projet d'utiliser trois capteurs bluetooth, positionné sur un mono-ski, pour récupérer certaines données et ensuite pouvoir les traiter.

Une application open-source est fournie avec ces capteurs et permet de visualiser en temps réel les données sur un téléphone Android.

On va ainsi pouvoir les aider en modifiant cette application, pour récupérer les données des différents capteurs et les enregistrer dans un fichier sur le téléphone pour permettre un traitement ultérieur de ces données. En plus de cet enregistrement, on va aussi pouvoir envoyer les données sur un serveur MQTT (dans le cas où une connexion internet existe) pour avoir une visualisation en direct sur un ordinateur.
