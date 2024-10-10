package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class DeliveryStarted extends AbstractEvent {

    private Long id;
    private Long orderId;
    private Long productId;
    private String address;
    private Integer qty;

    public DeliveryStarted(Delivery aggregate) {
        super(aggregate);

        this.id = aggregate.getId();
        this.orderId = aggregate.getOrderId();
        this.productId = aggregate.getProductId();
        this.address = aggregate.getAddress();
        this.qty = aggregate.getQty();
    }

    public DeliveryStarted() {
        super();
    }
}
//>>> DDD / Domain Event
