<template>
  <v-container fluid>
    <v-dialog v-model="dialog" max-width="90%">
      <v-card flat>
          <v-card-title>
          <span class="heading">Visor</span>
          </v-card-title>
          <v-card-text>
            <div ref="scrollContainer" style="max-height: 70vh; overflow-y: auto;">
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
                              <v-col cols="4">
                                  <v-text-field
                                  label="Numero de parte"
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.numeroParte"
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="4">
                                  <v-text-field
                                  label="Proyecto"
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.proyecto"
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="4">
                                  <v-select
                                      v-model="editedItem.idCliente"
                                      :items="clientes"
                                      prepend-icon="assignment_ind"
                                      item-text="name"
                                      item-value="id"
                                      label="Cliente"
                                      :rules="[(v) => !!v || 'Campo requerido']"
                                      required
                                      autocomplete="off"
                                      outlined
                                  ></v-select>
                              </v-col>
                              <v-col cols="6">
                                  <v-text-field
                                  label="Descripción"
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.descripcion"
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="6">
                                  <v-text-field
                                  label="Nivel de ingeniería"
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.nivelIngenieria"
                                  ></v-text-field>
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
                              <v-col cols="2">

                                <v-text-field
                                prepend-icon="event"
                                outlined
                                label="Fecha de inicio"
                                v-model="editedItem.fechaInicioProyecto"
                                ></v-text-field>

                              </v-col>
                              <v-col cols="2">
                                <v-text-field
                                prepend-icon="event"
                                outlined
                                label="Fecha de inicio"
                                v-model="editedItem.fechaFinProyecto"
                                ></v-text-field>
                              </v-col>
                              <v-col cols="2">
                                  <v-text-field
                                  label="Días disponibles"
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.dias_disponibles"
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="2">
                                  <v-text-field
                                  label="Estatus del proyecto"
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.estatus"
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="4">
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
                                  </v-data-table>
                              </v-col>
                          </v-row>
                          <v-divider class="my-4"></v-divider>
                          <v-subheader class="text-h6">Especificación de Materia prima</v-subheader>
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
                                label="Peso pieza"
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
                                ></v-text-field>
                            </v-col>
                            <v-col cols="3">
                                <v-text-field
                                label="% Merma"
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                v-model="merma"
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
                                ></v-text-field>
                            </v-col>
                            <v-col cols="2">
                                <v-text-field
                                label="Tiempo ciclo total (seg)"
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                v-model="tiempoCicloTotal"
                                type="number"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="2">
                                <v-text-field
                                label="Tiempo ciclo Máximo"
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                v-model="tiempoCicloMaximo"
                                type="number"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="2">
                                <v-text-field
                                label="WIP por Máquina"
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                v-model="editedItem.wipPorMaquina"
                                type="number"
                                ></v-text-field>
                            </v-col>
                            <v-col cols="3">
                                <v-text-field
                                label="Tiempo de llenado de célula"
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                title="Tiempo de llenado de célula"
                                v-model="tiempoLlenadoCelula"
                                disabled
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
                                  </v-data-table>
                              </v-col>
                          </v-row>
                      </v-card-text>
                  </v-card>
              </v-container>
            </div>
          </v-card-text>
          <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="dialog=false">
              Cerrar
          </v-btn>
          </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialogLog" max-width="90%">
      <v-card flat>
          <v-card-title>
          <span class="heading">Log del número de parte {{editedItem.numeroParte}}</span>
          </v-card-title>
          <v-card-text>
            <div ref="scrollContainer" style="max-height: 70vh; overflow-y: auto;">
              <v-container>
                <v-data-table
                  :headers="headersLog"
                  :items="log"
                  item-key="id"
                  show-expand
                  :single-expand="true"
                >
                  <template v-slot:item.estatus="{ item }">
                    <span
                      :style="{
                        color:
                          item.estatus === 'Aprobada'
                            ? 'green'
                            : item.estatus === 'Rechazada'
                            ? 'red'
                            : item.estatus === 'Pendiente'
                            ? 'orange'
                            : 'inherit'
                      }"
                    >
                      {{ item.estatus }}
                    </span>
                  </template>
                  <template v-slot:expanded-item="{ item }">
                    <td :colspan="headers.length" class="text-center pa-4">
                      <v-simple-table dense class="mx-auto" style="width: 100%;">
                        <thead>
                          <tr>
                            <th class="text-center">Campo</th>
                            <th class="text-center">De</th>
                            <th class="text-center">A</th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr v-for="(cambio, index) in item.cambios" :key="index">
                             <td class="text-center">{{ cambio.campo }}</td>
                            <td class="text-center">

                              <template v-if="cambio.campo === 'Imagen'">
                                <div class="d-flex justify-center">
                                  <v-img
                                  :src="getThumbUrl(cambio.de)"
                                  @error="onImgError($event)"
                                  cover
                                  max-height="40"
                                  max-width="40"
                                  class="mb-3"
                                  style="border-radius: 12px;"
                                  ></v-img>
                                </div>
                              </template>

                              <template v-else-if="cambio.campo === 'Componentes'">
                                <v-simple-table dense class="mx-auto" :style="{ maxWidth: '700px;', border: '1px solid #ccc', borderRadius: '4px',  backgroundColor: '#f0f0f0' }">
                                  <thead>
                                    <tr>
                                      <th class="text-center">Especificación</th>
                                      <th class="text-center">Tipo proveedor</th>
                                      <th class="text-center">Nombre proveedor</th>
                                      <th class="text-center">Código</th>
                                      <th class="text-center">Cantidad</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    <tr v-for="(comp, i) in cambio.de" :key="comp._id || i">
                                      <td class="text-center">{{ comp.especificacionComponente }}</td>
                                      <td class="text-center">{{ comp.tipoProveedor }}</td>
                                      <td class="text-center">{{ comp.nombreProveedor }}</td>
                                      <td class="text-center">{{ comp.codigoIdentificacionComponente }}</td>
                                      <td class="text-center">{{ comp.cantidadComponentesPorPieza }}</td>
                                    </tr>
                                  </tbody>
                                </v-simple-table>
                              </template>

                              <template v-else-if="cambio.campo === 'Rutas'">
                                <v-simple-table dense class="mx-auto" :style="{ maxWidth: '700px;', border: '1px solid #ccc', borderRadius: '4px',  backgroundColor: '#f0f0f0' }">
                                  <thead>
                                    <tr>
                                      <th class="text-center">Operación</th>
                                      <th class="text-center">No. de Máquina</th>
                                      <th class="text-center">Tonelaje</th>
                                      <th class="text-center">Descripción</th>
                                      <th class="text-center">FPC</th>
                                      <th class="text-center">Tiempo de Ciclo</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    <tr v-for="(comp, i) in cambio.de" :key="comp._id || i">
                                      <td class="text-center">{{ comp.operacion }}</td>
                                      <td class="text-center">{{ comp.numeroMaquina }}</td>
                                      <td class="text-center">{{ comp.tonelaje }}</td>
                                      <td class="text-center">{{ comp.descripcion }}</td>
                                      <td class="text-center">{{ comp.fpc }}</td>
                                      <td class="text-center">{{ comp.tiempoCiclo }}</td>
                                    </tr>
                                  </tbody>
                                </v-simple-table>
                              </template>

                              <template v-else>
                                {{ cambio.de }}
                              </template>
                            </td>
                            <td class="text-center">
                              <template v-if="cambio.campo === 'Imagen'">
                                <div class="d-flex justify-center">
                                  <v-img
                                  :src="getThumbUrl(cambio.a)"
                                  @error="onImgError($event)"
                                  cover
                                  max-height="40"
                                  max-width="40"
                                  class="mb-3"
                                  style="border-radius: 12px;"
                                  ></v-img>
                                </div>
                              </template>

                              <template v-else-if="cambio.campo === 'Componentes'">
                                <v-simple-table dense class="mx-auto" :style="{ maxWidth: '700px;', border: '1px solid #ccc', borderRadius: '4px',  backgroundColor: '#d4edda' }">
                                  <thead>
                                    <tr>
                                      <th class="text-center">Especificación</th>
                                      <th class="text-center">Tipo proveedor</th>
                                      <th class="text-center">Nombre proveedor</th>
                                      <th class="text-center">Código</th>
                                      <th class="text-center">Cantidad</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    <tr v-for="(comp, i) in cambio.a" :key="comp._id || i">
                                      <td class="text-center">{{ comp.especificacionComponente }}</td>
                                      <td class="text-center">{{ comp.tipoProveedor }}</td>
                                      <td class="text-center">{{ comp.nombreProveedor }}</td>
                                      <td class="text-center">{{ comp.codigoIdentificacionComponente }}</td>
                                      <td class="text-center">{{ comp.cantidadComponentesPorPieza }}</td>
                                    </tr>
                                  </tbody>
                                </v-simple-table>
                              </template>

                              <template v-else-if="cambio.campo === 'Rutas'">
                                <v-simple-table dense class="mx-auto" :style="{ maxWidth: '700px;', border: '1px solid #ccc', borderRadius: '4px',  backgroundColor: '#d4edda' }">
                                  <thead>
                                    <tr>
                                      <th class="text-center">Operación</th>
                                      <th class="text-center">No. de Máquina</th>
                                      <th class="text-center">Tonelaje</th>
                                      <th class="text-center">Descripción</th>
                                      <th class="text-center">FPC</th>
                                      <th class="text-center">Tiempo de Ciclo</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    <tr v-for="(comp, i) in cambio.a" :key="comp._id || i">
                                      <td class="text-center">{{ comp.operacion }}</td>
                                      <td class="text-center">{{ comp.numeroMaquina }}</td>
                                      <td class="text-center">{{ comp.tonelaje }}</td>
                                      <td class="text-center">{{ comp.descripcion }}</td>
                                      <td class="text-center">{{ comp.fpc }}</td>
                                      <td class="text-center">{{ comp.tiempoCiclo }}</td>
                                    </tr>
                                  </tbody>
                                </v-simple-table>
                              </template>

                              <template v-else>
                                {{ cambio.a }}
                              </template>
                            </td>
                          </tr>
                        </tbody>
                      </v-simple-table>
                    </td>
                  </template>
                  <template v-slot:item.actions="{ item }">
                    <span v-if="item && item.estatus === 'Pendiente' && ($store.getters.getUser.role === 'ROLE_GERENTE_INGENIERIA' || $store.getters.getUser.role === 'ROLE_ADMIN')">
                      <v-icon color="green" @click="approvePartUpdate(item)" title="Aprobar Cambio">
                      mdi-check
                      </v-icon>
                      <v-icon color="red" @click="denyPartUpdate(item)" title="Rechazar Cambio">
                      mdi-close
                      </v-icon>
                    </span>
                  </template>
                </v-data-table>

              </v-container>
            </div>
          </v-card-text>
          <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="blue darken-1" text @click="dialogLog=false">
              Cerrar
          </v-btn>
          </v-card-actions>
      </v-card>
    </v-dialog>


    <v-card flat>
        <v-layout align-end justify-end>
            <v-btn color="primary" dark class="mb-2" :to="{ name: 'Parte', params: { id: 'nueva' } }">
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
              label="Filtrar por número de parte, descripción, proyecto ..."
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
          :items="processedParts"
          :search="search"
          :options.sync="options"
          :loading="loading"
          loading-text="Consultando información..."
          no-data-text="No se encontró información."
        >
            <template v-slot:item.fileName="{ item }">
                <v-img
                :src="getThumbUrl(item.fileName)"
                @error="onImgError($event)"
                cover
                max-height="40"
                max-width="40"
                class="mb-3"
                style="border-radius: 12px;"
                ></v-img>
            </template>
            
            <template v-slot:item.estatus="{ item }">
                <span
                    :class="{
                    'estatus-activo': item.estatus === 'ACTIVO',
                    'estatus-vencido': item.estatus === 'VENCIDO',
                    'estatus-proximo': item.estatus === 'PROXIMO A VENCER',
                    'estatus-NA': item.estatus === 'N/A',
                    }"
                    class="estatus-cell"
                >
                    {{ item.estatus }}
                </span>
            </template>
            <template v-slot:item.actions="{ item }">
                <v-icon small class="mr-1" @click="openVisor(item)" title="Visualizar toda la información de esta Parte">
                mdi-magnify
                </v-icon>
                <v-icon small class="mr-1" @click="editItem(item)" title="Editar Parte" :disabled="item.actualizacionPendiente === true" v-if="$store.getters.getUser.role === 'ROLE_GERENTE_INGENIERIA' || $store.getters.getUser.role === 'ROLE_ADMIN' || $store.getters.getUser.role === 'ROLE_AREA_INGENIERIA'">
                mdi-pencil
                </v-icon>
                <v-icon small class="mr-1" @click="openLog(item)" title="Abrir log de cambios de esta Parte" :color="item.actualizacionPendiente ? '#FFA500' : ''">
                mdi-history
                </v-icon>
            </template>
        </v-data-table>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import PartService from "@/services/PartService";
import ClienteService from "@/services/ClienteService";

export default {
  data() {
    return {
      defaultImage: require('@/assets/placeholder-image.png'),
      parts:[],
      clientes:[],
      dialog: false,
      dialogLog: false,
      search: "",
      options: {
        itemsPerPage: 10,
      },
      loading: true,
      log:[],
      logItems: [
        {
          "fecha": "08-10-2025",
          "estatus": "Pendiente",
          "solicitante": "Axel Galeana",
          "aprobador": "Monserrat Urquiza",
          "fechaAprobacion": "09-10-2025",
          "cambios": [
            {"campo": "Descripcion", "de":"EXCLUDER SEAL ", "a": "EXCLUDER SEAL actualizado"},
            {"campo": "Nivel ingenieria", "de":"b+", "a": "a+"},
            {"campo": "Fecha fin", "de":"11-12-2025", "a": "11-01-2026"}
          ]
        },
        {
          "fecha": "06-08-2025",
          "estatus": "Aprobado",
          "solicitante": "Axel Galeana",
          "aprobador": "Monserrat Urquiza",
          "fechaAprobacion": "07-08-2025",
          "cambios": [
            {"campo": "Tiempo de ajuste por fechador", "de":"0", "a": "1"},
            {"campo": "Especificacion de material", "de":"SAE 1010", "a": "SAE 2010"},
            {"campo": "Fecha fin", "de":"11-12-2025", "a": "11-01-2026"}
          ]
        }
      ],
      headersLog: [
        { text: "Fecha de cambio", value: "fecha" },
        { text: "Solicitante", value: "userName" },
        { text: "Aprobador", value: "aprobador" },
        { text: "Estatus", value: "estatus" },
        { text: "Fecha aprobacion", value: "fechaAprobacion" },
        { text: "Acciones", value: "actions", width: '92px' },
      ],
      headers: [
        { text: "Num. Parte", value: "numeroParte" },
        {
          text: "Foto",
          sortable: false,
          value: "fileName",
          align: "center",
          width: "10px"
        },
        { text: "Proyecto", value: "proyecto" },
        { text: "Cliente", value: "nombreCliente" },
        { text: "Descripción", value: "descripcion" },
        { text: "Nivel Ingeniería", value: "nivelIngenieria" },
        { text: "Fecha Inicio", value: "fechaInicioProyecto", width: '110px' },
        { text: "Fecha Fin", value: "fechaFinProyecto", width: '110px' },
        { text: "Dias Disponibles", value: "dias_disponibles" },
        { text: "Estatus", value: "estatus" },
        /*{ text: "Volumen vendido", value: "volumen_vendido" },*/
        { text: "Acciones", value: "actions", width: '92px' },
      ],
        headers_componentes: [
            { text: "Especificacion de componente", value: "especificacionComponente" },
            { text: "Tipo de proveedor", value: "tipoProveedor" },
            { text: "Nombre de proveedor", value: "nombreProveedor" },
            { text: "Código de identificación de componentes", value: "codigoIdentificacionComponente" },
            { text: "Cantidad de componentes por pieza", value: "cantidadComponentesPorPieza" },
        ],
        headers_ruta_fabricacion: [
            { text: "Operacion", value: "operacion" },
            { text: "No. de máquina", value: "numeroMaquina" },
            { text: "Tonelaje (Tn)", value: "tonelaje" },
            { text: "Descripción", value: "descripcion" },
            { text: "FPC", value: "fpc" },
            { text: "Tiempo de ciclo", value: "tiempoCiclo" },
        ],
      editedItem: {
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
    getClientes() {
      return ClienteService.getAllClientes()
        .then((response) => {
          this.clientes = response.data;
          this.loading = false;
          console.log("this.parts: ", this.parts);
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
    getThumbUrl(fileName) {
      if (fileName) {
        const timestamp = new Date().getTime();
        return `${process.env.VUE_APP_HUB_PHOTOS_BASEURL}/tyrsa_foto_folder/thumbs/${fileName}?v=${timestamp}`;
      } else {
        return this.defaultImage;
      }
    },
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
    editItem(item) {
      this.$router.push({ name: 'Parte', params: { id: item.id } });
    },
    openVisor(item) {
      const dias_disponibles = this.calcularDiasDisponibles(item.fechaFinProyecto);

      const itemModificado = {
        ...item,
        componentes: item.componentes ? [...item.componentes] : [],
        dias_disponibles: dias_disponibles,
        estatus:
          dias_disponibles >= 183
            ? 'ACTIVO'
            : dias_disponibles > 0
            ? 'PROXIMO A VENCER'
            : dias_disponibles === 0
            ? 'VENCIDO'
            : 'N/A',
      };

      this.editedItem = itemModificado;
      this.dialog = true;
    },
    openLog(item) {
      
      return PartService.getPartLog(item.rootPartId)
        .then((response) => {
          this.log = response.data;
          this.loading = false;
          this.dialogLog = true;
        })
        .catch((error) => {
          this.log = [];
          this.loading = false;
          this.dialogLog = true;
          if (
            error.response.data.error &&
            error.response.data.error.toUpperCase().includes("TOKEN")
          ) {
            this.$store.dispatch("tokenerror", error.response.data.error);
          }
        });
    },
    approvePartUpdate(item){
      return PartService.approvePartUpdate(item.id)
        .then(() => {
          console.log("item: ", item);
          this.getParts();
          this.openLog(item);
        })
        .catch((error) => {
          this.log = [];
          this.getParts();
          this.openLog(item);
          if (
            error.response.data.error &&
            error.response.data.error.toUpperCase().includes("TOKEN")
          ) {
            this.$store.dispatch("tokenerror", error.response.data.error);
          }
        });
    },
    denyPartUpdate(item){
      return PartService.denyPartUpdate(item.id)
        .then(() => {
          console.log("item: ", item);
          this.getParts();
          this.openLog(item);
        })
        .catch((error) => {
          this.getParts();
          this.openLog(item);
          if (
            error.response.data.error &&
            error.response.data.error.toUpperCase().includes("TOKEN")
          ) {
            this.$store.dispatch("tokenerror", error.response.data.error);
          }
        });
    },
    getParts() {
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
    calcularDiasDisponibles(fechaFin) {
        if (!fechaFin) return '';
        const hoy = new Date();
        const fin = new Date(fechaFin);

        hoy.setHours(0, 0, 0, 0);
        fin.setHours(0, 0, 0, 0);

        const diff = Math.ceil((fin - hoy) / (1000 * 60 * 60 * 24));
        return diff >= 0 ? diff : 0;
    },
  },
  computed: {
    processedParts(){
        const hoy = new Date();
        hoy.setHours(0, 0, 0, 0);

        return this.parts.map(part => {
            if (!part.fechaFinProyecto) {
                return {
                ...part,
                dias_disponibles: 'N/A',
                estatus: 'N/A'
                };
            }

            const fechaFinProyecto = new Date(part.fechaFinProyecto);
            fechaFinProyecto.setHours(0, 0, 0, 0);

            const diffTime = fechaFinProyecto - hoy;
            const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

            return {
                ...part,
                dias_disponibles: diffDays >= 0 ? diffDays : 0,
                estatus: diffDays >= 182 ? 'ACTIVO' : diffDays > 0 && diffDays < 182 ? 'PROXIMO A VENCER' : 'VENCIDO'
            };
        });
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
    },
    tiempoLlenadoCelula() {
        if (this.tiempoCicloTotal && this.editedItem.wipPorMaquina) return this.tiempoCicloTotal*this.editedItem.wipPorMaquina;
        else return 0;
    },
  },
  watch: {
    dialog(val) {
      if (val) {
        this.$nextTick(() => {
          const scrollDiv = this.$refs.scrollContainer;
          if (scrollDiv) {
            scrollDiv.scrollTop = 0;
          }
        });
      }
      //val || this.close();
    },
  },
  created() {
    this.axios.defaults.headers.common[
      "Authorization"
    ] = `Bearer ${this.$store.getters.getUser.token}`;
    this.getParts();
    this.getClientes();
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

.estatus-NA {
  background-color: #a9a9a9; /* Rojo */
}
</style>