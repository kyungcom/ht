package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.util.*;
import lombok.*;

@Data
@ToString
public class PaymentApproved extends AbstractEvent {

    private Long id;
    private String paymentId;
    private String orderId;
    private Integer amount;
    private Boolean status;
}
