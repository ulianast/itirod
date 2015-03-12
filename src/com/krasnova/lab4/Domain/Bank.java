package com.krasnova.lab4.Domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import lombok.Getter;

public class Bank {
	@Getter
	private Map<String, Client> clients;
	@Getter
	private Map<String,Casier> casiers;
	@Getter
	private Map<UUID, Bill> bills;
	@Getter
	private int totalAmmount;
	
	public Bank(int p_totalAmmount){
		totalAmmount = p_totalAmmount;
		clients = new HashMap<String, Client>();
		bills = new HashMap<UUID, Bill>();
		casiers = new HashMap<String, Casier>();
	}
	
	public void hireCasiers(List<Casier> hiredCasiers){
		for(Casier casier : hiredCasiers){
			this.casiers.put(casier.getFirstName() + " " + casier.getLastName(), casier);
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
}
