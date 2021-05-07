package com.messaging.kafka.consumer.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.messaging.kafka.consumer.model.Driver;

public interface DriverRepository extends MongoRepository<Driver, String> {
    Driver findByUsername(String username);
}
