package com.krasnova.lab4.Domain.Queries;

import com.krasnova.lab4.Domain.BankDeamon;
import com.krasnova.lab4.Domain.Client;

public class DeamonClientQuery extends BankQuery {
	private BankDeamon deamon;

	public DeamonClientQuery(Client client, BankDeamon deamon) {
		super();
		this.client = client;
		this.deamon = deamon;
	}

	@Override
	public void run() {
		synchronized(this){			 
			deamon.setResult(client.getCashAmmount());
			this.notify();
		}
	}

}
