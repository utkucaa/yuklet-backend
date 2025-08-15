package com.project.yuklet.controller;

import com.project.yuklet.dto.ApiResponse;
import com.project.yuklet.entities.FileType;
import com.project.yuklet.entities.FileUpload;
import com.project.yuklet.services.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileUploadController {
    
    @Autowired
    private FileUploadService fileUploadService;
    
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<FileUpload>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileType") FileType fileType,
            @RequestParam("entityId") Long entityId) {
        
        FileUpload fileUpload = fileUploadService.uploadFile(file, fileType, entityId);
        return ResponseEntity.ok(ApiResponse.success("File uploaded successfully", fileUpload));
    }
    
    @GetMapping("/entity/{entityId}")
    public ResponseEntity<ApiResponse<List<FileUpload>>> getEntityFiles(
            @PathVariable Long entityId,
            @RequestParam FileType fileType) {
        
        List<FileUpload> files = fileUploadService.getEntityFiles(entityId, fileType);
        return ResponseEntity.ok(ApiResponse.success(files));
    }
    
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<FileUpload>>> getUserFiles() {
        List<FileUpload> files = fileUploadService.getUserFiles();
        return ResponseEntity.ok(ApiResponse.success(files));
    }
    
    @GetMapping("/{fileId}")
    public ResponseEntity<ApiResponse<FileUpload>> getFileInfo(@PathVariable Long fileId) {
        FileUpload file = fileUploadService.getFile(fileId);
        return ResponseEntity.ok(ApiResponse.success(file));
    }
    
    @GetMapping("/{fileId}/download")
    public ResponseEntity<ByteArrayResource> downloadFile(@PathVariable Long fileId) {
        FileUpload fileUpload = fileUploadService.getFile(fileId);
        byte[] fileContent = fileUploadService.getFileContent(fileId);
        
        ByteArrayResource resource = new ByteArrayResource(fileContent);
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileUpload.getOriginalName() + "\"")
                .contentType(MediaType.parseMediaType(fileUpload.getContentType()))
                .contentLength(fileUpload.getFileSize())
                .body(resource);
    }
    
    @DeleteMapping("/{fileId}")
    public ResponseEntity<ApiResponse<String>> deleteFile(@PathVariable Long fileId) {
        fileUploadService.deleteFile(fileId);
        return ResponseEntity.ok(ApiResponse.success("File deleted successfully", null));
    }
}
