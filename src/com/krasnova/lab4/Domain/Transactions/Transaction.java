package com.krasnova.lab4.Domain.Transactions;

import java.util.UUID;

import com.krasnova.lab4.Domain.BankOperation;
import com.krasnova.lab4.Domain.Bill;
import com.krasnova.lab4.Domain.Casier;
import com.krasnova.lab4.Domain.Client;

public abstract class Transaction implements Runnable{
	protected Bill firstBill;
	protected Client client;
	protected Casier casier;
	
	//protected abstract boolean performTransaction(BankOperation p_bankOperation);
}
