package ht.domain;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.azul.crs.client.Inventory;


@FeignClient(name = "inventory", url = "${api.url.inventory}")
public interface InventoryService {
    //아 몰?루 구현예정

    Inventory getInventoryByProductId(String ProductId);

}