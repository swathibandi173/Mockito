package com.swathi.BankingFundTransfer.Controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swathi.BankingFundTransfer.Dto.AccountDto;
import com.swathi.BankingFundTransfer.Dto.BeneficiaryDto;
import com.swathi.BankingFundTransfer.Dto.FundTransferDto;
import com.swathi.BankingFundTransfer.Entity.CustomerCredentials;
import com.swathi.BankingFundTransfer.Exception.UserNameAlreadyExistsException;
import com.swathi.BankingFundTransfer.Service.CustomerService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value ="/customers")
@EnableTransactionManagement
@Slf4j
public class CustomerController {
	
private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
	
	@Autowired
	private CustomerService customerService;
	
    @PostMapping("/")
	public ResponseEntity<Object> customerAccountOpening(@Valid @RequestBody AccountDto request)  throws UserNameAlreadyExistsException
	{
    	logger.info("inside customerAccountOpening");
    	Object saveCustomerDetails = customerService.saveCustomerDetails(request);
		return new ResponseEntity<>(saveCustomerDetails,HttpStatus.OK);
	}
    
    @PostMapping("/{userName}")
	public ResponseEntity<?> saveBeneficiaryDetails(@Valid @RequestBody BeneficiaryDto request,@PathVariable("userName") String userName)  
	{
    	logger.info("inside saveBeneficiaryDetails");
		return new ResponseEntity<>(customerService.saveBeneficiary(request,userName),HttpStatus.OK);
	}
    
    @PostMapping("/checkCredentials")
	public ResponseEntity<?> checkCredentials(@Valid @RequestBody CustomerCredentials custCredentials)  
	{
    	logger.info("inside checkCredentials");
		return new ResponseEntity<>(customerService.checkLoginCredential(custCredentials),HttpStatus.OK);
	}
    @PostMapping("/fundTransfer/{userName}")
   	public ResponseEntity<?> fundTransfer(@Valid @RequestBody FundTransferDto fundTransferDto,@PathVariable("userName") String userName)  
   	{
       	logger.info("inside checkCredentials");
   		return new ResponseEntity<>(customerService.fundTransfer(fundTransferDto,userName),HttpStatus.OK);
   	}
    @GetMapping("/{userName}")
   	public ResponseEntity<?> getCustomerDetails(@PathVariable("userName") String userName)  
   	{
       	logger.info("inside checkCredentials");
   		return new ResponseEntity<>(customerService.getCustomerDetails(userName),HttpStatus.OK);
   	}
    
	
	

}
