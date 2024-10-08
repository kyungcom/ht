package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class Payment extends AbstractEvent {

    private Long id;

    public Payment() {
        super();
    }
}
//>>> DDD / Domain Event
