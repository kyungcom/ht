package ht.domain;

import ht.PaymentApplication;
import ht.domain.PaymentApproved;
import ht.domain.PaymentRejected;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Payment_table")
@Data
//<<< DDD / Aggregate Root
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer amount;

    private Boolean status;

    private Long orderId;

    private String customerId;

    @PostPersist
    public void onPostPersist() {
        PaymentApproved paymentApproved = new PaymentApproved(this);
        paymentApproved.publishAfterCommit();
    }

    public static PaymentRepository repository() {
        PaymentRepository paymentRepository = PaymentApplication.applicationContext.getBean(
            PaymentRepository.class
        );
        return paymentRepository;
    }

    //<<< Clean Arch / Port Method
    public static void initiatePayment(OrderPlaced orderPlaced) {
        repository().findById(orderPlaced.getId()).ifPresent(payment -> {

            int price = orderPlaced.getQty() * orderPlaced.getPrice();
            payment.setAmount(price);

            try {
               payment.setStatus(true); // Payment is approved
               repository().save(payment);
               PaymentApproved paymentApproved = new PaymentApproved(payment);
               paymentApproved.publishAfterCommit();
            } catch(Exception e) {
               payment.setStatus(false); // Payment is rejected
               repository().save(payment);
               PaymentRejected paymentRejected = new PaymentRejected(payment);
               paymentRejected.publishAfterCommit();
            }
        });
        //implement business logic here:

        /** Example 1:  new item 
        Payment payment = new Payment();
        repository().save(payment);

        PaymentApproved paymentApproved = new PaymentApproved(payment);
        paymentApproved.publishAfterCommit();
        PaymentRejected paymentRejected = new PaymentRejected(payment);
        paymentRejected.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(orderPlaced.get???()).ifPresent(payment->{
            
            payment // do something
            repository().save(payment);

            PaymentApproved paymentApproved = new PaymentApproved(payment);
            paymentApproved.publishAfterCommit();
            PaymentRejected paymentRejected = new PaymentRejected(payment);
            paymentRejected.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
