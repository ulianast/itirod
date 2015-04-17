package com.krasnova.lab4.Tests.Domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.krasnova.lab4.Domain.Bill;

public class BillTests {

	@Test
	public void compareToTest_Equal() {
		//Data preparation
		Bill bill1 = new Bill(12);
		Bill bill2 = bill1;
		bill2.putMoneyOn(12);
		
		//Test & Assert
		assertEquals(0, bill1.compareTo(bill2));
	}
	
	@Test
	public void compareToTest_GreatterAndSmaller() {
		//Data preparation
		Bill bill1 = new Bill(12);
		Bill bill2 = new Bill(13);
		
		if(bill1.getBillNumber().compareTo(bill2.getBillNumber()) > 0){
			assertEquals(1, bill1.compareTo(bill2));
		}else{
			assertEquals(-1, bill1.compareTo(bill2));
		}
	}
	
	@Test
	public void takeMoneyOffTest_LackOfFunds(){
		//Data preparation
		Bill bill1 = new Bill(100);
		//Test
		boolean result = bill1.takeMoneyOff(101);
		//Assert
		assertFalse(result);		
	}

}
