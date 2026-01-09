package com.tyrsa.api_erp.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.tyrsa.api_erp.dto.UserResponse;
import com.tyrsa.api_erp.model.CampoActualizado;
import com.tyrsa.api_erp.model.Part;
import com.tyrsa.api_erp.model.PartLog;
import com.tyrsa.api_erp.repository.PartRepository;
import com.tyrsa.api_erp.util.ComparadorListas;
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
    @Autowired
    private ClienteService clienteService;

    @Value("${upload.path}")
    private String uploadDir;

    // Método para registrar usuario
    public void createPart(Part newPart, MultipartFile imageFile, String userGivenName) {
        if (newPart.getNumeroParte() == null || newPart.getNumeroParte().trim().isEmpty()) {
            throw new IllegalArgumentException("El número de parte es obligatorio.");
        }
        if(partRepository.existsByNumeroParte(newPart.getNumeroParte())) {
            throw new RuntimeException("Numero de parte ya existe");
        }

        List<CampoActualizado> cambios = new ArrayList<>();
        
        if (newPart.getNumeroParte() != null && !newPart.getNumeroParte().toString().isBlank())
            cambios.add(new CampoActualizado("Numero de Parte", "-", newPart.getNumeroParte()));

        if (newPart.getProyecto() != null && !newPart.getProyecto().toString().isBlank())
            cambios.add(new CampoActualizado("Proyecto", "-", newPart.getProyecto()));

        if (newPart.getIdCliente() != null && !newPart.getIdCliente().toString().isBlank())
            cambios.add(new CampoActualizado("Cliente", "-", newPart.getNombreCliente()));

        if (newPart.getDescripcion() != null && !newPart.getDescripcion().toString().isBlank())
            cambios.add(new CampoActualizado("Descripcion", "-", newPart.getDescripcion()));

        if (newPart.getNivelIngenieria() != null && !newPart.getNivelIngenieria().toString().isBlank())
            cambios.add(new CampoActualizado("Nivel Ingenieria", "-", newPart.getNivelIngenieria()));

        if (newPart.getFechaInicioProyecto() != null)
            cambios.add(new CampoActualizado("Fecha Inicio Proyecto", "-", newPart.getFechaInicioProyecto().toString()));

        if (newPart.getFechaFinProyecto() != null)
            cambios.add(new CampoActualizado("Fecha Fin Proyecto", "-", newPart.getFechaFinProyecto().toString()));

        if (newPart.getVolumenVendidoProyectoAnual() != null)
            cambios.add(new CampoActualizado("Volumen Vendido Proyecto Anual", "-", newPart.getVolumenVendidoProyectoAnual()));

        if (newPart.getEspecificacionMaterial() != null && !newPart.getEspecificacionMaterial().isBlank())
            cambios.add(new CampoActualizado("Especificacion Material", "-", newPart.getEspecificacionMaterial()));

        if (newPart.getTipoProveedor() != null && !newPart.getTipoProveedor().isBlank())
            cambios.add(new CampoActualizado("Tipo Proveedor", "-", newPart.getTipoProveedor()));

        if (newPart.getNombreProveedor() != null && !newPart.getNombreProveedor().isBlank())
            cambios.add(new CampoActualizado("Nombre Proveedor", "-", newPart.getNombreProveedor()));

        if (newPart.getCodigoIdentificacionMaterial() != null && !newPart.getCodigoIdentificacionMaterial().isBlank())
            cambios.add(new CampoActualizado("Codigo Identificacion Material", "-", newPart.getCodigoIdentificacionMaterial()));

        if (newPart.getIdClasificacionMaterial() != null && !newPart.getIdClasificacionMaterial().isBlank())
            cambios.add(new CampoActualizado("Clasificación de material", "-", newPart.getNombreClasificacionMaterial()));

        if (newPart.getPresentacionMateriaPrima() != null && !newPart.getPresentacionMateriaPrima().isBlank())
            cambios.add(new CampoActualizado("Presentacion Materia Prima", "-", newPart.getPresentacionMateriaPrima()));

        if (newPart.getPesoEstandarPackMP() != null)
            cambios.add(new CampoActualizado("Peso Estandar Pack MP", "-", newPart.getPesoEstandarPackMP()));

        if (newPart.getDiametroInterno() != null)
            cambios.add(new CampoActualizado("Diametro Interno", "-", newPart.getDiametroInterno()));

        if (newPart.getLargoCintaBlank() != null)
            cambios.add(new CampoActualizado("Largo Cinta Blank", "-", newPart.getLargoCintaBlank()));

        if (newPart.getLargoMaterialMaximaTolerancia() != null)
            cambios.add(new CampoActualizado("Largo Material Maxima Tolerancia", "-", newPart.getLargoMaterialMaximaTolerancia()));

        if (newPart.getAnchoCintaBlank() != null)
            cambios.add(new CampoActualizado("Ancho Cinta Blank", "-", newPart.getAnchoCintaBlank()));

        if (newPart.getAnchoMaterialMaximaTolerancia() != null)
            cambios.add(new CampoActualizado("Ancho Material Maxima Tolerancia", "-", newPart.getAnchoMaterialMaximaTolerancia()));

        if (newPart.getEspesor() != null)
            cambios.add(new CampoActualizado("Espesor", "-", newPart.getEspesor()));

        if (newPart.getEspesorMaterialMaximaTolerancia() != null)
            cambios.add(new CampoActualizado("Espesor Material Maxima Tolerancia", "-", newPart.getEspesorMaterialMaximaTolerancia()));

        if (newPart.getCoeficienteMaterial() != null)
            cambios.add(new CampoActualizado("Coeficiente Material", "-", newPart.getCoeficienteMaterial()));

        if (newPart.getPesoBlankMax() != null)
            cambios.add(new CampoActualizado("Peso Blank Max", "-", newPart.getPesoBlankMax()));

        if (newPart.getPesoPiezaTroquelado() != null)
            cambios.add(new CampoActualizado("Peso Pieza Troquelado", "-", newPart.getPesoPiezaTroquelado()));

        if (newPart.getPesoPiezaComponente() != null)
            cambios.add(new CampoActualizado("Peso Pieza Componente", "-", newPart.getPesoPiezaComponente()));

        if (newPart.getFactorConsumo() != null)
            cambios.add(new CampoActualizado("Factor Consumo", "-", newPart.getFactorConsumo()));

        if (newPart.getCodigoEmpaque() != null && !newPart.getCodigoEmpaque().isBlank())
            cambios.add(new CampoActualizado("Codigo Empaque", "-", newPart.getCodigoEmpaque()));

        if (newPart.getFactorConsumoEmpaquePieza() != null)
            cambios.add(new CampoActualizado("Factor Consumo Empaque Pieza", "-", newPart.getFactorConsumoEmpaquePieza()));

        if (newPart.getPiezasPallet() != null)
            cambios.add(new CampoActualizado("Piezas Pallet", "-", newPart.getPiezasPallet()));

        if (newPart.getPersonalRequerido() != null)
            cambios.add(new CampoActualizado("Personal Requerido", "-", newPart.getPersonalRequerido()));

        if (newPart.getTiempoCicloTotal() != null)
            cambios.add(new CampoActualizado("Tiempo Ciclo Total", "-", newPart.getTiempoCicloTotal()));

        if (newPart.getTiempoCicloMaximo() != null)
            cambios.add(new CampoActualizado("Tiempo Ciclo Maximo", "-", newPart.getTiempoCicloMaximo()));

        if (newPart.getWipPorMaquina() != null)
            cambios.add(new CampoActualizado("WIP por Máquina", "-", newPart.getWipPorMaquina()));

        /*if (newPart.getTiempoLlenadoCelula() != null)
            cambios.add(new CampoActualizado("Tiempo Llenado Celula", "-", newPart.getTiempoLlenadoCelula()));*/

        if (newPart.getPiezasPorHora() != null)
            cambios.add(new CampoActualizado("Piezas Por Hora", "-", newPart.getPiezasPorHora()));

        if (newPart.getTiempoTotalCambioModelo() != null)
            cambios.add(new CampoActualizado("Tiempo Total Cambio Modelo", "-", newPart.getTiempoTotalCambioModelo()));

        if (newPart.getTiempoLiberacion() != null)
            cambios.add(new CampoActualizado("Tiempo Liberacion", "-", newPart.getTiempoLiberacion()));

        if (newPart.getTiempoAjustePorFechador() != null)
            cambios.add(new CampoActualizado("Tiempo Ajuste Por Fechador", "-", newPart.getTiempoAjustePorFechador()));

        if (newPart.getPiezasDeAjuste() != null)
            cambios.add(new CampoActualizado("Piezas De Ajuste", "-", newPart.getPiezasDeAjuste()));

        if (newPart.getCantidadEconomicaPedido() != null)
            cambios.add(new CampoActualizado("Cantidad Economica Pedido", "-", newPart.getCantidadEconomicaPedido()));

        if (imageFile != null && !imageFile.isEmpty()) {
            String safeFilename = null;

            try {
                safeFilename = procesarImagen(imageFile);
            } catch (IOException e) {
                throw new RuntimeException("Error al procesar la imagen", e);
            }
            newPart.setFileName(safeFilename);
            cambios.add(new CampoActualizado("Imagen", "-", newPart.getFileName()));
        }

        LocalDateTime  fechaActual = LocalDateTime.now();
        newPart.setFechaActualizacion(fechaActual);
        newPart.setVersion("actual");
        Part saved = partRepository.save(newPart);

        saved.setRootPartId(saved.getId());
        partRepository.save(saved);

        PartLog log = new PartLog();
        log.setFecha(fechaActual);
        log.setEstatus("Pendiente");
        log.setUserName(userGivenName);
        log.setOldPartId("-");
        log.setNewPartId(saved.getId());
        log.setCambios(cambios);
        log.setRootPartId(saved.getRootPartId());
        log.setAprobador("Informacion inicial");
        log.setFechaAprobacion(fechaActual.toLocalDate());
        log.setEstatus("Aprobada");
        logRepository.save(log);

        //Enviar notificación a todos los equipos multidiciplinarios
        List<String> emails = userService.getActiveUsers().stream()
                .map(UserResponse::getEmail)
                .filter(email -> email != null && !email.isEmpty())
                .toList();

        if (!emails.isEmpty()) {
            emailService.newPartAdded(emails.toArray(new String[0]), newPart);
        }
    }


    public void updatePartById(String id, Part updatedPart, MultipartFile imageFile, String userGivenName) {
        Part existente = partRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("La parte con número " + id + " no existe."));

        // Copiar todos los campos del nuevaVersion
        LocalDateTime  fechaActual = LocalDateTime.now();

        List<CampoActualizado> cambios = new ArrayList<>();
        updatedPart.setId(null);
        if (!Objects.equals(updatedPart.getNumeroParte(), existente.getNumeroParte())) {
            cambios.add(new CampoActualizado("Numero de Parte", existente.getNumeroParte(), updatedPart.getNumeroParte()));
        }
        if (!Objects.equals(updatedPart.getProyecto(), existente.getProyecto())) {
            cambios.add(new CampoActualizado("Proyecto", existente.getProyecto(), updatedPart.getProyecto()));
        }
        if (!Objects.equals(updatedPart.getIdCliente(), existente.getIdCliente())) {
            cambios.add(new CampoActualizado("Cliente", existente.getNombreCliente(), updatedPart.getNombreCliente()));
        }
        if (!Objects.equals(updatedPart.getDescripcion(), existente.getDescripcion())) {
            cambios.add(new CampoActualizado("Descripcion", existente.getDescripcion(), updatedPart.getDescripcion()));
        }
        if (!Objects.equals(updatedPart.getNivelIngenieria(), existente.getNivelIngenieria())) {
            cambios.add(new CampoActualizado("Nivel Ingenieria", existente.getNivelIngenieria(), updatedPart.getNivelIngenieria()));
        }
        if (!Objects.equals(updatedPart.getFechaInicioProyecto(), existente.getFechaInicioProyecto())) {
            cambios.add(new CampoActualizado(
                    "Fecha Inicio Proyecto",
                    existente.getFechaInicioProyecto() == null ? "-" : existente.getFechaInicioProyecto().toString(),
                    updatedPart.getFechaInicioProyecto() == null ? "-" : updatedPart.getFechaInicioProyecto().toString()
            ));
        }
        if (!Objects.equals(updatedPart.getFechaFinProyecto(), existente.getFechaFinProyecto())) {
            cambios.add(new CampoActualizado(
                    "Fecha Fin Proyecto",
                    existente.getFechaFinProyecto() == null ? "-" : existente.getFechaFinProyecto().toString(),
                    updatedPart.getFechaFinProyecto() == null ? "-" : updatedPart.getFechaFinProyecto().toString()
            ));
        }
        if (!Objects.equals(updatedPart.getVolumenVendidoProyectoAnual(), existente.getVolumenVendidoProyectoAnual())) {
            cambios.add(new CampoActualizado("Volumen Vendido Proyecto Anual", existente.getVolumenVendidoProyectoAnual(), updatedPart.getVolumenVendidoProyectoAnual()));
        }
        if (!Objects.equals(updatedPart.getEspecificacionMaterial(), existente.getEspecificacionMaterial())) {
            cambios.add(new CampoActualizado("Especificacion Material", existente.getEspecificacionMaterial(), updatedPart.getEspecificacionMaterial()));
        }
        if (!Objects.equals(updatedPart.getTipoProveedor(), existente.getTipoProveedor())) {
            cambios.add(new CampoActualizado("Tipo Proveedor", existente.getTipoProveedor(), updatedPart.getTipoProveedor()));
        }
        if (!Objects.equals(updatedPart.getNombreProveedor(), existente.getNombreProveedor())) {
            cambios.add(new CampoActualizado("Nombre Proveedor", existente.getNombreProveedor(), updatedPart.getNombreProveedor()));
        }
        if (!Objects.equals(updatedPart.getCodigoIdentificacionMaterial(), existente.getCodigoIdentificacionMaterial())) {
            cambios.add(new CampoActualizado("Codigo Identificacion Material", existente.getCodigoIdentificacionMaterial(), updatedPart.getCodigoIdentificacionMaterial()));
        }
        if (!Objects.equals(updatedPart.getIdClasificacionMaterial(), existente.getIdClasificacionMaterial())) {
            cambios.add(new CampoActualizado("Clasificación de material", existente.getNombreClasificacionMaterial(), updatedPart.getNombreClasificacionMaterial()));
        }
        if (!Objects.equals(updatedPart.getPresentacionMateriaPrima(), existente.getPresentacionMateriaPrima())) {
            cambios.add(new CampoActualizado("Presentacion Materia Prima", existente.getPresentacionMateriaPrima(), updatedPart.getPresentacionMateriaPrima()));
        }
        if (!Objects.equals(updatedPart.getPesoEstandarPackMP(), existente.getPesoEstandarPackMP())) {
            cambios.add(new CampoActualizado("Peso Estandar Pack MP", existente.getPesoEstandarPackMP(), updatedPart.getPesoEstandarPackMP()));
        }
        if (!Objects.equals(updatedPart.getDiametroInterno(), existente.getDiametroInterno())) {
            cambios.add(new CampoActualizado("Diametro Interno", existente.getDiametroInterno(), updatedPart.getDiametroInterno()));
        }

        if (!Objects.equals(updatedPart.getLargoCintaBlank(), existente.getLargoCintaBlank())) {
            cambios.add(new CampoActualizado("Largo Cinta Blank", existente.getLargoCintaBlank(), updatedPart.getLargoCintaBlank()));
        }
        if (!Objects.equals(updatedPart.getLargoMaterialMaximaTolerancia(), existente.getLargoMaterialMaximaTolerancia())) {
            cambios.add(new CampoActualizado("Largo Material Maxima Tolerancia", existente.getLargoMaterialMaximaTolerancia(), updatedPart.getLargoMaterialMaximaTolerancia()));
        }
        if (!Objects.equals(updatedPart.getAnchoCintaBlank(), existente.getAnchoCintaBlank())) {
            cambios.add(new CampoActualizado("Ancho Cinta Blank", existente.getAnchoCintaBlank(), updatedPart.getAnchoCintaBlank()));
        }
        if (!Objects.equals(updatedPart.getAnchoMaterialMaximaTolerancia(), existente.getAnchoMaterialMaximaTolerancia())) {
            cambios.add(new CampoActualizado("Ancho Material Maxima Tolerancia", existente.getAnchoMaterialMaximaTolerancia(), updatedPart.getAnchoMaterialMaximaTolerancia()));
        }
        if (!Objects.equals(updatedPart.getEspesor(), existente.getEspesor())) {
            cambios.add(new CampoActualizado("Espesor", existente.getEspesor(), updatedPart.getEspesor()));
        }
        if (!Objects.equals(updatedPart.getEspesorMaterialMaximaTolerancia(), existente.getEspesorMaterialMaximaTolerancia())) {
            cambios.add(new CampoActualizado("Espesor Material Maxima Tolerancia", existente.getEspesorMaterialMaximaTolerancia(), updatedPart.getEspesorMaterialMaximaTolerancia()));
        }

        if (!Objects.equals(updatedPart.getCoeficienteMaterial(), existente.getCoeficienteMaterial())) {
            cambios.add(new CampoActualizado("Coeficiente Material", existente.getCoeficienteMaterial(), updatedPart.getCoeficienteMaterial()));
        }
        if (!Objects.equals(updatedPart.getPesoBlankMax(), existente.getPesoBlankMax())) {
            cambios.add(new CampoActualizado("Peso Blank Max", existente.getPesoBlankMax(), updatedPart.getPesoBlankMax()));
        }
        if (!Objects.equals(updatedPart.getPesoPiezaTroquelado(), existente.getPesoPiezaTroquelado())) {
            cambios.add(new CampoActualizado("Peso Pieza Troquelado", existente.getPesoPiezaTroquelado(), updatedPart.getPesoPiezaTroquelado()));
        }
        if (!Objects.equals(updatedPart.getPesoPiezaComponente(), existente.getPesoPiezaComponente())) {
            cambios.add(new CampoActualizado("Peso Pieza Componente", existente.getPesoPiezaComponente(), updatedPart.getPesoPiezaComponente()));
        }
        if (!Objects.equals(updatedPart.getFactorConsumo(), existente.getFactorConsumo())) {
            cambios.add(new CampoActualizado("Factor Consumo", existente.getFactorConsumo(), updatedPart.getFactorConsumo()));
        }

        if (!Objects.equals(updatedPart.getCodigoEmpaque(), existente.getCodigoEmpaque())) {
            cambios.add(new CampoActualizado("Codigo Empaque", existente.getCodigoEmpaque(), updatedPart.getCodigoEmpaque()));
        }
        if (!Objects.equals(updatedPart.getFactorConsumoEmpaquePieza(), existente.getFactorConsumoEmpaquePieza())) {
            cambios.add(new CampoActualizado("Factor Consumo Empaque Pieza", existente.getFactorConsumoEmpaquePieza(), updatedPart.getFactorConsumoEmpaquePieza()));
        }
        if (!Objects.equals(updatedPart.getPiezasPallet(), existente.getPiezasPallet())) {
            cambios.add(new CampoActualizado("Piezas Pallet", existente.getPiezasPallet(), updatedPart.getPiezasPallet()));
        }

        if (!Objects.equals(updatedPart.getPersonalRequerido(), existente.getPersonalRequerido())) {
            cambios.add(new CampoActualizado("Personal Requerido", existente.getPersonalRequerido(), updatedPart.getPersonalRequerido()));
        }
        if (!Objects.equals(updatedPart.getTiempoCicloTotal(), existente.getTiempoCicloTotal())) {
            cambios.add(new CampoActualizado("Tiempo Ciclo Total", existente.getTiempoCicloTotal(), updatedPart.getTiempoCicloTotal()));
        }
        if (!Objects.equals(updatedPart.getTiempoCicloMaximo(), existente.getTiempoCicloMaximo())) {
            cambios.add(new CampoActualizado("Tiempo Ciclo Maximo", existente.getTiempoCicloMaximo(), updatedPart.getTiempoCicloMaximo()));
        }
        if (!Objects.equals(updatedPart.getWipPorMaquina(), existente.getWipPorMaquina())) {
            cambios.add(new CampoActualizado("WIP por Máquina", existente.getWipPorMaquina(), updatedPart.getWipPorMaquina()));
        }
        /*if (!Objects.equals(updatedPart.getTiempoLlenadoCelula(), existente.getTiempoLlenadoCelula())) {
            cambios.add(new CampoActualizado("Tiempo Llenado Celula", existente.getTiempoLlenadoCelula(), updatedPart.getTiempoLlenadoCelula()));
        }*/
        if (!Objects.equals(updatedPart.getPiezasPorHora(), existente.getPiezasPorHora())) {
            cambios.add(new CampoActualizado("Piezas Por Hora", existente.getPiezasPorHora(), updatedPart.getPiezasPorHora()));
        }
        if (!Objects.equals(updatedPart.getTiempoTotalCambioModelo(), existente.getTiempoTotalCambioModelo())) {
            cambios.add(new CampoActualizado("Tiempo Total Cambio Modelo", existente.getTiempoTotalCambioModelo(), updatedPart.getTiempoTotalCambioModelo()));
        }
        if (!Objects.equals(updatedPart.getTiempoLiberacion(), existente.getTiempoLiberacion())) {
            cambios.add(new CampoActualizado("Tiempo Liberacion", existente.getTiempoLiberacion(), updatedPart.getTiempoLiberacion()));
        }
        if (!Objects.equals(updatedPart.getTiempoAjustePorFechador(), existente.getTiempoAjustePorFechador())) {
            cambios.add(new CampoActualizado("Tiempo Ajuste Por Fechador", existente.getTiempoAjustePorFechador(), updatedPart.getTiempoAjustePorFechador()));
        }
        if (!Objects.equals(updatedPart.getPiezasDeAjuste(), existente.getPiezasDeAjuste())) {
            cambios.add(new CampoActualizado("Piezas De Ajuste", existente.getPiezasDeAjuste(), updatedPart.getPiezasDeAjuste()));
        }
        if (!Objects.equals(updatedPart.getCantidadEconomicaPedido(), existente.getCantidadEconomicaPedido())) {
            cambios.add(new CampoActualizado("Cantidad Economica Pedido", existente.getCantidadEconomicaPedido(), updatedPart.getCantidadEconomicaPedido()));
        }
        if (ComparadorListas.listasDiferentes(existente.getComponentes(), updatedPart.getComponentes())) {
            cambios.add(new CampoActualizado(
                "Componentes",
                existente.getComponentes(),
                updatedPart.getComponentes()
            ));
        }

        if (ComparadorListas.listasDiferentes(updatedPart.getRutas(), existente.getRutas())) {
            cambios.add(new CampoActualizado(
                "Rutas",
                existente.getRutas(),
                updatedPart.getRutas()
            ));
        }

        updatedPart.setVersion("nueva");
        updatedPart.setFechaActualizacion(fechaActual);

        // Procesar imagen si viene una nueva
        if (imageFile != null && !imageFile.isEmpty()) {
            String safeFilename = null;

            try {
                safeFilename = procesarImagen(imageFile);
            } catch (IOException e) {
                throw new RuntimeException("Error al procesar la imagen", e);
            }
            updatedPart.setFileName(safeFilename);
            cambios.add(new CampoActualizado("Imagen", existente.getFileName(), updatedPart.getFileName()));
        }
        
        if (!cambios.isEmpty()){
            existente.setActualizacionPendiente(true);
            partRepository.save(existente);

            updatedPart.setRootPartId(existente.getRootPartId());
            updatedPart = partRepository.save(updatedPart);

            PartLog log = new PartLog();
            log.setFecha(fechaActual);
            log.setEstatus("Pendiente");
            log.setUserName(userGivenName);
            log.setOldPartId(existente.getId());
            log.setNewPartId(updatedPart.getId());
            log.setCambios(cambios);
            log.setRootPartId(existente.getRootPartId());
            logRepository.save(log);

            List<String> emails = userService.getUsersByRole("ROLE_GERENTE_INGENIERIA").stream()
                    .map(UserResponse::getEmail)
                    .filter(email -> email != null && !email.isEmpty())
                    .toList();

            if (!emails.isEmpty()) {
                emailService.sendPartChageAprobalRequest(emails.toArray(new String[0]), existente.getNumeroParte());
            }
        }
    }

    private String procesarImagen(MultipartFile imageFile) throws IOException {

        String originalFilename = imageFile.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("El nombre del archivo es inválido.");
        }

        Path uploadPath = Paths.get(uploadDir);
        Files.createDirectories(uploadPath);

        String safeFilename = getAvailableFilename(uploadPath, originalFilename);
        Path originalImagePath = uploadPath.resolve(safeFilename);

        Files.copy(imageFile.getInputStream(), originalImagePath);

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

            Path thumbnailPath = thumbsPath.resolve(safeFilename);
            String extension = safeFilename.substring(safeFilename.lastIndexOf('.') + 1);

            ImageIO.write(thumbnail, extension, thumbnailPath.toFile());
        }

        return safeFilename;
    }


    public Part getPartById(String id) {
        return partRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("La parte con id " + id + " no existe o no es la versión actual."));
    }

    public List<PartLog> getLogByRootPartId(String rootPartId) {
        List<PartLog> logs = logRepository.findByRootPartId(rootPartId);

        if (logs.isEmpty()) {
            throw new IllegalArgumentException(
                "La parte con rootPartId " + rootPartId + " no tiene registros en log."
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
        Map<String, String> clientes = clienteService.getClientesMap();
        List<Part> parts = partRepository.findAllByVersionOrderByFechaActualizacionDesc("actual");

        for (Part part : parts) {
            part.setNombreCliente(clientes.get(part.getIdCliente()));
        }

        return parts;
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

        //Enviar notificación a todos los equipos multidiciplinarios
        List<String> emails = userService.getActiveUsers().stream()
                .map(UserResponse::getEmail)
                .filter(email -> email != null && !email.isEmpty())
                .toList();

        if (!emails.isEmpty()) {
            emailService.partUpdated(emails.toArray(new String[0]), newPart);
        }
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

    private String getAvailableFilename(Path uploadPath, String originalFilename) {
        String name = originalFilename;
        String baseName;
        String extension = "";

        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            baseName = originalFilename.substring(0, dotIndex);
            extension = originalFilename.substring(dotIndex); // incluye el punto
        } else {
            baseName = originalFilename;
        }

        Path filePath = uploadPath.resolve(name);
        int counter = 1;

        while (Files.exists(filePath)) {
            name = baseName + "-" + counter + extension;
            filePath = uploadPath.resolve(name);
            counter++;
        }

        return name;
    }

    
}
