package com.inklusport.search.repository;

import com.inklusport.search.model.SearchDisability;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SearchDisabilityRepository extends MongoRepository<SearchDisability, String> {
}
