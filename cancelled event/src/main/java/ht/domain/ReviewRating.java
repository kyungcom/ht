package ht.domain;

import ht.domain.*;
import ht.infra.AbstractEvent;
import java.time.LocalDate;
import java.util.*;
import lombok.*;

//<<< DDD / Domain Event
@Data
@ToString
public class ReviewRating extends AbstractEvent {

    private Long id;

    public ReviewRating() {
        super();
    }
}
//>>> DDD / Domain Event
