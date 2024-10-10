package ht.infra;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ht.config.kafka.KafkaProcessor;
import ht.domain.*;
import javax.naming.NameParser;
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

        // Sample Logic //
        Order.updateStatus(event);
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

        // Sample Logic //
        Order.updateStatus(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
