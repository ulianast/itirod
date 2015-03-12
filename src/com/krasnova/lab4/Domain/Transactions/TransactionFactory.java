package com.krasnova.lab4.Domain.Transactions;

import java.util.Collections;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.*;

import com.krasnova.lab4.Domain.BankOperation;
import com.krasnova.lab4.Domain.Bill;
import com.krasnova.lab4.Domain.Casier;
import com.krasnova.lab4.Domain.Client;

public class TransactionFactory {
	private Map<Integer, Transaction > transPool;
	private TreeSet<UUID> processedBills;
	private static TransactionFactory INSTANCE = new TransactionFactory();
	
	private TransactionFactory(){
		transPool = new ConcurrentHashMap<Integer, Transaction> ();
		processedBills = new TreeSet<UUID>();
	}
	
	public static TransactionFactory getInstance(){
		return INSTANCE;
	}
	
	public Transaction getTransaction(Casier casier, Client client, Bill bill, int ammount, BankOperation op){	
		ClientBillTransaction trans = null;
		synchronized (bill.getBillNumber()) {
			if(processedBills.contains(bill.getBillNumber())){			
				try {
					bill.getBillNumber().wait();					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (processedBills) {
					processedBills.add(bill.getBillNumber());
				}
				trans =  new ClientBillTransaction(casier, client, bill, ammount, op);
			}else{
				synchronized (processedBills) {
					processedBills.add(bill.getBillNumber());
				}
				trans =  new ClientBillTransaction(casier, client, bill, ammount, op);
			}
		}
		return trans;
	}
	
	synchronized public Transaction getTransaction(Casier casier, Bill bill_1, Bill bill_2, int ammount){
		int key = bill_1.getBillNumber().hashCode() ^ bill_2.getBillNumber().hashCode();
		CrossBillTransaction trans;
		if(transPool.containsKey(key))
			trans = (CrossBillTransaction) transPool.get(key);
		else{
			trans = new CrossBillTransaction(casier, bill_1, bill_2, ammount); 
			transPool.put(key, trans);
		}
		return trans;
	}
	
	synchronized public void ReleseTransaction(Transaction trans){
		if(ClientBillTransaction.class.isInstance(trans)){
			ClientBillTransaction cbTrans = (ClientBillTransaction)trans;
			processedBills.remove(cbTrans.firstBill.getBillNumber());
			synchronized(cbTrans.firstBill.getBillNumber()){
				cbTrans.firstBill.getBillNumber().notify();
			}			
		}else if(ClientBillTransaction.class.isInstance(trans)){
			
		}
	}
	
	
}
