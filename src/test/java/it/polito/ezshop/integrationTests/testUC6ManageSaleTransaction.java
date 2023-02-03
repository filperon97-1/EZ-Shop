package it.polito.ezshop.integrationTests;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.exceptions.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class testUC6ManageSaleTransaction {
    private EZShop ezshop;
    private Integer transactionId;
    private final String barcode="0123450000106";
    private final String barcode2="0123450980101";
    private final Integer quantity=10;
    private final double pricePerUnit=2;
    private final double pricePerUnit2=45.60;
    private Integer customerId;
    private String card;

    @Before
    public void userPreconditions() throws InvalidCustomerNameException, InvalidCustomerIdException, InvalidPasswordException, InvalidRoleException, InvalidUsernameException, InvalidProductIdException, UnauthorizedException,
                                            InvalidCustomerCardException, InvalidProductDescriptionException,InvalidProductCodeException, InvalidPricePerUnitException {
        ezshop = new EZShop();
        ezshop.reset();
        Integer adminId = ezshop.createUser("admin", "admin", "Administrator");
        //System.out.println(adminId);
        assertTrue(adminId > 0);
        Integer shopManagerId = ezshop.createUser("shopManager", "shopManager", "ShopManager");
        assertTrue(shopManagerId > 0);
        Integer cashierId = ezshop.createUser("cashier", "cashier", "Cashier");
        assertTrue(cashierId > 0);

        ezshop.login("admin","admin");
        Integer productId = ezshop.createProductType("prodotto", barcode, pricePerUnit, "no");
        ezshop.updateQuantity(productId,quantity);
        Integer productId2 = ezshop.createProductType("sfddsf", barcode2, pricePerUnit2, "no");
        ezshop.updateQuantity(productId2,quantity);
        customerId=ezshop.defineCustomer("Giorberto");
        assertNotNull(customerId);
        card=ezshop.createCard();
        assertNotNull(card);
        assertTrue(ezshop.attachCardToCustomer(card, customerId));

    }

    @After
    public void resetPostConditions() {
        ezshop.logout();
        ezshop.reset();
    }

    // sale transaction with enough product quantity. Check quantity updated correctly and price is correct
    @Test
    public void testScenario6_1() throws UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidCreditCardException{
        int q=3;
        transactionId=ezshop.startSaleTransaction();
        assertTrue(ezshop.addProductToSale(transactionId,barcode,q));
        ProductType prod=ezshop.getProductTypeByBarCode(barcode);
        assertEquals((int) prod.getQuantity(), quantity - q);
        //can use getsaleTransaction only after endSaleTransaction
        assertNull(ezshop.getSaleTransaction(transactionId));
        assertTrue(ezshop.endSaleTransaction(transactionId));
        SaleTransaction s=ezshop.getSaleTransaction(transactionId);
        assertEquals(pricePerUnit * q, s.getPrice(), 0.0);

        String validCreditCard = "4485370086510891"; //valid one taken from DAO initDB, with enough credit
        boolean paymentSuccessful = ezshop.receiveCreditCardPayment(transactionId, validCreditCard);
        assertTrue(paymentSuccessful);
        assertEquals(ezshop.computeBalance(), q * pricePerUnit, 0.0);
    }

    // sale transaction with enough product quantity.A product get a discount
    @Test
    public void testScenario6_2() throws InvalidDiscountRateException, UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException{
        int q=3;
        double discountRate=0.25;
        transactionId=ezshop.startSaleTransaction();
        assertTrue(ezshop.addProductToSale(transactionId,barcode,q));
        ProductType prod=ezshop.getProductTypeByBarCode(barcode);
        assertEquals((int) prod.getQuantity(), quantity - q);
        //can use getsaleTransaction only after endSaleTransaction
        assertNull(ezshop.getSaleTransaction(transactionId));
        assertTrue(ezshop.applyDiscountRateToProduct(transactionId,barcode,discountRate));
        assertTrue(ezshop.endSaleTransaction(transactionId));
        SaleTransaction s=ezshop.getSaleTransaction(transactionId);
        double fullPrice = prod.getPricePerUnit() * q;
        assertEquals( fullPrice - (fullPrice*discountRate), s.getPrice(), 0.5);
    }

    // sale transaction with enough product quantity.The whole transaction gets a discount
    @Test
    public void testScenario6_3() throws InvalidDiscountRateException, UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException{
        int q=3;
        double discountRate=0.5;
        transactionId=ezshop.startSaleTransaction();
        assertTrue(ezshop.addProductToSale(transactionId,barcode,q));
        ProductType prod=ezshop.getProductTypeByBarCode(barcode);
        assertEquals((int) prod.getQuantity(), quantity - q);
        //can use getsaleTransaction only after endSaleTransaction
        SaleTransaction st = ezshop.getSaleTransaction(transactionId);
        assertNull(st);
        assertTrue(ezshop.applyDiscountRateToSale(transactionId,discountRate));
        assertTrue(ezshop.endSaleTransaction(transactionId));
        SaleTransaction s=ezshop.getSaleTransaction(transactionId);
        double fullPrice = prod.getPricePerUnit() * q;
        assertEquals(fullPrice - (fullPrice*discountRate), s.getPrice(), 0.5);
    }

    // sale transaction with enough product quantity. Update points on card, check if right amount of points and if card updated
    @Test
    public void testScenario6_4() throws InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException{
        int q=5;
        transactionId=ezshop.startSaleTransaction();
        assertTrue(ezshop.addProductToSale(transactionId,barcode2,q));
        ProductType prod=ezshop.getProductTypeByBarCode(barcode2);
        assertEquals((int) prod.getQuantity(), quantity - q);
        ezshop.endSaleTransaction(transactionId);
        int point=ezshop.computePointsForSale(transactionId);
        SaleTransaction s=ezshop.getSaleTransaction(transactionId);
        assertEquals(point, (int) s.getPrice() / 10);

        assertTrue(ezshop.modifyPointsOnCard(card,point));
        Customer c=ezshop.getCustomer(customerId);
        assertEquals((int) c.getPoints(), point);
    }

    // sale transaction with enough product quantity. Delete transaction, check quantity doesn't change
    @Test
    public void testScenario6_5() throws UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException{
        int q=5;
        transactionId=ezshop.startSaleTransaction();
        assertTrue(ezshop.addProductToSale(transactionId,barcode2,q));
        ProductType prod=ezshop.getProductTypeByBarCode(barcode2);
        assertEquals((int) prod.getQuantity(), quantity - q);
        ezshop.endSaleTransaction(transactionId);

        ezshop.deleteSaleTransaction(transactionId);
        prod=ezshop.getProductTypeByBarCode(barcode2);
        assertEquals((int) prod.getQuantity(), (int) quantity);
    }

    // not enough quantity for product
    @Test
    public void testScenario6_6() throws UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException{
        int q=50;
        transactionId=ezshop.startSaleTransaction();
        assertFalse(ezshop.addProductToSale(transactionId,barcode2,q));
        ProductType prod=ezshop.getProductTypeByBarCode(barcode2);
        assertEquals((int) prod.getQuantity(), (int) quantity);
        ezshop.endSaleTransaction(transactionId);
        ezshop.deleteSaleTransaction(transactionId);
    }

    // product code does not exist
    @Test
    public void testScenario6_7() throws UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException{
        transactionId=ezshop.startSaleTransaction();
        assertFalse(ezshop.addProductToSale(transactionId,"0123456789005",1));
        ezshop.endSaleTransaction(transactionId);
    }

    // add product in sale transaction then delete it
    @Test
    public void testScenario6_8() throws UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException{
        int q=3;
        transactionId=ezshop.startSaleTransaction();
        assertTrue(ezshop.addProductToSale(transactionId,barcode,q));
        assertTrue(ezshop.addProductToSale(transactionId,barcode2,q));
        assertTrue(ezshop.deleteProductFromSale(transactionId,barcode,q));
        ProductType prod=ezshop.getProductTypeByBarCode(barcode);
        assertEquals((int) prod.getQuantity(), (int) quantity);
        prod=ezshop.getProductTypeByBarCode(barcode2);
        assertEquals((int) prod.getQuantity(), quantity - q);
    }

    // delete product from sale transaction even though it's not there
    // delete product quantity>actual quantity
    @Test
    public void testScenario6_9() throws UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException{
        int q=3;
        transactionId=ezshop.startSaleTransaction();
        assertTrue(ezshop.addProductToSale(transactionId,barcode,q));
        assertFalse(ezshop.deleteProductFromSale(transactionId,barcode2,q));
        assertFalse(ezshop.deleteProductFromSale(transactionId,barcode,q+5));
        ProductType prod=ezshop.getProductTypeByBarCode(barcode2);
        assertEquals((int) prod.getQuantity(), (int) quantity);
        prod=ezshop.getProductTypeByBarCode(barcode);
        assertEquals((int) prod.getQuantity(), quantity - q);
    }

    // test discounts more
    @Test
    public void testScenario6_10() throws InvalidDiscountRateException, UnauthorizedException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException{
        int q=3;
        double discountRate=0.25;
        double prodRate=0.05;
        transactionId=ezshop.startSaleTransaction();
        assertTrue(ezshop.addProductToSale(transactionId,barcode2,q));
        assertTrue(ezshop.addProductToSale(transactionId,barcode,4));
        assertTrue(ezshop.applyDiscountRateToProduct(transactionId,barcode,prodRate));
        assertTrue(ezshop.applyDiscountRateToSale(transactionId,discountRate));
        assertTrue(ezshop.endSaleTransaction(transactionId));
        SaleTransaction s=ezshop.getSaleTransaction(transactionId);
        double price=pricePerUnit*4*(100-prodRate)/100;
        price+=q*pricePerUnit2;
        price=price*(100-discountRate)/100;
        assertTrue(price-s.getPrice()<0.001 || s.getPrice()-price<0.001);
    }

    // wrong IDs
    @Test
    public void testScenario6_11() throws InvalidTransactionIdException, UnauthorizedException {
        assertNull(ezshop.getSaleTransaction(4324));
    }



}
