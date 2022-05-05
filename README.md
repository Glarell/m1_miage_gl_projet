# GL MIAGE - PROJET
Le but de ce projet est de suivre et de gérer l'occupation d'un parc de bornes de recharge pour véhicules électriques et de permettre aux clients de trouver et de réserver les bornes disponibles.

Chaque Choix dans le package commandes correspond à une fonctionnalité
Il suffit de déclarer dans la méthode execute le comportement de ce choix
(On peut utiliser le scanner passé en paramètre)
Tout les choix héritent de ChoicesAbstract et donc doivent Override execute()
Choices permet de regrouper les différents choix pour les différents utilisateurs
Application permet de gérer le fonctionnement global de l'application et d'appliquer des fonctions génériques aux différents services