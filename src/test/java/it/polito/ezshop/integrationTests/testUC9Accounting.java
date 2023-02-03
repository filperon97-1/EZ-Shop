package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class testUC9Accounting {
    private EZShop ezshop;
    private User shopManager;

    @Before
    public void userPreconditions() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException, InvalidQuantityException, InvalidLocationException, InvalidOrderIdException {
        ezshop = new EZShop();
        ezshop.reset();
        Integer adminId = ezshop.createUser("admin", "admin", "Administrator");
        assertTrue(adminId > 0);
        Integer shopManagerId = ezshop.createUser("shopManager", "shopManager", "ShopManager");
        assertTrue(shopManagerId > 0);

        User admin = ezshop.login("admin", "admin");
        assertNotNull(admin);

        admin = ezshop.getUser(adminId);
        assertNotNull(admin);
        shopManager = ezshop.getUser(shopManagerId);
        assertNotNull(shopManager);


        ezshop.recordBalanceUpdate(150.93);

        Integer productId = ezshop.createProductType("prodotto", "0123450000106", 2.50, "no");
        assertTrue(productId >0);
        ezshop.updateQuantity(productId,10);
        assertEquals(Integer.valueOf(10), ezshop.getProductTypeByBarCode("0123450000106").getQuantity());
        assertTrue(ezshop.updatePosition(productId, "012-abc5-92"));

        Integer orderId = ezshop.payOrderFor("0123450000106", 3, 1.95);
        assertTrue(orderId >0);
        assertTrue(ezshop.recordOrderArrival(orderId));
        assertEquals(Integer.valueOf(13), ezshop.getProductTypeByBarCode("0123450000106").getQuantity());

        assertEquals(145.08, ezshop.computeBalance(), 0.1);

        assertTrue(ezshop.logout());
    }

    @After
    public void resetPostConditions() {
        ezshop.logout();
        ezshop.reset();
    }

    @Test
    public void testScenario1() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException {
        User uLogged = ezshop.login(shopManager.getUsername(), shopManager.getPassword());
        assertNotNull(uLogged);
        LocalDate from = LocalDate.now().minusDays(1);
        LocalDate to = null;
        int expectedSize = 2;
        List<BalanceOperation> list = ezshop.getCreditsAndDebits(from, to);
        assertEquals(expectedSize, list.size());

        from = null;
        to = null;
        expectedSize = 2;
        list = ezshop.getCreditsAndDebits(from, to);
        assertEquals(expectedSize, list.size());

        from = null;
        to = LocalDate.now().minusDays(1);
        expectedSize = 0;
        list = ezshop.getCreditsAndDebits(from, to);
        assertEquals(expectedSize, list.size());

        from = LocalDate.now().minusDays(1);
        to = LocalDate.now().plusDays(1);
        expectedSize = 2;
        list = ezshop.getCreditsAndDebits(from, to);
        assertEquals(expectedSize, list.size());
    }
}
