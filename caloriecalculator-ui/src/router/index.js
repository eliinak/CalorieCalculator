import Vue from 'vue'
import VueRouter from 'vue-router'
import HomeView from '../views/HomeView.vue'
// import UserView from '@/views/UserView'
import Register from '@/views/Register'
// import MyFood from '@/views/MyFood'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/user',
    name: 'User',
    component: () => import(/* webpackChunkName: "about" */ '../views/UserView.vue')
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/Profile.vue')
  },
  {
    path: '/myfood',
    name: 'MyFood',
    component: () => import('../views/MyFood.vue')
  },
  {
    path: '/bmi',
    name: 'Bmi',
    component: () => import('../views/BMI.vue')
  }
]

const router = new VueRouter({
  routes
})

export default router
