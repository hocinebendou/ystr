package ystr.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import ystr.domain.Stats;

import java.util.List;

public interface StatsRepository extends PagingAndSortingRepository<Stats, Long> {
    List<Stats> findAll();
}
