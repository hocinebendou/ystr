package ystr.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import ystr.domain.DYS481;

import java.util.Collection;


public interface DYS481Repository extends PagingAndSortingRepository<DYS481, Long> {
    Collection<DYS481> findAll();
    Collection<DYS481> findAll(Sort sort, int i);
}