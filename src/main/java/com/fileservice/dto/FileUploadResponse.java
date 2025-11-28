package com.fileservice.dto;

public class FileUploadResponse {
    private boolean success;
    private String message;
    private String fileId;
    private int recordCount;

    public FileUploadResponse() {}

    public FileUploadResponse(boolean success, String message, String fileId, int recordCount) {
        this.success = success;
        this.message = message;
        this.fileId = fileId;
        this.recordCount = recordCount;
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
    
    public int getRecordCount() { return recordCount; }
    public void setRecordCount(int recordCount) { this.recordCount = recordCount; }
}