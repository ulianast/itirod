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
			if ( ! processedBills.contains(bill.getBillNumber())){			
				processedBills.add(bill.getBillNumber());  // sync from the box
			} else{
				try {
					bill.getBillNumber().wait();
				} catch (InterruptedException e) {

					return null;
				}
				processedBills.add(bill.getBillNumber()); //sync from the box
			}
			trans =  new ClientBillTransaction(casier, client, bill, ammount, op);
		}
		return trans;
	}
	
	synchronized public Transaction getTransaction(Casier casier, Bill bill_1, Bill bill_2, int ammount){	
		//trying to sync access to 2 bills in direct order
		UUID firstBill, secondBill;
		if (bill_1.compareTo(bill_2) < 0){
			firstBill = bill_1.getBillNumber();
			secondBill = bill_2.getBillNumber();
		}else{
			firstBill = bill_2.getBillNumber();
			secondBill = bill_1.getBillNumber();
		}
		CrossBillTransaction trans = null;
		
		synchronized(firstBill){
			synchronized(secondBill){
				syncBillAccess(firstBill);
				syncBillAccess(secondBill);
				trans = new CrossBillTransaction(casier, bill_1, bill_2, ammount);
			}
		}
		return trans;
	}
	
	synchronized public void releseTransaction(Transaction trans){
		releseBill(trans.firstBill.getBillNumber());
		if(CrossBillTransaction.class.isInstance(trans)){
			CrossBillTransaction cbTrans = (CrossBillTransaction)trans;
			releseBill(cbTrans.getSecondBill().getBillNumber());
		}
	}
	
	private void releseBill(UUID billNumber){
		synchronized (billNumber){
			processedBills.remove(billNumber);
			billNumber.notify();
		}
	}
	
	private void syncBillAccess(UUID billNumber){
		if ( ! processedBills.contains(billNumber)){			
			processedBills.add(billNumber);  // sync from the box
		} else{
			try {
				billNumber.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			processedBills.add(billNumber); //sync from the box
		}
	}
	
}
