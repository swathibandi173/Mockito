package com.swathi.BankingFundTransfer.Utilities;

import java.util.List;

public interface CustomerDetails {
	public String getUserName();
	public String getFirstName();
	public String getLastName();
	
	public List<Account> getAccountDetails();
	
	interface Account {
      public Long getAccountNo();
      public String getAccountType();
      public  double getAvailableBalance();
      public String getBankName();
      public String getBranchName();
    }
	
	public List<Beneficiary> getBeneficiaryDetails();
	
	interface Beneficiary
	{
		public String getBeneficiaryName();
		public long getBeneficiaryAccountNo();
		public double getTransferLimit();
		
	}
}