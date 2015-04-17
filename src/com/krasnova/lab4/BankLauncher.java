package com.krasnova.lab4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import com.krasnova.lab4.Domain.*;

public class BankLauncher {

	public static void main(String[] args) {
	/*	final int CASIERS_COL = 3;
		final int CLIENTS_COL = 9;
		// initialization
		Bank bank = new Bank(427500);
		
		List<Casier> casiers = new ArrayList<Casier> (CASIERS_COL);
		List<Client> clients = new ArrayList<Client>(CLIENTS_COL);
		List<UUID> bills = new ArrayList<UUID>(CLIENTS_COL * 3);
		
		for (int i = 0; i < CASIERS_COL; i++){
			Casier newCasier = new Casier("casier" + i, "casier" + i, bank);
			casiers.add(newCasier);
		}
		bank.hireCasiers(casiers);
		
		for (int i = 0; i< CLIENTS_COL; i++){
			Client client = new Client("client" + i, "client" + i, (i + 1) * 100, bank); //400500 total cash
			for (int j = 0; j < 3; j++){								//27000 on bills
				bills.add(client.addNewBill(100 * j));
			}
			clients.add(client);
		}
		bank.addClients(clients);
		
		//Run
		for(Casier casier: casiers){
			Thread thread = new Thread(casier);
			thread.start();			
		}
		
		Random rand = new Random();		
		
		for(int i = 0 ; i< clients.size(); i++){
			Casier casier = casiers.get(i);
			int randOperation = rand.nextInt(2);
			
			ClientIntention intention = new ClientIntention ();
			intention.ammount = rand.nextInt(100 * i);
			intention.client = clients.get(i);
			intention.operation = BankOperation.CHECK_AMMOUNT;
			
			
			switch(randOperation){
				case 0:
				casier.(clients.get(i), 
						rand.nextInt(100 * i), 
						(UUID) clients.get(i).getBills().keySet().;
					break
				case 1:
					break;
				case 2: 
					break;
			
			}			
		}
		
		bank.setWorking(false);
		Thread.sleep(5000);
		
		System.in.read();*/

	}

}
