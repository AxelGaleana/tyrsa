<template>
  <v-app>
    <template
      v-if="
        this.$store.getters.isLoggedIn &&
          this.$store.getters.authStatus !== 'error'
      "
    >
      <app-navigation></app-navigation>
      <v-main>
        <router-view></router-view>
      </v-main>
    </template>
    <template v-else>
      <app-login-form></app-login-form>
    </template>
  </v-app>
</template>

<script>
import AppNavigation from "./components/AppNavigation";
import AppLoginForm from "./components/AppLoginForm";

export default {
  name: "App",
  components: {
    AppNavigation,
    AppLoginForm
  },
  data: () => ({
    //
  })
};
</script>
