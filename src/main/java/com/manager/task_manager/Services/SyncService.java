package com.manager.task_manager.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.task_manager.DTO.TaskDTO;
import com.manager.task_manager.Model.*;
import com.manager.task_manager.Repositories.SyncQueueRepository;
import com.manager.task_manager.Repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SyncService {

    private final SyncQueueRepository queueRepo;
    private final TaskRepository taskRepo;
    private final ObjectMapper objectMapper;
    private final int batchSize;
    private final int maxRetries;

    public SyncService(SyncQueueRepository queueRepo,
                       TaskRepository taskRepo,
                       ObjectMapper objectMapper,
                       @Value("${app.sync.batch-size:50}") int batchSize,
                       @Value("${app.sync.max-retries:3}") int maxRetries) {
        this.queueRepo = queueRepo;
        this.taskRepo = taskRepo;
        this.objectMapper = objectMapper;
        this.batchSize = batchSize;
        this.maxRetries = maxRetries;
    }

    @Transactional
    public Map<String, Object> processQueue() {
        int totalProcessed = 0;
        int totalFailed = 0;
        List<Map<String, Object>> errors = new ArrayList<>();

        boolean more = true;
        while (more) {
            List<SyncQueue> batch = queueRepo.findPending(maxRetries, PageRequest.of(0, batchSize));
            if (batch.isEmpty()) { more = false; break; }

            batch.forEach(q -> { q.setInProgress(true); queueRepo.save(q); });

            for (SyncQueue q : batch) {
                try {
                    processEntry(q);
                    totalProcessed++;
                } 
                catch (Exception ex) {
                    q.setAttempts(q.getAttempts() + 1);
                    q.setLastAttemptAt(LocalDateTime.now());
                    q.setLastError(ex.getMessage());
                    q.setInProgress(false);
                    queueRepo.save(q);

                    totalFailed++;
                    Map<String,Object> err = new HashMap<>();
                    err.put("task_id", q.getClientTaskId());
                    err.put("operation", q.getOperation().name().toLowerCase());
                    err.put("error", ex.getMessage());
                    err.put("timestamp", LocalDateTime.now());
                    errors.add(err);

                    if (q.getAttempts() >= maxRetries) {
                        try {
                            if (q.getClientTaskId() != null) {
                                UUID uid = UUID.fromString(q.getClientTaskId());
                                taskRepo.findById(uid).ifPresent(t -> {
                                    t.setSyncStatus("error");
                                    taskRepo.save(t);
                                });
                            }
                        } 
                        catch (Exception ignore) {}
                    }
                }
            }
        }

        Map<String,Object> out = new HashMap<>();
        out.put("success", totalFailed == 0);
        out.put("synced_items", totalProcessed);
        out.put("failed_items", totalFailed);
        out.put("errors", errors);
        return out;
    }

    @Transactional
    protected void processEntry(SyncQueue q) throws Exception {
        TaskDTO dto = objectMapper.readValue(q.getPayload(), TaskDTO.class);

        UUID clientId = null;
        if (dto.getId() != null) clientId = dto.getId();

        Optional<com.manager.task_manager.Model.Task> existingOpt = Optional.empty();
        if (clientId != null) existingOpt = taskRepo.findById(clientId);

        SyncOperation op = q.getOperation();
        if (op == SyncOperation.CREATE) {
            if (existingOpt.isPresent()) {
                com.manager.task_manager.Model.Task existing = existingOpt.get();
                if (isIncomingNewer(dto.getUpdatedAt(), existing.getUpdatedAt())) {
                    mergeDtoToEntity(dto, existing);
                    existing.setSyncStatus("synced");
                    existing.setLastSyncedAt(LocalDateTime.now());
                } 
                else {
                    existing.setLastSyncedAt(LocalDateTime.now());
                    existing.setSyncStatus("synced");
                    taskRepo.save(existing);
                }
            } 
            else {
                com.manager.task_manager.Model.Task t = dtoToEntity(dto);
                t.setSyncStatus("synced");
                t.setLastSyncedAt(LocalDateTime.now());
                taskRepo.save(t);
            }
        } 
        else if (op == SyncOperation.UPDATE) {
            if (existingOpt.isPresent()) {
                com.manager.task_manager.Model.Task existing = existingOpt.get();
                if (isIncomingNewer(dto.getUpdatedAt(), existing.getUpdatedAt())) {
                    mergeDtoToEntity(dto, existing);
                    existing.setSyncStatus("synced");
                    existing.setLastSyncedAt(LocalDateTime.now());
                    taskRepo.save(existing);
                } 
                else {
                    existing.setLastSyncedAt(LocalDateTime.now());
                    existing.setSyncStatus("synced");
                    taskRepo.save(existing);
                }
            } 
            else {
                com.manager.task_manager.Model.Task t = dtoToEntity(dto);
                t.setSyncStatus("synced");
                t.setLastSyncedAt(LocalDateTime.now());
                taskRepo.save(t);
            }
        } 
        else if (op == SyncOperation.DELETE) {
            if (existingOpt.isPresent()) {
                com.manager.task_manager.Model.Task existing = existingOpt.get();
                if (isIncomingNewer(dto.getUpdatedAt(), existing.getUpdatedAt())) {
                    existing.setDeleted(true);
                    existing.setUpdatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : LocalDateTime.now());
                    existing.setSyncStatus("synced");
                    existing.setLastSyncedAt(LocalDateTime.now());
                    taskRepo.save(existing);
                } 
                else {
                    existing.setLastSyncedAt(LocalDateTime.now());
                    existing.setSyncStatus("synced");
                    taskRepo.save(existing);
                }
            } 
            else {
        
            }
        }

        queueRepo.delete(q);
    }

    private boolean isIncomingNewer(LocalDateTime incoming, LocalDateTime existing) {
        if (incoming == null && existing == null) return false;
        if (incoming == null) return false;
        if (existing == null) return true;
        return incoming.isAfter(existing);
    }

    private com.manager.task_manager.Model.Task dtoToEntity(TaskDTO dto) {
        com.manager.task_manager.Model.Task t = new com.manager.task_manager.Model.Task();
        if (dto.getId() != null) t.setId(dto.getId());
        t.setTitle(dto.getTitle());
        t.setDescription(dto.getDescription());
        t.setCompleted(dto.isCompleted());
        t.setDeleted(dto.isDeleted());
        t.setUpdatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : LocalDateTime.now());
        return t;
    }

    private void mergeDtoToEntity(TaskDTO dto, com.manager.task_manager.Model.Task entity) {
        if (dto.getTitle() != null) entity.setTitle(dto.getTitle());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        entity.setCompleted(dto.isCompleted());
        entity.setDeleted(dto.isDeleted());
        entity.setUpdatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : LocalDateTime.now());
    }

    public long getPendingQueueSize() {
        return queueRepo.countByAttemptsLessThan(maxRetries);
    }
}
