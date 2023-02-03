package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class testUC7ManagePayment {

    private EZShop ezshop;
    private User admin;
    private Integer testTransactionTicketID; //Both transactionID and ticket ID (needed for testing as ticket is not managed directly anywhere)

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
		testTransactionTicketID = ezshop.startSaleTransaction();
		assertTrue(testTransactionTicketID > 0);
		int ptId = ezshop.createProductType("Blue Shirt – Small", "0123450000106", 9.90, "");
		assertTrue(ptId > 0);
        ezshop.updateQuantity(ptId,1);
        assertEquals(Integer.valueOf(1), ezshop.getProductTypeByBarCode("0123450000106").getQuantity());
		boolean addedProduct = ezshop.addProductToSale(testTransactionTicketID, "0123450000106", 1);
		assertTrue(addedProduct);
		boolean transactionClosed = ezshop.endSaleTransaction(testTransactionTicketID);
		assertTrue(transactionClosed);

        assertTrue(ezshop.logout());
    }

    @After
    public void resetPostConditions() {
        ezshop.logout();
        ezshop.reset();
    }

    @Test
    public void testScenario1() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, InvalidCreditCardException {
       
	   //VALID CREDIT CARD, CONFIRM PAYMENT
	   
	   User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
		
		String validCreditCard = "4485370086510891"; //valid one taken from DAO initDB, with enough credit

		boolean paymentSuccessful = ezshop.receiveCreditCardPayment(testTransactionTicketID, validCreditCard);
		assertTrue(paymentSuccessful);
	
    }
	
	@Test
    public void testScenario2() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, InvalidCreditCardException {
       
	   //INVALID CREDIT CARD
	   
	   User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
		
		String wrongCreditCard = "00000000";
		String validCreditCardButNotInDAO = "5424180123456789";	
		
		assertThrows(InvalidCreditCardException.class, () -> ezshop.receiveCreditCardPayment(testTransactionTicketID, wrongCreditCard));	
		boolean paymentFailed = ezshop.receiveCreditCardPayment(testTransactionTicketID, validCreditCardButNotInDAO);
		assertFalse(paymentFailed);
    }
	
	@Test
    public void testScenario3() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, InvalidCreditCardException {
       
	   //VALID CARD BUT NOT ENOUGH CREDIT
	   
	   User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
		
		String validCreditCardNoCredit = "4716258050958645"; //valid one taken from DAO initDB, with no credit
		
		boolean paymentFailed = ezshop.receiveCreditCardPayment(testTransactionTicketID, validCreditCardNoCredit);
		assertFalse(paymentFailed);
    }
	
	@Test
    public void testScenario4() throws UnauthorizedException, InvalidPasswordException, InvalidUsernameException, InvalidTransactionIdException, InvalidPaymentException {
       
	   //CASH PAYMENT, ENOUGH MONEY
	   
	   User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
		
		double cash = 20;	
		double expectedChange = 20-9.90; //In before section, added to transaction one single product of price 9.90
		
		double change = ezshop.receiveCashPayment(testTransactionTicketID, cash);
        assertEquals(change, expectedChange, 0.0);
    }
	
	@Test
    public void testScenario5() throws InvalidPasswordException, InvalidUsernameException {
       
	   //INVALID TICKET ID
	   
	   User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
		
		Integer invalidTicketID = 0;

		assertThrows(InvalidTransactionIdException.class, () -> ezshop.receiveCashPayment(invalidTicketID, 20));
    }

}
