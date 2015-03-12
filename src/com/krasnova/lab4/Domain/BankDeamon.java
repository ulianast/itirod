package com.krasnova.lab4.Domain;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class BankDeamon implements Runnable{
	private Bank bank;
	private Queue<Bill> checkedBills;
	private List<Bill> uncheckedBills;
	
	public BankDeamon(Bank p_bank){
		this.bank = p_bank;
		checkedBills = new ConcurrentLinkedQueue<Bill>();
		
	}
	
	@Override
	public void run() {
		while(true){
			for(Bill bill : bank.getBills().values()){
				
			}
		}
		
	}

}
