<template>
  <div id="app">
    <img alt="logo" src="./assets/logo.png" id="logo">
    <nav>
      <ul>
        <li><router-link to="/">[Home]</router-link></li>
        <li><router-link to="/listPokemon">[Pokémon]</router-link></li>
        <li><router-link to="/listCards">[Pokémon Cards]</router-link></li>
        <li><router-link to="/pokeListTuples">[Tuples]</router-link></li>
        <li><router-link to="/attacks">[Attacks]</router-link></li>
        <li><router-link to="/regions">[Regions]</router-link></li>
        <li><router-link to="/credit">[Credit]</router-link></li>
      </ul>
    </nav>
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  name: 'App'
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

nav ul li .router-link-active {
  color: #ff0000;
  font-weight: bold;
  text-decoration: underline;
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