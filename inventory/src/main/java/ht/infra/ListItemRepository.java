package ht.infra;

import ht.domain.*;
import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "listItems", path = "listItems")
public interface ListItemRepository
    extends PagingAndSortingRepository<ListItem, Long> {}
