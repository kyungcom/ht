package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AccountUpdated extends AbstractEvent {

    private Long id;

    public AccountUpdated(Account aggregate) {
        super(aggregate);
    }

    public AccountUpdated() {
        super();
    }
}
//>>> DDD / Domain Event
