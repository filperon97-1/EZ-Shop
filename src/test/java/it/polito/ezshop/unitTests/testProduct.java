package it.polito.ezshop.unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.model.Product;
import it.polito.ezshop.model.Product.Status;
import it.polito.ezshop.model.ProductType;

public class testProduct {

@Test
public void test0() {
	assertNotNull(new Product("RFID"));

}

@Test
public void test1() {
	Product p=new Product("RFID" );
	p.setRFID("ok");
	assertTrue(p.getRFID().equals("ok"));

}

@Test
public void test2() {
	Status s= Product.Status.BUYED;
	Product p=new Product("RFID" );
	p.setStatus(s);
	assertTrue(p.getStatus().equals(Product.Status.BUYED));

}

}
