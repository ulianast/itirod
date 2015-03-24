package com.krasnova.lab4.Domain;

import java.util.UUID;


import lombok.Getter;
import lombok.Setter;

public class Bill implements Comparable<Bill>{
	private int ammount;
	@Getter 
	private UUID billNumber;
	@Getter @Setter
	private Client client;
	
	synchronized public int getAmmount(){
		return ammount;
	}
	
	public Bill(int ammount){
		this.ammount = ammount;
		//client = p_client;
		billNumber = UUID.randomUUID();
	}
	
	synchronized public boolean transferTo(int ammount, Bill anotherBill){
		return (
				this.takeMoneyOff(ammount)
				&& anotherBill.putMoneyOn(ammount));
	}
	
	synchronized public boolean takeMoneyOff(int ammount){
		boolean success = false;
		if(this.ammount - ammount < 0)
			success = false;			
		else{
			this.ammount -= ammount;
			success = true;
		}
		return success;
	}	
	
	synchronized public boolean putMoneyOn(int ammount){
		this.ammount += ammount;
		return true;
	}

	@Override
	public int compareTo(Bill anotherBill) {
		return this.billNumber.compareTo(anotherBill.billNumber);
		
	}

	
}
