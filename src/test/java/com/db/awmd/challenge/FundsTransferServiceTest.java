package com.db.awmd.challenge;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransferRequest;
import com.db.awmd.challenge.exception.AccountNotExistException;
import com.db.awmd.challenge.exception.OverDraftException;
import com.db.awmd.challenge.exception.SystemException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.FundsTransferService;




@RunWith(SpringRunner.class)
@SpringBootTest
public class FundsTransferServiceTest {
	
	@InjectMocks
	AccountsService accService;
	
	@InjectMocks
	FundsTransferService transferService;
	
	@Test
	public void testTransferBalance() throws Exception, Exception, Exception {
		String accountFromId = "00098563728123";
		String accountToId = "00098983728343";
		BigDecimal amount = new BigDecimal(10);
		
		FundTransferRequest request = new FundTransferRequest(accountFromId, accountToId, amount);
		
		
		Account accFrom = new Account(accountFromId, BigDecimal.TEN);
		Account accTo = new Account(accountFromId, BigDecimal.TEN);
		
		when(accService.getAccount(accountFromId)).thenReturn(accFrom);
		when(accService.getAccount(accountToId)).thenReturn(accFrom);
				
		transferService.transferBalances(request);
		
		assertEquals(BigDecimal.ZERO, accFrom.getBalance());
		assertEquals(BigDecimal.TEN.add(BigDecimal.TEN), accTo.getBalance());
	}
	
	@Test(expected = OverDraftException.class)
	public void testOverdraftBalance() throws OverDraftException, AccountNotExistException, SystemException {
		String accountFromId = "00098563728123";
		String accountToId = "00098983728343";
		BigDecimal amount = new BigDecimal(20);
		
		FundTransferRequest request = new FundTransferRequest(accountFromId, accountToId, amount);
		
		Account accFrom = new Account(accountFromId, BigDecimal.TEN);
		Account accTo = new Account(accountFromId, BigDecimal.TEN);
		
		when(accService.getAccount(accountFromId)).thenReturn(accFrom);
		when(accService.getAccount(accountToId)).thenReturn(accTo);
		
		transferService.transferBalances(request);
	}

}
