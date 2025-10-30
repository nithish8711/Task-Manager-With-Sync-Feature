package com.manager.task_manager.DTO;

import java.time.LocalDateTime;
import java.util.Map;

public class SyncBatchItemDTO {
    private String id;
    private String taskId;
    private String operation;
    private Map<String, Object> data;
    private LocalDateTime createdAt;
    private int retryCount;

    // getters / setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTaskId() { return taskId; }
    public void setTaskId(String taskId) { this.taskId = taskId; }
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    public Map<String, Object> getData() { return data; }
    public void setData(Map<String, Object> data) { this.data = data; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public int getRetryCount() { return retryCount; }
    public void setRetryCount(int retryCount) { this.retryCount = retryCount; }
}
