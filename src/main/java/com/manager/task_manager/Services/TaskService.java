package com.manager.task_manager.Services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manager.task_manager.DTO.TaskDTO;
import com.manager.task_manager.Exception.TaskNotFoundException;
import com.manager.task_manager.Model.*;
import com.manager.task_manager.Repositories.SyncQueueRepository;
import com.manager.task_manager.Repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class TaskService {

    private final TaskRepository repository;
    private final SyncQueueRepository syncQueueRepository;
    private final ObjectMapper objectMapper;

    public TaskService(TaskRepository repository, SyncQueueRepository syncQueueRepository, ObjectMapper objectMapper) {
        this.repository = repository;
        this.syncQueueRepository = syncQueueRepository;
        this.objectMapper = objectMapper;
    }

    public List<Task> getAllTasks() {
        return repository.findByIsDeletedFalse();
    }

    public Task getTaskById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with ID: " + id));
    }

    @Transactional
    public Task createTask(TaskDTO dto, boolean enqueueForSync) {
        Task task = new Task();
        if (dto.getId() != null) task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.isCompleted());
        task.setDeleted(dto.isDeleted());
        task.setSyncStatus("pending");
        task.setUpdatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : LocalDateTime.now());
        Task saved = repository.save(task);

        if (enqueueForSync) enqueueSync(saved, SyncOperation.CREATE);
        return saved;
    }

    @Transactional
    public Task updateTask(UUID id, TaskDTO dto, boolean enqueueForSync) {
        Task existing = getTaskById(id);
        if (dto.getTitle() != null) existing.setTitle(dto.getTitle());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        existing.setCompleted(dto.isCompleted());
        existing.setDeleted(dto.isDeleted());
        existing.setSyncStatus("pending");
        existing.setUpdatedAt(dto.getUpdatedAt() != null ? dto.getUpdatedAt() : LocalDateTime.now());
        Task saved = repository.save(existing);

        if (enqueueForSync) enqueueSync(saved, SyncOperation.UPDATE);
        return saved;
    }

    @Transactional
    public void softDelete(UUID id, boolean enqueueForSync) {
        Task existing = getTaskById(id);
        existing.setDeleted(true);
        existing.setSyncStatus("pending");
        existing.setUpdatedAt(LocalDateTime.now());
        repository.save(existing);

        if (enqueueForSync) enqueueSync(existing, SyncOperation.DELETE);
    }

    private void enqueueSync(Task task, SyncOperation op) {
        try {
            SyncQueue q = new SyncQueue();
            q.setClientTaskId(task.getId() != null ? task.getId().toString() : null);
            q.setOperation(op);
            String payload = objectMapper.writeValueAsString(taskToDTO(task));
            q.setPayload(payload);
            q.setAttempts(0);
            syncQueueRepository.save(q);
        } 
        catch (Exception ex) {
            System.err.println("Failed to enqueue sync: " + ex.getMessage());
        }
    }

    private TaskDTO taskToDTO(Task t) {
        TaskDTO dto = new TaskDTO();
        dto.setId(t.getId());
        dto.setTitle(t.getTitle());
        dto.setDescription(t.getDescription());
        dto.setCompleted(t.isCompleted());
        dto.setDeleted(t.isDeleted());
        dto.setSyncStatus(t.getSyncStatus());
        dto.setUpdatedAt(t.getUpdatedAt());
        return dto;
    }
}
