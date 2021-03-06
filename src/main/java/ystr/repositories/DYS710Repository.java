package ystr.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import ystr.domain.DYS710;

import java.util.Collection;

public interface DYS710Repository extends PagingAndSortingRepository<DYS710, Long> {
    Collection<DYS710> findAll();
    Collection<DYS710> findAll(Sort sort, int i);
}
