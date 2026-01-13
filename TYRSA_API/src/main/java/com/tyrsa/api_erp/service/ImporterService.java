package com.tyrsa.api_erp.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tyrsa.api_erp.model.CampoActualizado;
import com.tyrsa.api_erp.model.Cliente;
import com.tyrsa.api_erp.model.Componente;
import com.tyrsa.api_erp.model.Part;
import com.tyrsa.api_erp.model.RutaFabricacion;

@Service
public class ImporterService {

    @Autowired
    private PartService partService;

    @Autowired
    private ClienteService clienteService;

    public void importarDesdeCsv(MultipartFile file) {

        // Validaciones básicas
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo CSV está vacío");
        }

        String filename = file.getOriginalFilename();

        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("El archivo debe ser un archivo .csv");
        }
        // Configuración MODERNA del CSV (no deprecated)
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()               // usa la primera fila como cabecera
                .setSkipHeaderRecord(true) // no procesa la cabecera como dato
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build();

        // Lectura y parseo
        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)
            );
            CSVParser parser = new CSVParser(reader, format)
        ) {
            String nombreSinExtension = Paths.get(file.getOriginalFilename())
                                        .getFileName()
                                        .toString()
                                        .replaceFirst("\\.[^.]+$", "");
            if (!clienteService.clienteExistePorNombre(nombreSinExtension)) {
                Cliente nuevoCliente = new Cliente();
                nuevoCliente.setName(nombreSinExtension);
                nuevoCliente.setActivo(true);
                
                clienteService.crearCliente(nuevoCliente);
            }

            Cliente cliente = clienteService.obtenerClientePorNombre(nombreSinExtension).orElse(null);

            Map<String, Integer> headers = parser.getHeaderMap();

            if (!headers.containsKey("NUMERO DE PARTE")) {
                throw new IllegalArgumentException(
                    "Columna requerida no encontrada: NUMERO DE PARTE2"
                );
            }

            for (CSVRecord record : parser) {
                String numeroParte = record.get("NUMERO DE PARTE");
                if (numeroParte != null && !numeroParte.isBlank()) {
                    if (!partService.partExiste(numeroParte)) {
                        // Mapeo CSV → Entidad
                        Part parte = new Part();
                        parte.setNumeroParte(numeroParte);
                        parte.setIdCliente(cliente.getId());
                        parte.setNombreCliente(cliente.getName());
                        if (headers.containsKey("PROYECTO")) parte.setProyecto(record.get("PROYECTO"));
                        if (headers.containsKey("DESCRIPCION")) parte.setDescripcion(record.get("DESCRIPCION"));
                        if (headers.containsKey("NIVEL DE INGENIERIA")) parte.setNivelIngenieria(record.get("NIVEL DE INGENIERIA"));
                        if (headers.containsKey("INICIO DE PROYECTO")) parte.setFechaInicioProyecto(parseFecha(record.get("INICIO DE PROYECTO")));
                        if (headers.containsKey("FIN DE PROYECTO")) parte.setFechaFinProyecto(parseFecha(record.get("FIN DE PROYECTO")));
                        if (headers.containsKey("ESPECIFICACION DE MATERIAL")) parte.setEspecificacionMaterial(record.get("ESPECIFICACION DE MATERIAL"));
                        if (headers.containsKey("TIPO DE PROVEEDOR")) parte.setTipoProveedor(record.get("TIPO DE PROVEEDOR"));
                        if (headers.containsKey("NOMBRE DE PROVEEDOR")) parte.setNombreProveedor(record.get("NOMBRE DE PROVEEDOR"));
                        if (headers.containsKey("CODIGO DE IDENTIFICACION DE MATERIA PRIMA")) parte.setCodigoIdentificacionMaterial(record.get("CODIGO DE IDENTIFICACION DE MATERIA PRIMA"));
                        if (headers.containsKey("PRESENTACION DE MATERIA PRIMA")) parte.setPresentacionMateriaPrima(record.get("PRESENTACION DE MATERIA PRIMA"));
                        if (headers.containsKey("LARGO DE CINTA/BLANK")) parte.setLargoCintaBlank(record.get("LARGO DE CINTA/BLANK"));
                        if (headers.containsKey("LARGO MAXIMA TOLERANCIA")) parte.setLargoMaterialMaximaTolerancia(record.get("LARGO MAXIMA TOLERANCIA"));
                        if (headers.containsKey("ANCHO CINTA / BLANK (mm)")) parte.setAnchoCintaBlank(record.get("ANCHO CINTA / BLANK (mm)"));
                        if (headers.containsKey("ANCHO MAXIMA TOLERANCIA")) parte.setAnchoMaterialMaximaTolerancia(record.get("ANCHO MAXIMA TOLERANCIA"));
                        if (headers.containsKey("ESPESOR (mm)")) parte.setEspesor(record.get("ESPESOR (mm)"));
                        if (headers.containsKey("ESPESOR MAXIMA TOLERANCIA")) parte.setEspesorMaterialMaximaTolerancia(record.get("ESPESOR MAXIMA TOLERANCIA"));
                        if (headers.containsKey("PESO PIEZA (TROQUELADO) (KG)")) parte.setPesoPiezaTroquelado(record.get("PESO PIEZA (TROQUELADO) (KG)"));
                        if (headers.containsKey("PESO PIEZA (COMPONENTE)")) parte.setPesoPiezaComponente(record.get("PESO PIEZA (COMPONENTE)"));
                        if (headers.containsKey("PESO DE ESTANDAR PACK MP")) parte.setPesoEstandarPackMP(record.get("PESO DE ESTANDAR PACK MP"));
                        if (headers.containsKey("DIAMETRO INTERNO (min / max)")) parte.setDiametroInterno(record.get("DIAMETRO INTERNO (min / max)"));
                        if (headers.containsKey("DIAMETRO EXTERNO (min / max)")) parte.setDiametroExterno(record.get("DIAMETRO EXTERNO (min / max)"));
                        if (headers.containsKey("TIEMPO TOTAL DE CAMBIO DE MODELO (min)")) parte.setTiempoTotalCambioModelo(record.get("TIEMPO TOTAL DE CAMBIO DE MODELO (min)"));
                        if (headers.containsKey("TIEMPO DE LIBERACION (min)")) parte.setTiempoLiberacion(record.get("TIEMPO DE LIBERACION (min)"));
                        if (headers.containsKey("TIEMPO DE AJUSTE POR FECHADOR (min)")) parte.setTiempoAjustePorFechador(record.get("TIEMPO DE AJUSTE POR FECHADOR (min)"));
                        if (headers.containsKey("PROYECTO")) parte.setPiezasDeAjuste(record.get("PIEZAS DE AJUSTE"));

                        //Componentes
                        List<Componente> componentes = new ArrayList<>();
                        for (int i=1; i<=10; i++)  {
                            String headerEspecificacionComponente = "ESPECIFICACION DE COMPONENTE " + i;
                            if (!headers.containsKey(headerEspecificacionComponente) || record.get(headerEspecificacionComponente) == null || record.get(headerEspecificacionComponente).isBlank() || record.get(headerEspecificacionComponente).equals("N/A")) {
                                break;
                            }
                            Componente componente = new Componente();
                            componente.setEspecificacionComponente(record.get(headerEspecificacionComponente));
                            componente.setTipoProveedor(record.get("TIPO DE PROVEEDOR " + i));
                            componente.setNombreProveedor(record.get("NOMBRE DE PROVEEDOR " + i));
                            componente.setCodigoIdentificacionComponente(record.get("CODIGO DE IDENTIFICACION DE COMPONENTES " + i));
                            componente.setCantidadComponentesPorPieza(record.get("CANTIDAD DE COMPONENTES POR PIEZA " + i));

                            componentes.add(componente);
                        }
                        if (componentes.size() > 0) parte.setComponentes(componentes.toArray(new Componente[0]));

                        //Rutas
                        List<RutaFabricacion> rutas = new ArrayList<>();
                        for (int i=1; i<=13; i++)  {
                            String headerOperacion = "OPERACION " + i;
                            if (!headers.containsKey(headerOperacion) || record.get(headerOperacion) == null || record.get(headerOperacion).isBlank() || record.get(headerOperacion).equals("N/A")) {
                                break;
                            }
                            RutaFabricacion ruta = new RutaFabricacion();
                            ruta.setOperacion(record.get(headerOperacion));
                            ruta.setNumeroMaquina(record.get("NO. DE MAQUINA " + i));
                            ruta.setTonelaje(record.get("TONELAJE (Tn) " + i));
                            ruta.setDescripcion(record.get("DESCRIPCION " + i));
                            ruta.setTiempoCiclo(record.get("TIEMPO CICLO (Seg) " + i));

                            rutas.add(ruta);
                        }
                        if (rutas.size() > 0) parte.setRutas(rutas.toArray(new RutaFabricacion[0]));



                        partService.createPart(parte, null, "Sistema");
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al importar el archivo CSV", e);
        }
    }

    private LocalDate parseFecha(String valor) {

        if (valor == null || valor.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(
                valor.trim(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
            );
        } catch (DateTimeParseException e) {
            return null; // no es una fecha válida
        }
    }
}
