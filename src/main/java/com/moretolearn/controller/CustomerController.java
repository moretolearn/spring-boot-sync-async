package com.moretolearn.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.moretolearn.model.Customer;
import com.moretolearn.service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	// [ASYNC METHOD FOR] GET DATA FROM CSV AND SAVE IN DB
	@PostMapping()
	public ResponseEntity<Object> saveCustomers(@RequestParam("file") MultipartFile files) {
		List<Customer> customers = customerService.getCustomers(); // NORMAL METHOD CALL TO GET ALL CUSTOMERS

		log.info("Customers : ", customers);

		// READ 3000 RECORDS FROM CSV AND SAVE INTO DB -> THROUGH ASYNC METHOD
		customerService.saveCustomers(files);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	// [ASYNC METHOD FOR] GET ALL CUSTOMERS DATA
	@GetMapping()
	public CompletableFuture<Object> getCustomers() {
		return customerService.getCustomersUsingAsync().thenApply(ResponseEntity::ok);
	}

	// [ASYNC METHOD FOR] GET DATA FROM MULTIPLE ASYNC METHODS
	@GetMapping("/multiple")
	public ResponseEntity<Object> getMultipleCustomers() throws InterruptedException, ExecutionException {
		CompletableFuture<List<Customer>> customerList1 = customerService.getCustomersUsingAsync();
		CompletableFuture<List<Customer>> customerList2 = customerService.getCustomersUsingAsync();
		CompletableFuture<List<Customer>> customerList3 = customerService.getCustomersUsingAsync();

		CompletableFuture.allOf(customerList1, customerList2, customerList3).join();

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/async")
	public CompletableFuture<Object> getCustomersAsync() {
		return customerService.getCustomersAsync().thenApply(ResponseEntity::ok);
	}

	@GetMapping("/async1")
	public ResponseEntity<Object> getCustomersAsync1() {
		customerService.getCustomersAsync1();
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
