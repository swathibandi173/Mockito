package com.swathi.BankingFundTransfer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swathi.BankingFundTransfer.Entity.Customer;
import com.swathi.BankingFundTransfer.Utilities.CustomerDetails;



@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long>{
	
	public Customer findByUserName(String userName);
	
	public Customer findByCustomerId(long customerId);

	public CustomerDetails findCustomerByUserName(String userName);

	

}
