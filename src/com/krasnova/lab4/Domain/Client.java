package com.krasnova.lab4.Domain;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


import lombok.Getter;

public class Client {
	@Getter 
	private Map<UUID,Bill> bills;
	@Getter 
	private String firstName;
	@Getter 
	private String lastName;
	 
	private int cashAmmount;
	
	public Client(String firstName, String lastName, int cash){
		bills = new ConcurrentHashMap<UUID, Bill> ();
		this.firstName = firstName;
		this.lastName = lastName;
		this.cashAmmount = cash;
	}
	
	public void addNewBill(int ammount){
		Bill newBill = new Bill(ammount);
		
		synchronized(newBill){
			newBill.setClient(this);
			this.bills.put(newBill.getBillNumber(), newBill);
		}
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
}
