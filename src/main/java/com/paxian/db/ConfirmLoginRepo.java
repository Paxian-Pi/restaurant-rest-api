package com.paxian.db;

import com.paxian.model.ConfirmLoginModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmLoginRepo extends MongoRepository<ConfirmLoginModel, String> { }
