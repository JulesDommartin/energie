# Energie

Ce projet a pour but de trouver le chemin optimal pour effectuer la livraison de client avec un véhicule électrique.  
Ce projet a été réalisé dans le cadre de l’option Energie en M2 Miage.


Pour compiler et lancer le programme, ouvrir un terminal, se placer dans le dossier "src", et exécuter les commandes suviantes:

- javac .\Main.java .\data\Assets.java .\data\Client.java .\data\Depot.java .\data\Point.java .\data\Vehicule.java  
- java Main

Pour changer d'instance, modifier le fichier suivant : .\data\Assets.java et modifier la ligne 10, remplacer 'instance_0' par 'instance_1'.

Export des données : 
Chaque ligne est un véhicule, "C" => passage au dépôt, "R" => rechargement de la batterie.
Exemple : 
- Véhicule 1 : 2,4,C,5,R,12
- Véhicule 2 : 6,3,C,7,C,8
