package ystr.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import ystr.domain.DYS644;

import java.util.Collection;


public interface DYS644Repository extends PagingAndSortingRepository<DYS644, Long> {
    Collection<DYS644> findAll();
    Collection<DYS644> findAll(Sort sort, int i);
}

