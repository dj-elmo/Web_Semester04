<template>
  <div class="login-container">
    <h2>Login</h2>
    <form @submit.prevent="handleLogin">
      <div>
        <label for="username">Benutzername:</label>
        <input id="username" v-model="username" type="text" autocomplete="username" />
      </div>
      <div>
        <label for="losung">Lösung:</label>
        <input id="losung" v-model="losung" type="password" autocomplete="current-password" />
      </div>
      <button type="submit">Login</button>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useLogin } from '@/stores/loginstore'
import { useInfo } from '@/composables/useInfo'

const username = ref('')
const losung = ref('')

const router = useRouter()
const loginStore = useLogin()
const { info, setzeInfo, loescheInfo } = useInfo()
const { loginState, logout, login } = loginStore

onMounted(() => {
  logout() // Benutzer wird beim Erscheinen ausgeloggt
})

function handleLogin() {
  login(username.value, losung.value)
  if (!loginState.loggedIn) {
    setzeInfo('Login fehlgeschlagen: Bitte beide Felder ausfüllen.')
    losung.value = ''
  } else {
    loescheInfo()
    router.push('/doener')
  }
}
</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 2rem auto;
  padding: 2rem;
  border: 1px solid #ccc;
  border-radius: 8px;
}
</style>