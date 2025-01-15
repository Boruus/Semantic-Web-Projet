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


