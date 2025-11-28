package com.fileservice.controller;

import com.fileservice.dto.FileUploadResponse;
import com.fileservice.dto.JsonDataResponse;
import com.fileservice.service.FileProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@CrossOrigin(origins = "*")
public class FileController {
    
    @Autowired
    private FileProcessingService fileProcessingService;
    
    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(new FileUploadResponse(false, "File is empty", null, 0));
            }
            
            String fileId = fileProcessingService.processFile(file);
            FileProcessingService.FileData fileData = fileProcessingService.getFileData(fileId);
            
            return ResponseEntity.ok(new FileUploadResponse(
                true, 
                "File processed successfully", 
                fileId, 
                fileData.getRecordCount()
            ));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new FileUploadResponse(false, "Error processing file: " + e.getMessage(), null, 0));
        }
    }
    
    @GetMapping("/data/{fileId}")
    public ResponseEntity<JsonDataResponse> getJsonData(@PathVariable String fileId) {
        try {
            FileProcessingService.FileData fileData = fileProcessingService.getFileData(fileId);
            
            if (fileData == null) {
                return ResponseEntity.notFound().build();
            }
            
            JsonDataResponse response = new JsonDataResponse();
            response.setSuccess(true);
            response.setMessage("Data retrieved successfully");
            response.setFileId(fileData.getFileId());
            response.setFileName(fileData.getFileName());
            response.setFileType(fileData.getFileType());
            response.setRecordCount(fileData.getRecordCount());
            response.setData(fileData.getData());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new JsonDataResponse(false, "Error retrieving data: " + e.getMessage()));
        }
    }
    
    @GetMapping("/list")
    public ResponseEntity<List<String>> listFiles() {
        try {
            List<String> fileIds = fileProcessingService.getAllFileIds();
            return ResponseEntity.ok(fileIds);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    

}