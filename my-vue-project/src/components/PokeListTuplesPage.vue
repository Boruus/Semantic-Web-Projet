<template>
  <div class="pokestyle">
    <h1>Pokémon List Tuples</h1>
    <table>
      <thead>
        <tr>
          <th>Predicate</th>
          <th>Object</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="result in results" :key="result.pred.value + result.obj.value">
          <td>{{ getPredicateName(result.pred.value) }}</td>
          <td>{{ result.obj.value }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'PokeListTuplesPage',
  data() {
    return {
      results: []
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
        SELECT * WHERE {
          ?sub ?pred ?obj .
        }
      `
      const endpoint = 'http://localhost:3030/pokeDB/sparql'
      const url = `${endpoint}?query=${encodeURIComponent(query)}&format=json`

      try {
        const response = await axios.get(url)
        const results = response.data.results.bindings
        this.results = results.map(result => ({
          pred: result.pred,
          obj: result.obj
        }))
      } catch (error) {
        console.error('Error fetching data from Fuseki:', error)
      }
    },
    getPredicateName(predicate) {
      const parts = predicate.split(/[/#]/)
      return parts[parts.length - 1]
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
</style>