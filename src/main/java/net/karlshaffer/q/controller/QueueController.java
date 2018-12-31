package net.karlshaffer.q.controller;

import net.karlshaffer.q.model.Queue;
import net.karlshaffer.q.model.QueueItem;
import net.karlshaffer.q.model.User;
import net.karlshaffer.q.service.QueueItemService;
import net.karlshaffer.q.service.QueueService;
import net.karlshaffer.q.service.UserService;
import net.karlshaffer.q.utility.RandomUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
public class QueueController extends ApiController {

    public static final String QUEUE_RESOURCE_PATH = "/queue";

    private UserService userService;
    private QueueService queueService;
    private QueueItemService queueItemService;

    @Autowired
    public QueueController(UserService userService, QueueService queueService, QueueItemService queueItemService) {
        this.userService = userService;
        this.queueService = queueService;
        this.queueItemService = queueItemService;
    }

    @GetMapping(QUEUE_RESOURCE_PATH)
    @PreAuthorize(value = "isAuthenticated()")
    public Queue newQueue(HttpServletRequest request) {
        String queueCode = RandomUtility.generateRandomAlphabeticString(Queue.DEFAULT_QUEUE_ID_LENGTH);

        User owner = userService.whoami(request);

        Queue newQueue = new Queue();
        newQueue.setQueueCode(queueCode);
        newQueue.setOwner(owner);
        newQueue.setCreationDate(Calendar.getInstance().getTime());
        newQueue.setQueueItems(new ArrayList<>());

        queueService.save(newQueue);

        return newQueue;
    }

    @GetMapping(QUEUE_RESOURCE_PATH + "/{queueCode}")
    public ResponseEntity<Queue> getQueue(@PathVariable String queueCode) {
        Queue queue = queueService.findByQueueCode(queueCode);

        if (queue == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(queue, HttpStatus.OK);
    }

    @DeleteMapping(QUEUE_RESOURCE_PATH + "/{queueCode}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Queue> deleteQueue(HttpServletRequest request, @PathVariable String queueCode) {
        User user = userService.whoami(request);
        Queue queue = queueService.findByQueueCode(queueCode);

        if (queue == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!queue.getOwner().equals(user) && !user.getRole().getName().equals("ROLE_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        queueService.delete(queue);
        return new ResponseEntity<>(queue, HttpStatus.OK);
    }

    /** Queue Item Routes */

    // TODO: Perhaps relocate to different controller.

    @GetMapping(QUEUE_RESOURCE_PATH + "/{queueCode}/items")
    public ResponseEntity<List<QueueItem>> getQueueItemsForQueue(@PathVariable String queueCode) {
        Queue queue = queueService.findByQueueCode(queueCode);

        if (queue == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(queue.getQueueItems(), HttpStatus.OK);
    }

    @PostMapping(QUEUE_RESOURCE_PATH + "/{queueCode}/items")
    public ResponseEntity<Iterable<QueueItem>> addQueueItem(@PathVariable String queueCode, @RequestBody QueueItem newItem) {
        Queue queue = queueService.findByQueueCode(queueCode);

        if (queue == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        newItem.setCreationDate(Calendar.getInstance().getTime());
        newItem.setQueue(queue);

        queueItemService.save(newItem);

        return new ResponseEntity<>(queueItemService.findByQueue(queue), HttpStatus.OK);
    }

    @PutMapping(QUEUE_RESOURCE_PATH + "/{queueCode}/items/{itemId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Iterable<QueueItem>> updateItem(HttpServletRequest request, @PathVariable String queueCode, @PathVariable Long itemId, @RequestBody QueueItem item) {
        Queue queue = queueService.findByQueueCode(queueCode);
        QueueItem oldItem = queueItemService.findByQueueAndItemId(queue, itemId);

        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }

        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }

        if (item.getCompletionDate() != null) {
            oldItem.setCompletionDate(item.getCompletionDate());
        }

        if (item.getServicer() != null) {
            oldItem.setServicer(item.getServicer());
        }

        queueItemService.save(oldItem);

        return new ResponseEntity<>(queueItemService.findByQueue(queue), HttpStatus.OK);
    }

    @PutMapping(QUEUE_RESOURCE_PATH + "/{queueCode}/items/{itemId}/claim")
    @PreAuthorize(value = "isAuthenticated()")
    public ResponseEntity<Iterable<QueueItem>> claimItem(HttpServletRequest request, @PathVariable String queueCode, @PathVariable Long itemId) {
        Queue queue = queueService.findByQueueCode(queueCode);
        QueueItem item = queueItemService.findByQueueAndItemId(queue, itemId);

        User claimaint = userService.whoami(request);
        item.setServicer(claimaint);

        queueItemService.save(item);

        return new ResponseEntity<>(queueItemService.findByQueue(queue), HttpStatus.OK);
    }

    @DeleteMapping(QUEUE_RESOURCE_PATH + "/{queueCode}/items/{itemId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Iterable<QueueItem>> deleteItem(HttpServletRequest request, @PathVariable String queueCode, @PathVariable Long itemId) {
        User user = userService.whoami(request);
        Queue queue = queueService.findByQueueCode(queueCode);

        if (!queue.getOwner().equals(user) && !user.getRole().getName().equals("ROLE_ADMIN")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        QueueItem item = queueItemService.findByQueueAndItemId(queue, itemId);
        queueItemService.delete(item);

        return new ResponseEntity<>(queueItemService.findByQueue(queue), HttpStatus.OK);
    }

}
