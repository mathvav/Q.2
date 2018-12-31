package net.karlshaffer.q.service;

import net.karlshaffer.q.model.Queue;
import net.karlshaffer.q.model.QueueItem;
import net.karlshaffer.q.repository.QueueItemRepository;
import net.karlshaffer.q.repository.QueueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueServiceImpl implements QueueService {

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private QueueItemRepository queueItemRepository;

    @Override
    public Queue findByQueueCode(String code) {
        return queueRepository.findByQueueCode(code);
    }

    @Override
    public Queue save(Queue queue) {
        return queueRepository.save(queue);
    }

    @Override
    public Queue delete(Queue queue) {
        queueRepository.delete(queue);
        return queue;
    }
}
