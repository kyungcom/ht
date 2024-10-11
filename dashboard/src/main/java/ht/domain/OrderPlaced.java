package ht.domain;

import ht.infra.AbstractEvent;
import lombok.Data;

@Data
public class OrderPlaced extends AbstractEvent {

    private Long id;
    private Long productId;
    private Long customerId;
    private Integer qty;
    private String status;
    private String address;
    private Integer price;
}
