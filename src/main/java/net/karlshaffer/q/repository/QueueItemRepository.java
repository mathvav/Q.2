package net.karlshaffer.q.repository;

import net.karlshaffer.q.model.Queue;
import net.karlshaffer.q.model.QueueItem;
import net.karlshaffer.q.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueItemRepository extends CrudRepository<QueueItem, Long> {
    Iterable<QueueItem> findAllByServicer(User servicer);
    Iterable<QueueItem> findAllByQueue(Queue queue);
}
