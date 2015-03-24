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
	public void run(){
		boolean isSuccess= false;
		
		synchronized(this){
			switch(op){
			case PUT_MONEY_ON:
				System.out.println("OPERATION: putting money on.");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(client.subMoney(ammount)){
					isSuccess =  firstBill.putMoneyOn(ammount);
				}else 
					isSuccess = false;
				break;
			case TAKE_MONEY_OFF:
				System.out.println("OPERATION: taking money off.");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
			System.out.println("OPERATION " + (isSuccess ? "SUCCESSFUL" : "FAILED"));
			this.notify();
		}
		
		return;		
	}

}
