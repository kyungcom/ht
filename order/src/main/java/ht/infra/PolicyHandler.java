package ht.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ht.config.kafka.KafkaProcessor;
import ht.domain.*;
import javax.naming.NameParser;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//<<< Clean Arch / Inbound Adaptor
@Service
@Transactional
public class PolicyHandler {

    @Autowired
    OrderRepository orderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OutOfStock'"
    )
    public void wheneverOutOfStock_UpdateStatus(
        @Payload OutOfStock outOfStock
    ) {
        OutOfStock event = outOfStock;
        System.out.println(
            "\n\n##### listener UpdateStatus : " + outOfStock + "\n\n"
        );

        // Order의 상태를 OutOfStock으로 변경
        // orderRepository.findById(event.getOrderId()).ifPresent(order -> {
        //     order.setStatus("OutOfStock");
        //     orderRepository.save(order);
        // });
                                                                                                                
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='PaymentRejected'"
    )
    public void wheneverPaymentRejected_UpdateStatus(
        @Payload PaymentRejected paymentRejected
    ) {
        PaymentRejected event = paymentRejected;
        System.out.println(
            "\n\n##### listener UpdateStatus : " + paymentRejected + "\n\n"
        );

        // Order의 상태를 PaymentRejected로 변경
        // orderRepository.findById(event.getOrderId()).ifPresent(order -> {
        //     order.setStatus("PaymentRejected");
        //     orderRepository.save(order);
        // });
    }
}
//>>> Clean Arch / Inbound Adaptor
