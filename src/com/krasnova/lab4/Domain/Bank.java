package com.krasnova.lab4.Domain;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.krasnova.lab4.IOutStream;
import com.krasnova.lab4.Domain.Queries.QueryFactory;


import lombok.Getter;
import lombok.Setter;

public class Bank {
	@Getter
	private List<Client> clients;
	private List<Thread> clientThreads;
	private List<ClientIntention> intentions;
	@Getter
	private List<Casier> casiers;
	private List<Thread> cashierThreads;
	@Getter
	private Map<UUID, Bill> bills;
	@Getter
	private int totalAmmount;
	@Setter
	private boolean working;
	@Getter 
	private boolean failed;
	private BankDeamon deamon;
	private IOutStream out;
	
	
	
	public Bank(int p_totalAmmount, IOutStream out){
		this.out = out;
		this.working = true;
		this.totalAmmount = p_totalAmmount;
		this.clients = new ArrayList<Client>();
		this.intentions = new ArrayList<ClientIntention>();
		this.bills = new HashMap<UUID, Bill>();
		this.casiers = new ArrayList<Casier>();
		this.cashierThreads = new ArrayList<Thread>();
		this.clientThreads = new ArrayList<Thread>();
		deamon = new BankDeamon(this,out);
		QueryFactory.getInstance().setDeamon(deamon);
		this.failed = false;
	}
	
	public void hireCasiers(List<Casier> hiredCasiers){
		this.casiers.addAll(hiredCasiers);
	}
	
	public void startWorkingDay(){
		Thread deamonThread = new Thread (deamon);
		deamonThread.setDaemon(true);
		deamonThread.start();
		for(Casier cashier : this.casiers){
			Thread thread = new Thread(cashier);
			thread.start();
			this.cashierThreads.add(thread);
		}
	}
	
	public void addClients(List<Client> addedClients){
		clients.addAll(addedClients);
	}
	
	public void addClientIntentions(List<ClientIntention> newIntentions){
		this.intentions.addAll(newIntentions);
	}
	
	boolean serveClient(ClientIntention intention){
		boolean result = false;
		System.out.println("casiers: " + casiers);
		for (int i = 0 ; i < casiers.size(); i++ ){
			if( ! this.isWorking()){
				System.out.println("Bank is not working");
				return false;
			}
			synchronized(casiers.get(i)){
				if (cashierThreads.get(i).getState() == Thread.State.WAITING){
					casiers.get(i).setClientIntention(intention);
					casiers.get(i).notify();
					result = true;
				}
			}
			if(result) 
				return result;
		}
		return false;
	}
	
	public void serveClients(){
		for(ClientIntention intent : this.intentions){
			Client client = intent.getClient();
			client.setIntention(intent);
			Thread clientThread = new Thread (client);
			clientThreads.add(clientThread);
			clientThread.start();
		}
	}
	
	public boolean isWorking(){
		return working && deamon.isAllFine();
	}
	
	public void setFailed(boolean isFailed){
		this.failed = isFailed;		
	}
	
	public boolean endWorkingDay(){
		this.working = false;
		for(Thread clientThr : clientThreads){
			try{
				clientThr.join();
			}catch(InterruptedException e){
				
			}
		}
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
