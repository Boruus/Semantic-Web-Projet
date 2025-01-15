<template>
  <div class="pokestyle">
    <h1>Pokémon List</h1>
    <table>
      <thead>
        <tr>
          <th>Page</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="pokemon in filteredResults" :key="pokemon.page" @click="selectPokemon(pokemon.page)">
          <td><a href="#" @click.prevent="selectPokemon(pokemon.page)">{{ pokemon.page }}</a></td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'PokeListPage',
  data() {
    return {
      results: []
    }
  },
  computed: {
    filteredResults() {
      return this.results.filter(pokemon => !pokemon.page.includes('/resource/'))
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      const query = `
        PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
        SELECT ?page ?property ?value WHERE {
          ?page ?property ?value .
        }
      `
      
      const endpoint = 'http://localhost:3030/semwebPokeDB/sparql'
      const url = `${endpoint}?query=${encodeURIComponent(query)}&format=json`

      try {
        const response = await axios.get(url)
        const results = response.data.results.bindings
        const groupedResults = results.reduce((acc, result) => {
          const page = result.page.value
          if (!acc[page]) {
            acc[page] = { page, properties: {} }
          }
          const property = result.property.value
          const value = result.value.value
          if (!acc[page].properties[property]) {
            acc[page].properties[property] = []
          }
          acc[page].properties[property].push(value)
          return acc
        }, {})
        this.results = Object.values(groupedResults)
      } catch (error) {
        console.error('Error fetching data from Fuseki:', error)
      }
    },
    selectPokemon(page) {
      const pageName = page.split('/').pop()
      this.$router.push({ name: 'PokemonDetailPage', params: { pageName } })
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

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  border: 1px solid #ddd;
  padding: 8px;
}

th {
  background-color: #f2f2f2;
  text-align: left;
}

tbody tr:nth-child(even) {
  background-color: #f9f9f9;
}

tbody tr:hover {
  background-color: #e0e0e0;
  cursor: pointer;
}
</style>