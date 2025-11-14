<template>
  <v-container fluid>
    <div class="text-center">
        <v-snackbar v-model="snackbar" :timeout="timeout" color="success">
        {{ text }}
        </v-snackbar>
        <v-snackbar v-model="snackbar_error" :timeout="timeout" color="error">
        Hubo un problema al guardar la Parte.
        </v-snackbar>
    </div>
    <v-form ref="form" lazy-validation v-model="valid">
    <v-card flat>
        <v-card-title>
        <span class="heading">{{ formTitle }}</span>
        </v-card-title>
        <v-card-text>
        <v-container>
            <v-card class="mb-4" outlined style="border-width: 3px; background-color: #f9fafb;">
                <v-card-title>
                    Información General
                </v-card-title>
                <v-card-text>
                    <v-row>
                    <!-- Columna izquierda: campos -->
                    <v-col cols="8">
                        <v-row>
                        <v-col cols="6">
                            <v-text-field
                            label="Numero de parte"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            :rules="editedIndex === 'nueva' ? [rules.required, rules.numeroParte] : [rules.required]"
                            required
                            v-model="editedItem.numeroParte"
                            :disabled="editedIndex !== 'nueva'"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="6">
                            <v-text-field
                            label="Proyecto"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            :rules="[v => !!v || 'Campo requerido']"
                            required
                            v-model="editedItem.proyecto"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="6">
                            <v-text-field
                            label="Descripción"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            :rules="[v => !!v || 'Campo requerido']"
                            required
                            v-model="editedItem.descripcion"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Nivel de ingeniería"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.nivelIngenieria"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                        <v-file-input
                            accept="image/jpeg, image/png"
                            :label="this.editedIndex === 'nueva' ? 'Seleccionar imagen' : 'Actualizar imagen'"
                            prepend-icon="mdi-image"
                            @change="handleImageUpload"
                            outlined
                            :hint="this.editedIndex === 'nueva' ? 'Seleccionar imagen' : 'Actualizar imagen'"
                            persistent-hint
                            ></v-file-input>
                        </v-col>
                        </v-row>
                    </v-col>

                    <!-- Columna derecha: imagen -->
                    <v-col cols="4" class="d-flex align-center justify-center">
                        <v-img
                        :src="getImageUrl(editedItem.fileName)"
                        @error="onImgError($event)"
                        max-width="180"
                        aspect-ratio="1"
                        cover
                        class="mb-3"
                        :style="editedItem.fileName ? 'border: 2px solid #E0E0E0; border-radius: 12px;' : 'border-radius: 12px;'"
                        ></v-img>
                    </v-col>
                    </v-row>
                </v-card-text>
            </v-card>

            <v-card class="mb-4" outlined style="border-width: 3px; background-color: #f0f4f8;">
                <v-card-title>
                    Ciclo de vida del proyecto
                </v-card-title>
                <v-card-text>
                    <v-row>
                        <v-col cols="4">

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
                                v-model="editedItem.fechaInicioProyecto"
                                label="Fecha de inicio"
                                readonly
                                v-bind="attrs"
                                v-on="on"
                                ></v-text-field>
                            </template>

                            <v-date-picker
                                v-model="editedItem.fechaInicioProyecto"
                                @change="menuinicioproyecto = false"
                                locale="es-mx"
                                no-title
                            ></v-date-picker>
                            </v-menu>

                        </v-col>
                        <v-col cols="4">
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
                                v-model="editedItem.fechaFinProyecto"
                                label="Fecha fin"
                                readonly
                                v-bind="attrs"
                                v-on="on"
                                ></v-text-field>
                            </template>

                            <v-date-picker
                                v-model="editedItem.fechaFinProyecto"
                                @change="menufinproyecto = false"
                                locale="es-mx"
                                no-title
                            ></v-date-picker>
                            </v-menu>
                        </v-col>
                        <v-col cols="4">
                            <v-text-field
                            label="Días disponibles"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            :value="diasDisponibles"
                            disabled
                            ></v-text-field>
                        </v-col>
                    </v-row>
                    <v-row>
                        <v-col cols="4">
                            <v-text-field
                            label="Estatus del proyecto"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            :value="estatus"
                            disabled
                            ></v-text-field>
                        </v-col>
                        <v-col cols="8">
                            <v-text-field
                            label="Volumen vendido del proyecto anual"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.volumenVendidoProyectoAnual"
                            ></v-text-field>
                        </v-col>
                    </v-row>
                </v-card-text>
            </v-card>
            <v-card class="mb-4" outlined style="border-width: 3px; background-color: #e6f2ea;">
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
                            v-model="editedItem.especificacionMaterial"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Tipo de proveedor"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.tipoProveedor"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Nombre de proveedor"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.nombreProveedor"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Codigo de identificación de materia"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.codigoIdentificacionMaterial"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Presentacion de materia prima"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.presentacionMateriaPrima"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="2">
                            <v-text-field
                            label="Peso de estándar pack MP"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.pesoEstandarPackMP"
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
                            v-model="editedItem.diametroInterno"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="6">
                            <v-text-field
                            label="Diámetro externo (min / max)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.diametroExterno"
                            ></v-text-field>
                        </v-col>
                    </v-row>
                    <v-divider class="my-4"></v-divider>
                    <v-subheader class="text-h6">Componentes</v-subheader>
                        <v-dialog v-model="dialogComponente" max-width="400px">
                        <template v-slot:activator="{ on }">
                            <v-layout align-end justify-end>
                            <v-btn color="primary" dark class="mb-2" v-on="on">
                                Componente<v-icon right color="white">add</v-icon>
                            </v-btn>
                            </v-layout>
                        </template>
                        <v-form ref="formComponente" lazy-validation v-model="validComponente">
                            <v-card>
                            <v-card-title>
                                <span class="heading">{{ componenteTitle + " componente" }}</span>
                            </v-card-title>
                            <v-card-text>
                                <v-container>
                                <v-row>
                                    <v-col cols="12">
                                    <v-text-field
                                        v-model="editedComponent.especificacionComponente"
                                        label= "Especificacion de componente"
                                        :rules="[rules.required]"
                                        required
                                        autocomplete="off"
                                        maxLength="255"
                                    ></v-text-field>
                                    </v-col>
                                    <v-col cols="12">
                                    <v-text-field
                                        v-model="editedComponent.tipoProveedor"
                                        label="Tipo proveedor"
                                        :rules="[v => !!v || 'Campo requerido']"
                                        required
                                        autocomplete="off"
                                        maxLength="50"
                                    ></v-text-field>
                                    </v-col>
                                    <v-col cols="12">
                                        <v-text-field
                                            v-model="editedComponent.nombreProveedor"
                                            label="Nombre de proveedor"
                                            :rules="[rules.required]"
                                            required
                                            autocomplete="off"
                                            maxLength = "50"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="12">
                                        <v-text-field
                                            v-model="editedComponent.codigoIdentificacionComponente"
                                            label="Código de identificación de componentes"
                                            :rules="[rules.required]"
                                            required
                                            autocomplete="off"
                                            maxLength = "50"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="12">
                                        <v-text-field
                                            v-model="editedComponent.cantidadComponentesPorPieza"
                                            label="Cantidad de componentes por pieza"
                                            :rules="[rules.required]"
                                            required
                                            autocomplete="off"
                                            maxLength = "50"
                                        ></v-text-field>
                                    </v-col>
                                </v-row>
                                </v-container>
                            </v-card-text>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn color="blue darken-1" text @click="closeComponente"> Cancelar </v-btn>
                                <v-btn color="blue darken-1" :disabled="!validComponente" text @click="saveComponente">
                                Agregar
                                </v-btn>
                            </v-card-actions>
                            </v-card>
                        </v-form>
                        </v-dialog>
                    <v-row>
                        <v-col cols="12">
                            <v-data-table
                            :headers="headers_componentes"
                            :options.sync="options"
                            :items="editedItem.componentes"
                            loading-text="Consultando información..."
                            no-data-text="No se encontró información."
                            :style="{ border: '1px solid #ccc', borderRadius: '4px',  backgroundColor: '#cce4d5' }"
                            >
                            <template v-slot:item.actions="{ item }">
                                <v-icon small class="mr-2" @click="editComponente(item)">
                                mdi-pencil
                                </v-icon>
                                <v-icon small @click="deleteComponent(item)">
                                mdi-delete
                                </v-icon>
                            </template>
                            </v-data-table>
                        </v-col>
                    </v-row>
                    <v-divider class="my-4"></v-divider>
                    <v-subheader class="text-h6">Especificación de Materia Prima</v-subheader>
                    <v-row>
                        <v-col cols="4">
                            <v-text-field
                            label="Largo de cinta/blank (Avance real) (mm)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Largo de cinta/blank (Avance real) (mm)"
                            v-model="editedItem.largoCintaBlank"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="4">
                            <v-text-field
                            label="Largo del material en máxima tolerancia"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Largo del material en máxima tolerancia"
                            v-model="editedItem.largoMaterialMaximaTolerancia"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="4">
                            <v-text-field
                            label="Ancho cinta/blank (mm)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Ancho cinta/blank (mm)"
                            v-model="editedItem.anchoCintaBlank"
                            ></v-text-field>
                        </v-col>
                    </v-row>
                    <v-row>
                        <v-col cols="4">
                            <v-text-field
                            label="Ancho del material en máxima tolerancia"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Ancho del material en máxima tolerancia"
                            v-model="editedItem.anchoMaterialMaximaTolerancia"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="4">
                            <v-text-field
                            label="Espesor (mm)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.espesor"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="4">
                            <v-text-field
                            label="Espesor del material en máxima tolerancia"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Espesor del material en máxima tolerancia"
                            v-model="editedItem.espesorMaterialMaximaTolerancia"
                            ></v-text-field>
                        </v-col>
                    </v-row>
                    <v-divider class="my-4"></v-divider>
                    <v-row>
                        <v-col cols="3">
                            <v-text-field
                            label="Coeficiente del material"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.coeficienteMaterial"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Peso blank (kg)=F.C."
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="pesoBlank"
                            disabled
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Peso blank (kg)=F.C. Max"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.pesoBlankMax"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Peso pieza (troquelado) (KG)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Peso pieza (troquelado) (KG)"
                            v-model="editedItem.pesoPiezaTroquelado"
                            ></v-text-field>
                        </v-col>
                    </v-row>
                    <v-row>
                        <v-col cols="3">
                            <v-text-field
                            label="Peso pieza (componente)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Peso pieza (componente) (Si aplica)"
                            v-model="editedItem.pesoPiezaComponente"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Factor de consumo"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Factor de consumo (logística)"
                            v-model="editedItem.factorConsumo"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Factor de aprovechamiento"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Factor de aprovechamiento"
                            v-model="factorAprovechamiento"
                            disabled
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="% Merma"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="merma"
                            disabled
                            ></v-text-field>
                        </v-col>
                    </v-row>
                </v-card-text>
            </v-card>
            <v-card class="mb-4" outlined style="border-width: 3px; background-color: #fffbee;">
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
                            v-model="editedItem.codigoEmpaque"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="4">
                            <v-text-field
                            label="Factor de consumo de empaque por pieza"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Factor de consumo de empaque por pieza"
                            v-model="editedItem.factorConsumoEmpaquePieza"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="4">
                            <v-text-field
                            label="Piezas por pallet"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.piezasPallet"
                            ></v-text-field>
                        </v-col>
                    </v-row>
                </v-card-text>
            </v-card>
            <v-card class="mb-4" outlined style="border-width: 3px; background-color: #f7f5fb;">
                <v-card-text>
                    <v-row>
                        <v-col cols="3">
                            <v-text-field
                            label="Número de operaciones"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.numeroOperaciones"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Número de máquinas"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.numeroMaquinas"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Número de operadores"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.numeroOperadores"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Número de ayudantes"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.numeroAyudantes"
                            ></v-text-field>
                        </v-col>
                    </v-row>
                    <v-row>
                        <v-col cols="3">
                            <v-text-field
                            label="Personal requerido"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="personalRequerido"
                            disabled
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Tiempo ciclo total (seg)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="tiempoCicloTotal"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Tiempo ciclo Máximo"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="tiempoCicloMaximo"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Tiempo de llenado de célula"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Tiempo de llenado de célula"
                            v-model="editedItem.tiempoLlenadoCelula"
                            ></v-text-field>
                        </v-col>
                    </v-row>
                    <v-row>
                        <v-col cols="3">
                            <v-text-field
                            label="Piezas por hora"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.piezasPorHora"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Tiempo total de cambio de modelo (min)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Tiempo total de cambio de modelo (min)"
                            v-model="editedItem.tiempoTotalCambioModelo"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Tiempo de liberación (min)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Tiempo de liberación (min)"
                            v-model="editedItem.tiempoLiberacion"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Tiempo de ajuste por fechador (min)"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Tiempo de ajuste por fechador (min)"
                            v-model="editedItem.tiempoAjustePorFechador"
                            ></v-text-field>
                        </v-col>
                    </v-row>
                    <v-row>
                        <v-col cols="9">
                            <v-text-field
                            label="Piezas de ajuste"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            v-model="editedItem.piezasDeAjuste"
                            ></v-text-field>
                        </v-col>
                        <v-col cols="3">
                            <v-text-field
                            label="Cantidad económica de pedido"
                            autocomplete="off"
                            maxLength="255"
                            outlined
                            title="Cantidad económica de pedido"
                            v-model="editedItem.cantidadEconomicaPedido"
                            ></v-text-field>
                        </v-col>
                    </v-row>
                </v-card-text>
            </v-card>
            <v-card class="mb-4" outlined style="border-width: 3px; background-color: #fff5f7;">
                <v-card-title>
                    Ruta de fabricación
                </v-card-title>
                <v-card-text>
                        <v-dialog v-model="dialogRuta" max-width="400px">
                        <template v-slot:activator="{ on }">
                            <v-layout align-end justify-end>
                            <v-btn color="primary" dark class="mb-2" v-on="on">
                                Ruta<v-icon right color="white">add</v-icon>
                            </v-btn>
                            </v-layout>
                        </template>
                        <v-form ref="formRuta" lazy-validation v-model="validRuta">
                            <v-card>
                            <v-card-title>
                                <span class="heading">{{ rutaTitle + " ruta de fabricación" }}</span>
                            </v-card-title>
                            <v-card-text>
                                <v-container>
                                <v-row>
                                    <v-col cols="12">
                                    <v-text-field
                                        v-model="editedRuta.operacion"
                                        label= "Operacion"
                                        :rules="[rules.required]"
                                        required
                                        autocomplete="off"
                                        maxLength="255"
                                    ></v-text-field>
                                    </v-col>
                                    <v-col cols="12">
                                    <v-text-field
                                        v-model="editedRuta.numeroMaquina"
                                        label="No. de máquina"
                                        :rules="[v => !!v || 'Campo requerido']"
                                        required
                                        autocomplete="off"
                                        maxLength="50"
                                    ></v-text-field>
                                    </v-col>
                                    <v-col cols="12">
                                        <v-text-field
                                            v-model="editedRuta.tonelaje"
                                            label="Tonelaje (Tn)"
                                            :rules="[rules.required]"
                                            required
                                            autocomplete="off"
                                            maxLength = "50"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="12">
                                        <v-text-field
                                            v-model="editedRuta.descripcion"
                                            label="Descripción"
                                            :rules="[rules.required]"
                                            required
                                            autocomplete="off"
                                            maxLength = "50"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="12">
                                        <v-text-field
                                            v-model="editedRuta.fpc"
                                            label="FPC"
                                            :rules="[rules.required]"
                                            required
                                            autocomplete="off"
                                            maxLength = "50"
                                        ></v-text-field>
                                    </v-col>
                                    <v-col cols="12">
                                        <v-text-field
                                            v-model="editedRuta.tiempoCiclo"
                                            label="Tiempo de ciclo"
                                            :rules="[rules.required]"
                                            required
                                            autocomplete="off"
                                            maxLength = "50"
                                        ></v-text-field>
                                    </v-col>
                                </v-row>
                                </v-container>
                            </v-card-text>
                            <v-card-actions>
                                <v-spacer></v-spacer>
                                <v-btn color="blue darken-1" text @click="closeRuta"> Cancelar </v-btn>
                                <v-btn color="blue darken-1" :disabled="!validRuta" text @click="saveRuta">
                                Agregar
                                </v-btn>
                            </v-card-actions>
                            </v-card>
                        </v-form>
                        </v-dialog>
                    <v-row>
                        <v-col cols="12">
                            <v-data-table
                            :headers="headers_ruta_fabricacion"
                            :items="editedItem.rutas"
                            :options.sync="options"
                            loading-text="Consultando información..."
                            no-data-text="No se encontró información."
                            :style="{ border: '1px solid #ccc', borderRadius: '4px',  backgroundColor: '#fff5f7' }"
                            >
                            <template v-slot:item.actions="{ item }">
                                <v-icon small class="mr-2" @click="editRuta(item)">
                                mdi-pencil
                                </v-icon>
                                <v-icon small @click="deleteRuta(item)">
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
        <v-btn color="blue darken-1" :disabled="!valid" text @click="save" :loading="loading">
            Guardar
        </v-btn>
        </v-card-actions>
    </v-card>
    </v-form>
</v-container>
</template>

<script>
import PartService from "@/services/PartService";

export default {
  data() {
    return {
        defaultImage: require('@/assets/placeholder-image.png'),
        imageFile: null,
        dialogComponente: false,
        dialogRuta: false,
        parts: [],
        loading: false,
        valid: true,
        validComponente: true,
        validRuta: true,
        snackbar: false,
        snackbar_error: false,
        text: "My timeout is set to 3000.",
        timeout: 3000,
        menuinicioproyecto: false,
        menufinproyecto: false,
        headers_componentes: [
            { text: "Especificacion de componente", value: "especificacionComponente" },
            { text: "Tipo de proveedor", value: "tipoProveedor" },
            { text: "Nombre de proveedor", value: "nombreProveedor" },
            { text: "Código de identificación de componentes", value: "codigoIdentificacionComponente" },
            { text: "Cantidad de componentes por pieza", value: "cantidadComponentesPorPieza" },
            { text: "Acciones", value: "actions" },
        ],
        ruta_fabricacion: [{"operacion":"1", "n_maquinas":"5", "tonelaje":"1", "descripcion":"Ejemplo descripcion", "fpc":"2", "tiempo_ciclo":"50"}, {"operacion":"2", "n_maquinas":"8", "tonelaje":"1", "descripcion":"Ejemplo descripcion 2", "fpc":"3", "tiempo_ciclo":"80"}],
        headers_ruta_fabricacion: [
            { text: "Operacion", value: "operacion" },
            { text: "No. de máquina", value: "numeroMaquina" },
            { text: "Tonelaje (Tn)", value: "tonelaje" },
            { text: "Descripción", value: "descripcion" },
            { text: "FPC", value: "fpc" },
            { text: "Tiempo de ciclo", value: "tiempoCiclo" },
            { text: "Acciones", value: "actions" },
        ],
        editedIndex: 'nueva',
        editedItem: {
            numeroParte: null,
            proyecto: null,
            descripcion: null,
            fechaInicioProyecto: null,
            fechaFinProyecto: null,
            componentes: [],
            rutas: []
        },
        editedComponent: {},
        editedRuta: {},
        defaultItem: {
            id: null,
            numeroParte: null,
            proyecto: null,
            descripcion: null,
            fechaInicioProyecto: null,
            fechaFinProyecto: null,
        },
        options: {
            itemsPerPage: 10,
        },
        rules: {
            required: (value) => !!value || "Campo Requerido",
            numeroParte: (value) => {
                return (
                    !this.numerosParte.includes(value) || "El numero de parte ya existe"
                );
            }
        },
    };
  },
  methods: {
    getImageUrl(fileName) {
      if (fileName) {
        const timestamp = new Date().getTime();
        return `${process.env.VUE_APP_HUB_PHOTOS_BASEURL}/tyrsa_foto_folder/${fileName}?v=${timestamp}`;
      } else {
        return this.defaultImage;
      }
    },
    onImgError(event) {
      event.target.src = this.defaultImage;
    },
    validateComponente() {
      this.validComponente = this.$refs.formComponente.validate();
    },
    validateRuta() {
      this.validRuta = this.$refs.formRuta.validate();
    },
    handleImageUpload(file) {
        if (file) {
            this.imageFile = file;
        } else {
            this.imageFile = null;
        }
    },
    editComponente(item) {
        this.editedComponent = { ...item }
        this.dialogComponente = true;
        //this.editedComponentIndex = index;
    },
    editRuta(item) {
        this.editedRuta = { ...item }
        this.dialogRuta = true;
        //this.editedComponentIndex = index;
    },
    closeComponente() {
      this.dialogComponente = false;
      this.$nextTick(() => {
        this.editedComponent = { ...this.defaultItem }
        //this.editedComponentIndex = -1;
      });
      this.$refs.formComponente.resetValidation();
    },
    closeRuta() {
      this.dialogRuta = false;
      this.$nextTick(() => {
        this.editedRuta = { ...this.defaultItem }
        //this.editedComponentIndex = -1;
      });
      this.$refs.formRuta.resetValidation();
    },
    saveComponente() {
        this.validateComponente()
        if (this.validComponente){
            if (!this.editedItem.componentes) {
                this.editedItem.componentes = [];
            }

            // Si es nuevo
            if (!this.editedComponent.id) {
                this.editedComponent.id = crypto.randomUUID();
                this.editedItem.componentes.push(this.editedComponent);
                this.text = "El nuevo componente ha sido agregado a la lista.";
                this.snackbar = true;
            } else {
                // Si es edición
                const index = this.editedItem.componentes.findIndex(c => c.id === this.editedComponent.id);
                if (index !== -1) {
                    this.editedItem.componentes.splice(index, 1, this.editedComponent);
                    this.text = "El componente ha sido actualizado.";
                    this.snackbar = true;
                }
            }
            this.closeComponente();
        }
    },
    saveRuta() {
        this.validateRuta()
        if (this.validRuta){
            if (!this.editedItem.rutas) {
                this.editedItem.rutas = [];
            }

            // Si es nuevo
            if (!this.editedRuta.id) {
                this.editedRuta.id = crypto.randomUUID();
                this.editedItem.rutas.push(this.editedRuta);
                this.text = "La ruta de fabricación ha sido agregado a la lista.";
                this.snackbar = true;
            } else {
                // Si es edición
                const index = this.editedItem.rutas.findIndex(c => c.id === this.editedRuta.id);
                if (index !== -1) {
                    this.editedItem.rutas.splice(index, 1, this.editedRuta);
                    this.text = "La ruta de fabricación ha sido actualizada.";
                    this.snackbar = true;
                }
            }
            this.closeRuta();
        }
    },
    deleteComponent(item) {
        const index = this.editedItem.componentes.findIndex(c => c.id === item.id);
        if (index !== -1) {
            this.editedItem.componentes.splice(index, 1);
        }
    },
    deleteRuta(item) {
        const index = this.editedItem.rutas.findIndex(c => c.id === item.id);
        if (index !== -1) {
            this.editedItem.rutas.splice(index, 1);
        }
    },
    getAllParts(){
      return PartService.getAllParts()
        .then((response) => {
          this.parts = response.data;
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
    validate() {
      this.valid = this.$refs.form.validate();
    },
    loadItem(id) {
        if (id !== 'nueva') {
            PartService.getPart(id)
            .then((response) => {
                console.log("response.data: ", response.data);
                if (!response.data.componentes) response.data.componentes = [];
                if (!response.data.rutas) response.data.rutas = [];
                this.editedItem = response.data;
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
    },
    save() {
      this.validate();
      if (this.valid) {
        this.loading = true;
        if (this.editedIndex !== 'nueva') {
            console.log('Imagen a enviar:', this.imageFile);
          PartService.updatePart(this.editedItem, this.imageFile)
            .then(() => {
              this.loading = false;
              this.text = "La Parte ha sido editada exitosamente.";
              this.snackbar = true;

                // Esperar 2 segundos y luego redirigir a /industrializacion
                setTimeout(() => {
                this.$router.push("/industrializacion");
                }, 2000);
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
            PartService.createPart(this.editedItem, this.imageFile)
            .then(() => {
                this.loading = false;
                this.text = "La nueva Parte ha sido creada exitosamente.";
                this.snackbar = true;

                setTimeout(() => {
                this.$router.push("/industrializacion");
                }, 2000);
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
      return this.editedIndex === 'nueva'
        ? "Nueva Parte"
        : "Editar Parte";
    },
    componenteTitle() {
      return !this.editedComponent.id
        ? "Nuevo"
        : "Editar";
    },
    rutaTitle() {
      return !this.editedRuta.id
        ? "Nueva"
        : "Editar";
    },
    numerosParte() {
      return this.parts.map(
        (part) => part.numeroParte
      );
    },
    diasDisponibles() {
        if (!this.editedItem.fechaFinProyecto) return 'N/A';

        const fechaFin = new Date(this.editedItem.fechaFinProyecto);
        const hoy = new Date();

        // Normalizar fechas para que la hora no afecte la diferencia
        fechaFin.setHours(0,0,0,0);
        hoy.setHours(0,0,0,0);

        const diffTime = fechaFin - hoy; // diferencia en milisegundos
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); // diferencia en días

        // Si la fecha fin es anterior a hoy, muestra 0 o valor que prefieras
        return diffDays >= 0 ? diffDays : 0;
    },
    estatus() {
        return this.diasDisponibles >= 10 ? 'ACTIVO' : this.diasDisponibles > 0 && this.diasDisponibles < 10 ? 'PROXIMO A VENCER' : this.diasDisponibles === 0 ? 'VENCIDO' : 'N/A'
    },
    pesoBlank() {
        return this.editedItem.largoCintaBlank ? this.editedItem.largoCintaBlank*this.editedItem.anchoCintaBlank*this.editedItem.espesor*this.editedItem.coeficienteMaterial : ""
    },
    merma() {
        return this.pesoBlank !== 0 && this.pesoBlank != null && this.editedItem.pesoPiezaTroquelado != null ? (((this.pesoBlank - this.editedItem.pesoPiezaTroquelado) / this.pesoBlank) * 100) : "";
    },
    factorAprovechamiento() {
        return !isNaN(this.merma) && this.merma != null ? (100 - this.merma)  : "";
    },
    personalRequerido() {
        return this.editedItem.numeroOperadores ? parseInt(this.editedItem.numeroOperadores) + parseInt(this.editedItem.numeroAyudantes) : "";
    },
    tiempoCicloTotal() {
        if (!this.editedItem || !Array.isArray(this.editedItem.rutas)) return 0;

        return this.editedItem.rutas.reduce((acumulado, ruta) => {
            const tiempo = parseFloat(ruta.tiempoCiclo);
            return acumulado + (isNaN(tiempo) ? 0 : tiempo);
        }, 0);
    },
    tiempoCicloMaximo() {
        if (!this.editedItem || !Array.isArray(this.editedItem.rutas)) return 0;

        return Math.max(
        ...this.editedItem.rutas.map(ruta => {
            const tiempo = parseFloat(ruta.tiempoCiclo);
            return isNaN(tiempo) ? 0 : tiempo;
        })
        );
    }
  },
  created() {
    this.axios.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${this.$store.getters.getUser.token}`;
    this.editedIndex = this.$route.params.id;
    this.loadItem(this.editedIndex);
    this.getAllParts();
  },
};
</script>