package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FundTransferRequest {
	
	  @NotNull
	  @NotEmpty
	  private final String fromAccountId;
	  
	  @NotNull
	  @NotEmpty
	  private final String toAccountId;


	  @NotNull
	  @Min(value = 0, message = "Amount must be positive.")
	  private BigDecimal amount;
	  
	  @JsonCreator
	  public FundTransferRequest(@JsonProperty("fromAaccountId") String fromAccountId,
	    @JsonProperty("toAaccountId") String toAccountId,	  
	    @JsonProperty("balance") BigDecimal amount) {
	    this.fromAccountId = fromAccountId;
	    this.toAccountId = toAccountId;
	    this.amount = amount;
	    
	  }

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getFromAccountId() {
		return fromAccountId;
	}

	public String getToAccountId() {
		return toAccountId;
	}
	  
	  

}
