package ystr.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ystr.domain.DYS385b;

import java.util.Collection;


public interface DYS385bRepository extends PagingAndSortingRepository<DYS385b, Long> {
    Collection<DYS385b> findAll();
}