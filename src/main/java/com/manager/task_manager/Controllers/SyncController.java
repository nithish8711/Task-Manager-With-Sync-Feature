package com.manager.task_manager.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.task_manager.DTO.*;
import com.manager.task_manager.Services.SyncService;
import com.manager.task_manager.Services.TaskService;

import com.manager.task_manager.DTO.SyncRequestDTO;
import com.manager.task_manager.DTO.SyncResponseDTO;
import com.manager.task_manager.DTO.SyncProcessedItemDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/sync")
public class SyncController {

    private final SyncService syncService;
    private final TaskService taskService;
    private final ObjectMapper objectMapper;

    public SyncController(SyncService syncService, TaskService taskService, ObjectMapper objectMapper) {
        this.syncService = syncService;
        this.taskService = taskService;
        this.objectMapper = objectMapper;
    }

    @PostMapping
    public ResponseEntity<Map<String,Object>> triggerSync() {
        Map<String,Object> result = syncService.processQueue();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/batch")
    public ResponseEntity<SyncResponseDTO> batchSync(@RequestBody SyncRequestDTO request) {
        List<SyncProcessedItemDTO> processed = new ArrayList<>();

        if (request.getItems() != null) {
            for (SyncBatchItemDTO item : request.getItems()) {
                try {
                    TaskDTO dto = new TaskDTO();
                    if (item.getTaskId() != null) dto.setId(UUID.fromString(item.getTaskId()));
                    if (item.getData() != null) {
                        Object title = item.getData().get("title");
                        Object desc = item.getData().get("description");
                        Object completed = item.getData().get("completed");
                        if (title != null) dto.setTitle(title.toString());
                        if (desc != null) dto.setDescription(desc.toString());
                        if (completed != null) dto.setCompleted(Boolean.parseBoolean(completed.toString()));
                    }
                    dto.setUpdatedAt(item.getCreatedAt() != null ? item.getCreatedAt() : LocalDateTime.now());
                    com.manager.task_manager.Model.Task serverTask = (dto.getId() == null)
                            ? taskService.createTask(dto, true)
                            : taskService.updateTask(dto.getId(), dto, true);

                    SyncProcessedItemDTO processedItem = new SyncProcessedItemDTO();
                    processedItem.setClientId(item.getTaskId());
                    processedItem.setServerId(serverTask.getServerId() != null ? serverTask.getServerId() : serverTask.getId().toString());
                    processedItem.setStatus("success");

                    Map<String,Object> resolved = new HashMap<>();
                    resolved.put("id", processedItem.getServerId());
                    resolved.put("title", serverTask.getTitle());
                    resolved.put("description", serverTask.getDescription());
                    resolved.put("completed", serverTask.isCompleted());
                    resolved.put("created_at", serverTask.getCreatedAt());
                    resolved.put("updated_at", serverTask.getUpdatedAt());
                    processedItem.setResolvedData(resolved);
                    processed.add(processedItem);
                } 
                catch (Exception ex) {
                    SyncProcessedItemDTO processedItem = new SyncProcessedItemDTO();
                    processedItem.setClientId(item.getTaskId());
                    processedItem.setStatus("failed");
                    processed.add(processedItem);
                }
            }
        }

        SyncResponseDTO response = new SyncResponseDTO();
        response.setProcessedItems(processed);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/status")
    public ResponseEntity<SyncStatusDTO> status() {
        SyncStatusDTO dto = new SyncStatusDTO();
        dto.setPendingSyncCount(syncService.getPendingQueueSize());
        dto.setLastSyncTimestamp(LocalDateTime.now()); 
        dto.setOnline(true);
        dto.setSyncQueueSize(syncService.getPendingQueueSize());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String,Object>> health() {
        Map<String,Object> h = new HashMap<>();
        h.put("status", "ok");
        h.put("timestamp", LocalDateTime.now());
        return ResponseEntity.ok(h);
    }
}
