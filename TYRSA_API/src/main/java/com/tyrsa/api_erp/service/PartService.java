package com.tyrsa.api_erp.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public void createPart(Part newPart, MultipartFile imageFile) {
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

            } catch (IOException e) {
                throw new RuntimeException("Error al guardar la imagen", e);
            }
        }

        newPart.setFechaActualizacion(LocalDateTime.now());
        newPart.setVersion("actual");
        partRepository.save(newPart);
    }


    public void updatePartById(String id, Part updatedPart, MultipartFile imageFile, UserDetails userDetails) {
        Part existente = partRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("La parte con número " + id + " no existe."));

        int changes = 0;
        // Copiar todos los campos del nuevaVersion
        LocalDateTime  fechaActual = LocalDateTime.now();
        PartLog log = new PartLog();
        log.setFecha(fechaActual);
        log.setEstatus("Pendiente");
        log.setUserName(userDetails.getUsername());
        log.setOldPartId(existente.getId());
        log.setNewPartId(updatedPart.getId());

        List<CampoActualizado> cambios = new ArrayList<>();
        updatedPart.setId(null);
        if (!Objects.equals(updatedPart.getProyecto(), existente.getProyecto())) {
            cambios.add(new CampoActualizado("Proyecto", existente.getProyecto(), updatedPart.getProyecto()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getDescripcion(), existente.getDescripcion())) {
            cambios.add(new CampoActualizado("Descripcion", existente.getDescripcion(), updatedPart.getDescripcion()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getNivelIngenieria(), existente.getNivelIngenieria())) {
            cambios.add(new CampoActualizado("Nivel Ingenieria", existente.getNivelIngenieria(), updatedPart.getNivelIngenieria()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getFechaInicioProyecto(), existente.getFechaInicioProyecto())) {
            cambios.add(new CampoActualizado("Fecha Inicio Proyecto", existente.getFechaInicioProyecto().toString(), updatedPart.getFechaInicioProyecto().toString()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getFechaFinProyecto(), existente.getFechaFinProyecto())) {
            cambios.add(new CampoActualizado("Fecha Fin Proyecto", existente.getFechaFinProyecto().toString(), updatedPart.getFechaFinProyecto().toString()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getVolumenVendidoProyectoAnual(), existente.getVolumenVendidoProyectoAnual())) {
            cambios.add(new CampoActualizado("Volumen Vendido Proyecto Anual", existente.getVolumenVendidoProyectoAnual(), updatedPart.getVolumenVendidoProyectoAnual()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getEspecificacionMaterial(), existente.getEspecificacionMaterial())) {
            cambios.add(new CampoActualizado("Especificacion Material", existente.getEspecificacionMaterial(), updatedPart.getEspecificacionMaterial()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getTipoProveedor(), existente.getTipoProveedor())) {
            cambios.add(new CampoActualizado("Tipo Proveedor", existente.getTipoProveedor(), updatedPart.getTipoProveedor()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getNombreProveedor(), existente.getNombreProveedor())) {
            cambios.add(new CampoActualizado("Nombre Proveedor", existente.getNombreProveedor(), updatedPart.getNombreProveedor()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getCodigoIdentificacionMaterial(), existente.getCodigoIdentificacionMaterial())) {
            cambios.add(new CampoActualizado("Codigo Identificacion Material", existente.getCodigoIdentificacionMaterial(), updatedPart.getCodigoIdentificacionMaterial()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getPresentacionMateriaPrima(), existente.getPresentacionMateriaPrima())) {
            cambios.add(new CampoActualizado("Presentacion Materia Prima", existente.getPresentacionMateriaPrima(), updatedPart.getPresentacionMateriaPrima()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getPesoEstandarPackMP(), existente.getPesoEstandarPackMP())) {
            cambios.add(new CampoActualizado("Peso Estandar Pack MP", existente.getPesoEstandarPackMP(), updatedPart.getPesoEstandarPackMP()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getDiametroInterno(), existente.getDiametroInterno())) {
            cambios.add(new CampoActualizado("Diametro Interno", existente.getDiametroInterno(), updatedPart.getDiametroInterno()));
            changes++;
        }

        if (!Objects.equals(updatedPart.getLargoCintaBlank(), existente.getLargoCintaBlank())) {
            cambios.add(new CampoActualizado("Largo Cinta Blank", existente.getLargoCintaBlank(), updatedPart.getLargoCintaBlank()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getLargoMaterialMaximaTolerancia(), existente.getLargoMaterialMaximaTolerancia())) {
            cambios.add(new CampoActualizado("Largo Material Maxima Tolerancia", existente.getLargoMaterialMaximaTolerancia(), updatedPart.getLargoMaterialMaximaTolerancia()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getAnchoCintaBlank(), existente.getAnchoCintaBlank())) {
            cambios.add(new CampoActualizado("Ancho Cinta Blank", existente.getAnchoCintaBlank(), updatedPart.getAnchoCintaBlank()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getAnchoMaterialMaximaTolerancia(), existente.getAnchoMaterialMaximaTolerancia())) {
            cambios.add(new CampoActualizado("Ancho Material Maxima Tolerancia", existente.getAnchoMaterialMaximaTolerancia(), updatedPart.getAnchoMaterialMaximaTolerancia()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getEspesor(), existente.getEspesor())) {
            cambios.add(new CampoActualizado("Espesor", existente.getEspesor(), updatedPart.getEspesor()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getEspesorMaterialMaximaTolerancia(), existente.getEspesorMaterialMaximaTolerancia())) {
            cambios.add(new CampoActualizado("Espesor Material Maxima Tolerancia", existente.getEspesorMaterialMaximaTolerancia(), updatedPart.getEspesorMaterialMaximaTolerancia()));
            changes++;
        }

        if (!Objects.equals(updatedPart.getCoeficienteMaterial(), existente.getCoeficienteMaterial())) {
            cambios.add(new CampoActualizado("Coeficiente Material", existente.getCoeficienteMaterial(), updatedPart.getCoeficienteMaterial()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getPesoBlankMax(), existente.getPesoBlankMax())) {
            cambios.add(new CampoActualizado("Peso Blank Max", existente.getPesoBlankMax(), updatedPart.getPesoBlankMax()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getPesoPiezaTroquelado(), existente.getPesoPiezaTroquelado())) {
            cambios.add(new CampoActualizado("Peso Pieza Troquelado", existente.getPesoPiezaTroquelado(), updatedPart.getPesoPiezaTroquelado()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getPesoPiezaComponente(), existente.getPesoPiezaComponente())) {
            cambios.add(new CampoActualizado("Peso Pieza Componente", existente.getPesoPiezaComponente(), updatedPart.getPesoPiezaComponente()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getFactorConsumo(), existente.getFactorConsumo())) {
            cambios.add(new CampoActualizado("Factor Consumo", existente.getFactorConsumo(), updatedPart.getFactorConsumo()));
            changes++;
        }

        if (!Objects.equals(updatedPart.getCodigoEmpaque(), existente.getCodigoEmpaque())) {
            cambios.add(new CampoActualizado("Codigo Empaque", existente.getCodigoEmpaque(), updatedPart.getCodigoEmpaque()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getFactorConsumoEmpaquePieza(), existente.getFactorConsumoEmpaquePieza())) {
            cambios.add(new CampoActualizado("Factor Consumo Empaque Pieza", existente.getFactorConsumoEmpaquePieza(), updatedPart.getFactorConsumoEmpaquePieza()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getPiezasPallet(), existente.getPiezasPallet())) {
            cambios.add(new CampoActualizado("Piezas Pallet", existente.getPiezasPallet(), updatedPart.getPiezasPallet()));
            changes++;
        }

        if (!Objects.equals(updatedPart.getNumeroOperaciones(), existente.getNumeroOperaciones())) {
            cambios.add(new CampoActualizado("Numero Operaciones", existente.getNumeroOperaciones(), updatedPart.getNumeroOperaciones()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getNumeroMaquinas(), existente.getNumeroMaquinas())) {
            cambios.add(new CampoActualizado("Numero Maquinas", existente.getNumeroMaquinas(), updatedPart.getNumeroMaquinas()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getNumeroOperadores(), existente.getNumeroOperadores())) {
            cambios.add(new CampoActualizado("Numero Operadores", existente.getNumeroOperadores(), updatedPart.getNumeroOperadores()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getNumeroAyudantes(), existente.getNumeroAyudantes())) {
            cambios.add(new CampoActualizado("Numero Ayudantes", existente.getNumeroAyudantes(), updatedPart.getNumeroAyudantes()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getPersonalRequerido(), existente.getPersonalRequerido())) {
            cambios.add(new CampoActualizado("Personal Requerido", existente.getPersonalRequerido(), updatedPart.getPersonalRequerido()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getTiempoCicloTotal(), existente.getTiempoCicloTotal())) {
            cambios.add(new CampoActualizado("Tiempo Ciclo Total", existente.getTiempoCicloTotal(), updatedPart.getTiempoCicloTotal()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getTiempoCicloMaximo(), existente.getTiempoCicloMaximo())) {
            cambios.add(new CampoActualizado("Tiempo Ciclo Maximo", existente.getTiempoCicloMaximo(), updatedPart.getTiempoCicloMaximo()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getTiempoLlenadoCelula(), existente.getTiempoLlenadoCelula())) {
            cambios.add(new CampoActualizado("Tiempo Llenado Celula", existente.getTiempoLlenadoCelula(), updatedPart.getTiempoLlenadoCelula()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getPiezasPorHora(), existente.getPiezasPorHora())) {
            cambios.add(new CampoActualizado("Piezas Por Hora", existente.getPiezasPorHora(), updatedPart.getPiezasPorHora()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getTiempoTotalCambioModelo(), existente.getTiempoTotalCambioModelo())) {
            cambios.add(new CampoActualizado("Tiempo Total Cambio Modelo", existente.getTiempoTotalCambioModelo(), updatedPart.getTiempoTotalCambioModelo()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getTiempoLiberacion(), existente.getTiempoLiberacion())) {
            cambios.add(new CampoActualizado("Tiempo Liberacion", existente.getTiempoLiberacion(), updatedPart.getTiempoLiberacion()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getTiempoAjustePorFechador(), existente.getTiempoAjustePorFechador())) {
            cambios.add(new CampoActualizado("Tiempo Ajuste Por Fechador", existente.getTiempoAjustePorFechador(), updatedPart.getTiempoAjustePorFechador()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getPiezasDeAjuste(), existente.getPiezasDeAjuste())) {
            cambios.add(new CampoActualizado("Piezas De Ajuste", existente.getPiezasDeAjuste(), updatedPart.getPiezasDeAjuste()));
            changes++;
        }
        if (!Objects.equals(updatedPart.getCantidadEconomicaPedido(), existente.getCantidadEconomicaPedido())) {
            cambios.add(new CampoActualizado("Cantidad Economica Pedido", existente.getCantidadEconomicaPedido(), updatedPart.getCantidadEconomicaPedido()));
            changes++;
        }

        updatedPart.setVersion("nueva");
        updatedPart.setFechaActualizacion(fechaActual);

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

                String safeNumeroParte = updatedPart.getNumeroParte().trim().replaceAll("[^a-zA-Z0-9_-]", "_");
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
                updatedPart.setFileName(fileName);

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
            existente.setActualizacionPendiente(true);
            partRepository.save(existente);
            partRepository.save(updatedPart);
        }
    }

    public Part getPartById(String id) {
        return partRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("La parte con id " + id + " no existe o no es la versión actual."));
    }

    public List<PartLog> getLogByPartId(String id) {
        List<PartLog> logs = logRepository.findByOldPartId(id);

        if (logs.isEmpty()) {
            throw new IllegalArgumentException(
                "La parte con id " + id + " no tiene registros en log."
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

        // Eliminar la versión obsoleta
        Part oldPart = partRepository.findById(log.getOldPartId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "La parte con id " + log.getOldPartId() + " no existe o no es la versión actual."));
        partRepository.delete(oldPart);

        // Actualizar la nueva parte a versión actual
        Part newPart = partRepository.findById(log.getNewPartId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "La nueva parte con id " + log.getNewPartId() + " no existe."));
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

        // Eliminar version actualizada
        Part newPart = partRepository.findById(log.getNewPartId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "La nueva parte con id " + log.getNewPartId() + " no existe."));
        partRepository.delete(newPart);

        //Actualizar parte actual para que no tenga actualizacion pendiente
        Part currentPart = partRepository.findById(log.getOldPartId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "La nueva parte con id " + log.getOldPartId() + " no existe."));
        currentPart.setActualizacionPendiente(false);
        partRepository.save(currentPart);
    }
    
}
