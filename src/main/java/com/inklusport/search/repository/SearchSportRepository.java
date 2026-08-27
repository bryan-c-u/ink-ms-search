package com.inklusport.search.repository;

import com.inklusport.search.model.SearchSport;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SearchSportRepository extends MongoRepository<SearchSport, String> {
}
