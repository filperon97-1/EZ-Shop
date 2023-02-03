package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import it.polito.ezshop.model.SaleTransaction;
import it.polito.ezshop.model.TicketEntry;
import it.polito.ezshop.model.SaleTransaction.Status;

public class testSaleTransaction {
	private List t=new ArrayList<TicketEntry>();
	private Status Status;
	@Test
	public void test0() {
		boolean thrown=false;
		  try {
			  //(int balanceID, LocalDate date, String type, Integer ticketNumber, List<TicketEntry> entries, double discountRate, double price, Status tatus) {

			  SaleTransaction h=new SaleTransaction(1,  LocalDate.now(), "debit", -1, t, 1.1, 1.1, Status);
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test1() {
		boolean thrown=false;
		  try {
			  SaleTransaction h=new SaleTransaction(1,  LocalDate.now(), "debit",1, null, 1.1, 1.1,Status);
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test2() {
		boolean thrown=false;
		  try {
			  SaleTransaction h=new SaleTransaction(1,  LocalDate.now(), "debit",1, t, -1.1, 1.1,Status);
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test3() {
		boolean thrown=false;
		  try {
			  SaleTransaction h=new SaleTransaction(1,  LocalDate.now(), "debit",1, t, 1.1, -1.1,Status);
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test4() {
		
		assertNotNull(new SaleTransaction(1,  LocalDate.now(), "debit",1, t, 1.1, 1.1,Status ));

	}
	 SaleTransaction h=new SaleTransaction(1,  LocalDate.now(), "debit",1, t, 1.1, 1.1,Status);
	//@SuppressWarnings("deprecation")
	@Test
	public void test5() {
		assertNotNull( h.getTotalAmount());
	}
}
