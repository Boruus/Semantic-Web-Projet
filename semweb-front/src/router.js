import { createRouter, createWebHistory } from 'vue-router'
import HomePage from './components/HomePage.vue'
import PokeListPage from './components/PokeListPage.vue'
import PokemonDetailPage from './components/PokemonDetailPage.vue'
import PokeListTuplesPage from './components/PokeListTuplesPage.vue'
import AttacksPage from './components/AttacksPage.vue'
import RegionsPage from './components/RegionsPage.vue'
import CreditPage from './components/Credit.vue'
import PokeListCardsPage from './components/PokeListCardsPage.vue'

const routes = [
  { path: '/', component: HomePage },
  { path: '/listPokemon', name: 'PokeListPage', component: PokeListPage },
  { path: '/listCards', name: 'PokeListCardsPage', component: PokeListCardsPage },
  { path: '/page/:pageName', name: 'PokemonDetailPage', component: PokemonDetailPage, props: true },
  { path: '/pokeListTuples', name: 'PokeListTuplesPage', component: PokeListTuplesPage },
  { path: '/attacks', name: 'AttacksPage', component: AttacksPage },
  { path: '/regions', name: 'RegionsPage', component: RegionsPage },
  { path: '/credit', name: 'CreditPage', component: CreditPage }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router