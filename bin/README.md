# Pokémon RDF Project

## Description
This project creates a semantic web representation of Pokémon data using RDF. It fetches Pokemon information, processes infoboxes, generates RDF triples, validates them using SHACL, and integrates with an RDF triplestore such as Apache Jena Fuseki.

- All the generated RDF triples can be found in the file `pokemon_output.ttl`.
- The SHACL shapes used for validation can be found in the file `resources/shacl/shapes.ttl`.
- The vocabulary used in the project is defined in `resources/vocabulary/vocabulary.ttl`.

---

## Installation and Setup

### 1. **Requirements**
- Java 8 or later.
- Apache Jena and Fuseki server.

### 2. **How to run**
- Start Apache Jena Fuseki.
- Create a dataset with this name : semwebPokeDB.
- Execute the file `PokemonRDFGenerator.java` to generate RDF triples, you can find them in pokemon_output.ttl.
- Run `RdfValidator.java` to validate RDF against SHACL shapes.

---

## File Descriptions

### 1. `FusekiConnection.java`
- Handles the connection to an Apache Jena Fuseki triplestore.
- **Functions**:
  - Inserts RDF data into a Fuseki server.
  - Queries data using SPARQL.
- **Configuration**:
  - Set server URLs and dataset details within this file.

### 2. `PokemonInfoboxRDFGenerator.java`
- Extracts structured data from Pokémon infoboxes.
- Converts the data into RDF triples, aligning with `schema.org` and custom vocabulary.

### 3. `PokemonNames.java`
- Provides utility functions for handling Pokémon names.
- **Features**:
  - Maps multilingual Pokémon names.
  - Validates name formats.

### 4. `PokemonPagesFetcher.java`
- Fetches Pokémon-related pages using the MediaWiki API.
- **Functions**:
  - Retrieves infobox content from Pokémon wiki pages.
  - Extracts external links and related page information.

### 5. `PokemonRDFGenerator.java`
- Orchestrates the RDF generation process.
- Uses data from `PokemonPagesFetcher` and `PokemonInfoboxRDFGenerator`.
- Produces RDF models compatible with SHACL validation.

### 6. `RdfValidator.java`
- Validates RDF triples against SHACL shapes.
- Ensures that the generated triples conform to predefined schema rules.

### 7. `TsvReader.java`
- Reads TSV files containing multilingual Pokémon names.
- Maps Pokémon IDs to their names in different languages.
- **Usage**:
  - Input multilingual Pokémon names from external sources for RDF generation.

### 8. `UtilsFunctions.java`
- Contains utility functions for:
  - Resolving redirects in MediaWiki pages.
  - Mapping Pokémon names to their unique IDs.
  - Converting languages into ISO codes.

---


# Front - Vue.js Interface

### Vue.js Components

- **App.vue**: The main component that manages navigation between different pages.
- **HomePage.vue**: The project's homepage with a description and objectives of the project.
- **PokeListPage.vue**: Displays a list of Pokémon pages. Clicking on a page redirects the user to the details of that page.
- **PokeListCardsPage.vue**: Displays a list of Pokémon cards. Clicking on a page redirects the user to the details of that page.
- **PokeListTuplesPage.vue**: Displays a list of RDF tuples (predicate-object) for Pokémon.
- **AttacksPage.vue**: Displays a list of Pokémon attacks (under development).
- **RegionsPage.vue**: Displays a list of Pokémon regions (under development).
- **Credit.vue**: Displays the project credits.
- **PokemonDetailPage.vue**: Displays the details of a selected Pokémon page.

### JavaScript Files

- **main.js**: Main entry point of the Vue.js application.
- **router.js**: Defines the routes for navigation between different pages of the application.
- **server.js**: An Express server that handles CORS requests and runs the Java RDF generator.

## Installation and Setup (Front)

1. If needed, clone the GitHub repository:
  ```sh
  git clone https://github.com/Boruus/Semantic-Web-Projet.git
   cd Semantic-Web-Projet/semweb-front

2. Installez les dépendances Node.js :

    npm install

## Démarrage du Projet

1. Démarrez le serveur Express :

    npm run serve

2. Ouvrez votre navigateur et accédez à 

    http://localhost:8080


### Navigation

- **Home** : Page d'accueil avec une description du projet.
- **Pokémon** : Affiche une liste de pages Pokémon. Cliquez sur une page pour voir les détails.
- **Pokémon Cards** : Affiche une liste de cartes Pokémon. Cliquez sur une page pour voir les détails.
- **Tuples** : Affiche une liste de tuples RDF (prédicat-objet) pour les Pokémon.
- **Attacks** : Affiche une liste des attaques Pokémon (en cours de développement).
- **Regions** : Affiche une liste des régions Pokémon (en cours de développement).
- **Credit** : Affiche les crédits du projet.

### Génération de RDF (en cours de développement)

Pour exécuter le générateur RDF, cliquez sur le bouton "Run Pokemon RDF Generator" sur la page principale. Cela enverra une requête au serveur Express pour exécuter le programme Java `PokemonRDFGenerator`.

## Développeurs

- **Boris Trombert**
- **Mathias CHANE-WAYE**

### Supervision

- **Antoine Zimmermann**
- **Maxime Lefrançois**

## Ressources

- **Bulbapedia**
- **Infobox Templates**