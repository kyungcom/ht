package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class PaymentRejected extends AbstractEvent {

    private Long id;
    private String paymentId;
    private Long orderId;
    private Integer amount;
    private Boolean status;
}
