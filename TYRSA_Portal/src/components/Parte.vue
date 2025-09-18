<template>
  <v-container fluid>
    <v-form ref="form" lazy-validation v-model="valid">
    <v-card flat>
        <v-card-title>
        <span class="heading">{{ formTitle }}</span>
        </v-card-title>
        <v-card-text>
        <v-container>
            <v-card class="mb-4" outlined style="border-width: 3px;">
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
            <v-card class="mb-4" outlined style="border-width: 3px;">
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
            <v-card class="mb-4" outlined style="border-width: 3px;">
                <v-card-title>
                    Materia prima
                </v-card-title>
                <v-card-text>
                    <v-divider class="my-4"></v-divider>
                    <v-subheader class="text-h6">Aceros</v-subheader>
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
                    <v-subheader class="text-h6">Especificación de Rollo</v-subheader>
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
                    <v-subheader class="text-h6">Componentes</v-subheader>
                    <v-row>
                        <v-col cols="12">
                            <v-data-table
                            :headers="headers_componentes"
                            :items="componentes"
                            :options.sync="options"
                            :loading="loading"
                            loading-text="Consultando información..."
                            no-data-text="No se encontró información."
                            :style="{ border: '1px solid #ccc', borderRadius: '4px' }"
                            >
                            <template v-slot:item.actions="{ item }">
                                <v-icon small class="mr-2" @click="editItem(item)">
                                mdi-pencil
                                </v-icon>
                                <v-icon small @click="deleteItem(item)">
                                mdi-delete
                                </v-icon>
                            </template>
                            </v-data-table>
                        </v-col>
                    </v-row>
                    <v-divider class="my-4"></v-divider>
                    <v-subheader class="text-h6">Expecificación de MP</v-subheader>
                    <v-row>
                        <v-col cols="2">
                            <v-text-field
                            label="Largo de cinta/blank (Avance real) (mm)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Largo del material en máxima tolerancia"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Ancho cinta/blank (mm)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Ancho del material en máxima tolerancia"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Espesor (mm)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Espesor del material en máxima tolerancia"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                    </v-row>
                    <v-divider class="my-4"></v-divider>
                    <v-row>
                        <v-col cols="2">
                            <v-text-field
                            label="Coeficiente del material"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Peso blank (kg)=F.C."
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Peso blank (kg)=F.C."
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Peso pieza (troquelado) (KG)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Peso pieza (componente) (Si aplica)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Factor de consumo (logística)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Factor de aprovechamiento"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="%Merma"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                    </v-row>
                </v-card-text>
            </v-card>
            <v-card class="mb-4" outlined style="border-width: 3px;">
                <v-card-title>
                    Producto Terminado
                </v-card-title>
                <v-card-text>
                    <v-row>
                        <v-col cols="4">
                            <v-text-field
                            label="Código de empaque"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="4">
                            <v-text-field
                            label="Factor de consumo de empaque por pieza"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="4">
                            <v-text-field
                            label="Piezas por pallet"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                    </v-row>
                </v-card-text>
            </v-card>
            <v-card class="mb-4" outlined style="border-width: 3px;">
                <v-card-text>
                    <v-row>
                        <v-col cols="1">
                            <v-text-field
                            label="Número de operaciones"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Número de máquinas"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Número de operadores"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Número de ayudantes"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Personal requerido"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Tiempo ciclo total (seg)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Tiempo ciclo Máximo"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Tiempo de llenado de célula"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Piezas por hora"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Tiempo total de cambio de modelo (min)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Tiempo de liberación (min)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Tiempo de ajuste por fechador (min)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                    </v-row>
                    <v-row>
                        <v-col cols="11">
                            <v-text-field
                            label="Piezas de ajuste"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                        <v-col cols="1">
                            <v-text-field
                            label="Cantidad económica de pedido"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            ></v-text-field>
                        </v-col>
                    </v-row>
                </v-card-text>
            </v-card>
            <v-card class="mb-4" outlined style="border-width: 3px;">
                <v-card-title>
                    Ruta de fabricación
                </v-card-title>
                <v-card-text>
                    <v-row>
                        <v-col cols="12">
                            <v-data-table
                            :headers="headers_ruta_fabricacion"
                            :items="ruta_fabricacion"
                            :options.sync="options"
                            :loading="loading"
                            loading-text="Consultando información..."
                            no-data-text="No se encontró información."
                            :style="{ border: '1px solid #ccc', borderRadius: '4px' }"
                            >
                            <template v-slot:item.actions="{ item }">
                                <v-icon small class="mr-2" @click="editItem(item)">
                                mdi-pencil
                                </v-icon>
                                <v-icon small @click="deleteItem(item)">
                                mdi-delete
                                </v-icon>
                            </template>
                            </v-data-table>
                        </v-col>
                    </v-row>
                </v-card-text>
            </v-card>
        </v-container>
        </v-card-text>
        <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="blue darken-1" text :to="'/industrializacion'"> Cancelar </v-btn>
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
        loading: false,
        valid: true,
        fechainicioproyecto: null,
        fechafinproyecto: null,
        editedIndex: -1,
        menuinicioproyecto: false,
        menufinproyecto: false,
        componentes: [{"especificacion_componente":"Componente 1", "tipo_proveedor":"tipo_proveedor", "nombre_proveedor":"nombre_proveedor", "codigo_identificacion_componente":"codigo_identificacion_componente", "cantidad_componentes_x_pieza":"cantidad_componentes_x_pieza"}, {"especificacion_componente":"Componente 2", "tipo_proveedor":"tipo_proveedor", "nombre_proveedor":"nombre_proveedor", "codigo_identificacion_componente":"codigo_identificacion_componente", "cantidad_componentes_x_pieza":"cantidad_componentes_x_pieza"}],
        headers_componentes: [
            { text: "Especificacion de componente", value: "especificacion_componente" },
            { text: "Tipo de proveedor", value: "tipo_proveedor" },
            { text: "Nombre de proveedor", value: "nombre_proveedor" },
            { text: "Código de identificación de componentes", value: "codigo_identificacion_componente" },
            { text: "Cantidad de componentes por pieza", value: "cantidad_componentes_x_pieza" },
            { text: "Acciones", value: "actions" },
        ],
        ruta_fabricacion: [{"operacion":"1", "n_maquinas":"5", "tonelaje":"1", "descripcion":"Ejemplo descripcion", "fpc":"2", "tiempo_ciclo":"50"}, {"operacion":"2", "n_maquinas":"8", "tonelaje":"1", "descripcion":"Ejemplo descripcion 2", "fpc":"3", "tiempo_ciclo":"80"}],
        headers_ruta_fabricacion: [
            { text: "Operacion", value: "operacion" },
            { text: "No. de máquina", value: "n_maquinas" },
            { text: "Tonelaje (Tn)", value: "tonelaje" },
            { text: "Descripción", value: "descripcion" },
            { text: "FPC", value: "fpc" },
            { text: "Tiempo de ciclo", value: "tiempo_ciclo" },
            { text: "Acciones", value: "actions" },
        ],
        options: {
            itemsPerPage: 10,
        },
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