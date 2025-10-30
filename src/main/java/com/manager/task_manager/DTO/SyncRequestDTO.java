package com.manager.task_manager.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class SyncRequestDTO {
    private List<SyncBatchItemDTO> items;
    private LocalDateTime clientTimestamp;

    // getters / setters
    public List<SyncBatchItemDTO> getItems() { return items; }
    public void setItems(List<SyncBatchItemDTO> items) { this.items = items; }
    public LocalDateTime getClientTimestamp() { return clientTimestamp; }
    public void setClientTimestamp(LocalDateTime clientTimestamp) { this.clientTimestamp = clientTimestamp; }
}
