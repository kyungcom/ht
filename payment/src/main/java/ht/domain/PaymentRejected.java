package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class PaymentRejected extends AbstractEvent {

    private Long id;
    private Integer amount;
    private Boolean status;
    private Long orderId;
    private Long customerId;
    private Long accountId;
    
    public PaymentRejected(Payment aggregate) {
        super(aggregate);
    }

    public PaymentRejected() {
        super();
    }
}
//>>> DDD / Domain Event
