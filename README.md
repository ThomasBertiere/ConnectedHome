# La maison intelligente
 
L’objectif principal de ce projet était de rendre une maison plus intelligente par le biais d'objets connectés. Dans un premier temps, nous nous sommes dirigés vers une maison intelligente au service de personnes touchées par un handicap. Puis nous avons étendu nos fonctionnalités à une utilisation quotidienne pour faciliter la vie de toute personne utilisant notre système.

## Fonctionnalités implémentées
- Allumer/éteindre des appareils électroniques de la maison à partir de la reconnaissance faciale d'un casque cérébral (lorsqu'un utilisateur serre les dents).
- Commander une télévision (changer les chaînes et le volume) en faisant des mouvements de tête grâce au gyroscope d'un casque cérébral. 
    - mouvement vers le haut : volume +
    - mouvement vers le bas : volume -
    - mouvement vers la droite : programme +
    - mouvement vers la gauche : programme -
- Allumage d'une bande de LEDs de façon progressive pour un réveil en douceur. Par le biais d’un capteur de luminosité, nous allons détecter lorsque la bande de LEDs émet à pleine puissance et la météo actuelle sera enoncée. 
- Utilisation un capteur de pression qui détecte lorsqu'une personne s'assied. De la musique est alors lancée sur un ordinateur de la maison.

## Outils et capteurs utilisés
- Projet Eclipse OM2M developpé par le LAAS/CNRS pour instancier les standards OneM2M permettant de faire communiquer nos appareils connectés dans l'internet et d'interagir avec eux. (Lien du projet OM2M : http://www.eclipse.org/om2m/)
- Casque cérébral Emotiv équipé d'électrodes et d'un gyroscope.
- API Emotiv pour pouvoir interagir avec le casque cérébral. 
- Carte Edison d'Intel utilisée comme gateway OM2M.
- Carte Arduino.
- Emetteur infrarouge grove pour la commande de la télévision.
- Capteur phidget de pression et de luminosité.
- Relais grove pour allumer/éteindre tous appareils électroniques reliés à une rallonge spécifique.
- API iTunes pour jouer de la musique lorsqu'un utilisateur s'assied.

## Architecture réalisée
![Image de l'architecture réalisée](./Images/Architecture.png)

## Fichiers du dossier
Vous trouverez dans ce dossier 6 projets java Eclipse : 
- Gestion du casque cérébral : 
    - FacialProject : utilisation des électrodes du casque cérébral pour reconnaitre les expressions faciales.
    - GyroProject : utilisation du gyroscope du casque cérébral pour détecter les mouvements de la tête.
- Serveur : projet à lancer sur un serveur OM2M pour le configurer, configurer le réseau d'objets connectés et exécuter les programmes traitant les données de ces objets
- Gateway : projet à lancer sur une gateway OM2M pour la configurer, lui donner une vision du réseau d'objets connectés et exécuter les programmes permettant la récupération et le transfert des données entre le serveur OM2M et les objets connectés.
- OrdinateurMusique : projet à lancer sur l'ordinateur devant jouer de la musique lorsqu'un utilisateur s'assied. Cet ordinateur doit être un mac pour pouvoir accéder à l'API iTunes.
- OrdinateurPhidget : projet à lancer sur l'ordinateur connecté aux capteurs phidget pour récupérer leur donnée et les envoyer à la gateway.

## Vidéo de démonstration
Une vidéo de démonstration de notre projet est visible sur ce lien : 
https://www.youtube.com/watch?v=jFGfroyDhUQ

## Contributeurs
Projet réalisé par Guillaume DE BRITO, Thomas BERTIERE, Bastien BIGUE et Rama DESPLATS dans le cadre du projet tutoré de la 4ème année Génie Informatique à l'INSA de Toulouse.
