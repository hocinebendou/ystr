package ystr.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ystr.domain.DYS385a;

import java.util.Collection;


public interface DYS385aRepository extends PagingAndSortingRepository<DYS385a, Long> {
        Collection<DYS385a> findAll();
}