package ht.infra;

import ht.config.kafka.KafkaProcessor;
import ht.domain.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class ViewViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private ViewRepository viewRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderPlaced_then_CREATE_1(
        @Payload OrderPlaced orderPlaced
    ) {
        try {
            if (!orderPlaced.validate()) return;

            // view 객체 생성
            View view = new View();
            // view 객체에 이벤트의 Value 를 set 함
            view.setOrderId(orderPlaced.getId());
            view.setCustomerId(orderPlaced.getCustomerId());
            view.setProductId(orderPlaced.getProductId());
            view.setOrderStatus("ORDER_PLACED");
            view.setQty(String.valueOf(orderPlaced.getQty()));
            // view 레파지 토리에 save
            viewRepository.save(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //>>> DDD / CQRS

    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryStarted_then_UPDATE_1(@Payload DeliveryStarted deliveryStarted) {
        try {
            if (!deliveryStarted.validate()) return;
            
            Optional<View> viewOptional = viewRepository.findByOrderId(deliveryStarted.getOrderId());

            if (viewOptional.isPresent()) {
                View view = viewOptional.get();
                if ("ORDER_PLACED".equals(view.getOrderStatus())) {
                    view.setDeliveryStatus("DELIVERY_STARTED");
                    // view 레포지토리에 save
                    viewRepository.save(view);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenDeliveryCancelled_then_UPDATE_2(@Payload DeliveryCancelled deliveryCancelled) {
        try {
            if (!deliveryCancelled.validate()) return;

            // view 레포지토리에서 해당 주문의 View 객체 조회
            Optional<View> viewOptional = viewRepository.findByOrderId(deliveryCancelled.getOrderId());
            if (viewOptional.isPresent()) {
                View view = viewOptional.get();
                view.setDeliveryStatus("DELIVERY_CANCELLED");
                // view 레포지토리에 save
                viewRepository.save(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void whenOrderCanceled_then_UPDATE_3(@Payload OrderCanceled orderCanceled) {
        try {
            if (!orderCanceled.validate()) return;

            // view 레포지토리에서 해당 주문의 View 객체 조회
            Optional<View> viewOptional = viewRepository.findByOrderId(orderCanceled.getId());
            if (viewOptional.isPresent()) {
                View view = viewOptional.get();
                view.setOrderStatus("ORDER_CANCELLED");
                // view 레포지토리에 save
                viewRepository.save(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
