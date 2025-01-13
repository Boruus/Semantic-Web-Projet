<template>
  <div class="pokestyle">
    <h1>Pokémon List</h1>
    <table>
      <thead>
        <tr>
          <th>Name</th>
          <th>Japanese Name</th>
          <th>National Dex</th>
          <th>Type</th>
          <th>Height</th>
          <th>Weight</th>
          <th>Ability</th>
          <th>Generation</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="pokemon in results" :key="pokemon.nationalDex + pokemon.type">
          <td>{{ pokemon.name }}</td>
          <td>{{ pokemon.japaneseName }}</td>
          <td>{{ pokemon.nationalDex }}</td>
          <td>{{ pokemon.type }}</td>
          <td>{{ pokemon.height }}</td>
          <td>{{ pokemon.weight }}</td>
          <td>{{ pokemon.ability }}</td>
          <td>{{ pokemon.generation }}</td>
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
        SELECT ?name ?japaneseName ?nationalDex ?type ?height ?weight ?ability ?generation WHERE {
          ?pokemon <http://www.bulbapedia.org/resource/name> ?name .
          ?pokemon <http://www.bulbapedia.org/resource/japaneseName> ?japaneseName .
          ?pokemon <http://www.bulbapedia.org/resource/nationalDex> ?nationalDex .
          ?pokemon <http://www.bulbapedia.org/resource/type> ?type .
          ?pokemon <http://www.bulbapedia.org/resource/height> ?height .
          ?pokemon <http://www.bulbapedia.org/resource/weight> ?weight .
          OPTIONAL { ?pokemon <http://www.bulbapedia.org/resource/ability> ?ability . }
          OPTIONAL { ?pokemon <http://www.bulbapedia.org/resource/generation> ?generation . }
        }
      `
      const endpoint = 'http://localhost:3030/pokeDB/sparql'
      const url = `${endpoint}?query=${encodeURIComponent(query)}&format=json`

      try {
        const response = await axios.get(url)
        const results = response.data.results.bindings

        // Utiliser un objet pour éliminer les doublons
        const uniqueResults = {}
        results.forEach(result => {
          const key = `${result.nationalDex.value}-${result.type.value}`
          if (!uniqueResults[key]) {
            uniqueResults[key] = {
              name: result.name.value,
              japaneseName: result.japaneseName.value,
              nationalDex: result.nationalDex.value,
              type: result.type.value,
              height: result.height.value,
              weight: result.weight.value,
              ability: result.ability ? result.ability.value : '',
              generation: result.generation ? result.generation.value : ''
            }
          }
        })

        // Convertir l'objet en tableau
        this.results = Object.values(uniqueResults)
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