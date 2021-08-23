package com.swathi.BankingFundTransfer.Dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class FundTransferDto {
	 @Size(min = 5, max = 13)
	 @NotNull(message="provide from account no ,only digits")
	 @Pattern(regexp = "[0-9]{13}",message = "provide a valid from account no") 
	private String fromAccountNo;
	 
	 @Size(min = 5, max = 13)
	 @NotNull(message="provide to account no ,only digits")
	 @Pattern(regexp = "[0-9]{13}",message = "provide a valid to account no") 
	private String toAccountNo;
	
	 @NotNull(message="provide transfer amount")
	 @Pattern(regexp = "[0-9.#]+",message = "provide valid Transfer amount")
	 private String transferAmount;
	 
	 private String remarks;

	public FundTransferDto() {
		super();
	}

	public FundTransferDto(String fromAccountNo, String toAccountNo, String transferAmount, String remarks) {
		super();
		this.fromAccountNo = fromAccountNo;
		this.toAccountNo = toAccountNo;
		this.transferAmount = transferAmount;
		this.remarks = remarks;
	}

	public String getFromAccountNo() {
		return fromAccountNo;
	}

	public void setFromAccountNo(String fromAccountNo) {
		this.fromAccountNo = fromAccountNo;
	}

	public String getToAccountNo() {
		return toAccountNo;
	}

	public void setToAccountNo(String toAccountNo) {
		this.toAccountNo = toAccountNo;
	}

	public String getTransferAmount() {
		return transferAmount;
	}

	public void setTransferAmount(String transferAmount) {
		this.transferAmount = transferAmount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String toString() {
		return "FundTransferDto [fromAccountNo=" + fromAccountNo + ", toAccountNo=" + toAccountNo + ", transferAmount="
				+ transferAmount + ", remarks=" + remarks + "]";
	}
	 
	 

}
