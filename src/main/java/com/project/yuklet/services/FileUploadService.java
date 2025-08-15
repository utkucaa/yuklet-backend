package com.project.yuklet.services;

import com.project.yuklet.entities.FileType;
import com.project.yuklet.entities.FileUpload;
import com.project.yuklet.entities.User;
import com.project.yuklet.exception.BadRequestException;
import com.project.yuklet.exception.ResourceNotFoundException;
import com.project.yuklet.reporsitory.FileUploadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.UUID;

@Service
public class FileUploadService {
    
    @Value("${file.upload.dir:uploads}")
    private String uploadDir;
    
    @Autowired
    private FileUploadRepository fileUploadRepository;
    
    @Autowired
    private UserService userService;
    
    private final List<String> allowedImageTypes = List.of("image/jpeg", "image/jpg", "image/png", "image/gif");
    private final List<String> allowedDocumentTypes = List.of("application/pdf", "application/msword", 
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    
    public FileUpload uploadFile(MultipartFile file, FileType fileType, Long entityId) {
        User currentUser = userService.getCurrentUser();
        
        if (file.isEmpty()) {
            throw new BadRequestException("File is empty");
        }
        
        validateFileType(file, fileType);
        
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new BadRequestException("File size cannot exceed 10MB");
        }
        
        try {
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            
            Path filePath = uploadPath.resolve(uniqueFileName);
            
            Files.copy(file.getInputStream(), filePath);
            
            FileUpload fileUpload = new FileUpload(
                    originalFileName,
                    uniqueFileName,
                    filePath.toString(),
                    file.getSize(),
                    file.getContentType(),
                    currentUser.getId(),
                    fileType,
                    entityId
            );
            
            return fileUploadRepository.save(fileUpload);
            
        } catch (IOException e) {
            throw new BadRequestException("Failed to upload file: " + e.getMessage());
        }
    }
    
    private void validateFileType(MultipartFile file, FileType fileType) {
        String contentType = file.getContentType();
        
        switch (fileType) {
            case PROFILE_IMAGE:
            case VEHICLE_IMAGE:
            case CARGO_IMAGE:
                if (!allowedImageTypes.contains(contentType)) {
                    throw new BadRequestException("Only image files are allowed for this upload type");
                }
                break;
            case DOCUMENT:
                if (!allowedDocumentTypes.contains(contentType)) {
                    throw new BadRequestException("Only PDF and Word documents are allowed");
                }
                break;
            case MESSAGE_ATTACHMENT:
                if (!allowedImageTypes.contains(contentType) && !allowedDocumentTypes.contains(contentType)) {
                    throw new BadRequestException("File type not supported");
                }
                break;
        }
    }
    
    public List<FileUpload> getEntityFiles(Long entityId, FileType fileType) {
        return fileUploadRepository.findByEntityIdAndFileType(entityId, fileType);
    }
    
    public List<FileUpload> getUserFiles() {
        User currentUser = userService.getCurrentUser();
        return fileUploadRepository.findByUploadedBy(currentUser.getId());
    }
    
    public FileUpload getFile(Long fileId) {
        return fileUploadRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File", "id", fileId));
    }
    
    public void deleteFile(Long fileId) {
        User currentUser = userService.getCurrentUser();
        FileUpload fileUpload = getFile(fileId);
        
        if (!fileUpload.getUploadedBy().equals(currentUser.getId())) {
            throw new BadRequestException("You can only delete your own files");
        }
        
        try {
            Path filePath = Paths.get(fileUpload.getFilePath());
            Files.deleteIfExists(filePath);
                
            fileUploadRepository.delete(fileUpload);
            
        } catch (IOException e) {
            throw new BadRequestException("Failed to delete file: " + e.getMessage());
        }
    }
    
    public byte[] getFileContent(Long fileId) {
        FileUpload fileUpload = getFile(fileId);
        
        try {
            Path filePath = Paths.get(fileUpload.getFilePath());
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new BadRequestException("Failed to read file: " + e.getMessage());
        }
    }
}
