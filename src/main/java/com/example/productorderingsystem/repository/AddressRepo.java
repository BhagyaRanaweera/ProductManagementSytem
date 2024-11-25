package com.example.productorderingsystem.repository;

import com.example.productorderingsystem.entity.Address;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddressRepo extends MongoRepository<Address,String> {
}