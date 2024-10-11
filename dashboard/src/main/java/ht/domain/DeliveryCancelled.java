package ht.domain;

import ht.infra.AbstractEvent;
import lombok.*;

@Data
public class DeliveryCancelled extends AbstractEvent{

    private Long id;
    private Long orderId;
    private Long productId;
    private String address;
    private Integer qty;
}