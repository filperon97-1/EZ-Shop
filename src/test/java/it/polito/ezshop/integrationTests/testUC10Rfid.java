package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRFIDException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;
import it.polito.ezshop.model.Product;
import it.polito.ezshop.model.ProductType;
public class testUC10Rfid {
	private EZShop ezshop;
    private User admin;
    private User shopManager;
    private User uLogged;
    private Integer shopManagerId;
    Integer Id;
    int ptId=0;
    Integer testTransactionID;
    @Before
    public void userPreconditions() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidLocationException, InvalidProductIdException, InvalidTransactionIdException, InvalidQuantityException {
        ezshop = new EZShop();

        Integer adminId = ezshop.createUser("admin", "admin", "Administrator");
        assertTrue(adminId >0);

        admin = ezshop.login("admin", "admin");
        assertNotNull(admin);
        ptId= ezshop.createProductType("Blue Shirt – Small", "0705632441947", 9.90, "note");
        assertTrue(ptId>0);
		assertTrue(ezshop.updatePosition(ptId, "012-abc5-92"));
        admin = ezshop.getUser(adminId);
        assertNotNull(admin);
        
        
        testTransactionID = ezshop.startSaleTransaction();
		assertTrue(testTransactionID > 0);	
		ezshop.updateQuantity(ptId,1);
        assertEquals(Integer.valueOf(1), ezshop.getProductTypeByBarCode("0705632441947").getQuantity());
		boolean addedProduct = ezshop.addProductToSale(testTransactionID, "0705632441947", 1);
		assertTrue(addedProduct);
		boolean transactionClosed = ezshop.endSaleTransaction(testTransactionID);
		assertTrue(transactionClosed);
		
        ezshop.recordBalanceUpdate(1000000);

    }

    @After
    public void resetPostConditions() {
    	ezshop.logout();
        ezshop.reset();
    }
	@Test
    public void testScenario10_1() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidOrderIdException, InvalidRFIDException {
    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
    	assertTrue(ezshop.recordBalanceUpdate(49.90));
    	//Product prod=new Product("1234567890","0705632441947");
		Integer Id=ezshop.issueOrder("0705632441947", 5, 9.90);
		assertTrue(Id>0);
		Id=ezshop.payOrderFor("0705632441947", 5,9.90);
		assertTrue(Id>0);
		it.polito.ezshop.data.Order c1 = ezshop.getAllOrders().stream().filter(c -> c.getProductCode().equals("0705632441947")).findFirst().orElse(null);
		assertNotNull(c1);
    	assertTrue(ezshop.recordOrderArrivalRFID(c1.getOrderId(), "001234567890"));
    }
	
	@Test
	public void testScenario10_2() throws InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException, InvalidRFIDException, InvalidUsernameException, InvalidPasswordException, InvalidPricePerUnitException, InvalidOrderIdException, InvalidLocationException {
		User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
    	assertTrue(ezshop.recordBalanceUpdate(49.90));
    	//Product prod=new Product("1234567890","0705632441947");
		Integer Id=ezshop.issueOrder("0705632441947", 5, 9.90);
		assertTrue(Id>0);
		Id=ezshop.payOrderFor("0705632441947", 5,9.90);
		assertTrue(Id>0);
		it.polito.ezshop.data.Order c1 = ezshop.getAllOrders().stream().filter(c -> c.getProductCode().equals("0705632441947")).findFirst().orElse(null);
		assertNotNull(c1);
    	assertTrue(ezshop.recordOrderArrivalRFID(c1.getOrderId(), "001234567890"));
    	 
    	testTransactionID = ezshop.startSaleTransaction();
 		assertTrue(testTransactionID > 0);	
    	
		it.polito.ezshop.model.ProductType pro=(ProductType) ezshop.getProductTypeByBarCode(c1.getProductCode());
		Optional<Product> prod = pro.getProductList().stream().findAny();
		assertFalse(Product.Status.BUYED.equals(prod.get().getStatus()));
		assertEquals(Integer.valueOf(5), ezshop.getProductTypeByBarCode("0705632441947").getQuantity());
		boolean addedProduct = ezshop.addProductToSaleRFID(testTransactionID, "001234567890");
		assertTrue(addedProduct);
		
		
		boolean deleted=ezshop.deleteProductFromSaleRFID(testTransactionID, "001234567890");
		assertTrue(deleted);
		boolean transactionClosed = ezshop.endSaleTransaction(testTransactionID);
		assertTrue(transactionClosed);
		
		
	}
	@Test
	public void testScenario10_3() throws InvalidTransactionIdException, UnauthorizedException, InvalidQuantityException, InvalidProductCodeException, InvalidRFIDException, InvalidOrderIdException, InvalidLocationException, InvalidPricePerUnitException, InvalidUsernameException, InvalidPasswordException {
		User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
    	assertTrue(ezshop.recordBalanceUpdate(49.90));
    	//Product prod=new Product("1234567890","0705632441947");
		Integer Id=ezshop.issueOrder("0705632441947", 5, 9.90);
		assertTrue(Id>0);
		Id=ezshop.payOrderFor("0705632441947", 5,9.90);
		assertTrue(Id>0);
		it.polito.ezshop.data.Order c1 = ezshop.getAllOrders().stream().filter(c -> c.getProductCode().equals("0705632441947")).findFirst().orElse(null);
		assertNotNull(c1);
    	assertTrue(ezshop.recordOrderArrivalRFID(c1.getOrderId(), "001234567890"));
    	
		testTransactionID = ezshop.startSaleTransaction();
		assertTrue(testTransactionID > 0);	
		assertEquals(Integer.valueOf(5), ezshop.getProductTypeByBarCode("0705632441947").getQuantity());
		boolean addedProduct = ezshop.addProductToSaleRFID(testTransactionID, "001234567890");
		assertTrue(addedProduct);
		
		boolean transactionClosed = ezshop.endSaleTransaction(testTransactionID);
		assertTrue(transactionClosed);
		Integer testReturnID = ezshop.startReturnTransaction(testTransactionID);
		assertTrue(testReturnID >= 0);
		boolean productReturned = ezshop.returnProductRFID(testReturnID, "001234567890");
		assertTrue(productReturned);
		transactionClosed = ezshop.endReturnTransaction(testReturnID,true);
		assertTrue(transactionClosed);
		
	}
}
