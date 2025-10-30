package com.manager.task_manager.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String title;

    private String description;
    private boolean completed = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();
    private boolean isDeleted = false;

    @Column(nullable = false)
    private String syncStatus = "pending";

    private String serverId;
    private LocalDateTime lastSyncedAt;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public UUID getId() { 
        return id; 
    }
    public void setId(UUID id) { 
        this.id = id; 
    }
    public String getTitle() { 
        return title; 
    }
    public void setTitle(String title) { 
        this.title = title; 
    }
    public String getDescription() { 
        return description; 
    }
    public void setDescription(String description) { 
        this.description = description; 
    }
    public boolean isCompleted() { 
        return completed; 
    }
    public void setCompleted(boolean completed) { 
        this.completed = completed; 
    }
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    public LocalDateTime getUpdatedAt() { 
        return updatedAt; 
    }
    public boolean isDeleted() { 
        return isDeleted; 
    }
    public void setDeleted(boolean deleted) { 
        isDeleted = deleted; 
    }
    public String getSyncStatus() { 
        return syncStatus; 
    }
    public void setSyncStatus(String syncStatus) { 
        this.syncStatus = syncStatus; 
    }
    public String getServerId() { 
        return serverId; 
    }
    public void setServerId(String serverId) { 
        this.serverId = serverId; 
    }
    public LocalDateTime getLastSyncedAt() { 
        return lastSyncedAt; 
    }
    public void setLastSyncedAt(LocalDateTime lastSyncedAt) { 
        this.lastSyncedAt = lastSyncedAt; 
    }
}
