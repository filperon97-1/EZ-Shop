package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.model.Order;


public class testOrder {
	@Test
	public void test0() {
		boolean thrown=false;
		  try {
			  new Order(-1, 1, "productCode", 1.1, 1, "PAYED");
			  } catch (Exception e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test2() {
		boolean thrown=false;
		  try {
			  new Order(1, 1, "", 1.1, 1, "PAYED");
			  } catch (Exception e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test3() {
		boolean thrown=false;
		  try {
			  new Order(1, 1, "productCode", -1.1, 1, "PAYED");
			  } catch (Exception e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test4() {
		boolean thrown=false;
		  try {
			  new Order(1, 1, "productCode", 1.1, -1, "PAYED");
			  } catch (Exception e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test5() {
		boolean thrown=false;
		  try {
			  new Order(1, 1, "productCode", 1.1, 1, "");
			  } catch (Exception e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test6() {
		assertNotNull(new Order(1, 1, "productCode", 1.1, 1, "PAYED"));

	}

}
