import { createRouter, createWebHistory } from 'vue-router'

import LoginView from '@/views/LoginView.vue'; 
import DoenerListeView from '@/views/DoenerListeView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {path: '/login', component: LoginView},
    {path: '/doener', component: DoenerListeView},
    {path: '/', redirect: '/doener'}
  ],
})

export default router
