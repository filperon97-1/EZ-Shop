package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.integrationTests.*;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import it.polito.ezshop.unitTests.testAccountBook;
import it.polito.ezshop.unitTests.testBalanceOperation;
import it.polito.ezshop.unitTests.testCreditCard;
import it.polito.ezshop.unitTests.testCustomer;
import it.polito.ezshop.unitTests.testLoyaltyCard;
import it.polito.ezshop.unitTests.testOrder;
import it.polito.ezshop.unitTests.testProduct;
import it.polito.ezshop.unitTests.testProductType;
import it.polito.ezshop.unitTests.testReturnTransaction;
import it.polito.ezshop.unitTests.testSaleTransaction;
import it.polito.ezshop.unitTests.testTicketEntry;
import it.polito.ezshop.unitTests.testUser;

@RunWith(Suite.class)
@Suite.SuiteClasses({testProduct.class,testAccountBook.class, testBalanceOperation.class, testCreditCard.class, testCustomer.class, 
			testLoyaltyCard.class, testOrder.class,  testProductType.class, testReturnTransaction.class,
			testSaleTransaction.class, testTicketEntry.class,testUser.class, IntegrationTests.class, Testezshop20210528.class})
public class TestEZShop {
    
}
