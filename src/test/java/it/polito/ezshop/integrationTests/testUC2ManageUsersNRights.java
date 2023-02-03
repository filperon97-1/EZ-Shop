package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class testUC2ManageUsersNRights {

    private EZShop ezshop;
    private User admin;
    private User shopManager;

    @Before
    public void userPreconditions() throws InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidUserIdException, UnauthorizedException {
        ezshop = new EZShop();
        ezshop.reset();
        Integer adminId = ezshop.createUser("admin", "admin", "Administrator");
        assertTrue(adminId > 0);
        Integer shopManagerId = ezshop.createUser("shopManager", "shopManager", "ShopManager");
        assertTrue(shopManagerId > 0);

        admin = ezshop.login("admin", "admin");
        assertNotNull(admin);

        admin = ezshop.getUser(adminId);
        assertNotNull(admin);
        shopManager = ezshop.getUser(shopManagerId);
        assertNotNull(shopManager);

        assertTrue(ezshop.logout());
    }

    @After
    public void resetPostConditions() {
        ezshop.logout();
        ezshop.reset();
    }

    @Test
    public void testScenario1() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
        User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);

        String username = "user1";
        String password = "pwd1";
        String role = "ShopManager";

        Integer userId = ezshop.createUser(username, password, role);
        assertTrue(userId > 0);

        User user = ezshop.getUser(userId);
        assertNotNull(user);
        assertEquals(user.getUsername(), username);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getRole(), role);
    }

    @Test
    public void testScenario2() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
        User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);

        String username = "user1";
        String password = "pwd1";
        String role = "ShopManager";

        Integer userId = ezshop.createUser(username, password, role);
        assertTrue(userId > 0);

        assertTrue(ezshop.deleteUser(userId));

        assertNull(ezshop.getUser(userId));
    }

    @Test
    public void testScenario3() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
        User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);

        String username = "user1";
        String password = "pwd1";
        String role = "ShopManager";

        Integer userId = ezshop.createUser(username, password, role);
        assertTrue(userId > 0);

        User user = ezshop.getUser(userId);
        assertNotNull(user);
        String newRole = "Cashier";
        user.setRole(newRole);
        assertTrue(ezshop.updateUserRights(user.getId(), user.getRole()));

        user = ezshop.getUser(userId);
        assertNotNull(user);
        assertEquals(user.getRole(), newRole);
    }

    @Test
    public void testScenario4() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, InvalidUserIdException, UnauthorizedException {
        User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);

        String username = "user1";
        String password = "pwd1";
        String role = "ShopManager";

        Integer userId = ezshop.createUser(username, password, role);
        assertTrue(userId > 0);
        User user = ezshop.getUser(userId);
        assertNotNull(user);

        assertTrue(ezshop.logout());

        uLogged = ezshop.login(shopManager.getUsername(), shopManager.getPassword());
        assertNotNull(uLogged);
        assertThrows(UnauthorizedException.class, () -> ezshop.deleteUser(userId));
        assertTrue(ezshop.logout());

        uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);
        User user2 = ezshop.getUser(userId);
        assertNotNull(user2);
        assertEquals(user2.getUsername(), user.getUsername());
        assertEquals(user2.getPassword(), user.getPassword());
        assertEquals(user2.getRole(), user.getRole());
    }

    @Test
    public void testScenario5() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException, UnauthorizedException {
        User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);

        String username = "user1";
        String password = "pwd1";
        String role = "ShopManager";

        Integer userId = ezshop.createUser(username, password, role);
        assertTrue(userId > 0);

        username = "user2";
        password = "pwd2";
        role = "Cashier";

        userId = ezshop.createUser(username, password, role);
        assertTrue(userId > 0);

        username = "user3";
        password = "pwd3";
        role = "Administrator";

        userId = ezshop.createUser(username, password, role);
        assertTrue(userId > 0);

        List<User> listUsers = ezshop.getAllUsers();
        assertEquals(5, listUsers.size());

    }

    @Test
    public void testScenario6() throws InvalidPasswordException, InvalidUsernameException, InvalidRoleException {
        User uLogged = ezshop.login(admin.getUsername(), admin.getPassword());
        assertNotNull(uLogged);

        String username = "user1";
        String password = "pwd1";
        String role = "ShopManager";

        Integer userId = ezshop.createUser(username, password, role);
        assertTrue(userId > 0);

        username = "user1";
        password = "pwd2";
        role = "Cashier";

        userId = ezshop.createUser(username, password, role);
        assertFalse(userId > 0);
    }
}
