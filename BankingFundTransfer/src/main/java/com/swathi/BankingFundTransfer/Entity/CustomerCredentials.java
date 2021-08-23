package com.swathi.BankingFundTransfer.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Customer_Credentials")
public class CustomerCredentials {
	@Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 @Column(name="custCredentials_id")
	 private Long custCredentialsId;
	 
	
	 private String userName;
	 

	 private String password;
	 

	 private int accountStatus;
	 
	 @OneToOne
	 @JoinColumn(name="customer_id")
	 private Customer customer;

	public CustomerCredentials() {
		super();
	}

	public CustomerCredentials(Long custCredentialsId, String userName, String password, int accountStatus,
			Customer customer) {
		super();
		this.custCredentialsId = custCredentialsId;
		this.userName = userName;
		this.password = password;
		this.accountStatus = accountStatus;
		this.customer = customer;
	}

	public Long getCustCredentialsId() {
		return custCredentialsId;
	}

	public void setCustCredentialsId(Long custCredentialsId) {
		this.custCredentialsId = custCredentialsId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(int accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	 
	 

}
