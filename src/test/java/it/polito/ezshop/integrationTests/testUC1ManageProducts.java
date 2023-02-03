package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class testUC1ManageProducts {

    private EZShop ezshop;
    private User admin;
    private User shopManager;
    private User cashier;

    @Before
    public void userPreconditions() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
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
        shopManager = ezshop.getUser(shopManagerId);
        assertNotNull(shopManager);
        cashier = ezshop.getUser(cashierId);
        assertNotNull(cashier);

        assertTrue(ezshop.logout());
    }

    @After
    public void resetPostConditions() {
        ezshop.logout();
        ezshop.reset();
    }

    @Test
    public void testScenario1() throws UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException, InvalidPasswordException, InvalidUsernameException {
        User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
        int ptId = ezshop.createProductType("Blue Shirt – Small", "0123450000106", 9.90, "");
        assertTrue(ptId > 0);
        boolean updated = ezshop.updatePosition(ptId, "057-ab5Tn-4301");
        assertTrue(updated);
        ProductType pt = ezshop.getProductTypeByBarCode("0123450000106");
        assertNotNull(pt);
        assertEquals("Blue Shirt – Small", pt.getProductDescription());
        assertEquals("0123450000106", pt.getBarCode());
        assertEquals(Double.valueOf(9.90), pt.getPricePerUnit());
        assertEquals("", pt.getNote());
    }

    @Test
    public void testScenario2() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidLocationException, InvalidProductIdException {
        User uLogged = ezshop.login(shopManager.getUsername(), shopManager.getPassword());
        assertNotNull(uLogged);
        int ptId = ezshop.createProductType("Blue Shirt – Small", "0123450000106", 9.90, "");
        assertTrue(ptId > 0);
        boolean updated = ezshop.updatePosition(ptId, "057-ab5Tn-4301");
        assertTrue(updated);

        ProductType pt = ezshop.getProductTypeByBarCode("0123450000106");
        assertNotNull(pt);
        assertEquals("057-ab5Tn-4301", pt.getLocation());
        pt.setLocation("123-ab4F-093");
        updated = ezshop.updatePosition(pt.getId(), pt.getLocation());
        assertTrue(updated);

        pt = ezshop.getProductTypeByBarCode("0123450000106");
        assertNotNull(pt);
        assertEquals("123-ab4F-093", pt.getLocation());
    }

    @Test
    public void testScenario3() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {
        User uLogged = ezshop.login(shopManager.getUsername(), shopManager.getPassword());
        assertNotNull(uLogged);
        int ptId = ezshop.createProductType("Blue Shirt – Small", "0123450000106", 9.90, "");
        assertTrue(ptId > 0);

        ProductType pt = ezshop.getProductTypeByBarCode("0123450000106");
        assertNotNull(pt);
        double newPrice = 8.50;
        pt.setPricePerUnit(newPrice);
        boolean updated = ezshop.updateProduct(pt.getId(), pt.getProductDescription(), pt.getBarCode(), pt.getPricePerUnit(), pt.getNote());
        assertTrue(updated);

        pt = ezshop.getProductTypeByBarCode("0123450000106");
        assertNotNull(pt);
        assertEquals(Double.valueOf(newPrice), pt.getPricePerUnit());
    }

    @Test
    public void testScenario4() {
        assertThrows(UnauthorizedException.class, () -> ezshop.createProductType("Blue Shirt – Small", "0123450000106", 9.90, ""));
    }

    @Test
    public void testScenario5() throws InvalidPasswordException, InvalidUsernameException {
        User uLogged = ezshop.login(cashier.getUsername(), cashier.getPassword());
        assertNotNull(uLogged);
        assertThrows(UnauthorizedException.class, () -> ezshop.createProductType("Blue Shirt – Small", "0123450000106", 9.90, ""));
    }

    @Test
    public void testScenario6() throws InvalidPasswordException, InvalidUsernameException {
        User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
        String wrongBarCode = "0123450000105";
        assertThrows(InvalidProductCodeException.class, () -> ezshop.createProductType("Blue Shirt – Small", wrongBarCode, 9.90, ""));
        assertThrows(InvalidProductCodeException.class, () -> ezshop.getProductTypeByBarCode(wrongBarCode));
    }

    @Test
    public void testScenario7() throws UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
        String barCode = "0123450000106";
        assertThrows(InvalidProductDescriptionException.class, () -> ezshop.createProductType(null, barCode, 9.90, ""));
        assertNull(ezshop.getProductTypeByBarCode(barCode));

        assertThrows(InvalidProductDescriptionException.class, () -> ezshop.createProductType("", barCode, 9.90, ""));
        assertNull(ezshop.getProductTypeByBarCode(barCode));
    }

    @Test
    public void testScenario8() throws UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidLocationException, InvalidProductIdException {
        User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
        int ptId = ezshop.createProductType("Blue Shirt – Small", "0123450000106", 9.90, "");
        assertTrue(ptId > 0);
        assertThrows(InvalidLocationException.class, () -> ezshop.updatePosition(ptId, "err.-4301"));

        int ptId1 = ezshop.createProductType("Water- 6 Bottles", "0123450980101", 9.90, "");
        assertTrue(ptId1 > 0);
        boolean updated1 = ezshop.updatePosition(ptId1, "057-ab5Tn-4301");
        assertTrue(updated1);

        boolean updated = ezshop.updatePosition(ptId, "057-ab5Tn-4301");
        assertFalse(updated);
    }

    @Test
    public void testScenario9() throws UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException {
        User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
        String barCode = "0123450000106";
        assertThrows(InvalidPricePerUnitException.class, () -> ezshop.createProductType("Blue Shirt – Small", barCode, -5.3, ""));
        assertNull(ezshop.getProductTypeByBarCode(barCode));
    }

    @Test
    public void testScenario10() throws UnauthorizedException, InvalidProductCodeException, InvalidPasswordException, InvalidUsernameException, InvalidProductDescriptionException, InvalidPricePerUnitException {
        User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
        int ptId = ezshop.createProductType("Blue Shirt – Small", "0123450000106", 9.90, "");
        assertTrue(ptId > 0);

        int ptId1 = ezshop.createProductType("Water- 6 Bottles", "0123450000106", 9.90, "");
        assertFalse(ptId1 > 0);
    }

    @Test
    public void testScenario11() throws InvalidPasswordException, InvalidUsernameException, UnauthorizedException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductCodeException, InvalidProductIdException {
        User uLogged = ezshop.login(shopManager.getUsername(), shopManager.getPassword());
        assertNotNull(uLogged);
        int ptId = ezshop.createProductType("Blue Shirt – Small", "0123450000106", 9.90, "");
        assertTrue(ptId > 0);
        assertTrue(ezshop.deleteProductType(ptId));
    }
}
