package net.karlshaffer.q.repository;

import net.karlshaffer.q.model.Queue;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueRepository extends CrudRepository<Queue, Long> {
    Queue findByQueueCode(String queueCode);
}
