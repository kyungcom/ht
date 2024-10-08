package ht.domain;

import ht.domain.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

//<<< PoEAA / Repository
@RepositoryRestResource(collectionResourceRel = "deadlines", path = "deadlines")
public interface DeadlineRepository
    extends PagingAndSortingRepository<Deadline, Long> {}
