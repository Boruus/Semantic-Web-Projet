<template>
  <div class="pokestyle">
    <h1>Semantic Web Project</h1>
    <p>The Semantic Web project is a large and long practical exercise that consists in integrating all the pieces that have been seen during the first sessions into a consolidated Web application. To make sure you can advance sufficiently fast to cover everything, you are allowed to work by pair.</p>
    
    <h2>Main Objective</h2>
    <p>The objective of the project is to build a Knowledge Graph (KG) from a wiki, as DBpedia or YAGO have been built from Wikipedia. As a starting point, you will use Bulbapedia, an encyclopedia about Pokémons with 50k+ articles.</p>
    
    <h2>Requirements</h2>
    <ul>
      <li>The KG must capture the wiki's content as much as possible. Every page should correspond to an entity in the KG. Infoboxes on wiki pages should translate into RDF triples. If a link exists across wiki pages, there should be an RDF triple between the corresponding entities.</li>
      <li>KG entities must have labels in multiple languages (wherever possible).</li>
      <li>The KG should be validated against schemas derived from wiki templates.</li>
      <li>The vocabulary of the KG should be consistent and reuse schema.org or the DBpedia ontology (but not both) as much as possible.</li>
      <li>The KG should have links to other Web pages, especially those of the original wiki, and to resources in other KGs.</li>
      <li>The KG should be accessible through a SPARQL endpoint.</li>
      <li>Answers to SPARQL queries on the KG should include implicit facts.</li>
      <li>Each entity, class ou property of the KG should be accessible through a Linked Data interface, serving Turtle and (basic) HTML.</li>
      <li>The Linked Data interface may build upon the SPARQL endpoint (that is, the description of an entity, class ou property is the result of a SPARQL query evaluated on the KG).</li>
    </ul>
    
    <h2>Pedagogical Objectives</h2>
    <ul>
      <li>Do a little software development, using Semantic Web programming frameworks</li>
      <li>Setup and interact with an RDF database</li>
      <li>Exploit multiple sources of heterogeneous data</li>
      <li>Present information online with rich metadata</li>
    </ul>
    
    <h2>Timeline</h2>
    <p>The project starts full time on Friday 13th December 2024 but some of the practical sessions already provided the building blocks for the project. You will be working on your project full time during the sessions of the 13th of December afternoon, 20th of December morning and 10th of January all day.</p>
    <p>You must deliver all of your working files before the 15th of January 2024. All files must be sent strictly before the end of the day, in Central Europe Time. Every extra minute will be penalised. You must additionally provide a written report explaining your choices, the functionalities, etc. before the 24th of January, 2025 end of the day, Central European Time. Everything that comes after this deadline will be rejected as if nothing was delivered.</p>
    
    <h2>Resources</h2>
    <p>Bulbapedia is a wiki powered by MediaWiki, the wiki engine originally developed for Wikipedia. Because of that, the wiki's content can be accessed via the MediaWiki API. See the list of API calls available on Bulbapedia. The main API calls to use in the project are query and parse (they are called actions in the documentation). With the query action, you can list pages and categories of the wiki. With the parse action, you can get the source of a given page or extract specific elements, such as links to other pages ou images.</p>
    <p>All MediaWiki-powered wikis use the same syntax to edit pages: wikitext. An introduction by example is available on MediaWiki. The most relevant feature of wikitext in the project is templating. A template is a page containing text that is meant to be reused at several places in the wiki. Invoking the template in some page has the effect of inserting the template's text in that page. Templates may have parameters. See the MediaWiki documentation on templates for more details.</p>
    <p>Many client libraries exist to ease the development of bots accessing a MediaWiki API. See the list maintained on MediaWiki. Most of these libraries ensure that bots are "well-behaved", which typically implies that they do not overload the server with many requests (throughput is limited) but also that they declare themselves as bots (by setting an appropriate User-Agent header in their HTTP requests). To avoid being banned by the wiki server, we strongly recommend that you use a client library to access Bulbapedia.</p>
    <p>Many data bases refering to the world of Pokémons have been developed over many years. One in particular (the Github project veekun/pokedex) intends to capture all information supposed to be contained in the players' Pokédex. The TSV file pokedex-i18n.tsv is an extraction of labels and prose in several languages from the Pokédex.</p>
  </div>
</template>

<script>
export default {
  name: 'HomePage'
}
</script>

<style scoped>
.pokestyle {
  font-family: 'Press Start 2P', cursive;
  margin: 20px;
  background-color: #f2f2f2;
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

h1, h2 {
  color: #ff0000;
}

ul {
  list-style-type: disc;
  margin-left: 20px;
}

p {
  margin: 10px 0;
}
</style>