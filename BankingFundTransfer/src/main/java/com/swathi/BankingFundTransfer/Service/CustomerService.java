package com.swathi.BankingFundTransfer.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;

import com.swathi.BankingFundTransfer.Dto.AccountDto;
import com.swathi.BankingFundTransfer.Dto.BeneficiaryDto;
import com.swathi.BankingFundTransfer.Dto.FundTransferDto;
import com.swathi.BankingFundTransfer.Entity.Account;
import com.swathi.BankingFundTransfer.Entity.Address;
import com.swathi.BankingFundTransfer.Entity.Beneficiary;
import com.swathi.BankingFundTransfer.Entity.Customer;
import com.swathi.BankingFundTransfer.Entity.CustomerCredentials;
import com.swathi.BankingFundTransfer.Entity.Transaction;
import com.swathi.BankingFundTransfer.Exception.InSufficientFundException;
import com.swathi.BankingFundTransfer.Exception.InvalidCredentialsException;
import com.swathi.BankingFundTransfer.Exception.ResourceNotFoundException;
import com.swathi.BankingFundTransfer.Exception.TransactionFailedException;
import com.swathi.BankingFundTransfer.Exception.TransferLimitException;
import com.swathi.BankingFundTransfer.Exception.UserNameAlreadyExistsException;
import com.swathi.BankingFundTransfer.Repository.AccountRepository;
import com.swathi.BankingFundTransfer.Repository.AddressRepository;
import com.swathi.BankingFundTransfer.Repository.BeneficiaryRepository;
import com.swathi.BankingFundTransfer.Repository.CustomerCredentialsRepository;
import com.swathi.BankingFundTransfer.Repository.CustomerRepository;
import com.swathi.BankingFundTransfer.Repository.TransactionRepository;
import com.swathi.BankingFundTransfer.Response.AccountCreationAcknowledgement;
import com.swathi.BankingFundTransfer.Utilities.CustomerDetails;
import com.swathi.BankingFundTransfer.Utilities.Utilities;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerService {
	
	private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
	@Autowired
	private CustomerRepository custRepo;
	@Autowired
	private AddressRepository addrRepo;
	@Autowired
	private AccountRepository accountRepo;
	@Autowired
	private CustomerCredentialsRepository custCredRepo;
	@Autowired
	private BeneficiaryRepository beneficiaryRepo;
	@Autowired
	private TransactionRepository transRepo;
	@Autowired
	private Utilities utilities;


	//@Transactional(rollbackFor = TransactionFailedException.class, isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
	public ResponseEntity<AccountCreationAcknowledgement> saveCustomerDetails(AccountDto request)
			throws UserNameAlreadyExistsException {
		logger.info("inside saveCustomerDetails  method");
		Date date = new Date();
		logger.info("Checking for if User Name exists.");
		if (Optional.ofNullable(custRepo.findByUserName(request.getUserName())).isPresent())
			throw new UserNameAlreadyExistsException("User Name already exists.Please try with another user name");
		Customer cust = new Customer();
		cust.setUserName(request.getUserName());
		cust.setFirstName(request.getFirstName());
		cust.setLastName(request.getLastName());
		cust.setDateOfBirth(LocalDate.parse(request.getDateOfBirth()));
		cust.setGender(request.getGender());
		cust.setMobileNumber(Long.valueOf(request.getMobileNumber()));
		cust.setEmailId(request.getEmailId());
		cust.setAadharCard(request.getAadharCard());
		cust.setPanCard(request.getPanCard());
		cust.setCreationDate(date);
		Address addr = new Address();
		addr.setAddress1(request.getAddress1());
		addr.setAddress2(request.getAddress2());
		addr.setCity(request.getCity());
		addr.setState(request.getState());
		addr.setZipCode(Long.valueOf(request.getZipCode()));
		addrRepo.save(addr);
		cust.setAddress(addr);
		custRepo.save(cust);
		logger.info("saved address and customer entities");
		String custId= Optional.ofNullable(cust.getCustomerId()).isEmpty() ? "0" :""+cust.getCustomerId();
		Account acct = new Account();
		acct.setAccountType(request.getAccountType());
		acct.setOpeningDeposit(Double.valueOf(request.getOpeningDeposit()));
		long accountNo = Long.valueOf((custId+""+utilities.accountNumberGeneration()).substring(0, 12));
		acct.setAccountNo(accountNo);
		acct.setAvailableBalance(Double.valueOf(request.getOpeningDeposit()));
		acct.setCreationDate(date);
		acct.setBankName(request.getBankName());
		acct.setBranchName(request.getBranchName());
		acct.setIfscCode(request.getIfscCode());
		acct.setCustomer(cust);
		accountRepo.save(acct);
		logger.info("saved account entity");
		CustomerCredentials credentials = new CustomerCredentials();
		credentials.setAccountStatus(1);
		credentials.setCustomer(cust);
		credentials.setUserName(cust.getUserName());
		String strPwd  = acct.getAccountId() == null ? "":acct.getAccountId().toString() +UUID.randomUUID().toString().split("-")[1];
		credentials.setPassword(strPwd);
		custCredRepo.save(credentials);
		logger.info("saved customer credentials entity");
		StringBuffer strMsg = new StringBuffer();
		strMsg.append("Account Opened for Customer :: ").append(cust.getFirstName()).append(" " + cust.getLastName());
		strMsg.append("");
		strMsg.append("  Use credentials for Login: ").append("User Name:: " + credentials.getUserName())
				.append(" Password:: " + credentials.getPassword());
		return new ResponseEntity<>(new AccountCreationAcknowledgement(strMsg.toString()), HttpStatus.OK);
	}
	
	public ResponseEntity<String> saveBeneficiary(BeneficiaryDto dto, String userName) {
		logger.info("inside saveBeneficiary  method");
		logger.info("Checking for if customer exists.");
		Customer customer = Optional.ofNullable(custRepo.findByUserName(userName))
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "User Name", userName));
		Beneficiary benificary = new Beneficiary();
		benificary.setBeneficiaryAccountNo(Long.valueOf(dto.getBeneficiaryAccountNo()));
		benificary.setBeneficiaryName(dto.getBeneficiaryName());
		benificary.setIfscCode(dto.getIfscCode());
		benificary.setTransferLimit(Double.valueOf(dto.getTransferLimit()));
		benificary.setCustomer(customer);
		beneficiaryRepo.save(benificary);
		return new ResponseEntity<>("Successfully added Beneficiary", HttpStatus.OK);
	}

	public ResponseEntity<String> checkLoginCredential(CustomerCredentials credentials) {
		logger.info("inside checkLoginCredential method");
		logger.info("Checking for if customer credentials");
		
		if (Optional.ofNullable(
				custCredRepo.findByUserNameAndPassword(credentials.getUserName(), credentials.getPassword()))
				.isEmpty()) {
			throw new InvalidCredentialsException("Authetication Failed! Please provide valid User Name or Password ");
		}
		return new ResponseEntity<>("Authetication Success", HttpStatus.OK);
	}
	
//@Transactional(rollbackFor = TransactionFailedException.class, isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRES_NEW)
	public ResponseEntity<String> fundTransfer(FundTransferDto fundTransDto, String userName) {
		Customer customer = Optional.ofNullable(custRepo.findByUserName(userName))
				.orElseThrow(() -> new ResourceNotFoundException("Customer", "Customer Name", userName));
		// Check is the account number exists, if does not exists return error message
		// else continue .
		Account fromAccDetails = Optional
				.ofNullable(accountRepo.findByAccountNoAndCustomerCustomerId(
						Long.valueOf(fundTransDto.getFromAccountNo()), customer.getCustomerId()))
				.orElseThrow(() -> new ResourceNotFoundException("Account Details", "Account Number",
						fundTransDto.getFromAccountNo()));

		Beneficiary toAccDetails = Optional
				.ofNullable(beneficiaryRepo.findByBeneficiaryAccountNoAndCustomerCustomerId(
						Long.valueOf(fundTransDto.getToAccountNo()), customer.getCustomerId()))
				.orElseThrow(() -> new ResourceNotFoundException("Customers Beneficiary Account Details  ", "Account Number",
						fundTransDto.getToAccountNo()));

		if (Double.valueOf(fundTransDto.getTransferAmount()) > fromAccDetails.getAvailableBalance()) {
			throw new InSufficientFundException(
					"InSufficent balance for the account number::" + fromAccDetails.getAccountNo());
		}

		if (Double.valueOf(fundTransDto.getTransferAmount()) > toAccDetails.getTransferLimit()) {
			throw new TransferLimitException("Transfer Limit Excessed for this benificary account number::"
					+ toAccDetails.getBeneficiaryAccountNo()+" Maximum tranfer Limit: "+toAccDetails.getTransferLimit());
		}

		Date date = new Date();
		Transaction sourceAcc = new Transaction();
		Transaction targetAcc = new Transaction();

		// inserting records to transactions tables for source account transaction
		Timestamp ts = new Timestamp(date.getTime());
		sourceAcc.setAmount(Double.valueOf(fundTransDto.getTransferAmount()));
		sourceAcc.setFromAccount(fromAccDetails.getAccountNo());
		sourceAcc.setTransactionTime(ts);
		sourceAcc.setTransactionType("Debit");
		sourceAcc.setRemarks(fundTransDto.getRemarks());
		transRepo.save(sourceAcc);

		// inserting records to transactions tables for target account transaction
		targetAcc.setAmount(Double.valueOf(fundTransDto.getTransferAmount()));
		targetAcc.setToAccount(toAccDetails.getBeneficiaryAccountNo());
		targetAcc.setTransactionTime(ts);
		targetAcc.setTransactionType("Credit");
		targetAcc.setRemarks(fundTransDto.getRemarks());
		transRepo.save(targetAcc);

		// updating the account details for the given account number
		if (Optional.ofNullable(transRepo).isPresent()) {
			fromAccDetails.setAvailableBalance(
					fromAccDetails.getAvailableBalance() - Double.valueOf(fundTransDto.getTransferAmount()));
			fromAccDetails.setOpeningDeposit(
					fromAccDetails.getOpeningDeposit() - Double.valueOf(fundTransDto.getTransferAmount()));
			accountRepo.save(fromAccDetails);
		}

		return new ResponseEntity<>("Transaction Done Successfully ", HttpStatus.OK);
	}

	public ResponseEntity<CustomerDetails> getCustomerDetails(String userName) {
		logger.info("inside getCustomerDetails method");
		return new ResponseEntity<>(custRepo.findCustomerByUserName(userName), HttpStatus.OK);
	}

}
