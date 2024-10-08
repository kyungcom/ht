package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class Notification extends AbstractEvent {

    private Long id;

    public Notification() {
        super();
    }
}
//>>> DDD / Domain Event
