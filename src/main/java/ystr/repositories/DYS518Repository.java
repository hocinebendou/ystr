package ystr.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ystr.domain.DYS518;

import java.util.Collection;

/**
 * Created by hocine on 2018/02/11.
 */
public interface DYS518Repository extends PagingAndSortingRepository<DYS518, Long> {

    Collection<DYS518> findAll();
}
