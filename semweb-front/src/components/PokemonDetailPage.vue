<template>
  <div class="pokestyle" v-if="details">
    <h1>Pokémon Detail</h1>
    <h2>{{ page }}</h2>
    <table>
      <thead>
        <tr>
          <th>Property</th>
          <th>Value</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(values, key) in details" :key="key">
          <td>{{ key }}</td>
          <td>
            <ul>
              <li v-for="value in values" :key="value">{{ value }}</li>
            </ul>
          </td>
        </tr>
      </tbody>
    </table>
    <button class="pokemon-button" @click="back">Back to List</button>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'PokemonDetailPage',
  props: ['pageName', 'origin'],
  data() {
    return {
      details: null,
      page: ''
    }
  },
  created() {
    this.fetchDetails()
  },
  methods: {
    async fetchDetails() {
      const page = `/resource/${this.pageName}`
      this.page = page
      //console.log('page:', page)

      const query = `
        PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
        PREFIX owl: <http://www.w3.org/2002/07/owl#>
        PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
        PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
        SELECT ?property ?value WHERE {
          <${this.pageName}> ?property ?value .
        }
      `
      
      const endpoint = 'http://localhost:3030/semwebPokeDB/sparql'
      const url = `${endpoint}?query=${encodeURIComponent(query)}&format=json`

      try {
        const response = await axios.get(url)
        const results = response.data.results.bindings
        const details = results.reduce((acc, result) => {
          const property = result.property.value
          const value = result.value.value
          if (!acc[property]) {
            acc[property] = new Set()
          }
          acc[property].add(value)
          return acc
        }, {})
        // Convertir les Sets en tableaux pour l'affichage
        for (const key in details) {
          details[key] = Array.from(details[key])
        }
        this.details = details
      } catch (error) {
        console.error('Error fetching data from Fuseki:', error)
      }
    },
    back() {
      if (this.pageName.includes('cards') || this.pageName.includes('Cards')) {
        this.$router.push({ name: 'PokeListCardsPage' })
      } else {
        this.$router.push({ name: 'PokeListPage' })
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

.pokemon-button {
  background-color: #ffcc00;
  border: 2px solid #2c3e50;
  border-radius: 10px;
  color: #2c3e50;
  font-family: 'Press Start 2P', cursive;
  font-size: 16px;
  padding: 10px 20px;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
}

.pokemon-button:hover {
  background-color: #2c3e50;
  color: #ffcc00;
}
</style>