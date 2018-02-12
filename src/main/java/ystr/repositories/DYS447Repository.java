package ystr.repositories;


import org.springframework.data.repository.PagingAndSortingRepository;
import ystr.domain.DYS447;

import java.util.Collection;


public interface DYS447Repository extends PagingAndSortingRepository<DYS447, Long> {
    Collection<DYS447> findAll();
}