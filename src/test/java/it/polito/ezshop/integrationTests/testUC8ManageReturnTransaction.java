package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class testUC8ManageReturnTransaction {

    private EZShop ezshop;
    private User admin;
    private Integer testReturnID;

    @Before
    public void setupPreconditions() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidTransactionIdException, InvalidQuantityException, InvalidProductIdException {
        ezshop = new EZShop();
        ezshop.reset();
        Integer adminId = ezshop.createUser("admin", "admin", "Administrator");
        assertTrue(adminId > 0);
        Integer shopManagerId = ezshop.createUser("shopManager", "shopManager", "ShopManager");
        assertTrue(shopManagerId > 0);
        Integer cashierId = ezshop.createUser("cashier", "cashier", "Cashier");
        assertTrue(cashierId > 0);

        admin = ezshop.login("admin", "admin");
        assertNotNull(admin);

        admin = ezshop.getUser(adminId);
        assertNotNull(admin);
        User shopManager = ezshop.getUser(shopManagerId);
        assertNotNull(shopManager);
        User cashier = ezshop.getUser(cashierId);
        assertNotNull(cashier);
		
		//Setup mockup sale transaction
        Integer testTransactionID = ezshop.startSaleTransaction();
		assertTrue(testTransactionID > 0);
		int ptId = ezshop.createProductType("Blue Shirt – Small", "0123450000106", 9.90, "");
		assertTrue(ptId > 0);
        ezshop.updateQuantity(ptId,1);
        assertEquals(Integer.valueOf(1), ezshop.getProductTypeByBarCode("0123450000106").getQuantity());
		boolean addedProduct = ezshop.addProductToSale(testTransactionID, "0123450000106", 1);
		assertTrue(addedProduct);
		boolean transactionClosed = ezshop.endSaleTransaction(testTransactionID);
		assertTrue(transactionClosed);
		
		//Setup mockup return transaction
		testReturnID = ezshop.startReturnTransaction(testTransactionID);
		assertTrue(testReturnID > 0);
		boolean productReturned = ezshop.returnProduct(testReturnID, "0123450000106", 1);
		assertTrue(productReturned);
		boolean returnTransactionCommitted = ezshop.endReturnTransaction(testReturnID, true);
		assertTrue(returnTransactionCommitted);

        assertTrue(ezshop.logout());
    }

    @After
    public void resetPostConditions() {
        ezshop.logout();
        ezshop.reset();
    }

    @Test
    public void testScenario1() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, InvalidCreditCardException {
       
	   //RETURN VALID TRANSACTION, CREDIT CARD
	   
	   User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
		
		String validCreditCard = "4485370086510891"; //valid one taken from DAO initDB, with credit
		double expectedReturnedAmount = 9.90; 

		double returnedAmount = ezshop.returnCreditCardPayment(testReturnID, validCreditCard);
        assertEquals(returnedAmount, expectedReturnedAmount, 0.0);
    }
	
	@Test
    public void testScenario2() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException {
       
	   //RETURN VALID TRANSACTION, CASH
	   
	   User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
		
		double expectedReturnedAmount = 9.90; 

		double returnedAmount = ezshop.returnCashPayment(testReturnID);
        assertEquals(returnedAmount, expectedReturnedAmount, 0.0);
    }
	
}
