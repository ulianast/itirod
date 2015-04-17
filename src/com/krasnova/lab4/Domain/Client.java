package com.krasnova.lab4.Domain;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


import lombok.Getter;
import lombok.Setter;

public class Client implements Runnable{
	@Getter 
	private Map<UUID,Bill> bills;
	@Getter 
	private String firstName;
	@Getter 
	private String lastName;
	 
	private int cashAmmount;
	
	private Bank bank;
	@Setter
	private ClientIntention intention;
	
	
	public Client(String firstName, String lastName, int cash, Bank bank) {
		bills = new ConcurrentHashMap<UUID, Bill> ();
		this.firstName = firstName;
		this.lastName = lastName;
		this.cashAmmount = cash;
		this.bank = bank;
	}
	
	public UUID addNewBill(int ammount){
		Bill newBill = new Bill(ammount);
		this.bills.put(newBill.getBillNumber(), newBill);
		bank.getBills().put(newBill.getBillNumber(), newBill);
		return newBill.getBillNumber();
	}
	
	synchronized public void addMoney(int p_ammount){
		cashAmmount += p_ammount;
	}
	
	synchronized public boolean subMoney(int p_ammount){
		if (cashAmmount < p_ammount)
			return false;
		cashAmmount -= p_ammount;
		return true;
	}
	
	synchronized public int getCashAmmount(){
		return this.cashAmmount;
	}

	@Override
	public void run() {
		System.out.println(intention);
		if (intention != null ){
			while( ! bank.serveClient(intention) && bank.isWorking());
		}
		
	}

}
