package com.moretolearn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.moretolearn.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

}
