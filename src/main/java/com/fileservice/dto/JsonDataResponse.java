package com.fileservice.dto;

import java.util.List;
import java.util.Map;

public class JsonDataResponse {
    private boolean success;
    private String message;
    private String fileId;
    private String fileName;
    private String fileType;
    private int recordCount;
    private List<Map<String, String>> data;

    public JsonDataResponse() {}

    public JsonDataResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    
    public int getRecordCount() { return recordCount; }
    public void setRecordCount(int recordCount) { this.recordCount = recordCount; }
    
    public List<Map<String, String>> getData() { return data; }
    public void setData(List<Map<String, String>> data) { this.data = data; }
}