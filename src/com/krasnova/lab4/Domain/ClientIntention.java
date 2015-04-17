package com.krasnova.lab4.Domain;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

public class ClientIntention {
	@Getter @Setter
	private BankOperation operation;
	@Getter @Setter
	private UUID firstBill;
	@Getter @Setter
	private UUID secondBill;
	@Getter @Setter
	private Client client;
	@Getter @Setter
	private int ammount;
	@Setter @Getter
	private boolean successEnd;
	
	public boolean endedSuccessfully(){
		return successEnd;
	}
	
	public ClientIntention(BankOperation op, UUID fBill, UUID sBill, Client client, int ammount){
		this.operation = op;
		this.firstBill = fBill;
		this.secondBill = sBill;
		this.client = client;
		this.ammount = ammount;
		this.successEnd = false;
	}
}
