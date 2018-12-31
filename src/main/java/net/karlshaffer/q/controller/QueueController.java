package net.karlshaffer.q.controller;

import net.karlshaffer.q.model.Queue;
import net.karlshaffer.q.model.QueueItem;
import net.karlshaffer.q.model.User;
import net.karlshaffer.q.service.QueueService;
import net.karlshaffer.q.service.UserService;
import net.karlshaffer.q.utility.RandomUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;

@RestController
public class QueueController extends ApiController {

    public static final String QUEUE_RESOURCE_PATH = "/queue";

    private UserService userService;
    private QueueService queueService;

    @Autowired
    public QueueController(UserService userService, QueueService queueService) {
        this.userService = userService;
        this.queueService = queueService;
    }

    @GetMapping(QUEUE_RESOURCE_PATH)
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

    @PostMapping(QUEUE_RESOURCE_PATH + "/{queueCode}/items")
    public ResponseEntity addQueueItem(@PathVariable String queueCode, @RequestBody QueueItem newItem) {
        return null;
    }

}
