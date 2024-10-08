package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.util.*;
import lombok.*;

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
}
