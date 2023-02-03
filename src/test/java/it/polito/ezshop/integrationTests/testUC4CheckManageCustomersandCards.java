package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import it.polito.ezshop.data.Customer;
import org.junit.*;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import it.polito.ezshop.exceptions.InvalidCustomerIdException;
import it.polito.ezshop.exceptions.InvalidCustomerNameException;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidOrderIdException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class testUC4CheckManageCustomersandCards {
	   private EZShop ezshop;
	    private User admin;
	    private User shopManager;
	    private User uLogged;
	    private Integer shopManagerId;
	    private Integer Id;

	    @Before
	    public void userPreconditions() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException, InvalidCustomerNameException {
	    	ezshop = new EZShop();
	        Integer adminId = ezshop.createUser("admin", "admin", "Administrator");
	        assertTrue(adminId >0);
	        

	        admin = ezshop.login("admin", "admin");
	        assertNotNull(admin);

	        admin = ezshop.getUser(adminId);
	        assertNotNull(admin);
	        Id=ezshop.defineCustomer("Giovanni");
	       
	    }

	    @After
	    public void resetPostConditions() {
	    	ezshop.logout();
	        ezshop.reset();
	    }

	    @Test
	    public void testScenario4_1() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidCustomerNameException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
	    	assertTrue(ezshop.defineCustomer("Paolo")>0);
	    }
	    @Test
	    public void testScenario4_2() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidOrderIdException, InvalidCustomerNameException, InvalidCustomerIdException, InvalidCustomerCardException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
	    	String t=ezshop.createCard();
	    	assertTrue(ezshop.attachCardToCustomer(t, Id));
	    }
	    @Test
	    public void testScenario4_3() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidOrderIdException, InvalidCustomerIdException, InvalidCustomerCardException, InvalidCustomerNameException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
			Customer c = ezshop.getCustomer(Id);
			assertNotNull(c);
	    	assertTrue(ezshop.modifyCustomer(c.getId(), c.getCustomerName(), null));
			c = ezshop.getCustomer(Id);
			assertNotNull(c);
			assertNull(c.getCustomerCard());
	    }
	    @Test
	    public void testScenario4_4() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidOrderIdException, InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
	    	assertTrue(ezshop.modifyCustomer(Id, "Aldo", ezshop.createCard()));
	    }
	    @Test
	    public void testScenario4_5() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidOrderIdException, InvalidCustomerIdException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
	    	assertTrue(ezshop.deleteCustomer(Id));
	    	
	    	
	    }
	    @Test
	    public void testScenario4_6() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidOrderIdException, InvalidCustomerIdException, InvalidCustomerNameException, InvalidCustomerCardException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
	    	assertTrue(ezshop.modifyCustomer(Id, "Aldo", ezshop.createCard()));
	    	assertTrue(ezshop.modifyCustomer(Id, "Aldo", ""));
	    	
	    }
	    @Test
	    public void testScenario4_7() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidOrderIdException, InvalidCustomerNameException, InvalidCustomerIdException, InvalidCustomerCardException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
	    	String t=ezshop.createCard();
	    	assertTrue(ezshop.attachCardToCustomer(t, Id));
	    	assertTrue(ezshop.modifyPointsOnCard(t, 300));
	    	it.polito.ezshop.data.Customer c1 = ezshop.getAllCustomers().stream().filter(c -> c.getId().equals(Id)).findFirst().orElse(null);
	    	assertEquals(c1.getCustomerCard(),t);
	    	assertEquals(c1.getCustomerName(),"Giovanni");
	    	assertEquals(c1.getId(),Id);
	    	assertTrue(c1.getPoints()==300);
	    }
	    
	}