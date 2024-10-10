package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ProductAdded extends AbstractEvent {

    private Long id;
    private String name;
    private Integer stock;

    public ProductAdded(Inventory aggregate) {
        super(aggregate);
    }

    public ProductAdded() {
        super();
    }
}
//>>> DDD / Domain Event
