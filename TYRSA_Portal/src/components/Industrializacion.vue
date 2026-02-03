<template>
  <v-container fluid>
    <v-dialog v-model="dialog" max-width="90%">
      <v-card flat>
          <v-card-title>
          <span class="heading">Visor</span>
          </v-card-title>
          <v-card-text ref="pdfDialogCard">
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
                                <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                  Número de parte
                                </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.numeroParte"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="4">
                                <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                  Proyecto
                                </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.proyecto"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="4">
                                <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                  Cliente
                                </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.nombreCliente"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="6">
                                <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                  Descripción
                                </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.descripcion"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="6">
                                <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                  Nivel de ingeniería
                                </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.nivelIngenieria"
                                  readonly
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
                                <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                  Fecha de inicio
                                </div>
                                <v-text-field
                                prepend-icon="event"
                                outlined
                                v-model="editedItem.fechaInicioProyecto"
                                readonly
                                ></v-text-field>

                              </v-col>
                              <v-col cols="2">
                                <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                  Fecha de fin
                                </div>
                                <v-text-field
                                prepend-icon="event"
                                outlined
                                v-model="editedItem.fechaFinProyecto"
                                readonly
                                ></v-text-field>
                              </v-col>
                              <v-col cols="2">
                                <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                  Días disponibles
                                </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.dias_disponibles"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="2">
                                <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                  Estatus del proyecto
                                </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.estatus"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="4">
                                <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                  Volumen vendido del proyecto anual
                                </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.volumenVendidoProyectoAnual"
                                  readonly
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
                                <v-col cols="3">
                                <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                  Especificación de Material
                                </div>
                                    <v-text-field
                                    autocomplete="off"
                                    maxLength="255"
                                    outlined
                                    v-model="editedItem.especificacionMaterial"
                                    readonly
                                    ></v-text-field>
                                </v-col>
                                <v-col cols="3">
                                <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                  Tipo de proveedor
                                </div>
                                    <v-text-field
                                    autocomplete="off"
                                    maxLength="255"
                                    outlined
                                    v-model="editedItem.tipoProveedor"
                                    readonly
                                    ></v-text-field>
                                </v-col>
                                <v-col cols="3">
                                <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                  Nombre de proveedor
                                </div>
                                    <v-text-field
                                    autocomplete="off"
                                    maxLength="255"
                                    outlined
                                    v-model="editedItem.nombreProveedor"
                                    readonly
                                    ></v-text-field>
                                </v-col>
                                <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Codigo de identificación de materia
                                  </div>
                                    <v-text-field
                                    autocomplete="off"
                                    maxLength="255"
                                    outlined
                                    v-model="editedItem.codigoIdentificacionMaterial"
                                    readonly
                                    ></v-text-field>
                                </v-col>
                            </v-row>
                            <v-row>
                                <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Clasificación de material
                                  </div>
                                    <v-text-field
                                    autocomplete="off"
                                    maxLength="255"
                                    outlined
                                    v-model="editedItem.nombreClasificacionMaterial"
                                    readonly
                                    ></v-text-field>
                                </v-col>
                                <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Presentacion de materia prima
                                  </div>
                                    <v-text-field
                                    title="Presentacion de materia prima"
                                    autocomplete="off"
                                    maxLength="255"
                                    outlined
                                    v-model="editedItem.presentacionMateriaPrima"
                                    readonly
                                    ></v-text-field>
                                </v-col>
                                <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Peso de estándar pack MP (kg)
                                  </div>
                                    <v-text-field
                                    title="Peso de estándar pack MP (kg)"
                                    autocomplete="off"
                                    maxLength="255"
                                    outlined
                                    v-model="editedItem.pesoEstandarPackMP"
                                    readonly
                                    type="number"
                                    ></v-text-field>
                                </v-col>
                            </v-row>
                          <v-divider class="my-4"></v-divider>
                          <v-subheader class="text-h6">Especificación de Rollo</v-subheader>
                          <v-row>
                              <v-col cols="6">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Diámetro interno (min / max) (mm)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.diametroInterno"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="6">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Diámetro externo (min / max) (mm)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.diametroExterno"
                                  readonly
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
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Largo de cinta/blank (Avance real) (mm)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                    title="Largo de cinta/blank (Avance real) (mm)"
                                    v-model="editedItem.largoCintaBlank"
                                    readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="4">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Tolerancia máxima de largo de material (mm)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  title="Tolerancia máxima de largo de material (mm)"
                                  v-model="editedItem.largoMaterialMaximaTolerancia"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="4">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Ancho cinta/blank (mm)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                    title="Ancho cinta/blank (mm)"
                                    v-model="editedItem.anchoCintaBlank"
                                    readonly
                                  ></v-text-field>
                              </v-col>
                        </v-row>
                        <v-row>
                              <v-col cols="4">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Tolerancia máxima de ancho de material (mm)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                    title="Tolerancia máxima de ancho de material (mm)"
                                    v-model="editedItem.anchoMaterialMaximaTolerancia"
                                    readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="4">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Espesor (mm)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.espesor"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="4">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Tolerancia máxima de espesor de material (mm)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                    title="Tolerancia máxima de espesor de material (mm)"
                                    v-model="editedItem.espesorMaterialMaximaTolerancia"
                                    readonly
                                  ></v-text-field>
                              </v-col>
                          </v-row>
                          <v-divider class="my-4"></v-divider>
                        <v-row>
                            <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Coeficiente del material
                                  </div>
                                <v-text-field
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                v-model="editedItem.coeficienteMaterial"
                                readonly
                                ></v-text-field>
                            </v-col>
                            <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Factor de consumo (kg)
                                  </div>
                                <v-text-field
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                v-model="pesoBlank"
                                readonly
                                ></v-text-field>
                            </v-col>
                            <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Factor de consumo Max (kg)
                                  </div>
                                <v-text-field
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                v-model="pesoBlankMax"
                                readonly
                                ></v-text-field>
                            </v-col>
                            <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Peso pieza (troquelado) (kg)
                                  </div>
                                <v-text-field
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                title="Peso pieza (troquelado) (kg)"
                                v-model="editedItem.pesoPiezaTroquelado"
                                readonly
                                ></v-text-field>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Peso pieza (kg)
                                  </div>
                                <v-text-field
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                title="Peso pieza (componente) (Si aplica) (kg)"
                                v-model="editedItem.pesoPiezaComponente"
                                readonly
                                ></v-text-field>
                            </v-col>
                            <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    % Factor de aprovechamiento
                                  </div>
                                <v-text-field
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                title="% Factor de aprovechamiento"
                                v-model="factorAprovechamiento"
                                readonly
                                ></v-text-field>
                            </v-col>
                            <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    % Merma
                                  </div>
                                <v-text-field
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                v-model="merma"
                                readonly
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
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Código de empaque
                                  </div>
                                <v-text-field
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                v-model="editedItem.codigoEmpaque"
                                readonly
                                ></v-text-field>
                            </v-col>
                            <v-col cols="4">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Factor de consumo de empaque por pieza
                                  </div>
                                <v-text-field
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                title="Factor de consumo de empaque por pieza"
                                v-model="editedItem.factorConsumoEmpaquePieza"
                                readonly
                                ></v-text-field>
                            </v-col>
                            <v-col cols="4">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Piezas por pallet
                                  </div>
                                <v-text-field
                                autocomplete="off"
                                maxLength="255"
                                outlined
                                v-model="editedItem.piezasPallet"
                                readonly
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
                  <v-card class="mb-4" outlined style="border-width: 3px; background-color: #f7f5fb;">
                      <v-card-text>
                          <v-row>
                              <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Personal requerido
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="personalRequerido"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Tiempo ciclo total (seg)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="tiempoCicloTotal"
                                  type="number"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Tiempo ciclo Máximo (seg)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="tiempoCicloMaximo"
                                  type="number"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    WIP por Máquina
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.wipPorMaquina"
                                  type="number"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                          </v-row>
                          <v-row>
                              <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Tiempo de llenado de célula (seg)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  title="Tiempo de llenado de célula (seg)"
                                  v-model="tiempoLlenadoCelula"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="2">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Piezas por hora
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="piezasPorHora"
                                  type="number"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="4">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Tiempo total de cambio de modelo (seg)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  title="Tiempo total de cambio de modelo (seg)"
                                  v-model="editedItem.tiempoTotalCambioModelo"
                                  type="number"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Tiempo de liberación (seg)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  title="Tiempo de liberación (seg)"
                                  v-model="editedItem.tiempoLiberacion"
                                  type="number"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                          </v-row>
                          <v-row>
                              <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Tiempo de ajuste por fechador (seg)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  title="Tiempo de ajuste por fechador (seg)"
                                  v-model="editedItem.tiempoAjustePorFechador"
                                  type="number"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="4">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Tiempo de llenado de célula hasta liberación (seg)
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="tiempoLlenadoCelulaHastaLiberación"
                                  type="number"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="2">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Piezas de ajuste
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  v-model="editedItem.piezasDeAjuste"
                                  type="number"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                              <v-col cols="3">
                                  <div class="field-label" style="font-weight: bold; margin-bottom: 4px;">
                                    Cantidad económica de pedido
                                  </div>
                                  <v-text-field
                                  autocomplete="off"
                                  maxLength="255"
                                  outlined
                                  title="Cantidad económica de pedido"
                                  v-model="editedItem.cantidadEconomicaPedido"
                                  type="number"
                                  readonly
                                  ></v-text-field>
                              </v-col>
                          </v-row>
                      </v-card-text>
                  </v-card>
              </v-container>
            </div>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>

            <v-btn color="green darken-1" text
                  @click="exportarPDF(editedItem.numeroParte)"
                  :loading="pdf_loading">
              <v-icon left color="green">picture_as_pdf</v-icon>
              Exportar PDF
            </v-btn>

            <v-btn color="green darken-1" text
                  @click="exportToExcelHumanTable(editedItem)"
                  :loading="excel_loading">
              <v-icon left color="green">mdi-file-excel</v-icon>
              Exportar Excel
            </v-btn>

            <v-btn color="blue darken-1" text @click="dialog=false">
              <v-icon left color="blue">close</v-icon>
              Cerrar
            </v-btn>
          </v-card-actions>
      </v-card>
    </v-dialog>

    <v-dialog v-model="dialogLog" max-width="90%">
      <v-card flat>
          <v-card-title>
          <span class="heading">Log del Número {{editedItem.numeroParte}}</span>
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
                                      <th class="text-center">Número Operadores</th>
                                      <th class="text-center">Número Ayudantes</th>
                                      <th class="text-center">Tiempo de Ciclo</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    <tr v-for="(comp, i) in cambio.de" :key="comp._id || i">
                                      <td class="text-center">{{ comp.operacion }}</td>
                                      <td class="text-center">{{ comp.numeroMaquina }}</td>
                                      <td class="text-center">{{ comp.tonelaje }}</td>
                                      <td class="text-center">{{ comp.descripcion }}</td>
                                      <td class="text-center">{{ comp.numeroOperadores }}</td>
                                      <td class="text-center">{{ comp.numeroAyudantes }}</td>
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
                                      <th class="text-center">Número Operadores</th>
                                      <th class="text-center">Número Ayudantes</th>
                                      <th class="text-center">Tiempo de Ciclo</th>
                                    </tr>
                                  </thead>
                                  <tbody>
                                    <tr v-for="(comp, i) in cambio.a" :key="comp._id || i">
                                      <td class="text-center">{{ comp.operacion }}</td>
                                      <td class="text-center">{{ comp.numeroMaquina }}</td>
                                      <td class="text-center">{{ comp.tonelaje }}</td>
                                      <td class="text-center">{{ comp.descripcion }}</td>
                                      <td class="text-center">{{ comp.numeroOperadores }}</td>
                                      <td class="text-center">{{ comp.numeroAyudantes }}</td>
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
                    <span
                      v-if="item && item.estatus === 'Pendiente' &&
                        ($store.getters.getUser.role === 'ROLE_GERENTE_INGENIERIA' ||
                        $store.getters.getUser.role === 'ROLE_ADMIN')">

                      <v-btn
                        icon
                        x-small
                        style="padding:0; min-width:24px; width:24px; height:24px;"
                        color="green"
                        :loading="loadingApprove"
                        @click="approvePartUpdate(item)"
                        title="Aprobar Cambio"
                      >
                        <v-icon>mdi-check</v-icon>
                      </v-btn>

                      <v-btn
                        icon
                        x-small
                        style="padding:0; min-width:24px; width:24px; height:24px;"
                        color="red"
                        :loading="loadingDeny"
                        @click="denyPartUpdate(item)"
                        title="Rechazar Cambio"
                      >
                        <v-icon>mdi-close</v-icon>
                      </v-btn>

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
            <v-btn color="primary" dark class="mb-2" v-if="$store.getters.getUser.role === 'ROLE_GERENTE_INGENIERIA' || $store.getters.getUser.role === 'ROLE_ADMIN' || $store.getters.getUser.role === 'ROLE_AREA_INGENIERIA'" :to="{ name: 'Parte', params: { id: 'nueva' } }">
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
              label="Filtrar por Número, descripción, proyecto ..."
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
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';
import * as XLSX from 'xlsx'
import { saveAs } from 'file-saver'

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
      loading: false,
      pdf_loading: false,
      excel_loading: false,
      loadingApprove: false,
      loadingDeny: false,
      log:[],
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
            { text: "Número de Operadores", value: "numeroOperadores" },
            { text: "Número de Ayudantes", value: "numeroAyudantes" },
            { text: "Tiempo de ciclo", value: "tiempoCiclo" }
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
    exportToExcelHumanTable() {
      this.excel_loading = true;
      const parte = this.editedItem || {}

      // Propiedades calculadas
      parte.factorAprovechamiento = this.factorAprovechamiento
      parte.merma = this.merma
      parte.personalRequerido = this.personalRequerido
      parte.tiempoCicloTotal = this.tiempoCicloTotal
      parte.tiempoCicloMaximo = this.tiempoCicloMaximo
      parte.piezasPorHora = this.piezasPorHora
      parte.tiempoLlenadoCelula = this.tiempoLlenadoCelula
      parte.tiempoLlenadoCelulaHastaLiberación = this.tiempoLlenadoCelulaHastaLiberación
      parte.factorConsumo = this.pesoBlank
      parte.factorConsumoMax = this.pesoBlankMax

      // Arrays seguros
      const componentes = Array.isArray(parte.componentes) ? parte.componentes : []
      const rutas = Array.isArray(parte.rutas) ? parte.rutas : []

      // Campos de PARTE en orden y con headers humanos
      const parteFields = [
        { key: 'numeroParte', label: 'Número de parte' },
        { key: 'proyecto', label: 'Proyecto' },
        { key: 'nombreCliente', label: 'Cliente' },
        { key: 'descripcion', label: 'Descripción' },
        { key: 'nivelIngenieria', label: 'Nivel de ingeniería' },
        { key: 'fechaInicioProyecto', label: 'Fecha de inicio' },
        { key: 'fechaFinProyecto', label: 'Fecha de fin' },
        { key: 'dias_disponibles', label: 'Días disponibles' },
        { key: 'estatus', label: 'Estatus del proyecto' },
        { key: 'volumenVendidoProyectoAnual', label: 'Volumen vendido del proyecto anual' },
        { key: 'especificacionMaterial', label: 'Especificación de material' },
        { key: 'tipoProveedor', label: 'Tipo de proveedor' },
        { key: 'nombreProveedor', label: 'Nombre de proveedor' },
        { key: 'codigoIdentificacionMaterial', label: 'Código de identificación de materia' },
        { key: 'nombreClasificacionMaterial', label: 'Clasificación de material' },
        { key: 'presentacionMateriaPrima', label: 'Presentación de materia prima' },
        { key: 'pesoEstandarPackMP', label: 'Peso de estándar pack MP (kg)' },
        { key: 'diametroInterno', label: 'Diámetro interno (min / max) (mm)' },
        { key: 'diametroExterno', label: 'Diámetro externo (min / max) (mm)' },
        { key: 'largoCintaBlank', label: 'Largo de cinta/blank (Avance real) (mm)' },
        { key: 'largoMaterialMaximaTolerancia', label: 'Tolerancia máxima de largo de material (mm)' },
        { key: 'anchoCintaBlank', label: 'Ancho cinta/blank (mm)' },
        { key: 'anchoMaterialMaximaTolerancia', label: 'Tolerancia máxima de ancho de material (mm)' },
        { key: 'espesor', label: 'Espesor (mm)' },
        { key: 'espesorMaterialMaximaTolerancia', label: 'Tolerancia máxima de espesor de material (mm)' },
        { key: 'coeficienteMaterial', label: 'Coeficiente del material' },
        { key: 'factorConsumo', label: 'Factor de consumo (kg)' },
        { key: 'factorConsumoMax', label: 'Factor de consumo Max (kg)' },
        { key: 'pesoPiezaTroquelado', label: 'Peso pieza (troquelado) (kg)' },
        { key: 'pesoPiezaComponente', label: 'Peso pieza (kg)' },
        { key: 'factorAprovechamiento', label: '% Factor de aprovechamiento' },
        { key: 'merma', label: '% Merma' },
        { key: 'codigoEmpaque', label: 'Código de empaque' },
        { key: 'factorConsumoEmpaquePieza', label: 'Factor de consumo de empaque por pieza' },
        { key: 'piezasPallet', label: 'Piezas por pallet' },
        { key: 'personalRequerido', label: 'Personal requerido' },
        { key: 'tiempoCicloTotal', label: 'Tiempo ciclo total (seg)' },
        { key: 'tiempoCicloMaximo', label: 'Tiempo ciclo Máximo (seg)' },
        { key: 'wipPorMaquina', label: 'WIP por Máquina' },
        { key: 'tiempoLlenadoCelula', label: 'Tiempo de llenado de célula (seg)' },
        { key: 'piezasPorHora', label: 'Piezas por hora' },
        { key: 'tiempoTotalCambioModelo', label: 'Tiempo total de cambio de modelo (seg)' },
        { key: 'tiempoLiberacion', label: 'Tiempo de liberación (seg)' },
        { key: 'tiempoAjustePorFechador', label: 'Tiempo de ajuste por fechador (seg)' },
        { key: 'tiempoLlenadoCelulaHastaLiberación', label: 'Tiempo de llenado de célula hasta liberación (seg)' },
        { key: 'piezasDeAjuste', label: 'Piezas de ajuste' },
        { key: 'cantidadEconomicaPedido', label: 'Cantidad económica de pedido' }
      ]

      // Headers
      const headerRow1 = [
        'PARTE', ...Array(parteFields.length - 1).fill(''),
        'COMPONENTES', ...Array(5).fill(''),
        'RUTA DE FABRICACIÓN', ...Array(7).fill('')
      ]

      const headerRow2 = [
        ...parteFields.map(f => f.label),
        'Especificación de componente',
        'Tipo de proveedor',
        'Nombre de proveedor',
        'Código de identificación de componente',
        'Cantidad por pieza',
        'Operación',
        'Número de máquina',
        'Tonelaje',
        'Descripción',
        'Tiempo ciclo',
        'Número de operadores',
        'Número de ayudantes'
      ]

      // Filas
      const maxRows = Math.max(componentes.length, rutas.length, 1)
      const rows = []

      for (let i = 0; i < maxRows; i++) {
        const comp = componentes[i]
        const ruta = rutas[i]

        rows.push([
          ...parteFields.map(f => i === 0 ? (parte[f.key] ?? '') : ''),
          comp?.especificacionComponente || '',
          comp?.tipoProveedor || '',
          comp?.nombreProveedor || '',
          comp?.codigoIdentificacionComponente || '',
          comp?.cantidadComponentesPorPieza || '',
          ruta?.operacion || '',
          ruta?.numeroMaquina || '',
          ruta?.tonelaje || '',
          ruta?.descripcion || '',
          ruta?.tiempoCiclo || '',
          ruta?.numeroOperadores || '',
          ruta?.numeroAyudantes || ''
        ])
      }

      const sheetData = [headerRow1, headerRow2, ...rows]
      const worksheet = XLSX.utils.aoa_to_sheet(sheetData)
      worksheet['!cols'] = Array(headerRow2.length).fill({ wch: 25 })

      const workbook = XLSX.utils.book_new()
      XLSX.utils.book_append_sheet(workbook, worksheet, 'Parte')

      const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' })
      saveAs(
        new Blob([excelBuffer], { type: 'application/octet-stream' }),
        `Parte_${parte.numeroParte || 'SIN_NUMERO'}.xlsx`
      )
      setTimeout(() => {
        this.excel_loading = false;
      }, 1);
    },
    async exportarPDF( numeroParte) {
      this.pdf_loading = true;
      const original = this.$refs.pdfDialogCard;
      if (!original) return;

      // CLONAR CONTENEDOR
      const clone = original.cloneNode(true);

      // QUITAR SCROLL Y LIMITES
      const normalize = (el) => {
        el.style.maxHeight = 'none';
        el.style.overflow = 'visible';
        Array.from(el.children).forEach(normalize);
      };
      normalize(clone);

      // OCULTAR CLON
      clone.style.position = 'absolute';
      clone.style.top = '-9999px';
      clone.style.left = '-9999px';
      document.body.appendChild(clone);

      // OBTENER TODAS LAS SECCIONES
      const sections = Array.from(clone.querySelectorAll('.v-card'));

      const pdf = new jsPDF('p', 'pt', 'letter');

      const pageWidth = pdf.internal.pageSize.getWidth();
      const pageHeight = pdf.internal.pageSize.getHeight();

      const marginTop = 50;
      const marginBottom = 50;
      const marginLeft = 40;

      const usableWidth = pageWidth - marginLeft * 2;

      let cursorY = marginTop;

      // TITULO CENTRADO
      const titulo = 'Número de parte: ' + numeroParte;

      pdf.setFont('helvetica', 'bold');
      pdf.setFontSize(16);
      pdf.text(
        titulo,
        pageWidth / 2,
        cursorY,
        { align: 'center' }
      );

      cursorY += 30;

      // RENDERIZAR SECCION POR SECCION
      for (const section of sections) {

        const canvas = await html2canvas(section, {
          scale: 2,
          useCORS: true,
          backgroundColor: '#ffffff'
        });

        const imgHeight =
          (canvas.height * usableWidth) / canvas.width;

        // SI NO CABE COMPLETA, NUEVA PAGINA
        if (cursorY + imgHeight > pageHeight - marginBottom) {
          pdf.addPage();
          cursorY = marginTop;
        }

        const imgData = canvas.toDataURL('image/jpeg', 0.98);

        pdf.addImage(
          imgData,
          'JPEG',
          marginLeft,
          cursorY,
          usableWidth,
          imgHeight
        );

        cursorY += imgHeight + 20;
      }

      pdf.save(numeroParte + '.pdf');

      document.body.removeChild(clone);
      this.pdf_loading = false;
    },
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
        rutas: item.rutas ? [...item.rutas] : [],
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
      this.editedItem = item;
      
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
      this.loadingApprove = true;
      return PartService.approvePartUpdate(item.id)
        .then(() => {
          console.log("item: ", item);
          this.getParts();
          this.openLog(item);
          this.loadingApprove = false;
        })
        .catch((error) => {
          this.log = [];
          this.getParts();
          this.openLog(item);
          this.loadingApprove = false;
          if (
            error.response.data.error &&
            error.response.data.error.toUpperCase().includes("TOKEN")
          ) {
            this.$store.dispatch("tokenerror", error.response.data.error);
          }
        });
    },
    denyPartUpdate(item){
      this.loadingDeny = true;
      return PartService.denyPartUpdate(item.id)
        .then(() => {
          console.log("item: ", item);
          this.getParts();
          this.openLog(item);
          this.loadingDeny = false;
        })
        .catch((error) => {
          this.getParts();
          this.openLog(item);
          this.loadingDeny = false;
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
        return this.editedItem.largoCintaBlank && this.editedItem.anchoCintaBlank && this.editedItem.espesor && this.editedItem.coeficienteMaterial
            ? parseFloat((
                Number(this.editedItem.largoCintaBlank) *
                Number(this.editedItem.anchoCintaBlank) *
                Number(this.editedItem.espesor) *
                Number(this.editedItem.coeficienteMaterial)
            ).toFixed(4))
            : "";
    },
    pesoBlankMax() {
        return this.editedItem.largoCintaBlank && this.editedItem.anchoCintaBlank && this.editedItem.espesor && this.editedItem.coeficienteMaterial
            ? (
                (Number(this.editedItem.largoCintaBlank) + (Number(this.editedItem.largoMaterialMaximaTolerancia) || 0)) *
                (Number(this.editedItem.anchoCintaBlank) + (Number(this.editedItem.anchoMaterialMaximaTolerancia) || 0)) *
                (Number(this.editedItem.espesor) + (Number(this.editedItem.espesorMaterialMaximaTolerancia) || 0)) *
                Number(this.editedItem.coeficienteMaterial)
            ).toFixed(4)
            : "";
    },
    merma() {
        return this.pesoBlank !== 0 && this.pesoBlank != null && this.editedItem.pesoPiezaTroquelado != null
            ? Math.round(((this.pesoBlank - this.editedItem.pesoPiezaTroquelado) / this.pesoBlank) * 100)
            : "";
    },
    factorAprovechamiento() {
        return !isNaN(this.merma) && this.merma != null
            ? Math.round(100 - this.merma)
            : "";
    },
    personalRequerido() {
        //return this.editedItem.numeroOperadores && this.editedItem.numeroAyudantes ? parseInt(this.editedItem.numeroOperadores) + parseInt(this.editedItem.numeroAyudantes) : "";
        if (!this.editedItem || !Array.isArray(this.editedItem.rutas)) return 0;

        return this.editedItem.rutas.reduce((acumulado, ruta) => {
        const operadores = parseFloat(ruta.numeroOperadores);
        const ayudantes = parseFloat(ruta.numeroAyudantes);

        return acumulado +
            (isNaN(operadores) ? 0 : operadores) +
            (isNaN(ayudantes) ? 0 : ayudantes);
        }, 0);
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
    piezasPorHora() {
        return this.tiempoCicloMaximo ? 3600/this.tiempoCicloMaximo : ""

    },
    tiempoLlenadoCelula() {
        return (this.tiempoCicloTotal && this.editedItem.wipPorMaquina)
            ? parseFloat((this.tiempoCicloTotal * this.editedItem.wipPorMaquina).toFixed(4))
            : 0;
    },
    tiempoLlenadoCelulaHastaLiberación () {
        return (this.tiempoLlenadoCelula && this.editedItem.tiempoTotalCambioModelo && this.editedItem.tiempoLiberacion && this.editedItem.tiempoAjustePorFechador)
            ? Number(this.tiempoLlenadoCelula) + Number(this.editedItem.tiempoTotalCambioModelo) + Number(this.editedItem.tiempoLiberacion) + Number(this.editedItem.tiempoAjustePorFechador)
            : 0;
    }
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