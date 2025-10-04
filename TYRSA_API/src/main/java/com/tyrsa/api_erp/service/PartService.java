package com.tyrsa.api_erp.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tyrsa.api_erp.model.Part;
import com.tyrsa.api_erp.repository.PartRepository;

@Service
public class PartService {

    @Autowired
    private PartRepository partRepository;

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

            } catch (IOException e) {
                throw new RuntimeException("Error al guardar la imagen", e);
            }
        }

        return partRepository.save(newPart);
    }


    public Part updatePartByNumeroParte(String numeroParte, Part updatedPart, MultipartFile imageFile) {
        Part existente = partRepository.findByNumeroParte(numeroParte)
            .orElseThrow(() -> new IllegalArgumentException("La parte con número " + numeroParte + " no existe."));

        existente.setProyecto(updatedPart.getProyecto());
        existente.setDescripcion(updatedPart.getDescripcion());
        existente.setNivelIngenieria(updatedPart.getNivelIngenieria());
        existente.setFechaInicioProyecto(updatedPart.getFechaInicioProyecto());
        existente.setFechaFinProyecto(updatedPart.getFechaFinProyecto());
        existente.setVolumenVendidoProyectoAnual(updatedPart.getVolumenVendidoProyectoAnual());
        existente.setEspecificacionMaterial(updatedPart.getEspecificacionMaterial());
        existente.setTipoProveedor(updatedPart.getTipoProveedor());
        existente.setNombreProveedor(updatedPart.getNombreProveedor());
        existente.setCodigoIdentificacionMaterial(updatedPart.getCodigoIdentificacionMaterial());
        existente.setPresentacionMateriaPrima(updatedPart.getPresentacionMateriaPrima());
        existente.setPesoEstandarPackMP(updatedPart.getPesoEstandarPackMP());
        existente.setDiametroInterno(updatedPart.getDiametroInterno());
        
        existente.setLargoCintaBlank(updatedPart.getLargoCintaBlank());
        existente.setLargoMaterialMaximaTolerancia(updatedPart.getLargoMaterialMaximaTolerancia());
        existente.setAnchoCintaBlank(updatedPart.getAnchoCintaBlank());
        existente.setAnchoMaterialMaximaTolerancia(updatedPart.getAnchoMaterialMaximaTolerancia());
        existente.setEspesor(updatedPart.getEspesor());
        existente.setEspesorMaterialMaximaTolerancia(updatedPart.getEspesorMaterialMaximaTolerancia());

        existente.setCoeficienteMaterial(updatedPart.getCoeficienteMaterial());
        existente.setPesoBlank(updatedPart.getPesoBlank());
        existente.setPesoBlankMax(updatedPart.getPesoBlankMax());
        existente.setPesoPiezaTroquelado(updatedPart.getPesoPiezaTroquelado());
        existente.setPesoPiezaComponente(updatedPart.getPesoPiezaComponente());
        existente.setFactorConsumo(updatedPart.getFactorConsumo());
        existente.setFactorAprovechamiento(updatedPart.getFactorAprovechamiento());
        existente.setMerma(updatedPart.getMerma());

        existente.setCodigoEmpaque(updatedPart.getCodigoEmpaque());
        existente.setFactorConsumoEmpaquePieza(updatedPart.getFactorConsumoEmpaquePieza());
        existente.setPiezasPallet(updatedPart.getPiezasPallet());

        existente.setNumeroOperaciones(updatedPart.getNumeroOperaciones());
        existente.setNumeroMaquinas(updatedPart.getNumeroMaquinas());
        existente.setNumeroOperadores(updatedPart.getNumeroOperadores());
        existente.setNumeroAyudantes(updatedPart.getNumeroAyudantes());
        existente.setPersonalRequerido(updatedPart.getPersonalRequerido());
        existente.setTiempoCicloTotal(updatedPart.getTiempoCicloTotal());
        existente.setTiempoCicloMaximo(updatedPart.getTiempoCicloMaximo());
        existente.setTiempoLlenadoCelula(updatedPart.getTiempoLlenadoCelula());
        existente.setPiezasPorHora(updatedPart.getPiezasPorHora());
        existente.setTiempoTotalCambioModelo(updatedPart.getTiempoTotalCambioModelo());
        existente.setTiempoLiberacion(updatedPart.getTiempoLiberacion());
        existente.setTiempoAjustePorFechador(updatedPart.getTiempoAjustePorFechador());
        existente.setPiezasDeAjuste(updatedPart.getPiezasDeAjuste());
        existente.setCantidadEconomicaPedido(updatedPart.getCantidadEconomicaPedido());

        existente.setComponentes(updatedPart.getComponentes());
        existente.setRutas(updatedPart.getRutas());

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

                String safeNumeroParte = existente.getNumeroParte().trim().replaceAll("[^a-zA-Z0-9_-]", "_");
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
                existente.setFileName(fileName);

            } catch (IOException e) {
                throw new RuntimeException("Error al actualizar la imagen", e);
            }
        }

        return partRepository.save(existente);
    }

    public Part getPartByNumeroParte(String numeroParte) {
        return partRepository.findByNumeroParte(numeroParte)
                .orElseThrow(() -> new IllegalArgumentException("La parte con número " + numeroParte + " no existe."));
    }

    public List<Part> getAllParts() {
        return partRepository.findAll();
    }

    
}
