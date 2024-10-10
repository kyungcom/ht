package ht.domain;

import ht.InventoryApplication;
import ht.domain.InventoryDecreased;
import ht.domain.InventoryIncreased;
import ht.domain.OutOfStock;
import ht.domain.ProductAdded;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Inventory_table")
@Data
//<<< DDD / Aggregate Root
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Integer stock;
    // private Long orderId;

    // @PostPersist
    // public void onPostPersist() {
    //     InventoryDecreased inventoryDecreased = new InventoryDecreased(this);
    //     inventoryDecreased.publishAfterCommit();
    // }

    // @PostUpdate
    // public void onPostUpdate() {
    //     OutOfStock outOfStock = new OutOfStock(this);
    //     outOfStock.publishAfterCommit();
    // }

    // @PreUpdate
    // public void onPreUpdate() {
    //     InventoryIncreased inventoryIncreased = new InventoryIncreased(this);
    //     inventoryIncreased.publishAfterCommit();
    // }

    public static InventoryRepository repository() {
        InventoryRepository inventoryRepository = InventoryApplication.applicationContext.getBean(
            InventoryRepository.class
        );
        return inventoryRepository;
    }

    //<<< Clean Arch / Port Method
    public static void decreaseInventory(OrderPlaced orderPlaced) {

        repository().findById(Long.valueOf(orderPlaced.getProductId())).ifPresent(inventory->{
            if (inventory.getStock() >= orderPlaced.getQty()){
                inventory.setStock(inventory.getStock() - orderPlaced.getQty()); // do something
                repository().save(inventory);

                InventoryDecreased inventoryDecreased = new InventoryDecreased(inventory);
                inventoryDecreased.publishAfterCommit();
            } else {
                OutOfStock outOfStock = new OutOfStock(inventory);
                outOfStock.setOrderId(orderPlaced.getId()); 
                outOfStock.publishAfterCommit();
            }
         });
        // implement business logic here:

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void increaseInventory(OrderCanceled orderCanceled) {

        repository().findById(orderCanceled.getId()).ifPresent(inventory->{
            
            inventory.setStock(inventory.getStock() + orderCanceled.getQty()); // do something
            repository().save(inventory);

            InventoryIncreased inventoryIncreased = new InventoryIncreased(inventory);
            inventoryIncreased.publishAfterCommit();

         });

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
