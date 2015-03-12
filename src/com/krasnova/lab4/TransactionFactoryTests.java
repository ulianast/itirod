package com.krasnova.lab4;

import static org.junit.Assert.*;

import org.junit.Test;

import com.krasnova.lab4.Domain.Bill;
import com.krasnova.lab4.Domain.Client;
import com.krasnova.lab4.Domain.Transactions.TransactionFactory;

public class TransactionFactoryTests {

	@Test
	public void test_getTransaction_getTwice() {
		TransactionFactory factory = TransactionFactory.getInstance();
		Client client = new Client("firstName1","lastName1",125);
		Bill bill = new Bill(345);
		
		Thread thread1 = new Thread();
	}

}
