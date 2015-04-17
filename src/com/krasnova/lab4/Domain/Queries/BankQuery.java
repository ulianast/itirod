package com.krasnova.lab4.Domain.Queries;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import com.krasnova.lab4.Domain.BankOperation;
import com.krasnova.lab4.Domain.Bill;
import com.krasnova.lab4.Domain.Casier;
import com.krasnova.lab4.Domain.Client;

public abstract class BankQuery implements Runnable{
	protected Bill firstBill;
	protected Client client;
	protected Casier casier;
	@Getter @Setter
	protected boolean rejected;
	
	//protected abstract boolean performTransaction(BankOperation p_bankOperation);
	public BankQuery(){
		rejected = false;
	}
}
