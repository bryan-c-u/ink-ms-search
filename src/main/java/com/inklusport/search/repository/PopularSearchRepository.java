package com.inklusport.search.repository;

import com.inklusport.search.model.PopularSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PopularSearchRepository extends MongoRepository<PopularSearch, String> {
    Optional<PopularSearch> findByQuery(String query);
    List<PopularSearch> findAllByOrderByScoreDesc(Pageable pageable);
    List<PopularSearch> findByQueryStartingWithIgnoreCaseOrderByScoreDesc(String prefix, Pageable pageable);
}
