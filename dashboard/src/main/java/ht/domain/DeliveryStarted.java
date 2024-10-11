package ht.domain;

import ht.infra.AbstractEvent;
import lombok.*;

@Data
public class DeliveryStarted extends AbstractEvent{

    private Long id;
    private Long orderId;
    private Long productId;
    private String address;
    private Integer qty;
}