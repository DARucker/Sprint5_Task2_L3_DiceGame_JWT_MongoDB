package com.sprint5.task2.fase3.mongo.repository;

import com.sprint5.task2.fase3.mongo.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
}
