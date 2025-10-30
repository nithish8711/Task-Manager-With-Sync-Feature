package com.manager.task_manager.DTO;

import java.time.LocalDateTime;

public class SyncStatusDTO {
    private long pendingSyncCount;
    private LocalDateTime lastSyncTimestamp;
    private boolean isOnline;
    private long syncQueueSize;

    // getters / setters
    public long getPendingSyncCount() { return pendingSyncCount; }
    public void setPendingSyncCount(long pendingSyncCount) { this.pendingSyncCount = pendingSyncCount; }
    public LocalDateTime getLastSyncTimestamp() { return lastSyncTimestamp; }
    public void setLastSyncTimestamp(LocalDateTime lastSyncTimestamp) { this.lastSyncTimestamp = lastSyncTimestamp; }
    public boolean isOnline() { return isOnline; }
    public void setOnline(boolean online) { isOnline = online; }
    public long getSyncQueueSize() { return syncQueueSize; }
    public void setSyncQueueSize(long syncQueueSize) { this.syncQueueSize = syncQueueSize; }
}
