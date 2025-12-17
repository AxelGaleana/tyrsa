<template>
  <v-container fluid>
    <v-dialog v-model="dialog" max-width="400px">
      <template v-slot:activator="{ on }">
        <div class="text-center">
          <v-snackbar v-model="snackbar" :timeout="timeout" color="success">
            {{ text }}
          </v-snackbar>
          <v-snackbar v-model="snackbar_error" :timeout="timeout" color="error">
            Hubo un problema al guardar material.
          </v-snackbar>
        </div>
        <v-layout align-end justify-end>
          <v-btn color="primary" dark class="mb-2" v-on="on">
            material<v-icon right color="white">add</v-icon>
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
                <v-col cols="12">
                  <v-text-field
                    v-model="editedItem.name"
                    label="Nombre"
                    :rules="rulesNombreMaterial"
                    required
                    autocomplete="off"
                    maxLength="50"
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    v-model="editedItem.coeficiente"
                    label="Coeficiente de material"
                    :rules="rulesNombreMaterial"
                    required
                    autocomplete="off"
                    maxLength="50"
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-switch
                    v-model="editedItem.activo"
                    :label="editedItem.activo ? 'Activo' : 'Inactivo'"
                  ></v-switch>
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
              label="Filtrar por Nombre"
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
          :items="materials"
          :search="search"
          :options.sync="options"
          :loading="loading"
          loading-text="Consultando información..."
          no-data-text="No se encontró información."
        >
          <template v-slot:item.active="{ item }">
            {{ item.activo === true ? "Activo" : "Inactivo" }}
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
import materialService from "@/services/MaterialService";

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
        { text: "Nombre", value: "name" },
        { text: "Coeficiente de material", value: "coeficiente" },
        { text: "Activo", value: "active" },
        { text: "Acciones", value: "actions" },
      ],
      rules: {
        required: (value) => !!value || "Campo Requerido",
        materials: (value) => {
          return (
            !this.nombreMaterials.includes(value) || "El nombre de material ya existe"
          );
        }
      },
      materials: [],
      editedIndex: -1,
      editedItem: {
        id: null,
        name: null,
        coeficiente: null,
        activo: true
      }
    };
  },
  methods: {
    getMaterials() {
      return materialService.getAllMaterials()
        .then((response) => {
          this.materials = response.data;
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
      this.editedIndex = this.materials.indexOf(item);
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
          materialService.actualizarMaterial(this.editedItem)
            .then(() => {
              this.loading = true;
              this.close();
              this.text = "El material ha sido actualizado exitosamente.";
              this.snackbar = true;
              this.getMaterials();
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
          materialService.crearMaterial(this.editedItem)
            .then(() => {
              this.loading = true;
              this.close();
              this.text = "El material ha sido creado exitosamente.";
              this.snackbar = true;
              this.getMaterials();
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
        ? "Nuevo Material"
        : "Editar Material";
    },
    nombreMaterials() {
      return this.materials.map(
        (material) => material.name
      );
    },
    rulesNombreMaterial() {
        const original = this.materials?.find(item => item.id === this.editedItem?.id);
        console.log("original: ", original)

        if (this.editedIndex === -1) {
        return [this.rules.required, this.rules.materials];
        }

        if (this.editedItem && original && this.editedItem.name !== original.name) {
        return [this.rules.required, this.rules.materials];
        }

        return [this.rules.required];
    }
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
    this.getMaterials();
  },
};
</script>