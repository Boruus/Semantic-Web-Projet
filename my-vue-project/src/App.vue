<template>
  <div id="app">
    <img alt="logo" src="./assets/logo.png" id="logo">
    <nav>
      <ul>
        <li><a href="#" @click.prevent="currentPage = 'home'">Home</a></li>
        <li><a href="#" @click.prevent="currentPage = 'pokemon'">Pokémon</a></li>
        <li><a href="#" @click.prevent="currentPage = 'pokeListTuples'">Tuples</a></li>
        <li><a href="#" @click.prevent="currentPage = 'attacks'">Attacks</a></li>
        <li><a href="#" @click.prevent="currentPage = 'regions'">Regions</a></li>
        <li><a href="#" @click.prevent="currentPage = 'credit'">Credit</a></li>
      </ul>
    </nav>
    <button class="pokemon-button" @click="runPokemonRDFGenerator">Run Pokemon RDF Generator</button>
    <component ref="pokeList" :is="currentPageComponent" :page="selectedPage" :details="selectedDetails" @back="selectedPage = null" @selectPokemon="selectPokemon"></component>
  </div>
</template>

<script>
import axios from 'axios'
import HomePage from './components/HomePage.vue'
import PokeListPage from './components/PokeListPage.vue'
import PokeListTuplesPage from './components/PokeListTuplesPage.vue'
import AttacksPage from './components/AttacksPage.vue'
import RegionsPage from './components/RegionsPage.vue'
import Credit from './components/Credit.vue'
import PokemonDetailPage from './components/PokemonDetailPage.vue'

export default {
  name: 'App',
  components: {
    HomePage,
    PokeListTuplesPage,
    PokeListPage,
    AttacksPage,
    RegionsPage,
    Credit,
    PokemonDetailPage
  },
  data() {
    return {
      currentPage: 'home',
      selectedPage: null,
      selectedDetails: null
    }
  },
  computed: {
    currentPageComponent() {
      if (this.selectedPage) {
        return 'PokemonDetailPage'
      }
      switch (this.currentPage) {
        case 'home':
          return 'HomePage'
        case 'pokeListTuples':
          return 'PokeListTuplesPage'
        case 'pokemon':
          return 'PokeListPage'
        case 'attacks':
          return 'AttacksPage'
        case 'regions':
          return 'RegionsPage'
        case 'credit':
          return 'Credit'
        default:
          return 'HomePage'
      }
    }
  },
  methods: {
    async runPokemonRDFGenerator() {
      try {
        const response = await axios.get('http://localhost:3000/run-pokemonRDFGenerator');
        console.log(response.data);
        alert('Pokemon RDF Generator executed successfully');
      } catch (error) {
        console.error('Error executing Pokemon RDF Generator:', error);
        alert('Error executing Pokemon RDF Generator');
      }
    },
    selectPokemon(page) {
      if (this.$refs.pokeList && this.$refs.pokeList.results) {
        const selectedPokemon = this.$refs.pokeList.results.find(pokemon => pokemon.page === page)
        this.selectedPage = page
        this.selectedDetails = selectedPokemon ? selectedPokemon.properties : null
      } else {
        console.error('PokeList results are not available')
      }
    }
  }
}
</script>

<style>
#app {
  font-family: 'Press Start 2P', cursive;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
  background-color: #f2f2f2;
}

#logo {
  height: 150px;
  pointer-events: none;
}

nav ul {
  list-style-type: none;
  padding: 0;
  background-color: #ffcc00;
  border-radius: 10px;
  display: inline-block;
  padding: 10px 20px;
}

nav ul li {
  display: inline;
  margin-right: 10px;
}

nav ul li a {
  text-decoration: none;
  color: #2c3e50;
  font-weight: bold;
}

nav ul li a:hover {
  color: #ff0000;
}

.pokemon-button {
  background-color: #c6c4bc;
  border: 2px solid #2c3e50;
  border-radius: 10px;
  color: #000000;
  font-family: 'Press Start 2P', cursive;
  font-size: 16px;
  padding: 10px 20px;
  cursor: pointer;
  transition: background-color 0.3s, color 0.3s;
}

.pokemon-button:hover {
  background-color: #000000;
  color: #ffffff;
}
</style>