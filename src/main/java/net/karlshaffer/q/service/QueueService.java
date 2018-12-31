package net.karlshaffer.q.service;

import net.karlshaffer.q.model.Queue;

public interface QueueService {
    Queue findByQueueCode(String code);
    Queue save(Queue queue);
    Queue delete(Queue queue);
}
