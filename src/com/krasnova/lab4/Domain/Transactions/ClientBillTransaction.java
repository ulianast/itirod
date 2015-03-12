package com.krasnova.lab4.Domain.Transactions;

import java.awt.event.ItemListener;

import org.omg.CosNaming.IstringHelper;

import com.krasnova.lab4.Domain.BankOperation;
import com.krasnova.lab4.Domain.Bill;
import com.krasnova.lab4.Domain.Casier;
import com.krasnova.lab4.Domain.Client;

public class ClientBillTransaction extends Transaction{
	int ammount;
	BankOperation op;
	
	public ClientBillTransaction(Casier casier,Client client, Bill bill, int ammount, BankOperation bankOperation){
		super();
		this.casier = casier;
		this.ammount = ammount;
		this.client = client;
		this.firstBill = bill;
		this.op = bankOperation;
		this.casier.setOperatinonComplete(false);
	}
	@Override
	synchronized public void run(){
		boolean isSuccess= false;
		
		synchronized(this){
			switch(op){
			case PUT_MONEY_ON:
				if(client.subMoney(ammount)){
					isSuccess =  firstBill.putMoneyOn(ammount);
				}else 
					isSuccess = false;
				break;
			case TAKE_MONEY_OFF:
				if(firstBill.takeMoneyOff(ammount)){
					client.addMoney(ammount);
					isSuccess = true;
				}else 
					isSuccess = false;
				break;
			}
		}
		
		this.casier.setOperatinonComplete(true);
		this.casier.setOpertionSuccessful(isSuccess);
		return;		
	}

}
