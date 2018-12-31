package net.karlshaffer.q.service;

import net.karlshaffer.q.exceptions.WebApplicationException;
import net.karlshaffer.q.model.Queue;
import net.karlshaffer.q.model.QueueItem;
import net.karlshaffer.q.model.User;
import net.karlshaffer.q.repository.QueueItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public QueueItem findByQueueAndItemId(Queue queue, Long id) {
        if (queueItemRepository.existsByQueueAndAndId(queue, id)) {
            Optional<QueueItem> item = queueItemRepository.findById(id);

            // No need to check if the item was found since we've verified that it exists already.
            return item.get();
        } else {
            throw new WebApplicationException("Desired Queue Item either does not exist or is not in that queue.",
                    HttpStatus.BAD_REQUEST);
        }
    }
}
