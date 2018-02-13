package ystr.repositories;


import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import ystr.domain.DYS626;

import java.util.Collection;

public interface DYS626Repository extends PagingAndSortingRepository<DYS626, Long> {
    Collection<DYS626> findAll();
    Collection<DYS626> findAll(Sort sort, int i);
}