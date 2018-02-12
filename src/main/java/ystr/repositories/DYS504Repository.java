package ystr.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ystr.domain.DYS504;

import java.util.Collection;


public interface DYS504Repository extends PagingAndSortingRepository<DYS504, Long> {
    Collection<DYS504> findAll();
}
