package com.krasnova.lab4.Tests.Domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;


import org.junit.Test;

import com.krasnova.lab4.ConsoleOutStream;
import com.krasnova.lab4.Domain.*;

public class BankTests {
	@Test
	public void bankTest_BnakIsWorking(){
		ConsoleOutStream out = new ConsoleOutStream();
		Bank bank = new Bank(0, out);
		
		Client client1 = new Client("client1", "client1", 100, bank);
		Client client2 = new Client("client2", "client2", 200, bank);
		Client client3 = new Client("client3", "clien3", 200, bank);
		
		UUID billNumber1 = client1.addNewBill(0);
		UUID billNumber2 = client2.addNewBill(500);
		UUID billNumber3 = client3.addNewBill(150);
		
		bank.addClients(new ArrayList<Client>(Arrays.asList(client1, client2, client3)));
		
		bank.startWorkingDay();
		waitFor(1000);
		
		//assert
		assert(bank.isWorking());
	}
	
	@Test
	public void bankTest_TwoCasiersServeThreeClientsTest() {
		//Data preparation
		ConsoleOutStream out = new ConsoleOutStream();
		Bank bank = new Bank(1150, out);
		
		Casier casier1 = new Casier("casier1", "casier1", bank, out);
		Casier casier2 = new Casier("casier2", "casier2", bank, out);
		
		Client client1 = new Client("client1", "client1", 100, bank);
		Client client2 = new Client("client2", "client2", 200, bank);
		Client client3 = new Client("client3", "clien3", 200, bank);
		
		UUID billNumber1 = client1.addNewBill(0);
		UUID billNumber2 = client2.addNewBill(500);
		UUID billNumber3 = client3.addNewBill(150);
		
		ClientIntention intention1 = new ClientIntention(
				BankOperation.PUT_MONEY_ON,
				billNumber1,
				null,
				client1,
				50);
		ClientIntention intention2 = new ClientIntention(
				BankOperation.TAKE_MONEY_OFF,
				billNumber2,
				null,
				client2,
				150);
		ClientIntention intention3 = new ClientIntention(
				BankOperation.PUT_MONEY_ON,
				billNumber3,
				null,
				client3,
				150);
		
		bank.addClients(new ArrayList<Client>(Arrays.asList(client1, client2, client3)));
		bank.hireCasiers(new ArrayList<Casier>(Arrays.asList(casier1, casier2)));
		bank.addClientIntentions(new ArrayList<ClientIntention>(Arrays.asList(intention1, intention2, intention3)));
		
		//Test
		bank.startWorkingDay();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {

		}
		bank.serveClients();
		bank.endWorkingDay();
		
		//Assert
		assert(intention1.endedSuccessfully());
		assert(intention2.endedSuccessfully());
		assert(intention3.endedSuccessfully());
	}
	
	@Test
	public void bankTest_TwoClientsTryToWorkWithOneBill(){
		
		ConsoleOutStream out = new ConsoleOutStream();
		Bank bank = new Bank(800, out);
		
		Casier casier1 = new Casier("casier1", "casier1", bank, out);
		Casier casier2 = new Casier("casier2", "casier2", bank, out);
		
		Client client1 = new Client("client1", "client1", 100, bank);
		Client client2 = new Client("client2", "client2", 200, bank);
		
		UUID billNumber1 = client1.addNewBill(500);
		
		ClientIntention intention1 = new ClientIntention(
				BankOperation.PUT_MONEY_ON,
				billNumber1,
				null,
				client1,
				50);
		ClientIntention intention2 = new ClientIntention(
				BankOperation.TAKE_MONEY_OFF,
				billNumber1,
				null,
				client2,
				150);
		
		bank.addClients(new ArrayList<Client>(Arrays.asList(client1, client2)));
		bank.hireCasiers(new ArrayList<Casier>(Arrays.asList(casier1, casier2)));
		bank.addClientIntentions(new ArrayList<ClientIntention>(Arrays.asList(intention1, intention2)));
		
		//Test
		bank.startWorkingDay();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

		}
		bank.serveClients();
		bank.endWorkingDay();
		
		//Assert
		assert(intention1.endedSuccessfully());
		assert(intention2.endedSuccessfully());
	}
	@Test
	public void bankTest_NoClients(){
		//Data preparation
		ConsoleOutStream out = new ConsoleOutStream();
		Bank bank = new Bank(0, out);
		
		Casier casier1 = new Casier("casier1", "casier1", bank, out);		
		bank.hireCasiers(new ArrayList<Casier>(Arrays.asList(casier1)));
		
		//Test
		bank.startWorkingDay();
		bank.serveClients();
		bank.endWorkingDay();
		
		//Assert
		//if we go to this instruction, bank has ended it's work correctly
		assert(true);
	}
	
	@Test
	public void bankTest_TwoConcurentCrossBillTransaction (){
		ConsoleOutStream out = new ConsoleOutStream();
		Bank bank = new Bank(1500, out);
		
		Casier casier1 = new Casier("casier1", "casier1", bank, out);
		Casier casier2 = new Casier("casier2", "casier2", bank, out);
		
		Client client1 = new Client("client1", "client1", 100, bank);
		Client client2 = new Client("client2", "client2", 200, bank);
		
		UUID billNumber1 = client1.addNewBill(500);
		UUID billNumber2 = client2.addNewBill(700);
		
		ClientIntention intention1 = new ClientIntention(
				BankOperation.TRANSFER,
				billNumber1,
				billNumber2,
				client1,
				50);
		ClientIntention intention2 = new ClientIntention(
				BankOperation.TRANSFER,
				billNumber2,
				billNumber1,
				client2,
				150);
		
		bank.addClients(new ArrayList<Client>(Arrays.asList(client1, client2)));
		bank.hireCasiers(new ArrayList<Casier>(Arrays.asList(casier1, casier2)));
		bank.addClientIntentions(new ArrayList<ClientIntention>(Arrays.asList(intention1, intention2)));		
		
		//Test
		bank.startWorkingDay();
		bank.serveClients();	
		bank.endWorkingDay();
	
		//Assert
		assert(intention1.endedSuccessfully());
 		assert(intention2.endedSuccessfully());		
	}
	
	private void waitFor(int miliseconds){
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {

		}
	}
}
