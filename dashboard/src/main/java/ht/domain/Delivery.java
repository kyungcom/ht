package ht.domain;

import javax.persistence.*;

import ht.infra.AbstractEvent;
import lombok.Data;

@Data
public class Delivery extends AbstractEvent {

    @Id
    // @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String address;
    private Integer qty;
    private String status;
    private Long orderId;
    private Long productId;
}
