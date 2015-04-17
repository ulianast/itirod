package com.krasnova.lab4.Tests.Domain;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import org.junit.Test;

import com.krasnova.lab4.ConsoleOutStream;
import com.krasnova.lab4.Domain.Bank;
import com.krasnova.lab4.Domain.BankDeamon;
import com.krasnova.lab4.Domain.BankOperation;
import com.krasnova.lab4.Domain.Casier;
import com.krasnova.lab4.Domain.Client;
import com.krasnova.lab4.Domain.ClientIntention;

public class BankDeamonTests {
	@Test 
	public void testDoCheckIterartion_noLeaking(){
		ConsoleOutStream out = new ConsoleOutStream();
		Bank bank = new Bank(800, out);
		
		Casier casier1 = new Casier("casier1", "casier1", bank, out);
		Casier casier2 = new Casier("casier2", "casier2", bank, out);
		
		Client client1 = new Client("client1", "client1", 100, bank);
		Client client2 = new Client("client2", "client2", 200, bank);
		
		UUID billNumber1 = client1.addNewBill(500);
		
		bank.addClients(new ArrayList<Client>(Arrays.asList(client1, client2)));
		
		BankDeamon deamon = getDeamon(bank);
		
		
		//test		
		invokeDoIterationCheckMethod(deamon);
		
		//assert
		assert(deamon.isAllFine());
	}
	
	@Test 
	public void testDoCheckIterartion_leakingDetected(){
		ConsoleOutStream out = new ConsoleOutStream();
		Bank bank = new Bank(800, out);
		
		Casier casier1 = new Casier("casier1", "casier1", bank, out);
		Casier casier2 = new Casier("casier2", "casier2", bank, out);
		
		Client client1 = new Client("client1", "client1", 50, bank);
		Client client2 = new Client("client2", "client2", 200, bank);
		
		UUID billNumber1 = client1.addNewBill(500);
		
		bank.addClients(new ArrayList<Client>(Arrays.asList(client1, client2)));
		
		BankDeamon deamon = getDeamon(bank);
		
		
		//test		
		invokeDoIterationCheckMethod(deamon);
		
		//assert
		assert( ! deamon.isAllFine());
	}
	
	@Test
	public void testDeamonWithClientBillIntentionBehavior(){
		ConsoleOutStream out = new ConsoleOutStream();
		Bank bank = new Bank(600, out);
		
		Casier casier1 = new Casier("casier1", "casier1", bank, out);
		
		Client client1 = new Client("client1", "client1", 100, bank);
		
		UUID billNumber1 = client1.addNewBill(500);
		
		ClientIntention intention1 = new ClientIntention(
				BankOperation.PUT_MONEY_ON,
				billNumber1,
				null,
				client1,
				50);
		
		bank.addClients(new ArrayList<Client>(Arrays.asList(client1)));
		bank.hireCasiers(new ArrayList<Casier>(Arrays.asList(casier1)));
		bank.addClientIntentions(new ArrayList<ClientIntention>(Arrays.asList(intention1)));
		
		//Test
		bank.startWorkingDay();
		bank.serveClients();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {

		}
		bank.endWorkingDay();
		
		//Assert
		BankDeamon deamon = getDeamon(bank);
		assert(deamon.isAllFine());
	}
	
	@Test
	public void testDeamonWithCrossBillIntentionBehavior(){
		ConsoleOutStream out = new ConsoleOutStream();
		Bank bank = new Bank(800, out);
		
		Casier casier1 = new Casier("casier1", "casier1", bank, out);
		
		Client client1 = new Client("client1", "client1", 100, bank);
		
		UUID billNumber1 = client1.addNewBill(500);
		UUID billNumber2 = client1.addNewBill(200);
		
		ClientIntention intention1 = new ClientIntention(
				BankOperation.TRANSFER,
				billNumber1,
				billNumber2,
				client1,
				50);
		
		bank.addClients(new ArrayList<Client>(Arrays.asList(client1)));
		bank.hireCasiers(new ArrayList<Casier>(Arrays.asList(casier1)));
		bank.addClientIntentions(new ArrayList<ClientIntention>(Arrays.asList(intention1)));
		
		//Test
		bank.startWorkingDay();
		bank.serveClients();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {

		}
		bank.endWorkingDay();
		
		//Assert
		BankDeamon deamon = getDeamon(bank);
		assert(deamon.isAllFine());
	}

/*	@Test
	public void noLeakingInBills() {
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
		bank.serveClients();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {

		}
		bank.endWorkingDay();
		
		//Assert
		assert(intention1.endedSuccessfully());
		assert(intention2.endedSuccessfully());
	} */
	
	private BankDeamon getDeamon(Bank bank){
		BankDeamon deamon = null;
		try {
			Field deamonField = Bank.class.getDeclaredField("deamon");
			deamonField.setAccessible(true);
			deamon = (BankDeamon) deamonField.get(bank);
		} catch (IllegalArgumentException | IllegalAccessException
				| NoSuchFieldException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return deamon;
	}
	
	private void invokeDoIterationCheckMethod(BankDeamon deamon){
		Method doIterationMethod;
		try {
			doIterationMethod = BankDeamon.class.getDeclaredMethod("doCheckIterration");
			doIterationMethod.setAccessible(true);
			try {
				doIterationMethod.invoke(deamon);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
