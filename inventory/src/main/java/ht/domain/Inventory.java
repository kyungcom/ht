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
    private Long orderId;

    @PostPersist
    public void onPostPersist() {
        // InventoryIncreased inventoryIncreased = new InventoryIncreased(this);
        // inventoryIncreased.publishAfterCommit();
        InventoryDecreased inventoryDecreased = new InventoryDecreased(this);
        inventoryDecreased.publishAfterCommit();

        ProductAdded productAdded = new ProductAdded(this);
        productAdded.publishAfterCommit();
    }

    @PostUpdate
    public void onPostUpdate() {
        OutOfStock outOfStock = new OutOfStock(this);
        outOfStock.publishAfterCommit();
    }

    @PreUpdate
    public void onPreUpdate() {
        // InventoryDecreased inventoryDecreased = new InventoryDecreased(this);
        // inventoryDecreased.publishAfterCommit();

        // OutOfStock outOfStock = new OutOfStock(this);
        // outOfStock.publishAfterCommit();
        InventoryIncreased inventoryIncreased = new InventoryIncreased(this);
        inventoryIncreased.publishAfterCommit();
    }

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

        /** Example 1:  new item 
        Inventory inventory = new Inventory();
        repository().save(inventory);

        InventoryDecreased inventoryDecreased = new InventoryDecreased(inventory);
        inventoryDecreased.publishAfterCommit();
        OutOfStock outOfStock = new OutOfStock(inventory);
        outOfStock.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(orderPlaced.get???()).ifPresent(inventory->{
            
            inventory // do something
            repository().save(inventory);

            InventoryDecreased inventoryDecreased = new InventoryDecreased(inventory);
            inventoryDecreased.publishAfterCommit();
            OutOfStock outOfStock = new OutOfStock(inventory);
            outOfStock.publishAfterCommit();

         });
        */

    }

    //>>> Clean Arch / Port Method
    //<<< Clean Arch / Port Method
    public static void increaseInventory(OrderCanceled orderCanceled) {
        //implement business logic here:

        /** Example 1:  new item 
        Inventory inventory = new Inventory();
        repository().save(inventory);

        InventoryIncreased inventoryIncreased = new InventoryIncreased(inventory);
        inventoryIncreased.publishAfterCommit();
        */

        /** Example 2:  finding and process
        
        repository().findById(orderCanceled.get???()).ifPresent(inventory->{
            
            inventory // do something
            repository().save(inventory);

            InventoryIncreased inventoryIncreased = new InventoryIncreased(inventory);
            inventoryIncreased.publishAfterCommit();

         });
        */

    }
    //>>> Clean Arch / Port Method

}
//>>> DDD / Aggregate Root
