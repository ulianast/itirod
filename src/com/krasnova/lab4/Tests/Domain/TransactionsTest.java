package com.krasnova.lab4.Tests.Domain;

import static org.junit.Assert.*;

import com.krasnova.lab4.ConsoleOutStream;
import com.krasnova.lab4.Domain.*;
import com.krasnova.lab4.Domain.Queries.BankQuery;
import com.krasnova.lab4.Domain.Queries.ClientBillQuery;
import com.krasnova.lab4.Domain.Queries.QueryFactory;


import org.junit.Test;

public class TransactionsTest {
	private Bank bank = null;
	private Client client1 = null;
	private Client client2 = null;
	private Casier casier1 = null;
	private Bill bill1 = null;
	private Bill bill2 = null;
	
	private void init(){
		ConsoleOutStream out = new ConsoleOutStream();
		bank = new Bank(1000, out);
		client1 = new Client("client1", "client1", 100, bank);
		client2 = new Client("client2", "client2", 500, bank);
		casier1 = new Casier("casier1", "casier1",bank, out);
		bill1 = new Bill(150);
		bill2 = new Bill(250);
	}

	@Test
	public void getTransactionTest_get2TransWithOneBill() {
		//Data preparation
		init();
		
		//Test
		TransactionThread transThread1 = new TransactionThread(
				client1, 
				casier1, 
				bill1, 
				100,
				BankOperation.PUT_MONEY_ON);
		transThread1.start();
		sleepFor(500);
		
		TransactionThread transThread2 = new TransactionThread(
				client2, 
				casier1, 
				bill1, 
				100,
				BankOperation.PUT_MONEY_ON);
		transThread2.start();
		sleepFor(500);
		
		//Assert
		assertNotNull(transThread1.result);
		assertEquals(Thread.State.WAITING, transThread2.getState());
		transThread2.interrupt();
		assertNull(transThread2.result);
		
	}
	
	@Test
	public void releseTransactionTest_get2TransWithOneBill() {
		//Data preparation
		init();
		
		//Test
		TransactionThread transThread1 = new TransactionThread(
				client1, 
				casier1, 
				bill1, 
				100,
				BankOperation.PUT_MONEY_ON);
		transThread1.start();
		sleepFor(100);
		
		TransactionThread transThread2 = new TransactionThread(
				client2, 
				casier1, 
				bill1, 
				100,
				BankOperation.PUT_MONEY_ON);
		transThread2.start();
		sleepFor(500);
		
		
		//Assert
		assertNotNull(transThread1.result);
		assertEquals(Thread.State.WAITING, transThread2.getState());
		transThread2.interrupt();
		assertNull(transThread2.result);
		
	}
	
	private void sleepFor(long ms){
		try {
			Thread.currentThread().sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private class TransactionThread extends Thread{
		private Client client1 = null;
		private Casier casier1 = null;
		private Bill bill1 = null;
		private Bill bill2 = null;
		private int ammount;
		private BankOperation operation;
		public BankQuery result= null;
		
		public TransactionThread(Client client, Casier casier, Bill bill, int ammount, BankOperation op){
			this.ammount = ammount;
			this.bill1 = bill;
			this.client1 = client;
			this.casier1 = casier;
			this.operation = op;
		}
		
		@Override
		public void run(){
			ClientBillQuery trans = null;
			BankQuery returnVal = QueryFactory.getInstance().getTransaction(
					this.casier1, 
					this.client1, 
					this.bill1, 
					this.ammount,
					this.operation);
			if(returnVal != null){
				trans = (ClientBillQuery)returnVal;
				sleepFor(2000);
				QueryFactory.getInstance().releseTransaction(trans);
			}
			this.result = trans;			
		}
	}

}
