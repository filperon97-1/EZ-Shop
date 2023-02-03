package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;


import java.util.List;

public class testUC5AuthenticateAuthorize {
    private static EZShop ezshop;
    private static  User admin;
    private static  User shopManager;
    private static User cashier;

    @BeforeClass
    public static void userPreconditions() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        ezshop = new EZShop();
        ezshop.reset();
        Integer adminId = ezshop.createUser("admin", "admin", "Administrator");
        assertTrue(adminId > 0);
        Integer shopManagerId = ezshop.createUser("shopManager", "shopManager", "ShopManager");
        assertTrue(shopManagerId > 0);
        Integer cashierId = ezshop.createUser("cashier", "cashier", "Cashier");
        assertTrue(cashierId > 0);
    }

    @After
    public void logOut(){
        ezshop.logout();
    }

    @AfterClass
    public static void resetPostConditions() {
        ezshop.reset();
    }

    // user exists and log in, check if he is authorized to do an action
    @Test
    public void testScenario5_1() throws InvalidUsernameException, InvalidPasswordException,UnauthorizedException {
        admin = ezshop.login("admin", "admin");
        assertNotNull(admin);
        List<Order> order=ezshop.getAllOrders();
    }

    // user logs out,check if it does not throw exceptions when logging out.
    // check that some exceptions thrown when try to do actions after logged out
    @Test
    public void testScenario5_2(){
    	boolean boolea=false;
        try{
            testScenario5_1();  // logIn
            ezshop.logout();
        }
        catch(Exception e){
        	boolea=true;
        }
        assertFalse(boolea);
        assertThrows(UnauthorizedException.class,()-> ezshop.getAllOrders());
    }

    // user does not exists and tries to log in
    @Test
    public void testScenario5_3() throws InvalidPasswordException, InvalidUsernameException{
        admin= ezshop.login("jjjj","jjjjj");
        assertNull(admin);
        assertThrows(UnauthorizedException.class,()-> ezshop.getAllOrders());
    }

    // user already logged in, another user logs in
    @Test
    public void testScenario5_4() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException{
        try{
            testScenario5_1();  // logIn
        }
        catch(Exception e){

        }
        shopManager= ezshop.login("shopManager", "shopManager");
        assertNotNull(shopManager);
        List<Order> order=ezshop.getAllOrders();
    }

    // no one logged in, tries to log out
    @Test
    public void testScenario5_5() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException{
        assertFalse(ezshop.logout());
    }

}
