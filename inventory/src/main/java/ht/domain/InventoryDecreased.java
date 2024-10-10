package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class InventoryDecreased extends AbstractEvent {

    private Long id;
    private String name;
    private Integer stock;

    public InventoryDecreased(Inventory aggregate) {
        super(aggregate);
    }

    public InventoryDecreased() {
        super();
    }
}
//>>> DDD / Domain Event
