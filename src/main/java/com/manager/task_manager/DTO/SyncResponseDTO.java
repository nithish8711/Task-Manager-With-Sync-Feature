package com.manager.task_manager.DTO;

import java.util.List;

public class SyncResponseDTO {
    private List<SyncProcessedItemDTO> processedItems;

    // getters / setters
    public List<SyncProcessedItemDTO> getProcessedItems() { 
        return processedItems; 
    }
    public void setProcessedItems(List<SyncProcessedItemDTO> processedItems) { 
        this.processedItems = processedItems; 
    }
}
