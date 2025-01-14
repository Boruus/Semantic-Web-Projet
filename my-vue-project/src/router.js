import { createRouter, createWebHistory } from 'vue-router'
import HomePage from './components/HomePage.vue'
import PokeListPage from './components/PokeListPage.vue'
import PokemonDetailPage from './components/PokemonDetailPage.vue'

const routes = [
  { path: '/', component: HomePage },
  { path: '/pokemon', component: PokeListPage },
  { path: '/pokemon/:page', name: 'PokemonDetail', component: PokemonDetailPage, props: true }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router