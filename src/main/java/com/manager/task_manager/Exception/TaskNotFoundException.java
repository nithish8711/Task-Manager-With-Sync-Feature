package com.manager.task_manager.Exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) { super(message); }
}
