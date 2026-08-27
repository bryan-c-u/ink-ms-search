package com.inklusport.search.repository;

import com.inklusport.search.model.SearchLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SearchLogRepository extends MongoRepository<SearchLog, String> {
    Page<SearchLog> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
