package net.karlshaffer.q.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Queue {

    public static final int DEFAULT_QUEUE_ID_LENGTH = 6;

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String queueCode;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @NotNull
    private User owner;

    @OneToMany(mappedBy = "queue", fetch = FetchType.EAGER)
    private List<QueueItem> queueItems;

    @Column
    @NotNull
    private Date creationDate;

}
