package com.krasnova.lab4.Domain.Queries;

import java.awt.event.ItemListener;

import org.omg.CosNaming.IstringHelper;

import com.krasnova.lab4.Domain.BankOperation;
import com.krasnova.lab4.Domain.Bill;
import com.krasnova.lab4.Domain.Casier;
import com.krasnova.lab4.Domain.Client;

public class ClientBillQuery extends BankQuery{
	int ammount;
	BankOperation op;
	
	public ClientBillQuery(Casier casier,Client client, Bill bill, int ammount, BankOperation bankOperation){
		super();
		this.casier = casier;
		this.ammount = ammount;
		this.client = client;
		this.firstBill = bill;
		this.op = bankOperation;
		this.casier.setOperatinonComplete(false);
	}
	@Override
	public void run(){
		boolean isSuccess= false;
		
		synchronized(this){
			switch(op){
			case PUT_MONEY_ON:
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
				if(client.subMoney(ammount)){
					isSuccess =  firstBill.putMoneyOn(ammount);
				}else 
					isSuccess = false;
				break;
			case TAKE_MONEY_OFF:
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
				}
				if(firstBill.takeMoneyOff(ammount)){
					client.addMoney(ammount);
					isSuccess = true;
				}else 
					isSuccess = false;
				break;
			case CHECK_AMMOUNT:
				break;
			case TRANSFER:
				break;
			}			
			
			this.casier.setOperatinonComplete(true);
			this.casier.setOpertionSuccessful(isSuccess);
			this.notify();
		}		
		return;		
	}

}
