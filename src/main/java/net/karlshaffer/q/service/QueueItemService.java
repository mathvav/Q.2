package net.karlshaffer.q.service;

import net.karlshaffer.q.model.Queue;
import net.karlshaffer.q.model.QueueItem;
import net.karlshaffer.q.model.User;

public interface QueueItemService {
    Iterable<QueueItem> findByServicer(User servicer);
    Iterable<QueueItem> findByQueue(Queue queue);
    Iterable<QueueItem> findAll();
    QueueItem save(QueueItem queueItem);
    QueueItem delete(QueueItem queueItem);
    QueueItem findByQueueAndItemId(Queue queue, Long id);
}
