<template>
  <v-container fluid>
    <v-dialog v-model="dialog" max-width="400px">

    </v-dialog>

    <v-card flat>
        <v-layout align-end justify-end>
            <v-btn color="primary" dark class="mb-2" :to="'/parte'">
            Nueva parte
            <v-icon right color="white">add</v-icon>
            </v-btn>
        </v-layout>
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
          :items="lista_industrializacion"
          :search="search"
          :options.sync="options"
          :loading="loading"
          loading-text="Consultando información..."
          no-data-text="No se encontró información."
        >
            <template v-slot:item.foto="{ /*item*/ }">
                <v-img
                :src="require('@/assets/parte_ejemplo.png')"
                contain
                max-height="40"
                max-width="60"
                ></v-img>
            </template>

            <template v-slot:item.estatus="{ item }">
                <span
                    :class="{
                    'estatus-activo': item.estatus === 'Activo',
                    'estatus-vencido': item.estatus === 'VENCIDO',
                    'estatus-proximo': item.estatus === 'PROXIMO A VENCER',
                    }"
                    class="estatus-cell"
                >
                    {{ item.estatus }}
                </span>
            </template>
            <template v-slot:item.actions="{ item }">
                <v-icon small class="mr-2" @click="editItem(item)" title="Editar Parte">
                mdi-pencil
                </v-icon>
                <v-icon small @click="deleteItem(item)" title="Visualizar toda la información de esta Parte">
                mdi-magnify
                </v-icon>
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
        { text: "Num. Parte", value: "numero_parte" },
        {
          text: "Foto",
          sortable: false,
          value: "foto",
          align: "center",
          width: "10px"
        },
        { text: "Proyecto", value: "proyecto" },
        { text: "Descripción", value: "descripcion" },
        { text: "Nivel Ingeniería", value: "nivel_ingenieria" },
        { text: "Fecha Inicio", value: "fecha_inicio" },
        { text: "Fecha Fin", value: "fecha_fin" },
        { text: "Dias Disponibles", value: "dias_disponibles" },
        { text: "Estatus", value: "estatus" },
        /*{ text: "Volumen vendido", value: "volumen_vendido" },*/
        { text: "Acciones", value: "actions" },
      ],
      lista_industrializacion: [
        {"numero_parte": "10189018", "foto": "", "proyecto": "N/A", "descripcion":"EXCLUDER SEAL ", "nivel_ingenieria":"04488 (B)", "fecha_inicio":"21/09/2011", "fecha_fin":"Sin fecha de Cierre", "dias_disponibles": "No disponible", "estatus": "Activo", "volumen_vendido": "8"},
        {"numero_parte": "10266638", "foto": "", "proyecto": "FORD CD4", "descripcion":"DUST SHELD", "nivel_ingenieria":"(A) 10557", "fecha_inicio":"21/09/2011", "fecha_fin":"Sin registro", "dias_disponibles": "No disponible", "estatus": "Activo", "volumen_vendido": "9"},
        {"numero_parte": "10266638", "foto": "", "proyecto": "N/A", "descripcion":"DUSTSHIELD DEFLECTOR", "nivel_ingenieria":"11889(C2)", "fecha_inicio":"21/09/2011", "fecha_fin":"Sin registro", "dias_disponibles": "No disponible", "estatus": "VENCIDO", "volumen_vendido": "10"},
        {"numero_parte": "326470", "foto": "", "proyecto": "NISSAN L32H&X11C", "descripcion":"DUTSHELD FOR TULIP.", "nivel_ingenieria":"20060721(C2)", "fecha_inicio":"21/09/2011", "fecha_fin":"Sin registro", "dias_disponibles": "No disponible", "estatus": "VENCIDO", "volumen_vendido": "11"},
        {"numero_parte": "8383210617", "foto": "", "proyecto": "NISSAN L32H&X11C", "descripcion":"DEFLECTOR PARA TULIPAN", "nivel_ingenieria":"20060720(C2)", "fecha_inicio":"21/09/2011", "fecha_fin":"Sin registro", "dias_disponibles": "No disponible", "estatus": "VENCIDO", "volumen_vendido": "41"},
        {"numero_parte": "8384210617", "foto": "", "proyecto": "NISSAN 'L32H' & 'XIIC0'", "descripcion":"DUTSHIELD FOR BEARING", "nivel_ingenieria":"11889(D2)", "fecha_inicio":"21/09/2011", "fecha_fin":"Sin registro", "dias_disponibles": "No disponible", "estatus": "VENCIDO", "volumen_vendido": "65"},
        {"numero_parte": "8386210617", "foto": "", "proyecto": "NISSAN 'L32H' & 'XIIC0'", "descripcion":"DUTSHIELD FOR BEARING", "nivel_ingenieria":"11889 (D2)", "fecha_inicio":"21/09/2011", "fecha_fin":"Sin registro", "dias_disponibles": "No disponible", "estatus": "VENCIDO", "volumen_vendido": "98"},
        {"numero_parte": "8387210617", "foto": "", "proyecto": "NISSAN 'L32H' & 'XIIC0'", "descripcion":"DUTSHIELD FOR TULIP LH MT", "nivel_ingenieria":"11889(D2)", "fecha_inicio":"21/09/2011", "fecha_fin":"31/10/2025", "dias_disponibles": "44", "estatus": "PROXIMO A VENCER", "volumen_vendido": "45"},
        {"numero_parte": "8447210617", "foto": "", "proyecto": "FORD B410 4X4 REAR", "descripcion":"DUTSHIELD DEFLECTOR", "nivel_ingenieria":"2006-07-12 (D)", "fecha_inicio":"21/09/2011", "fecha_fin":"Sin registro", "dias_disponibles": "No disponible", "estatus": "VENCIDO", "volumen_vendido": "65"},
        {"numero_parte": "8869210616", "foto": "", "proyecto": "VW TAYRON", "descripcion":"PXS CLOSURE CAP", "nivel_ingenieria":"(A ) 87504", "fecha_inicio":"21/09/2011", "fecha_fin":"01/12/2032", "dias_disponibles": "2632", "estatus": "Activo", "volumen_vendido": "43"}
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

<style scoped>
.estatus-cell {
  display: inline-block;
  padding: 4px 8px;
  border-radius: 4px;
  color: white;
  font-weight: bold;
  text-align: center;
}

.estatus-activo {
  background-color: #4caf50; /* Verde */
}

.estatus-vencido {
  background-color: #f44336; /* Rojo */
}

.estatus-proximo {
  background-color: #FFA500; /* Rojo */
}
</style>