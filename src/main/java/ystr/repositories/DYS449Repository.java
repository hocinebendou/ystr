package ystr.repositories;


import org.springframework.data.repository.PagingAndSortingRepository;
import ystr.domain.DYS449;

import java.util.Collection;

public interface DYS449Repository extends PagingAndSortingRepository<DYS449, Long> {
    Collection<DYS449> findAll();
}
