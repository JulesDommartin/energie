# Energie

Ce projet a pour but de trouver le chemin optimal pour effectuer la livraison de client avec un véhicule électrique.  
Ce projet a été réalisé dans le cadre de l’option Energie en M2 Miage.


ATTENTION: Ce projet est sous licence MIT.

## Requirements

- Java 8 min
- JUnit 5

## Compilation

Pour compiler et lancer le programme, ouvrir un terminal, se placer dans le dossier "src", et exécuter les commandes suviantes:

- javac ./Main.java ./data/Assets.java ./data/Client.java ./data/Depot.java ./data/Point.java ./data/Vehicule.java  
- java Main

Pour changer d'instance, modifier le fichier suivant : ./data/Assets.java et modifier la ligne 10, remplacer 'instance_0' par 'instance_1'.

Export des données : 
Chaque ligne est un véhicule, "C" => passage au dépôt, "R" => rechargement de la batterie.
Exemple : 
- Véhicule 1 : 2,4,C,5,R,12
- Véhicule 2 : 6,3,C,7,C,8

JSON format : 
{
  "depot": {
    "latitude": 46.55,
    "longitude": 34.44
  },
  "clients": [
    {
      "latitude": 45.54,
      "longitude": 34.43
    },
    {
      ...
    },
    ...
  ]
}


## Tests

Les tests sont dans le fichier ApplicationTest
Ils ont été écrits avec la librairie Junit 4.0