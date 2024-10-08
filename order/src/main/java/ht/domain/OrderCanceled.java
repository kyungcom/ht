package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class OrderCanceled extends AbstractEvent {

    private Long id;
    private String productId;
    private String customerId;
    private Integer qty;
    private String status;

    public OrderCanceled(Order aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.productId = aggregate.getProductId();
        this.customerId = aggregate.getCustomerId();
        this.qty = aggregate.getQty();
        this.status = aggregate.getStatus();
    }

    public OrderCanceled() {
        super();
    }
}
//>>> DDD / Domain Event
