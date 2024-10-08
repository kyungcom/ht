package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class OrderPlaced extends AbstractEvent {

    private Long id;
    private String productId;
    private String customerId;
    private Integer qty;
    private String status;
    private String address;
}
