package com.krasnova.lab4.Domain.Transactions;

import com.krasnova.lab4.Domain.BankOperation;
import com.krasnova.lab4.Domain.Bill;
import com.krasnova.lab4.Domain.Casier;

public class CrossBillTransaction extends Transaction {
	private Bill secondBill;
	private int ammount;
	
	public CrossBillTransaction(Casier casier, Bill firstBill, Bill secondBill, int ammount){
		this.firstBill = firstBill;
		this.secondBill = secondBill;
		this.ammount = ammount;
		this.casier = casier;
	}
	
	@Override
	synchronized public void run() {
		casier.setOperatinonComplete(false);
		boolean isSuccess = false;
		synchronized(this){
			isSuccess = firstBill.transferTo(ammount, secondBill);
		}
		casier.setOperatinonComplete(true);
		casier.setOpertionSuccessful(isSuccess);
	}
}
