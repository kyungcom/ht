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
    InventoryRepository inventoryRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString) {}

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OrderPlaced'"
    )
    public void wheneverOrderPlaced_DecreaseInventory(
        @Payload OrderPlaced orderPlaced
    ) {
        OrderPlaced event = orderPlaced;
        System.out.println(
            "\n\n##### listener DecreaseInventory : " + orderPlaced + "\n\n"
        );

        // 재고 감소 로직
        // inventoryRepository.findByProductId(event.getProductId()).ifPresent(inventory -> {
            // 재고가 0 이하인 경우 OutOfStock 이벤트 발생
        //     if (inventory.getStock() <= 0) {
        //         OutOfStock outOfStock = new OutOfStock();
        //         outOfStock.setId(inventory.getId());
        //         outOfStock.setName(inventory.getName());
        //         outOfStock.setStock(inventory.getStock());
        //         outOfStock.publish();

        //         System.out.println(
        //             "\n\n##### OutOfStock Event Published : " + outOfStock + "\n\n"
        //         );
        //     }
        //     else{
        //         inventory.setStock(inventory.getStock() - event.getQty());
        //         inventoryRepository.save(inventory);
        //     }
        // });
        Inventory.decreaseInventory(event);
    }

    @StreamListener(
        value = KafkaProcessor.INPUT,
        condition = "headers['type']=='OrderCanceled'"
    )
    public void wheneverOrderCanceled_IncreaseInventory(
        @Payload OrderCanceled orderCanceled
    ) {
        OrderCanceled event = orderCanceled;
        System.out.println(
            "\n\n##### listener IncreaseInventory : " + orderCanceled + "\n\n"
        );

         // 재고 증가 로직 실행
        //  inventoryRepository.findByProductId(event.getProductId()).ifPresent(inventory -> {
        //     inventory.setStock(inventory.getStock() + event.getQty());
        //     inventoryRepository.save(inventory);
        // });

        // Sample Logic //
        Inventory.increaseInventory(event);
    }
}
//>>> Clean Arch / Inbound Adaptor
