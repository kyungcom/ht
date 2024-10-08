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
    DeliveryRepository deliveryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='InventoryDecreased'"
    )
    public void wheneverInventoryDecreased_StartDelivery(
        @Payload InventoryDecreased inventoryDecreased
    ) {
        InventoryDecreased event = inventoryDecreased;
        System.out.println(
            "\n\n##### listener StartDelivery : " + inventoryDecreased + "\n\n"
        );

        // Sample Logic //
        Delivery.startDelivery(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='InventoryIncreased'"
    )
    public void wheneverInventoryIncreased_CancelDelivery(
        @Payload InventoryIncreased inventoryIncreased
    ) {
        InventoryIncreased event = inventoryIncreased;
        System.out.println(
            "\n\n##### listener CancelDelivery : " + inventoryIncreased + "\n\n"
        );

        // Sample Logic //
        Delivery.cancelDelivery(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
