import Vue from 'vue'
import Router from 'vue-router'
import YandexMap from './views/YandexMap'
import GoogleMap from './views/GoogleMap'

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: '/',
      name: 'home',
      component: YandexMap
    },
    {
      path: '/yandex-map',
      name: 'yandexMap',
      component: YandexMap
    },
    {
      path: '/google-map',
      name: 'googleMap',
      component: GoogleMap
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (about.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import(/* webpackChunkName: "about" */ './views/About.vue')
    }
  ]
})
