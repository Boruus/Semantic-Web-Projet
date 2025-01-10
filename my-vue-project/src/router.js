import { createRouter, createWebHistory } from 'vue-router'
import HomePage from './components/HomePage.vue'
import PokeListPage from './components/PokeListPage.vue'
import AttacksPage from './components/AttacksPage.vue'
import RegionsPage from './components/RegionsPage.vue'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: HomePage
  },
  {
    path: '/pokemon',
    name: 'Pokemon',
    component: PokeListPage
  },
  {
    path: '/attacks',
    name: 'Attacks',
    component: AttacksPage
  },
  {
    path: '/regions',
    name: 'Regions',
    component: RegionsPage
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router