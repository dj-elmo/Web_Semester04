<template>
  <div>
    <header class="kopf-header">
      <div class="kopf-header-row">
        <div class="kopf-logo-title">
          <!--<img src="/images/donerLogo.png" alt="Logo" class="kopf-logo" />-->
          <span class="kopf-title">Dönerverleih</span>
        </div>
        <nav class="kopf-nav">
          <router-link class="kopf-nav-link" to="/doener" v-if="loginStore.loginState.loggedIn">Katalog</router-link>
        </nav>
        <nav class="kopf-nav">
          <router-link class="kopf-nav-link" to="/login">Login</router-link>
        </nav>
      </div>
    </header>

    <!-- Infobox -->
    <div v-if="info" class="info-box">
      <div class="info-header">
        <span>Info</span>
        <button class="close-btn" @click="loescheInfo">x</button>
      </div>
      <div class="info-content">{{ info }}</div>
    </div>

    <!--<DoenerListeView />-->
    <!--<LoginView />-->
    <RouterView />

    <footer class="kopf-footer">
      <span v-if="loginStore.loginState.loggedIn" class="username">
        {{ loginStore.loginState.username }}
      </span>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import DoenerListeView from './views/DoenerListeView.vue'
import { useInfo } from '@/composables/useInfo';
import LoginView from './views/LoginView.vue';
import { useLogin } from '@/stores/loginstore'

const loginStore = useLogin()
const {info, loescheInfo} = useInfo()
</script>

<style>
.info-box {
  border: 1px solid #ccc;
  margin: 10px 0;
}
.kopf-header-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.kopf-nav .kopf-nav-link{
  color: white
}

.info-header {
  width: 100%;
  background-color: rgb(199, 206, 212);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-content {
  background-color: #f0f8ff;
  margin-top: 10px; /* Abstand zwischen Header und Content */
}
.close-btn {
  background: none;
  border: none;
  color: red;
  font-weight: bold;
  cursor: pointer;
}
.kopf-footer {
  display: flex;
  justify-content: flex-end; 
  align-items: center;
  padding-right: 2rem
}
.username {
  margin-left: 1rem;
  color: white;
}
</style>
