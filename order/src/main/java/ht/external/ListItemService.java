package ht.external;

// import java.util.Date;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ht.domain.ListItem;

@FeignClient(name = "listitem", url = "${api.url.inventory}")
public interface ListItemService {
    @GetMapping(path = "/listItems")
    public List<ListItem> getListItem();

    @GetMapping(path = "/listItems/{id}")
    public ListItem getListItem(@PathVariable("id") Long id);
}
