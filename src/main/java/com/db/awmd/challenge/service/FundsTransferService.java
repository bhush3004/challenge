package com.db.awmd.challenge.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.db.awmd.challenge.constant.ErrorCode;
import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.FundTransferRequest;
import com.db.awmd.challenge.exception.AccountNotExistException;
import com.db.awmd.challenge.exception.OverDraftException;
import com.db.awmd.challenge.exception.SystemException;

/**
 * The Class FundsTransferService.
 */
public class FundsTransferService {
	
	/** The account service. */
	@Autowired
	AccountsService accountService;
	
	/** The nofity service. */
	@Autowired
	NotificationService nofityService;
	
	/**
	 * Transfer balances.
	 *
	 * @param request the request
	 * @throws OverDraftException the over draft exception
	 * @throws AccountNotExistException the account not exist exception
	 * @throws SystemException the system exception
	 * Wnat to use @Transactional annotation but hibernate is not configured in project
	 */
	public void transferBalances(FundTransferRequest request) throws OverDraftException, AccountNotExistException, SystemException {
		Account fromAccount = accountService.getAccount(request.getFromAccountId());
		checkAccountExists(fromAccount);
		
		Account toAccount = accountService.getAccount(request.getToAccountId());
		checkAccountExists(toAccount);
		
		//Checking if amount is not over the balance
		if(fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
			throw new OverDraftException("Account with id:" + fromAccount.getAccountId() + " does not have enough balance to transfer.", ErrorCode.ACCOUNT_ERROR);
		}
		
		//Subtract amount from fromAccount & send notification
		fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
		nofityService.notifyAboutTransfer(fromAccount, "Amount transfered to " + request.getToAccountId() + " of amount " + request.getAmount());
		
		//Add amount to toAccount & send notification
		toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));
		nofityService.notifyAboutTransfer(toAccount, "Amount received from " + request.getFromAccountId() + " of amount " + request.getAmount());
		
		
	}
	
	
	/**
	 * Check account exists else throw exception.
	 *
	 * @param account the account
	 */
	public void checkAccountExists(Account account) {
		if (null == account) {
			new AccountNotExistException("Account with id:" + account.getAccountId() + " does not exist.", ErrorCode.ACCOUNT_ERROR);
		}
	}

}
