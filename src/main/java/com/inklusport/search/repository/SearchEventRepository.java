package com.inklusport.search.repository;

import com.inklusport.search.model.SearchEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SearchEventRepository extends MongoRepository<SearchEvent, String> {
}
