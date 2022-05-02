package com.db.awmd.challenge.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.domain.FundTransferRequest;
import com.db.awmd.challenge.exception.AccountNotExistException;
import com.db.awmd.challenge.exception.CheckBalanceException;
import com.db.awmd.challenge.exception.OverDraftException;
import com.db.awmd.challenge.service.FundsTransferService;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class FundsTransferController.
 */
@RestController
@RequestMapping("/v1/accounts/transfer")

/** The Constant log. */
@Slf4j
public class FundsTransferController {
	
	/** The funds service. */
	@Autowired
	FundsTransferService fundsService;
	
	
	/**
	 * Transfer money.
	 *
	 * @param request the request
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@PostMapping(consumes = { "application/json" })
	public ResponseEntity transferMoney(@RequestBody @Valid FundTransferRequest request) throws Exception {

		try {
			fundsService.transferBalances(request);							
			return new ResponseEntity<>(request, HttpStatus.ACCEPTED);
		} catch (AccountNotExistException | OverDraftException e) {
			log.error("Fail to transfer balances, please check with system administrator.");
			throw e;
		} catch (CheckBalanceException cbEx) {
			log.error("Fail to check balances after transfer, please check with system administrator.");
			throw cbEx;
		}
	}
	

}
