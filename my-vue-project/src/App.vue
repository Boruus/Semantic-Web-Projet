<template>
  <div id="app">
    <img alt="logo" src="./assets/logo.png" id="logo">
    <nav>
      <ul>
        <li><a href="#" @click.prevent="currentPage = 'home'">Home</a></li>
        <li><a href="#" @click.prevent="currentPage = 'pokemon'">Pokémon</a></li>
        <li><a href="#" @click.prevent="currentPage = 'attacks'">Attacks</a></li>
        <li><a href="#" @click.prevent="currentPage = 'regions'">Regions</a></li>
      </ul>
    </nav>
    <component :is="currentPageComponent"></component>
  </div>
</template>

<script>
import HomePage from './components/HomePage.vue'
import PokeListPage from './components/PokeListPage.vue'
import AttacksPage from './components/AttacksPage.vue'
import RegionsPage from './components/RegionsPage.vue'

export default {
  name: 'App',
  components: {
    HomePage,
    PokeListPage,
    AttacksPage,
    RegionsPage
  },
  data() {
    return {
      currentPage: 'home'
    }
  },
  computed: {
    currentPageComponent() {
      switch (this.currentPage) {
        case 'home':
          return 'HomePage'
        case 'pokemon':
          return 'PokeListPage'
        case 'attacks':
          return 'AttacksPage'
        case 'regions':
          return 'RegionsPage'
        default:
          return 'HomePage'
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
</style>