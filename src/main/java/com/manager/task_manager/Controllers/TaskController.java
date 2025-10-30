package com.manager.task_manager.Controllers;

import com.manager.task_manager.DTO.TaskDTO;
import com.manager.task_manager.Model.Task;
import com.manager.task_manager.Services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAll() {
        List<Task> tasks = service.getAllTasks();
        List<TaskDTO> dtos = tasks.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getById(@PathVariable UUID id) {
        Task t = service.getTaskById(id);
        return ResponseEntity.ok(toDto(t));
    }

    @PostMapping
    public ResponseEntity<TaskDTO> create(@Validated @RequestBody TaskDTO dto) {
        Task saved = service.createTask(dto, true);
        TaskDTO out = toDto(saved);
        return ResponseEntity.created(URI.create("/api/tasks/" + saved.getId())).body(out);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> update(@PathVariable UUID id, @Validated @RequestBody TaskDTO dto) {
        Task updated = service.updateTask(id, dto, true);
        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.softDelete(id, true);
        return ResponseEntity.noContent().build();
    }

    private TaskDTO toDto(Task t) {
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
