package com.krasnova.lab4.Domain;

import java.util.UUID;

import com.krasnova.lab4.IOutStream;
import com.krasnova.lab4.Domain.Queries.BankQuery;
import com.krasnova.lab4.Domain.Queries.ClientBillQuery;
import com.krasnova.lab4.Domain.Queries.CrossBillQuery;
import com.krasnova.lab4.Domain.Queries.QueryFactory;

import lombok.Getter;
import lombok.Setter;

public class Casier implements Runnable{
	@Getter
	private String firstName;
	@Getter
	private String lastName;
	private Bank bank;
	@Getter
	private BankOperation performedOperation;
	@Getter @Setter
	private boolean operatinonComplete;
	@Getter @Setter
	private boolean opertionSuccessful;
	@Getter @Setter
	private ClientIntention clientIntention;
	private IOutStream outStream;
	
	public Casier(String p_firstName, String p_SecondName, Bank p_bank, IOutStream out){
		this.bank = p_bank;
		this.firstName = p_firstName;
		this.lastName = p_SecondName;
		this.outStream = out;
	}

	@Override
	public void run() {
		outStream.outln("Casier " + this.firstName + " begins working");
		while(bank.isWorking()){
			synchronized(this){
				try {
					outStream.outln("Casier " + this.firstName + " is waiting for client");
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if ( clientIntention != null){
					outStream.outln("Casier " + this.firstName + " started serving client " + clientIntention.getClient().getFirstName() +
							" who wants to perform " + clientIntention.getOperation() + "operation");
					boolean result = false; 
					emulateOperation(1000);
					
					switch (clientIntention.getOperation()){
						case PUT_MONEY_ON:                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          
							result = doDeposit(
									clientIntention.getClient(),
									clientIntention.getAmmount(),
									clientIntention.getFirstBill());
							break;
						case TAKE_MONEY_OFF:
							result = doWithdraw(
									clientIntention.getClient(),
									clientIntention.getAmmount(),
									clientIntention.getFirstBill());
							break;
						case TRANSFER:
							result = doTransfer(
									clientIntention.getClient(),
									clientIntention.getAmmount(), 
									clientIntention.getFirstBill(), 
									clientIntention.getSecondBill());
							break;						
					}					
					outStream.outln("Casier " + this.firstName + " end serving client " + clientIntention.getClient().getFirstName() +
							" with " + (result ? "success" : "fail") + " result");
					clientIntention.setSuccessEnd(result);
				}
			}
		}
		outStream.outln("Casier " + this.firstName + " stoped working");
	}
	
	private boolean doDeposit(Client client, int ammount, UUID billNumber){
		ClientBillQuery trans= (ClientBillQuery) QueryFactory.getInstance().getTransaction(
				this,
				client, 
				this.bank.getBills().get(billNumber),
				ammount,
				BankOperation.PUT_MONEY_ON);
		
		boolean transRez = executeTransaction(trans);
		
		return transRez;
	}
	
	private boolean doWithdraw(Client client, int ammount, UUID billNumber){
		ClientBillQuery trans= (ClientBillQuery) QueryFactory.getInstance().getTransaction(
				this,
				client, 
				this.bank.getBills().get(billNumber),
				ammount,
				BankOperation.TAKE_MONEY_OFF);
				
		boolean transRez = executeTransaction(trans);
		
		return transRez;		
	}
	
	private boolean doTransfer(Client client, int ammount, UUID firstBill, UUID secondBill){
		CrossBillQuery transaction = (CrossBillQuery) QueryFactory.getInstance().getTransaction(
				this,
				this.bank.getBills().get(firstBill),
				this.bank.getBills().get(secondBill),
				ammount);
		
		boolean transRez = executeTransaction(transaction);		
		return transRez;		
	}
	
	private boolean executeTransaction( BankQuery trans){
		if(trans.isRejected())
			return false;
		
		Thread thread = new Thread(trans);
		
		boolean rezult = false;
		synchronized(trans){
			thread.start();
			try {
				trans.wait();			
			} catch (InterruptedException e) {
			}
			rezult = operatinonComplete && opertionSuccessful;
		}
		
		QueryFactory.getInstance().releseTransaction(trans);
		return rezult;
	}
	
	private void emulateOperation(long miliseconds){
		try {
			Thread.sleep(miliseconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
