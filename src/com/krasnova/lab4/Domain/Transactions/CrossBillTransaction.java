package com.krasnova.lab4.Domain.Transactions;

import lombok.Getter;

import com.krasnova.lab4.Domain.BankOperation;
import com.krasnova.lab4.Domain.Bill;
import com.krasnova.lab4.Domain.Casier;

public class CrossBillTransaction extends Transaction {
	@Getter
	private Bill secondBill;
	private int ammount;
	
	public CrossBillTransaction(Casier casier, Bill firstBill, Bill secondBill, int ammount){
		this.firstBill = firstBill;
		this.secondBill = secondBill;
		this.ammount = ammount;
		this.casier = casier;
	}
	
	@Override
	public void run() {
		casier.setOperatinonComplete(false);
		boolean isSuccess = false;
		
		synchronized(this){		
			isSuccess = firstBill.transferTo(ammount, secondBill);
			casier.setOperatinonComplete(true);
			casier.setOpertionSuccessful(isSuccess);
			this.notify();
		}
	}
}
