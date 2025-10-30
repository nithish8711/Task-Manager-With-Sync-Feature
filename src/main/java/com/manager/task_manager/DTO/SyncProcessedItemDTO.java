package com.manager.task_manager.DTO;

import java.util.Map;

public class SyncProcessedItemDTO {
    private String clientId;
    private String serverId;
    private String status;
    private Map<String, Object> resolvedData;

    // getters / setters
    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public String getServerId() { return serverId; }
    public void setServerId(String serverId) { this.serverId = serverId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Map<String, Object> getResolvedData() { return resolvedData; }
    public void setResolvedData(Map<String, Object> resolvedData) { this.resolvedData = resolvedData; }
}
