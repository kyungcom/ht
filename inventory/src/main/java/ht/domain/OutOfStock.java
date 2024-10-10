package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class OutOfStock extends AbstractEvent {

    private Long id;
    private String name;
    private Integer stock;
    private Long orderId;

    public OutOfStock(Inventory aggregate) {
        super(aggregate);

        this.id = aggregate.getId();
        this.name = aggregate.getName();
        this.stock = aggregate.getStock();
    }

    public OutOfStock() {
        super();
    }
}
//>>> DDD / Domain Event
