package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.Test;

import it.polito.ezshop.model.ReturnTransaction;
import it.polito.ezshop.model.ReturnTransaction.Status;

public class testReturnTransaction {
		private LocalDate data=LocalDate.now();
		//private Status PAYED;
		Status Status=ReturnTransaction.Status.PENDING;
		@Test
		public void test0() {
			boolean thrown=false;
			  try {
				  assertNull(new  ReturnTransaction(-1,1, data));
				  } catch (IllegalArgumentException e) {
				    thrown = true;
				  }
				  assertTrue(thrown);
		}
		@Test
		public void test1() {
			boolean thrown=false;
			  try {
				  assertNull(new  ReturnTransaction(1,-1, data));
				  } catch (IllegalArgumentException e) {
				    thrown = true;
				  }
				  assertTrue(thrown);
				 
		}
		@Test
		public void test2() {
			boolean thrown=false;
			  try {
				  assertNull(new  ReturnTransaction(1,-1, null));
				  } catch (IllegalArgumentException e) {
				    thrown = true;
				  }
				  assertTrue(thrown);
		}
		@Test
		public void test3() {
			assertNotNull(new ReturnTransaction(1,1, data));

		}
		@Test
		public void test4() {
			boolean thrown=false;
			//ReturnTransaction t=null;
			  try {
				  assertNull( new  ReturnTransaction(-1,1, data, 0.1, "return", Status));
				  } catch (IllegalArgumentException e) {
				    thrown = true;
				  }
				  assertTrue(thrown);
				
		}
		@Test
		public void test5() {
			boolean thrown=false;
			  try {
				  assertNull(new  ReturnTransaction(1,-1, data, 0.1, "return", Status));
				  } catch (IllegalArgumentException e) {
				    thrown = true;
				  }
				  assertTrue(thrown);
			
		}
		@Test
		public void test6() {
			boolean thrown=false;
			  try {
				  assertNull(new  ReturnTransaction(1,1, null, 0.1, "return", Status));
				  } catch (IllegalArgumentException e) {
				    thrown = true;
				  }
				  assertTrue(thrown);
			
		}
		@Test
		public void test7() {
			boolean thrown=false;
			  try {
				  assertNotNull(new  ReturnTransaction(1,1, data, -0.1, "return", Status));
				  } catch (IllegalArgumentException e) {
				    thrown = true;
				  }
				  assertFalse(thrown);
				 
		}
		@Test
		public void test8() {
			boolean thrown=false;
			  try {
				  assertNull(new  ReturnTransaction(1,1, data, 0.1, "", Status));
				  } catch (IllegalArgumentException e) {
				    thrown = true;
				  }
				  assertTrue(thrown);
		}
		@Test
		public void test9() {
			boolean thrown=false;
			  try {
				  assertNull(new  ReturnTransaction(-1,1, data, 0.1, "return", null));
				  } catch (IllegalArgumentException e) {
				    thrown = true;
				  }
				  assertTrue(thrown);
		}
		@Test
		public void test10() {
			assertNotNull(new ReturnTransaction(1,1, data, 1.1, "return", Status));

		}
		
	}
