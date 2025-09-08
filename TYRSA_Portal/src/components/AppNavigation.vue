<template>
  <div>
    <v-app-bar app fixed light>
      <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
      <v-toolbar-title>{{ $route.name }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn @click="logout" icon>
        <v-icon title="Salir del sistema">logout</v-icon>
      </v-btn>
    </v-app-bar>
    <v-navigation-drawer
      v-model="drawer"
      app
      dark
      floating
      permanent
      src="@/assets/somoscidec.png"
    >
      <v-list nav>
        <v-list-item>
          <v-img
            contain
            aspect-ratio="2.7"
            src="@/assets/logo_bioacceso.png"
          ></v-img>
        </v-list-item>
      </v-list>
      <v-divider light></v-divider>
      <v-list nav>
        <v-list-item
          v-for="(link, i) in activeLinks"
          :key="i"
          :to="link.to"
          :active-class="color"
        >
          <v-list-item-icon>
            <v-icon>{{ link.icon }}</v-icon>
          </v-list-item-icon>
          <v-list-item-content>
            <v-list-item-title>{{ link.text }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
  </div>
</template>

<script>
import LogoutService from "@/services/LogoutService";

export default {
  name: "AppNavigation",
  data() {
    return {
      drawer: true,
      links: [
        {
          to: "/administradores",
          icon: "person",
          text: "Administradores"
        }
      ]
    };
  },
  computed: {
    image() {
      return this.$store.state.image;
    },
    color() {
      return this.$store.state.color;
    },
    activeLinks() {
      let role = this.$store.getters.getUser.role
      console.log("this.$store.getters.getUser: ", this.$store.getters.getUser);
      let l = [];
      this.links.forEach(function (element) {
        if (role === 'ROLE_ADMIN' /*&& element.text.toString() !== 'Administradores' && element.text.toString() !== 'Dispositivos' && element.text.toString() !== 'Contratistas'*/){
          l.push(element);        
        }
      });
      return l;
    }
  },
  methods: {
    async logout() {
      return LogoutService.logout(this.$store.getters.getUser.condumexId)
        .then(() => {
          this.$router.push({ path: "/" }).catch(() => {});
          this.$store.dispatch("logout");
        })
        .catch(error => {
          this.loading = false;
          if (
            error.response.data.error &&
            error.response.data.error.toUpperCase().includes("TOKEN")
          ) {
            this.$store.dispatch("tokenerror", error.response.data.error);
          }
        });
    }
  }
};
</script>

<style scoped>
.theme--light.v-divider {
  border: 1px solid;
  border-color: rgba(245, 241, 241, 0.12) !important;
}
</style>
