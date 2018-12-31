package net.karlshaffer.q.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
public class QueueItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "queue_id")
    @NotNull
    @JsonIgnore
    private Queue queue;

    private boolean complete;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User servicer;

    private Date creationDate;

    private Date completionDate;

}
