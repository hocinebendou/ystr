package ystr.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ystr.domain.DYS612;

import java.util.Collection;


public interface DYS612Repository extends PagingAndSortingRepository<DYS612, Long> {
    Collection<DYS612> findAll();
}