package com.krasnova.lab4.Domain;

import java.util.UUID;

import com.krasnova.lab4.Domain.Transactions.ClientBillTransaction;
import com.krasnova.lab4.Domain.Transactions.CrossBillTransaction;
import com.krasnova.lab4.Domain.Transactions.Transaction;
import com.krasnova.lab4.Domain.Transactions.TransactionFactory;

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
	
	public Casier(String p_firstName, String p_SecondName, Bank p_bank){
		this.bank = p_bank;
		this.firstName = p_firstName;
		this.lastName = p_SecondName;
	}

	@Override
	public void run() {
		System.out.println("Casier " + this.firstName + " begins working");
		while(bank.isWorking()){
			synchronized(this){
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if ( clientIntention != null){
					System.out.println("Casier" + this.firstName + "started serving client " + clientIntention.getClient().getFirstName() +
							" who wants to perform " + clientIntention.getOperation() + "operation");
					System.out.println();
					boolean result = false;
					int ammount; 
					
					switch (clientIntention.getOperation()){
						case CHECK_AMMOUNT:
							clientIntention.setAmmount (checkMoney(
									clientIntention.getClient(), 
									clientIntention.getFirstBill()));
							break;
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
					System.out.println("Casier" + this.firstName + "end serving client " + clientIntention.getClient().getFirstName() +
							" with " + (result ? "success" : "fail") + " result");
				}
			}
		}
		System.out.println("Casier " + this.firstName + " stoped working");
	}
	
	private boolean doDeposit(Client client, int ammount, UUID billNumber){
		ClientBillTransaction trans= (ClientBillTransaction) TransactionFactory.getInstance().getTransaction(
				this,
				client, 
				this.bank.getBills().get(billNumber),
				ammount,
				BankOperation.PUT_MONEY_ON);
		
		System.out.println("");
		boolean transRez = executeTransaction(trans);
		
		return transRez;
	}
	
	private boolean doWithdraw(Client client, int ammount, UUID billNumber){
		ClientBillTransaction trans= (ClientBillTransaction) TransactionFactory.getInstance().getTransaction(
				this,
				client, 
				this.bank.getBills().get(billNumber),
				ammount,
				BankOperation.TAKE_MONEY_OFF);
				
		boolean transRez = executeTransaction(trans);
		
		return transRez;		
	}
	
	private int checkMoney(Client client, UUID billNumber){
		return client.getBills().get(billNumber).getAmmount();
	}
	
	private boolean doTransfer(Client client, int ammount, UUID firstBill, UUID secondBill){
		CrossBillTransaction transaction = (CrossBillTransaction) TransactionFactory.getInstance().getTransaction(
				this,
				this.bank.getBills().get(firstBill),
				this.bank.getBills().get(secondBill),
				ammount);
		
		boolean transRez = executeTransaction(transaction);		
		return transRez;		
	}
	
	private boolean executeTransaction( Transaction trans){
		Thread thread = new Thread(trans);
		
		boolean rezult = false;
		synchronized(trans){
			thread.start();
			try {
				trans.wait();
				rezult = operatinonComplete && opertionSuccessful;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}
		
		TransactionFactory.getInstance().releseTransaction(trans);
		return rezult;
	}
}
