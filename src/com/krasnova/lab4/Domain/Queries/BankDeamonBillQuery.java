package com.krasnova.lab4.Domain.Queries;
import com.krasnova.lab4.Domain.*;

public class BankDeamonBillQuery extends BankQuery {
	private BankDeamon deamon;

	public BankDeamonBillQuery(Bill bill, BankDeamon deamon) {
		super();
		this.firstBill = bill;
		this.deamon = deamon;
	}

	@Override
	public void run() {
		synchronized(this){			 
			deamon.setResult(firstBill.getAmmount());
			this.notify();
		}
	}

}
