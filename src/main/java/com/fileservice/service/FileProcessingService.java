package com.fileservice.service;

import com.fileservice.dto.JsonDataResponse;
import com.fileservice.parser.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileProcessingService {
    
    private final Map<String, FileData> fileStorage = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public String processFile(MultipartFile file) throws Exception {
        String fileId = UUID.randomUUID().toString();
        String fileName = file.getOriginalFilename();
        String fileType = getFileExtension(fileName);
        
        byte[] fileBytes = file.getBytes();
        List<Map<String, String>> jsonData = parseFile(fileBytes, fileType);
        
        FileData fileData = new FileData();
        fileData.setFileId(fileId);
        fileData.setFileName(fileName);
        fileData.setFileType(fileType);
        fileData.setData(jsonData);
        fileData.setRecordCount(jsonData.size());
        fileData.setProcessedAt(new Date());
        
        fileStorage.put(fileId, fileData);
        
        return fileId;
    }
    
    public JsonDataResponse processFileToJson(MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        String fileType = getFileExtension(fileName);
        
        byte[] fileBytes = file.getBytes();
        List<Map<String, String>> jsonData = parseFile(fileBytes, fileType);
        
        JsonDataResponse response = new JsonDataResponse();
        response.setSuccess(true);
        response.setMessage("File processed successfully");
        response.setFileName(fileName);
        response.setFileType(fileType);
        response.setRecordCount(jsonData.size());
        response.setData(jsonData);
        
        return response;
    }
    
    public FileData getFileData(String fileId) {
        return fileStorage.get(fileId);
    }
    
    public List<String> getAllFileIds() {
        return new ArrayList<>(fileStorage.keySet());
    }
    

    
    private List<Map<String, String>> parseFile(byte[] bytes, String fileType) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        
        switch (fileType.toLowerCase()) {
            case "csv":
                return CSVParser.parse(bis);
            case "txt":
                return TxtParser.parse(bis);
            case "psv":
            case "pipe":
                return PipeParser.parse(bis);
            case "xls":
            case "xlsx":
                return ExcelParser.parse(bis);
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }
    
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1);
    }
    
    public static class FileData {
        private String fileId;
        private String fileName;
        private String fileType;
        private List<Map<String, String>> data;
        private int recordCount;
        private Date processedAt;
        
        // Getters and setters
        public String getFileId() { return fileId; }
        public void setFileId(String fileId) { this.fileId = fileId; }
        
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }
        
        public String getFileType() { return fileType; }
        public void setFileType(String fileType) { this.fileType = fileType; }
        
        public List<Map<String, String>> getData() { return data; }
        public void setData(List<Map<String, String>> data) { this.data = data; }
        
        public int getRecordCount() { return recordCount; }
        public void setRecordCount(int recordCount) { this.recordCount = recordCount; }
        
        public Date getProcessedAt() { return processedAt; }
        public void setProcessedAt(Date processedAt) { this.processedAt = processedAt; }
    }
}