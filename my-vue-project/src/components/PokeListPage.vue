<template>
  <div class="pokestyle">
    <h1>Pokémon List</h1>
    <ul>
      <li v-for="pokemon in pokemons" :key="pokemon.id">
        <img :src="pokemon.image" :alt="pokemon.name" class="pokemon-image">
        <span>{{ pokemon.name }}</span>
      </li>
    </ul>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'PokeListPage',
  data() {
    return {
      pokemons: []
    }
  },
  created() {
    this.fetchPokemons()
  },
  methods: {
    async fetchPokemons() {
      const query = `
        PREFIX dbo: <http://dbpedia.org/ontology/>
        PREFIX dbr: <http://dbpedia.org/resource/>
        SELECT ?pokemon ?name ?image
        WHERE {
          ?pokemon a dbo:Pokemon ;
                   dbo:name ?name ;
                   dbo:image ?image .
        }
        LIMIT 10
      `
      const endpoint = 'http://localhost:3030/your-dataset/sparql'
      const url = `${endpoint}?query=${encodeURIComponent(query)}&format=json`

      try {
        const response = await axios.get(url)
        const results = response.data.results.bindings
        this.pokemons = results.map(result => ({
          id: result.pokemon.value,
          name: result.name.value,
          image: result.image.value
        }))
      } catch (error) {
        console.error('Error fetching data from Fuseki:', error)
      }
    }
  }
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

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.pokemon-image {
  width: 50px;
  height: 50px;
  margin-right: 10px;
}
</style>