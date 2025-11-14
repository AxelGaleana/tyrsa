package com.tyrsa.api_erp.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tyrsa.api_erp.dto.UserResponse;
import com.tyrsa.api_erp.model.CampoActualizado;
import com.tyrsa.api_erp.model.Part;
import com.tyrsa.api_erp.model.PartLog;
import com.tyrsa.api_erp.repository.PartRepository;
import com.tyrsa.api_erp.repository.LogRepository;
import java.util.Optional;



@Service
public class PartService {

    @Autowired
    private PartRepository partRepository;
    @Autowired
    private LogRepository logRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @Value("${upload.path}")
    private String uploadDir;

    // Método para registrar usuario
    public Part createPart(Part newPart, MultipartFile imageFile) {
        if (newPart.getNumeroParte() == null || newPart.getNumeroParte().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de parte es obligatorio.");
        }
        if(partRepository.existsByNumeroParte(newPart.getNumeroParte())) {
            throw new RuntimeException("Numero de parte ya existe");
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String originalFilename = imageFile.getOriginalFilename();

                if (originalFilename == null || originalFilename.isBlank()) {
                    throw new IllegalArgumentException("El nombre del archivo es inválido.");
                }

                String extension = "";

                int dotIndex = originalFilename.lastIndexOf('.');
                if (dotIndex >= 0) {
                    extension = originalFilename.substring(dotIndex + 1); // sin el punto
                }

                //String fileName = newPart.getNumeroParte().trim() + "." + extension;
                String safeNumeroParte = newPart.getNumeroParte().trim().replaceAll("[^a-zA-Z0-9_-]", "_");
                String fileName = safeNumeroParte + "." + extension;

                // Ruta original
                Path uploadPath = Paths.get(uploadDir);
                Files.createDirectories(uploadPath);
                Path originalImagePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), originalImagePath, StandardCopyOption.REPLACE_EXISTING);

                // Crear miniatura
                try (InputStream in = Files.newInputStream(originalImagePath)) {
                    BufferedImage originalImage = ImageIO.read(in);

                    if (originalImage == null) {
                        throw new IllegalArgumentException("El archivo no es una imagen válida.");
                    }

                    int thumbnailWidth = 150;
                    int thumbnailHeight = (originalImage.getHeight() * thumbnailWidth) / originalImage.getWidth();

                    BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = thumbnail.createGraphics();
                    g.drawImage(originalImage, 0, 0, thumbnailWidth, thumbnailHeight, null);
                    g.dispose();

                    // Guardar miniatura
                    Path thumbsPath = uploadPath.resolve("thumbs");
                    Files.createDirectories(thumbsPath);

                    Path thumbnailPath = thumbsPath.resolve(fileName);
                    ImageIO.write(thumbnail, extension, thumbnailPath.toFile());
                }

                newPart.setFileName(fileName);
                newPart.setVersion("actual");
                newPart.setFechaActualizacion(LocalDateTime.now());

            } catch (IOException e) {
                throw new RuntimeException("Error al guardar la imagen", e);
            }
        }

        return partRepository.save(newPart);
    }


    public Part updatePartByNumeroParte(String numeroParte, Part updatedPart, MultipartFile imageFile, UserDetails userDetails) {
        Part existente = partRepository.findByNumeroParte(numeroParte)
            .orElseThrow(() -> new IllegalArgumentException("La parte con número " + numeroParte + " no existe."));

        int changes = 0;
        Part nuevaVersion = new Part();
        // Copiar todos los campos del nuevaVersion
        BeanUtils.copyProperties(existente, nuevaVersion, "id", "version", "fechaCreacion");
        LocalDateTime  fechaActual = LocalDateTime.now();
        PartLog log = new PartLog();
        log.setFecha(fechaActual);
        log.setEstatus("Pendiente");
        log.setUserName(userDetails.getUsername());
        log.setNumeroParte(updatedPart.getNumeroParte());

        List<CampoActualizado> cambios = new ArrayList<>();
        if (!updatedPart.getProyecto().equals(existente.getProyecto())) {
            nuevaVersion.setProyecto(updatedPart.getProyecto());
            cambios.add(new CampoActualizado("Proyecto", existente.getProyecto(), updatedPart.getProyecto()));
            changes++;
        }
        if (!updatedPart.getDescripcion().equals(existente.getDescripcion())) {
            nuevaVersion.setDescripcion(updatedPart.getDescripcion());
            cambios.add(new CampoActualizado("Descripcion", existente.getDescripcion(), updatedPart.getDescripcion()));
            changes++;
        }
        if (!updatedPart.getNivelIngenieria().equals(existente.getNivelIngenieria())) {
            nuevaVersion.setNivelIngenieria(updatedPart.getNivelIngenieria());
            cambios.add(new CampoActualizado("Nivel Ingenieria", existente.getNivelIngenieria(), updatedPart.getNivelIngenieria()));
            changes++;
        }
        if (!updatedPart.getFechaInicioProyecto().equals(existente.getFechaInicioProyecto())) {
            nuevaVersion.setFechaInicioProyecto(updatedPart.getFechaInicioProyecto());
            cambios.add(new CampoActualizado("Fecha Inicio Proyecto", existente.getFechaInicioProyecto().toString(), updatedPart.getFechaInicioProyecto().toString()));
            changes++;
        }
        if (!updatedPart.getFechaFinProyecto().equals(existente.getFechaFinProyecto())) {
            nuevaVersion.setFechaFinProyecto(updatedPart.getFechaFinProyecto());
            cambios.add(new CampoActualizado("Fecha Fin Proyecto", existente.getFechaFinProyecto().toString(), updatedPart.getFechaFinProyecto().toString()));
            changes++;
        }
        if (!updatedPart.getVolumenVendidoProyectoAnual().equals(existente.getVolumenVendidoProyectoAnual())) {
            nuevaVersion.setVolumenVendidoProyectoAnual(updatedPart.getVolumenVendidoProyectoAnual());
            cambios.add(new CampoActualizado("Volumen Vendido Proyecto Anual", existente.getVolumenVendidoProyectoAnual(), updatedPart.getVolumenVendidoProyectoAnual()));
            changes++;
        }
        if (!updatedPart.getEspecificacionMaterial().equals(existente.getEspecificacionMaterial())) {
            nuevaVersion.setEspecificacionMaterial(updatedPart.getEspecificacionMaterial());
            cambios.add(new CampoActualizado("Especificacion Material", existente.getEspecificacionMaterial(), updatedPart.getEspecificacionMaterial()));
            changes++;
        }
        if (!updatedPart.getTipoProveedor().equals(existente.getTipoProveedor())) {
            nuevaVersion.setTipoProveedor(updatedPart.getTipoProveedor());
            cambios.add(new CampoActualizado("Tipo Proveedor", existente.getTipoProveedor(), updatedPart.getTipoProveedor()));
            changes++;
        }
        if (!updatedPart.getNombreProveedor().equals(existente.getNombreProveedor())) {
            nuevaVersion.setNombreProveedor(updatedPart.getNombreProveedor());
            cambios.add(new CampoActualizado("Nombre Proveedor", existente.getNombreProveedor(), updatedPart.getNombreProveedor()));
            changes++;
        }
        if (!updatedPart.getCodigoIdentificacionMaterial().equals(existente.getCodigoIdentificacionMaterial())) {
            nuevaVersion.setCodigoIdentificacionMaterial(updatedPart.getCodigoIdentificacionMaterial());
            cambios.add(new CampoActualizado("Codigo Identificacion Material", existente.getCodigoIdentificacionMaterial(), updatedPart.getCodigoIdentificacionMaterial()));
            changes++;
        }
        if (!updatedPart.getPresentacionMateriaPrima().equals(existente.getPresentacionMateriaPrima())) {
            nuevaVersion.setPresentacionMateriaPrima(updatedPart.getPresentacionMateriaPrima());
            cambios.add(new CampoActualizado("Presentacion Materia Prima", existente.getPresentacionMateriaPrima(), updatedPart.getPresentacionMateriaPrima()));
            changes++;
        }
        if (!updatedPart.getPesoEstandarPackMP().equals(existente.getPesoEstandarPackMP())) {
            nuevaVersion.setPesoEstandarPackMP(updatedPart.getPesoEstandarPackMP());
            cambios.add(new CampoActualizado("Peso Estandar Pack MP", existente.getPesoEstandarPackMP(), updatedPart.getPesoEstandarPackMP()));
            changes++;
        }
        if (!updatedPart.getDiametroInterno().equals(existente.getDiametroInterno())) {
            nuevaVersion.setDiametroInterno(updatedPart.getDiametroInterno());
            cambios.add(new CampoActualizado("Diametro Interno", existente.getDiametroInterno(), updatedPart.getDiametroInterno()));
            changes++;
        }

        if (!updatedPart.getLargoCintaBlank().equals(existente.getLargoCintaBlank())) {
            nuevaVersion.setLargoCintaBlank(updatedPart.getLargoCintaBlank());
            cambios.add(new CampoActualizado("Largo Cinta Blank", existente.getLargoCintaBlank(), updatedPart.getLargoCintaBlank()));
            changes++;
        }
        if (!updatedPart.getLargoMaterialMaximaTolerancia().equals(existente.getLargoMaterialMaximaTolerancia())) {
            nuevaVersion.setLargoMaterialMaximaTolerancia(updatedPart.getLargoMaterialMaximaTolerancia());
            cambios.add(new CampoActualizado("Largo Material Maxima Tolerancia", existente.getLargoMaterialMaximaTolerancia(), updatedPart.getLargoMaterialMaximaTolerancia()));
            changes++;
        }
        if (!updatedPart.getAnchoCintaBlank().equals(existente.getAnchoCintaBlank())) {
            nuevaVersion.setAnchoCintaBlank(updatedPart.getAnchoCintaBlank());
            cambios.add(new CampoActualizado("Ancho Cinta Blank", existente.getAnchoCintaBlank(), updatedPart.getAnchoCintaBlank()));
            changes++;
        }
        if (!updatedPart.getAnchoMaterialMaximaTolerancia().equals(existente.getAnchoMaterialMaximaTolerancia())) {
            nuevaVersion.setAnchoMaterialMaximaTolerancia(updatedPart.getAnchoMaterialMaximaTolerancia());
            cambios.add(new CampoActualizado("Ancho Material Maxima Tolerancia", existente.getAnchoMaterialMaximaTolerancia(), updatedPart.getAnchoMaterialMaximaTolerancia()));
            changes++;
        }
        if (!updatedPart.getEspesor().equals(existente.getEspesor())) {
            nuevaVersion.setEspesor(updatedPart.getEspesor());
            cambios.add(new CampoActualizado("Espesor", existente.getEspesor(), updatedPart.getEspesor()));
            changes++;
        }
        if (!updatedPart.getEspesorMaterialMaximaTolerancia().equals(existente.getEspesorMaterialMaximaTolerancia())) {
            nuevaVersion.setEspesorMaterialMaximaTolerancia(updatedPart.getEspesorMaterialMaximaTolerancia());
            cambios.add(new CampoActualizado("Espesor Material Maxima Tolerancia", existente.getEspesorMaterialMaximaTolerancia(), updatedPart.getEspesorMaterialMaximaTolerancia()));
            changes++;
        }

        if (!updatedPart.getCoeficienteMaterial().equals(existente.getCoeficienteMaterial())) {
            nuevaVersion.setCoeficienteMaterial(updatedPart.getCoeficienteMaterial());
            cambios.add(new CampoActualizado("Coeficiente Material", existente.getCoeficienteMaterial(), updatedPart.getCoeficienteMaterial()));
            changes++;
        }
        if (!updatedPart.getPesoBlankMax().equals(existente.getPesoBlankMax())) {
            nuevaVersion.setPesoBlankMax(updatedPart.getPesoBlankMax());
            cambios.add(new CampoActualizado("Peso Blank Max", existente.getPesoBlankMax(), updatedPart.getPesoBlankMax()));
            changes++;
        }
        if (!updatedPart.getPesoPiezaTroquelado().equals(existente.getPesoPiezaTroquelado())) {
            nuevaVersion.setPesoPiezaTroquelado(existente.getPesoPiezaTroquelado());
            cambios.add(new CampoActualizado("Peso Pieza Troquelado", existente.getPesoPiezaTroquelado(), updatedPart.getPesoPiezaTroquelado()));
            changes++;
        }
        if (!updatedPart.getPesoPiezaComponente().equals(existente.getPesoPiezaComponente())) {
            nuevaVersion.setPesoPiezaComponente(updatedPart.getPesoPiezaComponente());
            cambios.add(new CampoActualizado("Peso Pieza Componente", existente.getPesoPiezaComponente(), updatedPart.getPesoPiezaComponente()));
            changes++;
        }
        if (!updatedPart.getFactorConsumo().equals(existente.getFactorConsumo())) {
            nuevaVersion.setFactorConsumo(updatedPart.getFactorConsumo());
            cambios.add(new CampoActualizado("Factor Consumo", existente.getFactorConsumo(), updatedPart.getFactorConsumo()));
            changes++;
        }

        if (!updatedPart.getCodigoEmpaque().equals(existente.getCodigoEmpaque())) {
            nuevaVersion.setCodigoEmpaque(updatedPart.getCodigoEmpaque());
            cambios.add(new CampoActualizado("Codigo Empaque", existente.getCodigoEmpaque(), updatedPart.getCodigoEmpaque()));
            changes++;
        }
        if (!updatedPart.getFactorConsumoEmpaquePieza().equals(existente.getFactorConsumoEmpaquePieza())) {
            nuevaVersion.setFactorConsumoEmpaquePieza(updatedPart.getFactorConsumoEmpaquePieza());
            cambios.add(new CampoActualizado("Factor Consumo Empaque Pieza", existente.getFactorConsumoEmpaquePieza(), updatedPart.getFactorConsumoEmpaquePieza()));
            changes++;
        }
        if (!updatedPart.getPiezasPallet().equals(existente.getPiezasPallet())) {
            nuevaVersion.setPiezasPallet(updatedPart.getPiezasPallet());
            cambios.add(new CampoActualizado("Piezas Pallet", existente.getPiezasPallet(), updatedPart.getPiezasPallet()));
            changes++;
        }

        if (!updatedPart.getNumeroOperaciones().equals(existente.getNumeroOperaciones())) {
            nuevaVersion.setNumeroOperaciones(updatedPart.getNumeroOperaciones());
            cambios.add(new CampoActualizado("Numero Operaciones", existente.getNumeroOperaciones(), updatedPart.getNumeroOperaciones()));
            changes++;
        }
        if (!updatedPart.getNumeroMaquinas().equals(existente.getNumeroMaquinas())) {
            nuevaVersion.setNumeroMaquinas(updatedPart.getNumeroMaquinas());
            cambios.add(new CampoActualizado("Numero Maquinas", existente.getNumeroMaquinas(), updatedPart.getNumeroMaquinas()));
            changes++;
        }
        if (!updatedPart.getNumeroOperadores().equals(existente.getNumeroOperadores())) {
            nuevaVersion.setNumeroOperadores(updatedPart.getNumeroOperadores());
            cambios.add(new CampoActualizado("Numero Operadores", existente.getNumeroOperadores(), updatedPart.getNumeroOperadores()));
            changes++;
        }
        if (!updatedPart.getNumeroAyudantes().equals(existente.getNumeroAyudantes())) {
            nuevaVersion.setNumeroAyudantes(updatedPart.getNumeroAyudantes());
            cambios.add(new CampoActualizado("Numero Ayudantes", existente.getNumeroAyudantes(), updatedPart.getNumeroAyudantes()));
            changes++;
        }
        if (!updatedPart.getPersonalRequerido().equals(existente.getPersonalRequerido())) {
            nuevaVersion.setPersonalRequerido(updatedPart.getPersonalRequerido());
            cambios.add(new CampoActualizado("Personal Requerido", existente.getPersonalRequerido(), updatedPart.getPersonalRequerido()));
            changes++;
        }
        if (!updatedPart.getTiempoCicloTotal().equals(existente.getTiempoCicloTotal())) {
            nuevaVersion.setTiempoCicloTotal(updatedPart.getTiempoCicloTotal());
            cambios.add(new CampoActualizado("Tiempo Ciclo Total", existente.getTiempoCicloTotal(), updatedPart.getTiempoCicloTotal()));
            changes++;
        }
        if (!updatedPart.getTiempoCicloMaximo().equals(existente.getTiempoCicloMaximo())) {
            nuevaVersion.setTiempoCicloMaximo(updatedPart.getTiempoCicloMaximo());
            cambios.add(new CampoActualizado("Tiempo Ciclo Maximo", existente.getTiempoCicloMaximo(), updatedPart.getTiempoCicloMaximo()));
            changes++;
        }
        if (!updatedPart.getTiempoLlenadoCelula().equals(existente.getTiempoLlenadoCelula())) {
            nuevaVersion.setTiempoLlenadoCelula(updatedPart.getTiempoLlenadoCelula());
            cambios.add(new CampoActualizado("Tiempo Llenado Celula", existente.getTiempoLlenadoCelula(), updatedPart.getTiempoLlenadoCelula()));
            changes++;
        }
        if (!updatedPart.getPiezasPorHora().equals(existente.getPiezasPorHora())) {
            nuevaVersion.setPiezasPorHora(updatedPart.getPiezasPorHora());
            cambios.add(new CampoActualizado("Piezas Por Hora", existente.getPiezasPorHora(), updatedPart.getPiezasPorHora()));
            changes++;
        }
        if (!updatedPart.getTiempoTotalCambioModelo().equals(existente.getTiempoTotalCambioModelo())) {
            nuevaVersion.setTiempoTotalCambioModelo(updatedPart.getTiempoTotalCambioModelo());
            cambios.add(new CampoActualizado("Tiempo Total Cambio Modelo", existente.getTiempoTotalCambioModelo(), updatedPart.getTiempoTotalCambioModelo()));
            changes++;
        }
        if (!updatedPart.getTiempoLiberacion().equals(existente.getTiempoLiberacion())) {
            nuevaVersion.setTiempoLiberacion(updatedPart.getTiempoLiberacion());
            cambios.add(new CampoActualizado("Tiempo Liberacion", existente.getTiempoLiberacion(), updatedPart.getTiempoLiberacion()));
            changes++;
        }
        if (!updatedPart.getTiempoAjustePorFechador().equals(existente.getTiempoAjustePorFechador())) {
            nuevaVersion.setTiempoAjustePorFechador(updatedPart.getTiempoAjustePorFechador());
            cambios.add(new CampoActualizado("Tiempo Ajuste Por Fechador", existente.getTiempoAjustePorFechador(), updatedPart.getTiempoAjustePorFechador()));
            changes++;
        }
        if (!updatedPart.getPiezasDeAjuste().equals(existente.getPiezasDeAjuste())) {
            nuevaVersion.setPiezasDeAjuste(updatedPart.getPiezasDeAjuste());
            cambios.add(new CampoActualizado("Piezas De Ajuste", existente.getPiezasDeAjuste(), updatedPart.getPiezasDeAjuste()));
            changes++;
        }
        if (!updatedPart.getCantidadEconomicaPedido().equals(existente.getCantidadEconomicaPedido())) {
            nuevaVersion.setCantidadEconomicaPedido(updatedPart.getCantidadEconomicaPedido());
            cambios.add(new CampoActualizado("Cantidad Economica Pedido", existente.getCantidadEconomicaPedido(), updatedPart.getCantidadEconomicaPedido()));
            changes++;
        }        

        nuevaVersion.setComponentes(updatedPart.getComponentes());
        nuevaVersion.setRutas(updatedPart.getRutas());

        // Asignar versión nueva con fecha y hora
        String fechaVersion = fechaActual.format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        nuevaVersion.setVersion(fechaVersion);
        nuevaVersion.setFechaActualizacion(fechaActual);

        // Procesar imagen si viene una nueva
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String originalFilename = imageFile.getOriginalFilename();

                if (originalFilename == null || originalFilename.isBlank()) {
                    throw new IllegalArgumentException("El nombre del archivo es inválido.");
                }

                String extension = "";
                int dotIndex = originalFilename.lastIndexOf('.');
                if (dotIndex >= 0) {
                    extension = originalFilename.substring(dotIndex + 1);
                }

                String safeNumeroParte = nuevaVersion.getNumeroParte().trim().replaceAll("[^a-zA-Z0-9_-]", "_");
                String fileName = safeNumeroParte + "." + extension;

                // Ruta para guardar
                Path uploadPath = Paths.get(uploadDir);
                System.out.println("Upload directory: " + uploadDir);
                Files.createDirectories(uploadPath);

                Path originalImagePath = uploadPath.resolve(fileName);
                Files.copy(imageFile.getInputStream(), originalImagePath, StandardCopyOption.REPLACE_EXISTING);

                // Miniatura
                try (InputStream in = Files.newInputStream(originalImagePath)) {
                    BufferedImage originalImage = ImageIO.read(in);
                    if (originalImage == null) {
                        throw new IllegalArgumentException("El archivo no es una imagen válida.");
                    }

                    int thumbnailWidth = 150;
                    int thumbnailHeight = (originalImage.getHeight() * thumbnailWidth) / originalImage.getWidth();

                    BufferedImage thumbnail = new BufferedImage(thumbnailWidth, thumbnailHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g = thumbnail.createGraphics();
                    g.drawImage(originalImage, 0, 0, thumbnailWidth, thumbnailHeight, null);
                    g.dispose();

                    Path thumbsPath = uploadPath.resolve("thumbs");
                    Files.createDirectories(thumbsPath);
                    Path thumbnailPath = thumbsPath.resolve(fileName);
                    ImageIO.write(thumbnail, extension, thumbnailPath.toFile());
                }

                // Actualizar nombre del archivo en la entidad
                nuevaVersion.setFileName(fileName);

            } catch (IOException e) {
                throw new RuntimeException("Error al actualizar la imagen", e);
            }
        }
        if (changes > 0){
            log.setCambios(cambios);
            logRepository.save(log);

            List<String> emails = userService.getUsersByRole("ROLE_GERENTE_INGENIERIA").stream()
                    .map(UserResponse::getEmail)
                    .filter(email -> email != null && !email.isEmpty())
                    .toList();

            if (!emails.isEmpty()) {
                emailService.sendPartChageAprobalRequest(emails.toArray(new String[0]), existente.getNumeroParte());
            }
        }
        existente.setActualizacionPendiente(true);
        partRepository.save(existente);

        return partRepository.save(nuevaVersion);
    }

    public Part getPartByNumeroParte(String numeroParte) {
        return partRepository.findByNumeroParteAndVersion(numeroParte, "actual")
            .orElseThrow(() -> new IllegalArgumentException("La parte con número " + numeroParte + " no existe o no es la versión actual."));
    }

    public List<PartLog> getLogByNumeroParte(String numeroParte) {
        List<PartLog> logs = logRepository.findByNumeroParte(numeroParte);

        if (logs.isEmpty()) {
            throw new IllegalArgumentException(
                "La parte con número " + numeroParte + " no tiene registros en log."
            );
        }

        // Ordenar descendente por fecha (más reciente primero)
        logs.sort((l1, l2) -> l2.getFecha().compareTo(l1.getFecha()));

        List<PartLog> result = new ArrayList<>();

        // Agregar los últimos dos logs (más recientes)
        for (int i = 0; i < 2 && i < logs.size(); i++) {
            result.add(logs.get(i));
        }

        // Agregar el log más viejo
        if (logs.size() > 2) {
            result.add(logs.get(logs.size() - 1));
        } else if (logs.size() == 2) {
            // Si solo hay 2 registros, el más viejo ya está incluido, no duplicar
            // (ya está result[1])
        } else if (logs.size() == 1) {
            // Solo hay uno, ya está incluido
        }

        return result;
    }

    public List<Part> getAllParts() {
        return partRepository.findAllByVersionOrderByFechaActualizacionDesc("actual");
    }

    public void approvePartUpdate(String logId, String approver) {
        // Actualizar log
        PartLog log = logRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el log con id: " + logId));

        log.setAprobador(approver);
        log.setFechaAprobacion(LocalDate.now());
        log.setEstatus("Aprobada");
        logRepository.save(log);

        String numeroParte = log.getNumeroParte();

        // Eliminar la versión obsoleta
        Part oldPart = partRepository.findByNumeroParteAndVersion(numeroParte, "actual")
                .orElseThrow(() -> new IllegalArgumentException(
                        "La parte con número " + numeroParte + " no existe o no es la versión actual."));
        partRepository.delete(oldPart);

        // Actualizar la nueva parte a versión actual
        Part newPart = partRepository.findByNumeroParte(numeroParte)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La nueva parte con número " + numeroParte + " no existe."));
        newPart.setVersion("actual");
        partRepository.save(newPart);
    }

    public void denyPartUpdate(String logId, String approver) {
        // Actualizar log
        PartLog log = logRepository.findById(logId)
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el log con id: " + logId));

        log.setAprobador(approver);
        log.setFechaAprobacion(LocalDate.now());
        log.setEstatus("Rechazada");
        logRepository.save(log);

        String numeroParte = log.getNumeroParte();

        // Eliminar version actualizada
        Part newPart = partRepository.findByNumeroParteAndVersion(numeroParte, log.getFecha().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")))
                .orElseThrow(() -> new IllegalArgumentException(
                        "La nueva parte con número " + numeroParte + " no existe."));
        partRepository.delete(newPart);

        //Actualizar parte actual para que no tenga actualizacion pendiente
        Part currentPart = partRepository.findByNumeroParteAndVersion(numeroParte, "actual")
                .orElseThrow(() -> new IllegalArgumentException(
                        "La nueva parte con número " + numeroParte + " no existe."));
        currentPart.setActualizacionPendiente(false);
        partRepository.save(currentPart);
    }
    
}
