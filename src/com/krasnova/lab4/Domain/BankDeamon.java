package com.krasnova.lab4.Domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.krasnova.lab4.IOutStream;
import com.krasnova.lab4.Domain.Queries.BankDeamonBillQuery;
import com.krasnova.lab4.Domain.Queries.BankQuery;
import com.krasnova.lab4.Domain.Queries.DeamonClientQuery;
import com.krasnova.lab4.Domain.Queries.QueryFactory;

import lombok.Getter;
import lombok.Setter;


public class BankDeamon implements Runnable{
	private Bank bank;
	@Getter
	private Map<UUID,Bill> checkedBills;
	@Getter
	private Map<String,Client> checkedClients;
	@Setter
	private int result;
	@Getter
	private boolean allFine;
	private IOutStream out;
	
	public BankDeamon(Bank p_bank, IOutStream out){
		this.bank = p_bank;
		checkedBills = new HashMap<UUID, Bill>();
		checkedClients = new HashMap<String,Client>();
		this.out = out;
		allFine = true;
		out.outln("Bank Deamon stared system observing");
	}
	
	@Override
	public void run() {
		while(true){
			doCheckIterration();		
			if( ! allFine) {
				break;
			}
		}		
	}
	
	private void doCheckIterration(){
		int calculatedAmmount = 0;
		for(Bill bill : bank.getBills().values()){
			BankDeamonBillQuery q = (BankDeamonBillQuery) QueryFactory.getInstance().getTransaction(bill,this);
			executeQuery(q);
			calculatedAmmount += result;
			checkedBills.put(bill.getBillNumber(), bill);
		}			
		for(Client client: bank.getClients()){
			DeamonClientQuery q = (DeamonClientQuery) QueryFactory.getInstance().getTransaction(client, this);
			executeQuery(q);
			calculatedAmmount += result;
			checkedClients.put(client.getFirstName() + client.getLastName(), client);
		}
		allFine = calculatedAmmount == bank.getTotalAmmount();
		if( ! allFine){
			out.outln("-----BANK DEAMON: LEAK IS DETECTED");
		}
	}
	
	private void executeQuery(BankQuery trans){
		synchronized(trans){
			Thread thread = new Thread(trans);
			thread.setDaemon(true);
			thread.start();
			try {
				trans.wait();
			} catch (InterruptedException e) {
			}
			QueryFactory.getInstance().releseTransaction(trans);
		}
	}

}
