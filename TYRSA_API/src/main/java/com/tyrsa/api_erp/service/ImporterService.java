package com.tyrsa.api_erp.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tyrsa.api_erp.model.Cliente;
import com.tyrsa.api_erp.model.Part;

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

            for (CSVRecord record : parser) {
                if (record.get("NUMERO DE PARTE") != null || !record.get("NUMERO DE PARTE").isBlank()) {
                    String numeroParte = record.get("NUMERO DE PARTE");
                    if (!partService.partExiste(numeroParte)) {
                        // Mapeo CSV → Entidad
                        Part parte = new Part();
                        parte.setNumeroParte(numeroParte);
                        parte.setProyecto(record.get("PROYECTO"));
                        parte.setIdCliente(cliente.getId());
                        parte.setNombreCliente(cliente.getName());
                        parte.setDescripcion(record.get("DESCRIPCION"));
                        parte.setNivelIngenieria(record.get("NIVEL DE INGENIERIA"));
                        parte.setFechaInicioProyecto(parseFecha(record.get("INICIO DE PROYECTO")));
                        parte.setFechaFinProyecto(parseFecha(record.get("FIN DE PROYECTO")));
                        parte.setEspecificacionMaterial(record.get("ESPECIFICACION DE MATERIAL"));
                        parte.setTipoProveedor(record.get("TIPO DE PROVEEDOR"));
                        parte.setNombreProveedor(record.get("NOMBRE DE PROVEEDOR"));
                        parte.setCodigoIdentificacionMaterial(record.get("CODIGO DE IDENTIFICACION DE MATERIA PRIMA"));
                        parte.setPresentacionMateriaPrima(record.get("PRESENTACION DE MATERIA PRIMA"));
                        parte.setLargoCintaBlank(record.get("LARGO DE CINTA/BLANK"));
                        parte.setLargoMaterialMaximaTolerancia(record.get("LARGO MAXIMA TOLERANCIA"));
                        parte.setAnchoCintaBlank(record.get("ANCHO CINTA / BLANK \n(mm)"));
                        parte.setAnchoMaterialMaximaTolerancia(record.get("ANCHO MAXIMA TOLERANCIA"));
                        parte.setEspesor(record.get("ESPESOR (mm)"));
                        parte.setEspesorMaterialMaximaTolerancia(record.get("ESPESOR MAXIMA TOLERANCIA"));
                        parte.setPesoPiezaTroquelado(record.get("PESO PIEZA (TROQUELADO) (KG)"));
                        parte.setPesoPiezaComponente(record.get("PESO PIEZA (COMPONENTE)"));
                        parte.setPesoEstandarPackMP(record.get("PESO DE ESTANDAR PACK MP"));
                        parte.setDiametroInterno(record.get("DIAMETRO INTERNO (min / max)"));
                        parte.setDiametroExterno(record.get("DIAMETRO EXTERNO (min / max)"));
                        parte.setTiempoTotalCambioModelo(record.get("TIEMPO TOTAL DE CAMBIO DE MODELO (min)"));
                        parte.setTiempoLiberacion(record.get("TIEMPO DE LIBERACION (min)"));
                        parte.setTiempoAjustePorFechador(record.get("TIEMPO DE AJUSTE POR FECHADOR (min)"));
                        parte.setPiezasDeAjuste(record.get("PIEZAS DE AJUSTE"));
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
