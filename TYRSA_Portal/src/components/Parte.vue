<template>
  <v-container fluid>
    <v-form ref="form" lazy-validation v-model="valid">
    <v-card flat>
        <v-card-title>
        <span class="heading">{{ formTitle }}</span>
        </v-card-title>
        <v-card-text>
        <v-container>
            <v-card class="mb-4" outlined>
                <v-card-title>
                    Información General
                </v-card-title>
                <v-card-text>
                    <v-row>
                        <v-col cols="3">
                            <v-text-field
                            label="Numero de parte"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Proyecto"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Descripción"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Nivel de ingeniería"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                    </v-row>
                </v-card-text>
            </v-card>
            <v-card class="mb-4" outlined>
                <v-card-title>
                    Ciclo de vida del proyecto
                </v-card-title>
                <v-card-text>
                    <v-row>
                        <v-col cols="2">

                            <v-menu
                            v-model="menuinicioproyecto"
                            :close-on-content-click="false"
                            transition="scale-transition"
                            offset-y
                            min-width="auto"
                            >
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field
                                prepend-icon="event"
                                outlined
                                v-model="fechainicioproyecto"
                                label="Fecha de inicio"
                                readonly
                                v-bind="attrs"
                                v-on="on"
                                ></v-text-field>
                            </template>

                            <v-date-picker
                                v-model="fechainicioproyecto"
                                @change="menuinicioproyecto = false"
                                locale="es-mx"
                                no-title
                            ></v-date-picker>
                            </v-menu>

                        </v-col>
                        <v-col cols="2">
                            <v-menu
                            v-model="menufinproyecto"
                            :close-on-content-click="false"
                            transition="scale-transition"
                            offset-y
                            min-width="auto"
                            >
                            <template v-slot:activator="{ on, attrs }">
                                <v-text-field
                                prepend-icon="event"
                                outlined
                                v-model="fechafinproyecto"
                                label="Fecha de inicio"
                                readonly
                                v-bind="attrs"
                                v-on="on"
                                ></v-text-field>
                            </template>

                            <v-date-picker
                                v-model="fechafinproyecto"
                                @change="menufinproyecto = false"
                                locale="es-mx"
                                no-title
                            ></v-date-picker>
                            </v-menu>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Días disponibles"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Estatus del proyecto"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="4">
                            <v-text-field
                            label="Volumen vendido del proyecto anual"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                    </v-row>
                </v-card-text>
            </v-card>
            <v-card class="mb-4" outlined>
                <v-card-title>
                    Materia prima
                </v-card-title>
                <v-card-text>
                    <v-divider class="my-4"></v-divider>
                    <v-subheader>Aceros</v-subheader>
                    <v-row>
                        <v-col cols="2">
                            <v-text-field
                            label="Especificación de Material"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Tipo de proveedor"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Nombre de proveedor"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Codigo de identificación de materia"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Presentacion de materia prima"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Peso de estándar pack MP"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                    </v-row>
                    <v-divider class="my-4"></v-divider>
                    <v-subheader>Especificación de Rollo</v-subheader>
                    <v-row>
                        <v-col cols="6">
                            <v-text-field
                            label="Diámetro interno (min / max)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="6">
                            <v-text-field
                            label="Diámetro externo (min / max)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                    </v-row>
                    <v-divider class="my-4"></v-divider>
                    <v-subheader>Componentes</v-subheader>
                </v-card-text>
            </v-card>
        </v-container>
        </v-card-text>
        <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text > Cancelar </v-btn>
        <v-btn color="blue darken-1" :disabled="!valid" text >
            Guardar
        </v-btn>
        </v-card-actions>
    </v-card>
    </v-form>
</v-container>
</template>

<script>
export default {
  data() {
    return {
        valid: true,
        fechainicioproyecto: null,
        fechafinproyecto: null,
        editedIndex: -1,
        menuinicioproyecto: false,
        menufinproyecto: false,
    };
  },
  methods: {
  },
  computed: {
    formTitle() {
      return this.editedIndex === -1
        ? "Nueva Parte"
        : "Editar Parte";
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
  },
};
</script>