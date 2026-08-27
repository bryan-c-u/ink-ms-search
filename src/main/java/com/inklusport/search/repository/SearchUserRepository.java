package com.inklusport.search.repository;

import com.inklusport.search.model.SearchUser;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SearchUserRepository extends MongoRepository<SearchUser, String> {
}
