DetailPage.vue
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
    <button class="pokemon-button" @click="$emit('back')">Back to List</button>
  </div>
</template>

<script>
export default {
  name: 'PokemonDetailPage',
  props: ['page', 'details'],
  methods: {
    back() {
      this.$emit('back')
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