package com.krasnova.lab4.Domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import lombok.Getter;
import lombok.Setter;

public class Bank {
	@Getter
	private Map<String, Client> clients;
	@Getter
	private List<Casier> casiers;
	private List<Thread> cashierThreads;
	@Getter
	private Map<UUID, Bill> bills;
	@Getter
	private int totalAmmount;
	@Setter
	private boolean working;
	
	
	
	public Bank(int p_totalAmmount){
		this.working = true;
		this.totalAmmount = p_totalAmmount;
		this.clients = new HashMap<String, Client>();
		this.bills = new HashMap<UUID, Bill>();
		this.casiers = new ArrayList<Casier>();
		this.cashierThreads = new ArrayList<Thread>();
		
	}
	
	public void hireCasiers(List<Casier> hiredCasiers){
		this.casiers.addAll(hiredCasiers);
	}
	
	public void startWorkingDay(){
		for(Casier cashier : this.casiers){
			Thread thread = new Thread(cashier);
			thread.start();
			this.cashierThreads.add(thread);
		}
	}
	
	public void addClients(List<Client> addedClients){
		for(Client client : addedClients){
			this.clients.put(client.getFirstName() + " " + client.getLastName(), client);
			for(Bill bill : client.getBills().values()){
				this.bills.put(bill.getBillNumber(), bill);
			}
		}
	}
	
	public boolean serveNextClient(ClientIntention intention){
		for (int i = 0 ; i < casiers.size(); i++ ){
			synchronized(casiers.get(i)){
				if (cashierThreads.get(i).getState() == Thread.State.WAITING){
					casiers.get(i).setClientIntention(intention);
					casiers.get(i).notify();
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean isWorking(){
		return working;
	}
	
	public boolean endWorkingDay(){
		this.working = false;
		for(int i = 0; i < cashierThreads.size(); i++){
			synchronized(casiers.get(i)){
				if (cashierThreads.get(i).getState() == Thread.State.WAITING){
					casiers.get(i).notify();
				}
			}
			try{
				cashierThreads.get(i).join();
			}catch(InterruptedException e){
				
			}
		}
		return true;
	}
}
