package it.polito.ezshop.integrationTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({testUC1ManageProducts.class, testUC9Accounting.class, testUC3ManageOrderTest.class,
        testUC4CheckManageCustomersandCards.class, testUC2ManageUsersNRights.class, testUC10Rfid.class,
        testUC7ManagePayment.class, testUC8ManageReturnTransaction.class, testUC6ManageSaleTransaction.class, testUC5AuthenticateAuthorize.class })
public class IntegrationTests {
}
