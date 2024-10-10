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
    private Long productId;
    private Long customerId;
    private Integer qty;
    private String status;
    private String address;
    private Integer price;

    public OrderCanceled(Order aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.productId = aggregate.getProductId();
        this.customerId = aggregate.getCustomerId();
        this.qty = aggregate.getQty();
        this.status = aggregate.getStatus();
        this.address = aggregate.getAddress();
        this.price = aggregate.getPrice();
    }

    public OrderCanceled() {
        super();
    }
}
//>>> DDD / Domain Event
