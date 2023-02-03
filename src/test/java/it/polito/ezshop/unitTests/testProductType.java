package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.model.ProductType;
public class testProductType {

	@Test
	public void test0() {
		boolean thrown=false;
		  try {
			  new ProductType(-1, 1, "loc", "not","productDescription", " barCode", 1.2);
			  } catch (Exception e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test1() {
		boolean thrown=false;
		  try {
			new ProductType(1,-1, "loc", "not","productDescription", " barCode", 1.2);
			  } catch (Exception e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test4() {
		boolean thrown=false;
		  try {
			  new ProductType(1, 1, "loc", "not","", " barCode", 1.2);
			  } catch (Exception e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void tes5() {
		boolean thrown=false;
		  try {
			  new ProductType(1, 1, "loc", "not","productDescription", "", 1.2);
			  } catch (Exception e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test6() {
		boolean thrown=false;
		  try {
			  new ProductType(1, 1, "loc", "not","productDescription", "barCode", -0.1);
			  } catch (Exception e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
	}
	@Test
	public void test7() throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException {
		assertNotNull(new ProductType(1, 1, "loc", "not","productDescription", "barCode", 1.1));

	}
	
	@Test
	public void test8() throws NumberFormatException {
			  assertTrue(ProductType.checkBarcode("0705632441947"));
	}
	@Test
	public void test9() throws NumberFormatException {
			  assertFalse(ProductType.checkBarcode("07056324447"));
	}
	@Test
	public void test10() throws NumberFormatException {
			  assertFalse(ProductType.checkBarcode("0744444056324447"));
	}

}
