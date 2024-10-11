package ht.infra;

import ht.domain.*;
import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "views", path = "views")
public interface ViewRepository
    extends PagingAndSortingRepository<View, Long> {
        Optional<View> findByOrderId(Long orderId);
    }
