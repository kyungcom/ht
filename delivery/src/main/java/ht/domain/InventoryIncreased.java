package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class InventoryIncreased extends AbstractEvent {

    private Long id;
    private String name;
    private Integer stock;
}
