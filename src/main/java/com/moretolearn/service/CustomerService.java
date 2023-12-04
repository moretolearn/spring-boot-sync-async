package com.moretolearn.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.moretolearn.model.CSVOperation;
import com.moretolearn.model.Customer;
import com.moretolearn.repository.CustomerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public List<Customer> saveAllCustomer(List<Customer> customers) {
		return (List<Customer>) customerRepository.saveAll(customers);
	}

	public List<Customer> getCustomer() {
		return (List<Customer>) customerRepository.findAll();
	}

	// [ASYNC METHOD FOR] GET DATA FROM CSV AND SAVE IN DB
	@Async("taskExecutor")
	public CompletableFuture<List<Customer>> saveCustomers(MultipartFile file) {
		List<Customer> customers = null;

		Long start = System.currentTimeMillis();

		// GET DATA FROM CSV AND SAVE IN DB
		customers = saveAllCustomer(CSVOperation.parseCSVFile(file));

		Long end = System.currentTimeMillis();

		log.info("Completation Time : {}", (end - start));

		return CompletableFuture.completedFuture(customers);
	}

	// [NORMAL METHOD FOR] GET ALL CUSTOMERS DATA
	public List<Customer> getCustomers() {
		return getCustomer();
	}

	// [ASYNC METHOD FOR] GET ALL CUSTOMERS DATA
	@Async("taskExecutor")
	public CompletableFuture<List<Customer>> getCustomersUsingAsync() {
		List<Customer> customers = getCustomer();
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // WAIT FOR SOME SECONDS
		return CompletableFuture.completedFuture(customers);
	}
	
	@Async("taskExecutor")
	public CompletableFuture<List<Customer>> getCustomersAsync() {
		return CompletableFuture.completedFuture(getCustomer());
	}
	
	@Async("taskExecutor")
	public void getCustomersAsync1() {
		getCustomer();
		System.out.println("Async");
	}

}