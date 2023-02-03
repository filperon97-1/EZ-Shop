package it.polito.ezshop.integrationTests;

import static org.junit.Assert.*;

import it.polito.ezshop.model.Order;
import it.polito.ezshop.model.ProductType;
import org.junit.*;

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
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidUserIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class testUC3ManageOrderTest {
	   private EZShop ezshop;
	    private User admin;
	    private User shopManager;
	    private User uLogged;
	    private Integer shopManagerId;
	    Integer Id;
	    int ptId=0;
	    @Before
	    public void userPreconditions() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidLocationException, InvalidProductIdException {
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

	        ezshop.recordBalanceUpdate(1000000);

	    }

	    @After
	    public void resetPostConditions() {
	    	ezshop.logout();
	        ezshop.reset();
	    }
	    @Test
	    public void testScenario3_1_1() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
	    	boolean thrown=false;
			try {
				ezshop.issueOrder("Cabbage", 3, 3.3);
			  } catch (Exception e) {
			    thrown = true;
			  }
			  assertTrue(thrown);
			  ezshop.logout();
			  
	    }
	    @Test
	    public void testScenario3_1_2() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidOrderIdException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
	    	ProductType p= (ProductType) ezshop.getProductTypesByDescription("Blue Shirt – Small").get(0);
	    	Integer Id=ezshop.issueOrder(p.getBarCode(), 3, 9.90);
	    	assertTrue(Id>0);
	    	Order o = (Order) ezshop.getAllOrders().stream().filter(or -> or.getOrderId().equals(Id)).findFirst().orElse(null);
	    	assertNotNull(o);
	    	assertEquals("ISSUED", o.getStatus());
	    }
	    @Test
	    public void testScenario3_2() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidOrderIdException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
			Integer Id=ezshop.issueOrder("0705632441947", 3, 9.90);
			assertTrue(Id>0);
	    	it.polito.ezshop.data.Order c1 = ezshop.getAllOrders().stream().filter(c -> c.getProductCode().equals("0705632441947")).findFirst().orElse(null);
	    	assertNotNull(c1);
	    	assertTrue(ezshop.payOrder(c1.getOrderId()));
	    	
	    }
	    @Test
	    public void testScenario3_3() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidOrderIdException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
	    	assertTrue(ezshop.recordBalanceUpdate(49.90));
			Integer Id=ezshop.issueOrder("0705632441947", 3, 9.90);
			assertTrue(Id>0);
			assertTrue(ezshop.payOrder(Id));
			it.polito.ezshop.data.Order c1 = ezshop.getAllOrders().stream().filter(c -> c.getProductCode().equals("0705632441947")).findFirst().orElse(null);
			assertNotNull(c1);
	    	assertTrue(ezshop.recordOrderArrival(c1.getOrderId()));
	    }
	    @Test
	    public void testScenario3_4() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidOrderIdException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
	    	assertTrue(ezshop.recordBalanceUpdate(49.90));
			Integer Id=ezshop.issueOrder("0705632441947", 5, 9.90);
			assertTrue(Id>0);
			Id=ezshop.payOrderFor("0705632441947", 5,9.90);
			assertTrue(Id>0);
			it.polito.ezshop.data.Order c1 = ezshop.getAllOrders().stream().filter(c -> c.getProductCode().equals("0705632441947")).findFirst().orElse(null);
			assertNotNull(c1);
	    	assertTrue(ezshop.recordOrderArrival(c1.getOrderId()));
	    }
	    @Test
	    public void testScenario3_5() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidOrderIdException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
	    	assertTrue(ezshop.recordBalanceUpdate(50.00));
	    	Integer Id=ezshop.issueOrder("0705632441947", 3, 15.50);
			assertTrue(Id>0);
			for (it.polito.ezshop.data.Order c:ezshop.getAllOrders()) {
				assertEquals(c.getQuantity(),3);
				assertTrue(c.getPricePerUnit()==15.50);
				assertEquals(c.getProductCode(),"0705632441947");
				assertEquals(c.getOrderId(),Id);
				assertEquals(c.getStatus(),"ISSUED");
			}
			Integer Id1=ezshop.payOrderFor("0705632441947", 3,15.50);
			assertTrue(Id>0);
			it.polito.ezshop.data.Order c1 = ezshop.getAllOrders().stream().filter(c -> c.getOrderId().equals(Id1)).findFirst().orElse(null);
			assertNotNull(c1);
			assertEquals(c1.getStatus(),"PAYED");
	    	assertTrue(ezshop.recordOrderArrival(c1.getOrderId()));
	    	 c1 = ezshop.getAllOrders().stream().filter(c -> c.getOrderId().equals(Id1)).findFirst().orElse(null);
	    	assertEquals(c1.getStatus(),"COMPLETED");
	    	assertTrue(ezshop.computeBalance()>0);
	    }
	    @Test
	    public void testScenario3_6() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidUserIdException, InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidQuantityException, InvalidOrderIdException {
	    	User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
	    	//assertTrue(ezshop.recordBalanceUpdate(50.00));
	    	Integer Id=ezshop.issueOrder("0705632441947", 3, 15.50);
			assertTrue(Id>0);

			//Pay order for an not existing product type
			Integer Id1=ezshop.payOrderFor("0705632441954", 3,15.50);
			assertTrue(Id1<0);

	    }
	    
	}