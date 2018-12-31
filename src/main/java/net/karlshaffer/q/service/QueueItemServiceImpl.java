package net.karlshaffer.q.service;

import net.karlshaffer.q.model.Queue;
import net.karlshaffer.q.model.QueueItem;
import net.karlshaffer.q.model.User;
import net.karlshaffer.q.repository.QueueItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QueueItemServiceImpl implements QueueItemService {

    @Autowired
    private QueueItemRepository queueItemRepository;

    @Override
    public Iterable<QueueItem> findByServicer(User servicer) {
        return queueItemRepository.findAllByServicer(servicer);
    }

    @Override
    public Iterable<QueueItem> findByQueue(Queue queue) {
        return queueItemRepository.findAllByQueue(queue);
    }

    @Override
    public Iterable<QueueItem> findAll() {
        return queueItemRepository.findAll();
    }

    @Override
    public QueueItem save(QueueItem queueItem) {
        return queueItemRepository.save(queueItem);
    }

    @Override
    public QueueItem delete(QueueItem queueItem) {
        queueItemRepository.delete(queueItem);
        return queueItem;
    }
}
