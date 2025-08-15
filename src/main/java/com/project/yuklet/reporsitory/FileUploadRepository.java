package com.project.yuklet.reporsitory;

import com.project.yuklet.entities.FileUpload;
import com.project.yuklet.entities.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {
    
    List<FileUpload> findByEntityIdAndFileType(Long entityId, FileType fileType);
    
    List<FileUpload> findByUploadedBy(Long uploadedBy);
    
    List<FileUpload> findByFileType(FileType fileType);
    
    void deleteByEntityIdAndFileType(Long entityId, FileType fileType);
}
