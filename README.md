#SendData

##Description
Ce dépôt concerne un mini-projet pour le cours de M2M de la formation Master 2 M2PGI à l'IM²AG, Université Joseph Fourrier.

Le but principale était de comprendre le fonctionnement du protocole MQTT et ainsi de l'utiliser dans un système embarqué, tel que sur un carte Arduino Intel Galileo ou, dans notre cas, avec un téléphone Android.

##Objectifs
L’objectif de ce mini-projet est d’envoyer des données provenant de capteurs d’un téléphone, s’exécutant sous Android, via le protocole MQTT à un serveur et ainsi enregistrer les données dans des fichiers.

Notre projet peut se diviser en trois parties :

* Dans un premier temps, nous devons récupérer des valeurs, en temps réel, de différents capteurs (accéléromètre et magnétomètre) installés sur téléphone s’exécutant avec le système Android. Une fois ces valeurs récupéré, il a fallait les afficher à l’écran. Il faut donc que les valeurs affichés soit actualisées à chaque fois qu’un capteur a une nouvelle valeur. Voici un capture écran de l’application : 

![Capture d'écran de l'application Android] (images/screenshot_appli.png)

* Parallèlement, nous avons développer une application basique permettant d’envoyer des messages via le protocole MQTT à un serveur hébergé en local sur notre ordinateur. Les messages envoyés à partir de notre application devaient ensuite passer par une fonction permettant le formatage des données avant qu'elles soient enregistrés dans des fichiers (les informations concernant l’accéléromètre devant être séparée des informations concernant le magnétomètre). Voici un exemple du contenu de ces deux fichiers (les données sont formatés de manière très basique) :

![Capture d'écran des deux fichiers de données] (images/screenshot_data.png)

* Dans une dernière étape, nous avons combiné ces deux programmes. Ainsi, des que nous recevons de nouvelles données d’un capteur du téléphone, nous devions l’afficher à l’écran et l’envoyer via le protocole MQTT sur un serveur local pour que les données soient enregistrés dans des fichiers.
