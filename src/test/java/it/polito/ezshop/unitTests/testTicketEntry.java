package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.model.ProductType;
import it.polito.ezshop.model.TicketEntry;

public class testTicketEntry {
	@Test
	public void test0() {
		boolean thrown=false;
		  try {
			  TicketEntry t=new TicketEntry("","productDescription",1, -1.1, 1.1);
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test1() {
		boolean thrown=false;
		  try {
			  TicketEntry t=new TicketEntry("barCode","",1, -1.1, 1.1);
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test2() {
		boolean thrown=false;
		  try {
			  TicketEntry t=new TicketEntry("barCode","productDescription",-1, 1.1, 1.1);
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test3() {
		boolean thrown=false;
		  try {
			  TicketEntry t=new TicketEntry("barCode","productDescription",1, -1.1, 1.1);
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test4() {
		boolean thrown=false;
		  try {
			  TicketEntry t=new TicketEntry("barCode","productDescription",1, 1.1, -1.1);
			  } catch (IllegalArgumentException e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test5() {
		assertNotNull(new TicketEntry("barCode","productDescription",1, 1.1, 1.1));

	}
}
