import { defineStore } from 'pinia';

export const useLogin = defineStore('loginstore', {
  state: () => ({
    loginState: {
      username: '',
      loggedIn: false,
    },
  }),
  actions: {
    login(username: string, losung: string) {
      if (username.trim() !== '' && losung.trim() !== '') {
        this.loginState.username = username;
        this.loginState.loggedIn = true;
      } else {
        this.logout();
      }
    },
    logout() {
      this.loginState.username = '';
      this.loginState.loggedIn = false;
    },
  },
});