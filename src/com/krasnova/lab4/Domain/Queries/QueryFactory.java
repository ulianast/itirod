package com.krasnova.lab4.Domain.Queries;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.*;

import lombok.Setter;

import com.krasnova.lab4.Domain.BankDeamon;
import com.krasnova.lab4.Domain.BankOperation;
import com.krasnova.lab4.Domain.Bill;
import com.krasnova.lab4.Domain.Casier;
import com.krasnova.lab4.Domain.Client;

public class QueryFactory {
	private TreeSet<UUID> processedBills;
	private Queue<Client> processedClients;
	private static QueryFactory INSTANCE = new QueryFactory();
	@Setter
	private BankDeamon deamon;	
	
	private QueryFactory(){
		processedBills = new TreeSet<UUID>();
		processedClients = new ConcurrentLinkedQueue<Client>();
	}
	
	public static QueryFactory getInstance(){
		return INSTANCE;
	}	
	
	public BankQuery getTransaction(Casier casier, Client client, Bill bill, int ammount, BankOperation op){
		if (isRejected(bill) || isRejected(client)){
    		ClientBillQuery rejTrans = new ClientBillQuery(casier, client, bill, ammount, op);
    		rejTrans.setRejected(true);
    		return rejTrans;
    	}
		ClientBillQuery trans = null;
		synchronized (bill.getBillNumber()) {
			syncBillAccess(bill.getBillNumber());			
			synchronized(client){
				syncClientAccess(client);
				trans =  new ClientBillQuery(casier, client, bill, ammount, op);
			}
		}
		return trans;
	}
	
    public BankQuery getTransaction(Casier casier, Bill bill_1, Bill bill_2, int ammount){	
    	if (isRejected(bill_1) || isRejected(bill_2)){
    		CrossBillQuery rejTrans = new CrossBillQuery(casier, bill_1, bill_2, ammount);
    		rejTrans.setRejected(true);
    		return rejTrans;
    	}
		//trying to sync access to 2 bills in direct order
		UUID firstBill, secondBill;
		if (bill_1.compareTo(bill_2) < 0){
			firstBill = bill_1.getBillNumber();
			secondBill = bill_2.getBillNumber();
		}else{
			firstBill = bill_2.getBillNumber();
			secondBill = bill_1.getBillNumber();
		}
		CrossBillQuery trans = null;
		
		synchronized(firstBill){			
			syncBillAccess(firstBill);
			synchronized(secondBill){				
				syncBillAccess(secondBill);
				trans = new CrossBillQuery(casier, bill_1, bill_2, ammount);
			}
		}
		return trans;
	}
    
    //this method is used by BankDeamon
    public BankQuery getTransaction(Bill bill_1, BankDeamon deamon){
    	BankDeamonBillQuery trans = null;
    	synchronized(bill_1.getBillNumber()){
    		syncBillAccess(bill_1.getBillNumber());
    		trans = new BankDeamonBillQuery(bill_1, deamon);
    	}
    	return trans;
    }
    
  //this method is used by BankDeamon
    public BankQuery getTransaction(Client client, BankDeamon deamon){
    	DeamonClientQuery trans = null;
    	synchronized(client){
    		syncClientAccess(client);
    		trans = new DeamonClientQuery(client, deamon);
    	}
    	return trans;
    }
	
	public void releseTransaction(BankQuery trans){
		if (ClientBillQuery.class.isInstance(trans) 
			|| CrossBillQuery.class.isInstance(trans)
			|| BankDeamonBillQuery.class.isInstance(trans)){
			
			releseBill(trans.firstBill.getBillNumber());
			if(CrossBillQuery.class.isInstance(trans)){
				CrossBillQuery cbTrans = (CrossBillQuery)trans;
				releseBill(cbTrans.getSecondBill().getBillNumber());
			} else if (ClientBillQuery.class.isInstance(trans)){
				releseClient(trans.client);
			}
			return;
		} else if(DeamonClientQuery.class.isInstance(trans)){
			releseClient(trans.client);
		}
	}
	
	private boolean isRejected(Bill bill){
		if(deamon == null ){
			return false;
		}
		
		boolean result = false;
		synchronized(deamon.getCheckedBills()) {
			if( ! deamon.getCheckedBills().containsKey(bill.getBillNumber())) {
				result = true;
			}
		}
		return result;
	}
	
	private boolean isRejected(Client client){
		if(deamon == null ){
			return false;
		}
		
		boolean result = false;
		synchronized(deamon.getCheckedClients()) {
			if( ! deamon.getCheckedClients().containsKey(client.getFirstName() + client.getLastName())) {
				result = true;
			}
		}
		return result;
	}
	
	private void releseBill(UUID billNumber){
		synchronized (billNumber){
			processedBills.remove(billNumber);
			billNumber.notify();
		}
	}
	
	private void releseClient(Client client){
		synchronized (client){
			processedClients.remove(client);
			client.notify();
		}
	}
	
	private void syncBillAccess(UUID billNumber){
		if ( ! processedBills.contains(billNumber)){
			processedBills.add(billNumber);  // sync from the box
		} else{
			try {
				billNumber.wait();				
			} catch (InterruptedException e) {
			}
			processedBills.add(billNumber); //sync from the box
		}
	}
	
	private void syncClientAccess(Client client){
		if ( ! processedClients.contains(client)){
			processedClients.add(client);  // sync from the box
		} else{
			try {
				client.wait();				
			} catch (InterruptedException e) {
			}
			processedClients.add(client);  // sync from the box
		}
	}
	
}
