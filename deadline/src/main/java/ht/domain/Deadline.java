package ht.domain;

import ht.DeadlineApplication;
import ht.domain.DeadlineReached;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Deadline_table")
@Data
//<<< DDD / Aggregate Root
public class Deadline {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Date deadline;

    private Long orderId;

    private Date startedTime;

    @PostPersist
    public void onPostPersist() {
        DeadlineReached deadlineReached = new DeadlineReached(this);
        deadlineReached.publishAfterCommit();
    }

    public static DeadlineRepository repository() {
        DeadlineRepository deadlineRepository = DeadlineApplication.applicationContext.getBean(
            DeadlineRepository.class
        );
        return deadlineRepository;
    }
}
//>>> DDD / Aggregate Root
