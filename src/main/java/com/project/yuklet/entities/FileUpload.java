package com.project.yuklet.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "file_uploads")
public class FileUpload {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "original_name")
    private String originalName;
    
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "file_path")
    private String filePath;
    
    @Column(name = "file_size")
    private Long fileSize;
    
    @Column(name = "content_type")
    private String contentType;
    
    @Column(name = "uploaded_by")
    private Long uploadedBy;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "file_type")
    private FileType fileType;
    
    @Column(name = "entity_id")
    private Long entityId;
    
    @Column(name = "upload_date")
    private LocalDateTime uploadDate = LocalDateTime.now();
    
    public FileUpload() {}
    
    public FileUpload(String originalName, String fileName, String filePath, Long fileSize, 
                     String contentType, Long uploadedBy, FileType fileType, Long entityId) {
        this.originalName = originalName;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.contentType = contentType;
        this.uploadedBy = uploadedBy;
        this.fileType = fileType;
        this.entityId = entityId;
    }
        
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getOriginalName() {
        return originalName;
    }
    
    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public Long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public Long getUploadedBy() {
        return uploadedBy;
    }
    
    public void setUploadedBy(Long uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
    
    public FileType getFileType() {
        return fileType;
    }
    
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }
    
    public Long getEntityId() {
        return entityId;
    }
    
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    
    public LocalDateTime getUploadDate() {
        return uploadDate;
    }
    
    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }
}
