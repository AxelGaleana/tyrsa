<template>
  <v-container fluid>
    <v-dialog v-model="dialog" max-width="400px">
      <template v-slot:activator="{ on }">
        <div class="text-center">
          <v-snackbar v-model="snackbar" :timeout="timeout" color="success">
            {{ text }}
          </v-snackbar>
          <v-snackbar v-model="snackbar_error" :timeout="timeout" color="error">
            Hubo un problema al guardar administrador.
          </v-snackbar>
        </div>
        <v-layout align-end justify-end>
          <v-btn color="primary" dark class="mb-2" v-on="on">
            user<v-icon right color="white">add</v-icon>
          </v-btn>
        </v-layout>
      </template>
      <v-form ref="form" lazy-validation v-model="valid">
        <v-card>
          <v-card-title>
            <span class="heading">{{ formTitle }}</span>
          </v-card-title>
          <v-card-text>
            <v-container>
              <v-row>
                <v-col cols="6">
                  <v-text-field
                    v-model="editedItem.username"
                    :label= labelUserIds
                    :rules="
                      editedIndex === -1
                        ? [rules.required, rules.administrator]
                        : [rules.required]
                    "
                    required
                    :disabled="editedIndex > -1 ? true : false"
                    autocomplete="off"
                    maxLength="255"
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    v-model="editedItem.name"
                    label="Nombre"
                    :rules="[v => !!v || 'Campo requerido']"
                    required
                    autocomplete="off"
                    maxLength="50"
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    v-if="editedIndex === -1"
                    v-model="editedItem.email"
                    label="Email"
                    :rules="[rules.required, rules.email, rules.emails]"
                    required
                    autocomplete="off"
                    maxLength = "50"
                  ></v-text-field>
                  <v-text-field
                    v-else
                    v-model="editedItem.email"
                    label="Email"
                    :rules="
                      editedItem.id_condumex &&
                      editedItem.email !==
                        administrators.find(
                          (item) => item.id_condumex === editedItem.id_condumex
                        ).email
                      ? [
                          rules.required, 
                          rules.email, 
                          rules.emails
                        ]
                      : [
                          rules.required, 
                          rules.email
                        ]
                    "
                    required
                    autocomplete="off"
                    maxLength="50"
                  ></v-text-field>
                </v-col>
                <v-col cols="12" v-if="editedIndex > -1">
                  <v-switch
                    v-model="editedItem.active"
                    :label="editedItem.active ? 'Activo' : 'Inactivo'"
                  ></v-switch>
                </v-col>
                <v-col cols="12">
                  <v-select
                    v-if="editedIndex === -1"
                    v-model="editedItem.role"
                    :items="filteredRoles"
                    prepend-icon="assignment_ind"
                    item-text="role"
                    item-value="role"
                    label="Rol de Usuario"
                    :rules="[(v) => !!v || 'Campo requerido']"
                    required
                    autocomplete="off"
                  ></v-select>

                  <v-select
                    v-else
                    v-model="editedItem.role"
                    :items="filteredRoles"
                    prepend-icon="assignment_ind"
                    item-text="role"
                    item-value="role"
                    label="Rol de Administrador"
                    :rules="[(v) => !!v || 'Campo requerido']"
                    required
                    autocomplete="off"
                  ></v-select>
                </v-col>
              </v-row>
            </v-container>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" text @click="close"> Cancelar </v-btn>
            <v-btn color="blue darken-1" :disabled="!valid" text @click="save">
              Guardar
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-form>
    </v-dialog>

    <v-card flat>
      <v-card-title class="title">
        <v-layout>
          <v-col cols="12">
            <v-text-field
              v-model="search"
              append-icon="filter_list"
              label="Filtrar por ID de usuario, Nombre, Apellidos ..."
              single-line
              hide-details
              clearable
              solo
            ></v-text-field>
          </v-col>
        </v-layout>
      </v-card-title>
      <v-card-text>
        <v-data-table
          :headers="headers"
          :items="users"
          :search="search"
          :options.sync="options"
          :loading="loading"
          loading-text="Consultando información..."
          no-data-text="No se encontró información."
        >
          <template v-slot:item.active="{ item }">
            {{ item.active === true ? "Activo" : "Inactivo" }}
          </template>
          <template v-slot:item.actions="{ item }">
            <v-icon small class="mr-2" @click="editItem(item)">
              mdi-pencil
            </v-icon>
            <!-- <v-icon small @click="deleteItem(item)">
              mdi-delete
            </v-icon> -->
          </template>
        </v-data-table>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import UserService from "@/services/UserService";

export default {
  data() {
    return {
      snackbar: false,
      snackbar_error: false,
      text: "My timeout is set to 3000.",
      timeout: 3000,
      valid: true,
      dialog: false,
      search: "",
      local_auth: process.env.VUE_APP_LOCAL_AUTHENTICATION,
      options: {
        itemsPerPage: 10,
      },
      loading: true,
      headers: [
        { text: "ID de usuario", value: "username" },
        { text: "Nombre", value: "name" },
        { text: "Email", value: "email" },
        { text: "Rol", value: "role" },
        { text: "Activo", value: "active" },
        { text: "Acciones", value: "actions" },
      ],
      rules: {
        required: (value) => !!value || "Campo Requerido",
        administrator: (value) => {
          return (
            !this.userIds.includes(value) || "El ID de usuario ya existe"
          );
        },
        email: value => {
          return (
            /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/.test(
              value
            ) || "El email debe ser válido"
          );
        },
        emails: value => {
          return !this.emails.includes(value) || "El email ya existe";
        }
      },
      users: [],
      sites: [],
      roles: [],
      siteFilter: null,
      preselectedRoles: null,
      editedIndex: -1,
      preselectedSites: null,
      editedItem: {
        name: null,
        lastname: null,
        surname: null,
        email: null,
        id_condumex: null,
        active: null,
        roles: null,
      },
      defaultItem: {
        name: null,
        lastname: null,
        surname: null,
        email: null,
        id_condumex: null,
        active: null,
        roles: null,
      },
    };
  },
  methods: {
    getUsers() {
      return UserService.getAllUsers()
        .then((response) => {
          this.users = response.data;
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
          if (
            error.response.data.error &&
            error.response.data.error.toUpperCase().includes("TOKEN")
          ) {
            this.$store.dispatch("tokenerror", error.response.data.error);
          }
        });
    },
    getRoles() {
      return UserService.getAllRoles()
        .then((response) => {
          this.roles = response.data;         
          this.loading = false;
        })
        .catch((error) => {
          this.loading = false;
          if (
            error.response.data.error &&
            error.response.data.error.toUpperCase().includes("TOKEN")
          ) {
            this.$store.dispatch("tokenerror", error.response.data.error);
          }
        });
    },
    editItem(item) {
      this.editedIndex = this.users.indexOf(item);
      this.editedItem = Object.assign({}, item);
      this.dialog = true;
    },
    close() {
      this.dialog = false;
      this.$nextTick(() => {
        this.editedItem = Object.assign({}, this.defaultItem);
        this.editedIndex = -1;
      });
      this.$refs.form.reset();
    },
    validate() {
      this.valid = this.$refs.form.validate();
    },
    save() {
      this.validate();
      if (this.valid) {
        if (this.editedIndex > -1) {
          UserService.updateUser(this.editedItem)
            .then(() => {
              this.loading = true;
              this.close();
              this.text = "El usuario ha sido actualizado exitosamente.";
              this.snackbar = true;
              this.getUsers();
            })
            .catch((error) => {
              this.loading = false;
              if (
                error.response.data.error &&
                error.response.data.error.toUpperCase().includes("TOKEN")
              ) {
                this.$store.dispatch("tokenerror", error.response.data.error);
              }
              this.snackbar_error = true;
            });
        } else {
          UserService.createUser(this.editedItem)
            .then(() => {
              this.loading = true;
              this.close();
              this.text = "El Usuario ha sido creado exitosamente.";
              this.snackbar = true;
              this.getUsers();
            })
            .catch((error) => {
              this.loading = false;
              if (
                error.response.data.error &&
                error.response.data.error.toUpperCase().includes("TOKEN")
              ) {
                this.$store.dispatch("tokenerror", error.response.data.error);
              }
              this.snackbar_error = true;
            });
        }
      }
    }
  },
  computed: {
    formTitle() {
      return this.editedIndex === -1
        ? "Nuevo Usuario"
        : "Editar Usuario";
    },
    userIds() {
      return this.users.map(
        (user) => user.username
      );
    },
    emails() {
      return this.users.map(
        user => user.email);
    },
    labelUserIds() {
      return this.local_auth === 'true' ? "ID Usuario" : "ID Condumex";
    },
    sortedSites() {
      this.sites.map((b) => {
        b.site = b.active === 0 ? b.site + " (Inactivo)" : b.site;
      });
      return this.sites.slice().sort((a, b) => b.active - a.active);
    },
    activeSites() {
      return this.sites
        .filter((element) => element.active === 1)
        .sort((a, b) => (a.site > b.site && 1) || -1);
    },
    validateSoporteRole() {
      return this.$store.getters.getUser.role === "Soporte";
    },
    validateRHPlusRole() {
      return this.$store.getters.getUser.role === "RH+";
    },
    filteredRoles() {
      if(this.validateSoporteRole){
      return this.roles;
      }else{
        return this.roles.filter((role) => role.role != "Soporte" && role.role != "RH+" );
      }
    },
  },

  watch: {
    dialog(val) {
      val || this.close();
    },
  },
  created() {
    this.axios.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${this.$store.getters.getUser.token}`;
    this.getUsers();
    this.getRoles();
  },
};
</script>