<template>
  <v-container fluid fill-height>
    <v-layout align-center justify-center>
      <v-flex xs12 sm8 md4>
        <v-form ref="form" v-model="valid" lazy-validation>
          <v-card class="elevation-12">
            <v-toolbar color="primary">
              <v-toolbar-title class="white--text">
                <v-img
                  contain
                  max-height="50"
                  src="@/assets/logo_bioacceso.png"
                ></v-img>
              </v-toolbar-title>
            </v-toolbar>
            <v-card-text>
              <v-text-field
                v-model="userid"
                :rules="[() => !!userid || 'ID Condumex requerido']"
                prepend-icon="person"
                :label="labelUserIds"
                autocomplete="off"
                counter
                required
              >
              </v-text-field>
              <v-text-field
                v-model="passwd"
                :append-icon="show1 ? 'visibility' : 'visibility_off'"
                :type="show1 ? 'text' : 'password'"
                class="input-group--focused"
                @click:append="show1 = !show1"
                :rules="[() => !!passwd || 'Contraseña requerida']"
                counter
                prepend-icon="lock"
                label="Contraseña"
                required
              >
              </v-text-field>
            </v-card-text>
            <v-card-actions>
              <v-col cols="8">
                <v-text-field
                  v-if="this.$store.getters.authStatus === 'error'"
                  label="Error"
                  error
                  :placeholder="this.$store.getters.showError"
                  error-messages="Si es necesario, contactar a soporte TI para validar credenciales Condumex."
                  readOnly
                ></v-text-field>
              </v-col>
              <v-spacer></v-spacer>
              <table>
                <tr>
                  <td style="text-align:right">
                    <v-btn
                      small
                      type="submit"
                      color="primary"
                      :loading="loading"
                      @click.prevent="validate"
                      >Ingresar</v-btn
                    >   
                  </td>
                </tr> 
                <tr>
                  <td style="text-align:right">                 

                    <v-dialog v-model="dialog" max-width="550px">
                      <template v-slot:activator="{ on }">
                        <div class="text-center">
                          <v-snackbar v-model="snackbar" :timeout="timeout" color="success">
                            {{ text }}
                          </v-snackbar>
                          <v-snackbar v-model="snackbar_error" :timeout="timeout" color="error">
                            Hubo un problema al restaurar contraseña.
                          </v-snackbar>
                          <v-snackbar v-model="snackbar_unexisting_userid_error" :timeout="timeout" color="error">
                            El ID de usuario no existe, favor de corregirlo.
                          </v-snackbar>
                        </div>
                        <v-layout>
                          <p style="font-size:80%;">
                            <a href="#" color="blue darken-1" text v-on="on" v-if="local_auth === 'true'"> Restablecer Contraseña</a>
                          </p>
                        </v-layout>
                      </template>
                      <v-form ref="form2" lazy-validation v-model="valid_recover_password">
                        <v-card>
                          <v-card-title>
                            <span class="heading">Restablecer Contraseña</span>
                          </v-card-title>
                          <v-card-text>
                            <v-container>
                              <v-row>
                                <v-col cols="8">
                                  <p class="font-weight-regular">
                                    Ingresa el ID de usuario con el que te registraste:
                                  </p>
                                  <v-text-field
                                    v-model="userid_to_restore"
                                    label="ID Usuario"
                                    :rules="[() => !!userid_to_restore || 'ID Condumex requerido']"
                                    counter
                                    required
                                    autocomplete="off"
                                  ></v-text-field>
                                </v-col>
                              </v-row>
                            </v-container>
                          </v-card-text>
                          <v-card-actions>
                            <v-spacer></v-spacer>
                            <v-btn color="blue darken-1" text @click="close">
                              Cancelar
                            </v-btn>
                            <v-btn color="blue darken-1" :disabled="!valid_recover_password" text @click="recover_password" :loading="loading_recover_password">
                              Restablecer
                            </v-btn>
                          </v-card-actions>
                        </v-card>
                      </v-form>
                    </v-dialog>
                  </td>
                </tr> 
              </table>





            </v-card-actions>
          </v-card>
        </v-form>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import Vue from "vue";
export default {
  name: "AppLoginForm",
  data: () => ({
    userid: "",
    userid_to_restore: "",
    passwd: "",
    valid: true,
    valid_recover_password: true,
    show1: false,
    loading: false,
    loading_recover_password: false,
    dialog: false,
    snackbar: false,
    snackbar_error: false,
    snackbar_unexisting_userid_error: false,
    timeout: 5000,
    text: "My timeout is set to 3000.",
    local_auth: process.env.VUE_APP_LOCAL_AUTHENTICATION
  }),
  methods: {
    validate() {
      if (this.$refs.form.validate()) {
        this.loading = true;
        //Call LDAP Auth REST Service
        const user = { username: this.userid, password: this.passwd };
        this.$store.dispatch("login", user)
          .then(() => {
            // Login exitoso, redirigir a la vista principal
            this.$router.push({ name: "Industrialización" });
          })
          .catch(() => {
            // Opcional: manejar errores si decides usar reject en el store
          })
          .finally(() => {
            this.loading = false; // Para desactivar loading en cualquier caso
        });
      }
    },
    ckeckLoadingStatus() {
      if (this.$store.getters.authStatus === "error") {
        this.loading = false;
      }
    },
    close() {
      this.dialog = false;
      this.$refs.form2.reset();
    },
    validate_recover_password() {
      this.valid_recover_password = this.$refs.form2.validate();
    },
    recover_password() {
      //console.log(this.userid_to_restore);
      this.validate_recover_password();
      if (this.valid_recover_password) {
        this.loading_recover_password = true;
        Vue.axios.post("/user/" + this.userid_to_restore + "/reset-password")
          .then(() => {
            this.close();
            this.text = "La contraseña se ha restablecido correctamente, le llegará por correo electrónico en unos segundos.";
            this.snackbar = true;
            this.loading_recover_password = false;
          })
          .catch(err => {
            if (err.response.data) {
              this.loading_recover_password = false;
              const error = err.response.data.message;
              //console.log("error:" + err.response.data.message);
              if(error === "Admin not found") this.snackbar_unexisting_userid_error = true;
              else this.snackbar_error = true;
            }
        });
      }
    }
  },
  computed: {
    labelUserIds() {
      return this.local_auth === 'true' ? "ID Usuario" : "ID Condumex";
    }
  },
  created() {
    this.$vuetify.theme.primary = "#9C27B0";
  },
  updated() {
    this.ckeckLoadingStatus();
  }
};
</script>
