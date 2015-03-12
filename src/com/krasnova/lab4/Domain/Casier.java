package com.krasnova.lab4.Domain;

import java.util.UUID;

import com.krasnova.lab4.Domain.Transactions.ClientBillTransaction;
import com.krasnova.lab4.Domain.Transactions.TransactionFactory;

import lombok.Getter;
import lombok.Setter;

public class Casier{
	@Getter
	private String firstName;
	@Getter
	private String lastName;
	private Bank bank;
	@Getter
	private BankOperation performedOperation;
	@Getter @Setter
	private boolean operatinonComplete;
	@Getter @Setter
	private boolean opertionSuccessful;
	
	public Casier(String p_firstName, String p_SecondName, Bank p_bank){
		this.bank = p_bank;
		this.firstName = p_firstName;
		this.lastName = p_SecondName;
	}
	
	public void serveNextClient(){
		this.operatinonComplete = false;
		this.opertionSuccessful = false;
	}
	
	public boolean doDeposit(Client client, int ammount, UUID billNumber){
		ClientBillTransaction trans= (ClientBillTransaction) TransactionFactory.getInstance().getTransaction(
				this,
				client, 
				this.bank.getBills().get(billNumber),
				ammount,
				BankOperation.PUT_MONEY_ON);
		
		Thread thread = new Thread(trans);
		
		boolean rezult = false;
		synchronized(trans){
			try {
				trans.wait();
				rezult = operatinonComplete && opertionSuccessful;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		return rezult;
	}
	
	public boolean doWithdraw(Client p_client, int p_ammount, UUID p_billNumber){
		if(p_client.getBills().get(p_billNumber).takeMoneyOff(p_ammount)){
			p_client.addMoney(p_ammount);
			return true;
		}else return false;		
	}
	
	public int checkMoney(Client p_client, UUID p_billNumber){
		return p_client.getBills().get(p_billNumber).getAmmount();
	}
	
	public boolean doTransfer(Client p_client, int p_ammount, UUID p_firstBill, UUID p_secondBill){
		if(bank.getBills().containsKey(p_secondBill)){
			Bill secondBill = bank.getBills().get(p_secondBill);
			return p_client.getBills().get(p_firstBill).transferTo(p_ammount, secondBill);
		}else return false;		
	}
}
