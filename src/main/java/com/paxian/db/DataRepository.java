package com.paxian.db;

import com.paxian.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRepository extends MongoRepository<UserModel, String> {
    UserModel findByUsername(String username);
}
