<template>
  <div>
    <v-app-bar app fixed light>
      <!-- <v-app-bar-nav-icon></v-app-bar-nav-icon> -->
      <v-toolbar-title>{{ $route.name }}</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-btn @click="dialogVisible = true" icon>
        <v-icon title="Actualizar Contraseña">mdi-key</v-icon>
      </v-btn>
      <div class="vertical-divider"></div>
      <v-btn @click="logout" icon>
        <v-icon title="Salir del sistema">mdi-logout</v-icon>
      </v-btn>
    </v-app-bar>
    <v-dialog v-model="dialogVisible" max-width="400px">
      <v-form ref="formp" lazy-validation v-model="validp">
        <v-card>
          <v-card-title>
            <span class="heading">Actualizar Contraseña</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="12">
                  <v-text-field
                    v-model="currentpassword"
                    type="password"
                    label="Ingresa la contraseña actual"
                    :rules="[v => !!v || 'Campo requerido']"
                    required
                    autocomplete="off"
                    maxLength="50"
                    outlined
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    v-model="password"
                    type="password"
                    label="Ingresa la nueva contraseña"
                    :rules="[v => !!v || 'Campo requerido']"
                    required
                    autocomplete="off"
                    maxLength="50"
                    outlined
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    v-model="password2"
                    type="password"
                    label="Vuelve a ingresar la nueva contraseña"
                    :rules="[v => !!v || 'Campo requerido', passwordMatchRule]"
                    required
                    autocomplete="off"
                    maxLength="50"
                    outlined
                  ></v-text-field>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="closepassword"> Cancelar </v-btn>
            <v-btn color="blue darken-1" :disabled="!validp" text @click="savepassword" :loading="loading_p">
              Guardar
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-form>
    </v-dialog>
    <v-navigation-drawer
      v-model="drawer"
      app
      dark
      floating
      permanent
      src="@/assets/menu_lateral.png"
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
    <v-snackbar v-model="snackbar_p" :timeout="timeout_p" color="success">
      La contraseña se actualizó con éxito
    </v-snackbar>

    <v-snackbar v-model="snackbar_p_error" :timeout="timeout_p" color="error">
      {{ text_p }}
    </v-snackbar>
  </div>
</template>

<script>
import LogoutService from "@/services/LogoutService";
import UserService from "@/services/UserService";

export default {
  name: "AppNavigation",
  data() {
    return {
      snackbar_p: false,
      snackbar_p_error: false,
      text_p: "My timeout is set to 3000.",
      timeout_p: 3000,
      loading_p: false,
      dialogVisible: false,
      validp: true,
      currentpassword: null,
      password: null,
      password2: null,
      drawer: true,
      links: [
        {
          to: "/users",
          icon: "person",
          text: "Users"
        },
        {
          to: "/clientes",
          icon: "mdi-account-group",
          text: "Clientes"
        },
        {
          to: "/materiales",
          icon: "mdi-layers",
          text: "Materiales"
        },
        {
          to: "/industrializacion",
          icon: "mdi-nut",
          text: "Industrialización"
        }
      ]
    };
  },
  computed: {
    passwordMatchRule() {
      return v => v === this.password || 'Las contraseñas no coinciden';
    },
    image() {
      return this.$store.state.image;
    },
    color() {
      return this.$store.state.color;
    },
    activeLinks() {
      let role = this.$store.getters.getUser.role
      let l = [];
      this.links.forEach(function (element) {
        const name = element.text.toString();
        if (name === "Industrialización" || name === "Clientes") {
          l.push(element);
          return;
        }
        if (name === "Materiales" && (role === "ROLE_ADMIN" || role === "ROLE_GERENTE_INGENIERIA" || role === "ROLE_AREA_INGENIERIA")) {
          l.push(element);
          return;
        }
        if (name === "Users" && (role === "ROLE_ADMIN" || role === "ROLE_GERENTE_INGENIERIA")) {
          l.push(element);
          return;
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
          this.loading_p = false;
          if (
            error.response.data.error &&
            error.response.data.error.toUpperCase().includes("TOKEN")
          ) {
            this.$store.dispatch("tokenerror", error.response.data.error);
          }
        });
    },
    closepassword(){
      this.dialogVisible = false;
      this.$refs.formp.reset();
    },
    validate() {
      this.validp = this.$refs.formp.validate();
    },
    savepassword() {
      this.validate();
      if (this.validp) {
          this.loading_p = true;
          let passwordRequest = {
            currentPassword: this.currentpassword,
            newPassword: this.password
          };
          UserService.updatePassword(this.$store.getters.getUser.username, passwordRequest)
            .then(() => {
              this.loading_p = false;
              this.closepassword();
              this.snackbar_p = true;
            })
            .catch((error) => {
              if (
                error.response.data.error &&
                error.response.data.error.toUpperCase().includes("TOKEN")
              ) {
                this.$store.dispatch("tokenerror", error.response.data.error);
              }
              this.loading_p = false;
              this.closepassword();
              this.text_p = error.response.data;
              this.snackbar_p_error = true;
            });
        }
    }
  },
  created() {
    this.axios.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${this.$store.getters.getUser.token}`;
  }
};
</script>

<style scoped>
.theme--light.v-divider {
  border: 1px solid;
  border-color: rgba(245, 241, 241, 0.12) !important;
}
.vertical-divider {
  width: 1px;
  height: 24px;
  background-color: #ccc;
  margin: 0 12px;
}
</style>
