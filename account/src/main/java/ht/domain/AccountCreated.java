package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class AccountCreated extends AbstractEvent {

    private Long id;
    private Integer balance;
    private String customerId;

    public AccountCreated(Account aggregate) {
        super(aggregate);
    }

    public AccountCreated() {
        super();
    }
}
//>>> DDD / Domain Event
