package com.manager.task_manager.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sync_queue")
public class SyncQueue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String clientTaskId;

    @Enumerated(EnumType.STRING)
    private SyncOperation operation;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private int attempts = 0;
    private boolean inProgress = false;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime lastAttemptAt;
    @Column(columnDefinition = "TEXT")
    private String lastError;

    // Getters / setters
    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }
    public String getClientTaskId() { 
        return clientTaskId; 
    }
    public void setClientTaskId(String clientTaskId) { 
        this.clientTaskId = clientTaskId; 
    }
    public SyncOperation getOperation() { 
        return operation; 
    }
    public void setOperation(SyncOperation operation) { 
        this.operation = operation; 
    }
    public String getPayload() { 
        return payload; 
    }
    public void setPayload(String payload) { 
        this.payload = payload; 
    }
    public int getAttempts() { 
        return attempts; 
    }
    public void setAttempts(int attempts) { 
        this.attempts = attempts; 
    }
    public boolean isInProgress() { 
        return inProgress; 
    }
    public void setInProgress(boolean inProgress) { 
        this.inProgress = inProgress; 
    }
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }
    public LocalDateTime getLastAttemptAt() { 
        return lastAttemptAt; 
    }
    public void setLastAttemptAt(LocalDateTime lastAttemptAt) { 
        this.lastAttemptAt = lastAttemptAt; 
    }
    public String getLastError() { 
        return lastError; 
    }
    public void setLastError(String lastError) { 
        this.lastError = lastError; 
    }
}
