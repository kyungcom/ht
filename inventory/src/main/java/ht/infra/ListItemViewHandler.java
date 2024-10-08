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
public class ListItemViewHandler {

    //<<< DDD / CQRS
    @Autowired
    private ListItemRepository listItemRepository;
    //>>> DDD / CQRS
}
