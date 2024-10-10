package ht.domain;

import ht.domain.OrderPlaced;
import ht.domain.OrderCanceled;
import ht.OrderApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;


@Entity
@Table(name="Order_table")
@Data

//<<< DDD / Aggregate Root
public class Order  {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)

    private Long id;
    private Long productId;
    private Long customerId;
    private Integer qty;
    private String status;
    private String address;
    private Integer price;

    @PostPersist
    public void onPostPersist(){
        OrderPlaced orderPlaced = new OrderPlaced(this);
        orderPlaced.publishAfterCommit();
    }

    @PreRemove
    public void onPreRemove() {
        OrderCanceled orderCancelled = new OrderCanceled(this);
        orderCancelled.publishAfterCommit();
    }

    public static OrderRepository repository(){
        OrderRepository orderRepository = OrderApplication.applicationContext.getBean(OrderRepository.class);
        return orderRepository;
    }

    public void inventory(){
        // ht.external.InventoryQuery inventoryQuery = new ht.external.InventoryQuery();
        // OrderApplication.applicationContext
        //     .getBean(ht.external.Service.class)
        //     .( inventoryQuery);
    }
    
    
    // public void listItem(ListItemCommand listItemCommand){
    public void listItem(){
        // ht.external.ListItemQuery listItemQuery = new ht.external.ListItemQuery();
        // OrderApplication.applicationContext
        //     .getBean(ht.external.InventoryService.class)
        //     .listItem( listItemQuery);
    }
    

//<<< Clean Arch / Port Method
    public static void updateStatus(OutOfStock outOfStock){

        repository().findById(outOfStock.getOrderId()).ifPresent(order->{
            
            order.setStatus("OrderCanceled");
            repository().save(order);

            OrderCanceled orderCancelled = new OrderCanceled(order);
            orderCancelled.publishAfterCommit();
            
         });
        
        //implement business logic here:

        /** Example 1:  new item 
        Order order = new Order();
        repository().save(order);

        */

        /** Example 2:  finding and process
        
        repository().findById(outOfStock.get???()).ifPresent(order->{
            
            order // do something
            repository().save(order);


         });
        */
    }

//>>> Clean Arch / Port Method
//<<< Clean Arch / Port Method
    public static void updateStatus(PaymentRejected paymentRejected){

        repository().findById(paymentRejected.getOrderId()).ifPresent(order->{
            
            order.status = "PaymentRejected"; // do something
            repository().save(order);

            OrderCanceled orderCancelled = new OrderCanceled(order);
            orderCancelled.publishAfterCommit();

         });
        
        //implement business logic here:

        /** Example 1:  new item 
        Order order = new Order();
        repository().save(order);

        */

        /** Example 2:  finding and process
        
        repository().findById(paymentRejected.get???()).ifPresent(order->{
            
            order // do something
            repository().save(order);


         });
        */  
    }
//>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
