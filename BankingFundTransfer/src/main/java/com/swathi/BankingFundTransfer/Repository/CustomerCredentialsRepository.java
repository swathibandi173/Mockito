package com.swathi.BankingFundTransfer.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.swathi.BankingFundTransfer.Entity.CustomerCredentials;

@Repository
public interface CustomerCredentialsRepository extends JpaRepository<CustomerCredentials,Long>{
	
	public String findByPassword(String password);
	public CustomerCredentials findByUserNameAndPassword(String userName,String password);

}
