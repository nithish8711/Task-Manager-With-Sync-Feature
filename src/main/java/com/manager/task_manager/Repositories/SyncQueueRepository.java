package com.manager.task_manager.Repositories;

import com.manager.task_manager.Model.SyncQueue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SyncQueueRepository extends JpaRepository<SyncQueue, Long> {

    @Query("select s from SyncQueue s where s.attempts < :maxRetries and s.inProgress = false order by s.createdAt asc")
    List<SyncQueue> findPending(int maxRetries, Pageable pageable);

    long countByAttemptsLessThan(int maxRetries);
}
