# Semantic Web Project - Vue.js Interface

## Description

Ce projet est une interface utilisateur pour un projet de Web sémantique basé sur Vue.js. 
L'objectif du projet est de construire un Knowledge Graph (KG) à partir de Bulbapedia, une encyclopédie sur les 
Pokémon. L'interface permet de visualiser les données RDF générées et de naviguer à travers différentes pages 
d'informations sur les Pokémons Cards, les attaques, les régions, etc.

## Structure du Projet

### Composants Vue.js

- **App.vue** : Le composant principal qui gère la navigation entre les différentes pages et exécute le générateur RDF.
- **HomePage.vue** : La page d'accueil du projet avec une description et les objectifs du projet.
- **PokeListPage.vue** : Affiche une liste de pages Pokémon. En cliquant sur une page, l'utilisateur est redirigé vers les détails de cette page.
- **PokeListTuplesPage.vue** : Affiche une liste de tuples RDF (prédicat-objet) pour les Pokémon.
- **AttacksPage.vue** : Affiche une liste des attaques Pokémon (en cours de développement).
- **RegionsPage.vue** : Affiche une liste des régions Pokémon (en cours de développement).
- **Credit.vue** : Affiche les crédits du projet.
- **PokemonDetailPage.vue** : Affiche les détails d'une page Pokémon sélectionnée.

### Fichiers JavaScript

- **server.js** : Un serveur Express qui gère les requêtes CORS et exécute le générateur RDF Java.

### Fichiers Java

- **pokemonRDFGenerator.java** : Un programme Java qui génère des données RDF à partir de Bulbapedia.

## Installation

1. Clonez le dépôt GitHub :
   ```sh
   git clone link
   cd Semantic-Web-Projet/my-vue-project


2. Installez les dépendances Node.js :

    npm install

## Démarrage du Projet

1. Démarrez le serveur Express :

    npm run serve

2. Ouvrez votre navigateur et accédez à 

    http://localhost:8080


### Utilisation

## Navigation

- **Home** : Page d'accueil avec une description du projet.
- **Pokémon** : Affiche une liste de pages Pokémon. Cliquez sur une page pour voir les détails.
- **Tuples** : Affiche une liste de tuples RDF (prédicat-objet) pour les Pokémon.
- **Attacks** : Affiche une liste des attaques Pokémon (en cours de développement).
- **Regions** : Affiche une liste des régions Pokémon (en cours de développement).
- **Credit** : Affiche les crédits du projet.

## Génération de RDF

(en cours de développement)
Pour exécuter le générateur RDF, cliquez sur le bouton "Run Pokemon RDF Generator" sur la page principale. 
Cela enverra une requête au serveur Express pour exécuter le programme Java pokemonRDFGenerator.