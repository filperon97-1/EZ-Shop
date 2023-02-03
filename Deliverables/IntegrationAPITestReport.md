# Integration and API Test Documentation

Authors: Andrea Colli Vignarelli, Roberto Comella, Stefano Palmieri, Filippo Peron

Date:

Version:

# Contents

- [Dependency graph](#dependency graph)

- [Integration approach](#Integration approach)

- [Tests](#tests)

- [Scenarios](#scenarios)

- [Coverage of scenarios and FR](#Coverage of Scenarios and FR)
- [Coverage of non-functional requirements](#Coverage of Non-Functional Requirements)



# Dependency graph 

```plantuml
    left to right direction

    class EZShop

    class User
    class Customer
    class ProductType
    class BalanceOperation 
    class Debit
    class Credit
    class AccountBook
    class ReturnTransaction
    class SaleTransaction
    class SaleTransaction
    class Order
    class LoyaltyCard
    class TicketEntry
    class DAO
    class CreditCard

    EZShop -> User
    EZShop -> DAO
    EZShop -> Customer
    EZShop -> ProductType
    EZShop -> BalanceOperation
    EZShop -> AccountBook
    EZShop -> ReturnTransaction
    EZShop -> SaleTransaction
    EZShop -> Order
    EZShop -> LoyaltyCard
    EZShop -> CreditCard
    
    ReturnTransaction -> BalanceOperation
    ReturnTransaction -> Debit
    ReturnTransaction -> ProductType
    SaleTransaction -> ProductType
    SaleTransaction -> Credit
    Debit -> BalanceOperation
    SaleTransaction -> TicketEntry
    Credit -> BalanceOperation
    DAO -> User
    DAO -> Order
    DAO -> AccountBook
    DAO -> CreditCard
    DAO -> Customer
    DAO -> LoyaltyCard
    Customer -> LoyaltyCard
```
     
# Integration approach

Concerning the integration approach, it has been adopted a hybrid/mixed system. First, we have started from the 
bottom testing the singular isolated methods in the different classes of our backend. Secondly, we jumped directly
to test the different scenarios with the API from the official requirements and if needed some additional ones
to check the entire manipulation of data from the API. The only intermediate class is the DAO, which is tested with the API.
This approach has been chosen due to the nature of the DAO class.
- Step 1: class AccountBook
- Step 2: class BalanceOperation
- Step 3: class CreditCard
- Step 4: class Customer
- Step 5: class LoyaltyCard
- Step 6: class Order
- Step 7: class ProductType
- Step 8: class ReturnTransaction
- Step 9: class SaleTransaction
- Step 10: class TicketEntry
- Step 11: class User
- Step 12: class it.polito.ezshop.data.EZShop + DAO + ProductType + User
- Step 13: class it.polito.ezshop.data.EZShop + DAO + SaleTransaction + User
- Step 14: class it.polito.ezshop.data.EZShop + DAO + SaleTransaction + User
- Step 15: class it.polito.ezshop.data.EZShop + DAO + ProductType + User + Order
- Step 16: class it.polito.ezshop.data.EZShop + DAO + Customer + LoyaltyCard + User
- Step 17: class it.polito.ezshop.data.EZShop + DAO + User
- Step 18: class it.polito.ezshop.data.EZShop + DAO + User + Order
- Step 19: class it.polito.ezshop.data.EZShop + DAO + User + ProductType + Order + BalanceOperation
- Step 20: class it.polito.ezshop.data.EZShop + DAO + Customer + ProductType + SaleTransaction + LoyaltyCard


#  Tests

   JUnit test classes should be here src/test/java/it/polito/ezshop.

## Step 1
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| AccountBook | unitTests.testAccountBook.test0 |
| AccountBook | unitTests.testAccountBook.test1 |
| AccountBook | unitTests.testAccountBook.test2 |
| AccountBook | unitTests.testAccountBook.test3 |


## Step 2
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| BalanceOperation | unitTests.testBalanceOperation.test0 |
| BalanceOperation | unitTests.testBalanceOperation.test1 |
| BalanceOperation | unitTests.testBalanceOperation.test2 |
| BalanceOperation | unitTests.testBalanceOperation.test3 |
| BalanceOperation | unitTests.testBalanceOperation.test4 |

## Step 3
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| CreditCard | unitTests.testCreditCard.test0 |
| CreditCard | unitTests.testCreditCard.test1 |
| CreditCard | unitTests.testCreditCard.test2 |

## Step 4
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| Customer | unitTests.testCustomer.test0 |
| Customer | unitTests.testCustomer.test1 |
| Customer | unitTests.testCustomer.test3 |

## Step 5
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| LoyaltyCard | unitTests.testLoyaltyCard.test0 |
| LoyaltyCard | unitTests.testLoyaltyCard.test1 |
| LoyaltyCard | unitTests.testLoyaltyCard.test2 |
| LoyaltyCard | unitTests.testLoyaltyCard.test3 |
| LoyaltyCard | unitTests.testLoyaltyCard.test4 |
| LoyaltyCard | unitTests.testLoyaltyCard.test5 |
| LoyaltyCard | unitTests.testLoyaltyCard.test6 |

## Step 6
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| Order | unitTests.testOrder.test0 |
| Order | unitTests.testOrder.test1 |
| Order | unitTests.testOrder.test2 |
| Order | unitTests.testOrder.test3 |
| Order | unitTests.testOrder.test4 |

## Step 7
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| ProductType | unitTests.testProductType.test0 |
| ProductType | unitTests.testProductType.test1 |
| ProductType | unitTests.testProductType.test2 |
| ProductType | unitTests.testProductType.test3 |
| ProductType | unitTests.testProductType.test4 |
| ProductType | unitTests.testProductType.test5 |
| ProductType | unitTests.testProductType.test6 |
| ProductType | unitTests.testProductType.test7 |
| ProductType | unitTests.testProductType.test8 |
| ProductType | unitTests.testProductType.test9 |
| ProductType | unitTests.testProductType.test10 |

## Step 8
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| ReturnTransaction | unitTests.testReturnTransaction.test0 |
| ReturnTransaction | unitTests.testReturnTransaction.test1 |
| ReturnTransaction | unitTests.testReturnTransaction.test2 |
| ReturnTransaction | unitTests.testReturnTransaction.test3 |
| ReturnTransaction | unitTests.testReturnTransaction.test4 |
| ReturnTransaction | unitTests.testReturnTransaction.test5 |
| ReturnTransaction | unitTests.testReturnTransaction.test6 |
| ReturnTransaction | unitTests.testReturnTransaction.test7 |
| ReturnTransaction | unitTests.testReturnTransaction.test8 |
| ReturnTransaction | unitTests.testReturnTransaction.test9 |
| ReturnTransaction | unitTests.testReturnTransaction.test10 |

## Step 9
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| SaleTransaction | unitTests.testSaleTransaction.test0 |
| SaleTransaction | unitTests.testSaleTransaction.test1 |
| SaleTransaction | unitTests.testSaleTransaction.test2 |
| SaleTransaction | unitTests.testSaleTransaction.test3 |
| SaleTransaction | unitTests.testSaleTransaction.test4 |
| SaleTransaction | unitTests.testSaleTransaction.test5 |

## Step 10
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| TicketEntry | unitTests.testTicketEntry.test0 |
| TicketEntry | unitTests.testTicketEntry.test1 |
| TicketEntry | unitTests.testTicketEntry.test2 |
| TicketEntry | unitTests.testTicketEntry.test3 |
| TicketEntry | unitTests.testTicketEntry.test4 |
| TicketEntry | unitTests.testTicketEntry.test5 |

## Step 11
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| User | unitTests.testUser.test0 |
| User | unitTests.testUser.test1 |
| User | unitTests.testUser.test2 |
| User | unitTests.testUser.test3 |
| User | unitTests.testUser.test4 |

## Step 12
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| EZShop+DAO+User | integrationTests.testUC1ManageProducts.userPreconditions |
| EZShop+DAO | integrationTests.testUC1ManageProducts.resetPostConditions |
| EZShop+DAO+ProductType | integrationTests.testUC1ManageProducts.testScenario1 |
| EZShop+DAO+ProductType | integrationTests.testUC1ManageProducts.testScenario2 |
| EZShop+DAO+ProductType | integrationTests.testUC1ManageProducts.testScenario3 |
| EZShop | integrationTests.testUC1ManageProducts.testScenario4 |

## Step 13
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| EZShop+DAO+User+SaleTransaction | integrationTests.testUC7ManagePayment.setupPreconditions |
| EZShop+DAO | integrationTests.testUC7ManagePayment.resetPostConditions |
| EZShop+DAO | integrationTests.testUC7ManagePayment.testScenario1 |
| EZShop+DAO | integrationTests.testUC7ManagePayment.testScenario2 |
| EZShop+DAO | integrationTests.testUC7ManagePayment.testScenario3 |
| EZShop+DAO | integrationTests.testUC7ManagePayment.testScenario4 |
| EZShop+DAO | integrationTests.testUC7ManagePayment.testScenario5 |

## Step 14
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| EZShop+DAO+User+SaleTransaction | integrationTests.testUC8ManageReturnTransaction.setupPreconditions |
| EZShop+DAO | integrationTests.testUC8ManageReturnTransaction.resetPostConditions |
| EZShop+DAO | integrationTests.testUC8ManageReturnTransaction.testScenario1 |
| EZShop+DAO | integrationTests.testUC8ManageReturnTransaction.testScenario2 |


## Step 15
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| EZShop+DAO+User+ProductType | integrationTests.testUC3ManageOrderTest.userPreconditions |
| EZShop+DAO | integrationTests.testUC3ManageOrderTest.resetPostConditions |
| EZShop+DAO+Order| integrationTests.testUC3ManageOrderTest.testScenario1.2|
| EZShop+DAO+Order| integrationTests.testUC3ManageOrderTest.testScenario2 |
| EZShop+DAO+Order| integrationTests.testUC3ManageOrderTest.testScenario3 |
| EZShop+DAO+Order| integrationTests.testUC3ManageOrderTest.testScenario4 |
| EZShop+DAO+Order | integrationTests.testUC3ManageOrderTest.testScenario5 |


## Step 16
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| EZShop+DAO+User | integrationTests.testUC4CheckManageCustomersandCards.userPreconditions |
| EZShop+DAO | integrationTests.testUC4CheckManageCustomersandCards.resetPostConditions |
| EZShop+DAO+Customer| integrationTests.testUC4CheckManageCustomersandCards.testScenario1 |
| EZShop+DAO+Customer+LoyaltyCard  | integrationTests.testUC4CheckManageCustomersandCards.testScenario2 |
| EZShop+DAO+Customer+LoyaltyCard | integrationTests.testUC4CheckManageCustomersandCards.testScenario3 |
| EZShop+DAO+Customer+LoyaltyCard | integrationTests.testUC4CheckManageCustomersandCards.testScenario4 |
| EZShop+DAO+Customer+LoyaltyCard | integrationTests.testUC4CheckManageCustomersandCards.testScenario5 |
| EZShop+DAO+Customer+LoyaltyCard | integrationTests.testUC4CheckManageCustomersandCards.testScenario6|

## Step 17
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| EZShop+DAO+User | integrationTests.testUC2ManageUsersNRights.userPreconditions |
| EZShop+DAO | integrationTests.testUC2ManageUsersNRights.resetPostConditions |
| EZShop+DAO+User| integrationTests.testUC2ManageUsersNRights.testScenario1|
| EZShop+DAO+User| integrationTests.testUC2ManageUsersNRights.testScenario2 |
| EZShop+DAO+User| integrationTests.testUC2ManageUsersNRights.testScenario3 |
| EZShop+DAO+User| integrationTests.testUC2ManageUsersNRights.testScenario4 |
| EZShop+DAO+User | integrationTests.testUC2ManageUsersNRights.testScenario5 |
| EZShop+DAO+User | integrationTests.testUC2ManageUsersNRights.testScenario6 |

## Step 18
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| EZShop+DAO+User | integrationTests.testUC5AuthenticateAuthorize.userPreconditions |
| EZShop+DAO+User | integrationTests.testUC5AuthenticateAuthorize.logOut |
| EZShop+DAO+User | integrationTests.testUC5AuthenticateAuthorize.resetPostConditions |
| EZShop+DAO+User+Order | integrationTests.testUC5AuthenticateAuthorize.test_Scenario5_1 |
| EZShop+DAO+User+Order | integrationTests.testUC5AuthenticateAuthorize.test_Scenario5_2 |
| EZShop+DAO+User+Order | integrationTests.testUC5AuthenticateAuthorize.test_Scenario5_3 |
| EZShop+DAO+User+Order | integrationTests.testUC5AuthenticateAuthorize.test_Scenario5_4 |
| EZShop+DAO+User| integrationTests.testUC5AuthenticateAuthorize.test_Scenario5_5 |


## Step 19
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| EZShop+DAO+User+ProductType+Order | integrationTests.testUC9Accounting.userPreconditions |
| EZShop+DAO | integrationTests.testUC9Accounting.resetPostConditions |
| EZShop+DAO+BalanceOperation | integrationTests.testUC9Accounting.testScenario1|

## Step 20
| Classes  | JUnit test cases | 
| ----------------- |:-----------|
| EZShop+DAO+User+ProductType+Customer+LoyaltyCard | integrationTests.testUC6ManageSaleTransaction.userPreconditions |
| EZShop+DAO | integrationTests.testUC9Accounting.resetPostConditions |
| EZShop+DAO+SaleTransaction+ProductType | integrationTests.testUC6ManageSaleTransaction.testScenario6_1 |
| EZShop+DAO+SaleTransaction+ProductType | integrationTests.testUC6ManageSaleTransaction.testScenario6_2 |
| EZShop+DAO+SaleTransaction+ProductType | integrationTests.testUC6ManageSaleTransaction.testScenario6_3 |
| EZShop+DAO+SaleTransaction+ProductType+Customer | integrationTests.testUC6ManageSaleTransaction.testScenario6_4 |
| EZShop+DAO+SaleTransaction+ProductType | integrationTests.testUC6ManageSaleTransaction.testScenario6_5 |
| EZShop+DAO+SaleTransaction+ProductType | integrationTests.testUC6ManageSaleTransaction.testScenario6_6 |
| EZShop+DAO+SaleTransaction+ProductType | integrationTests.testUC6ManageSaleTransaction.testScenario6_7 |
| EZShop+DAO+SaleTransaction+ProductType | integrationTests.testUC6ManageSaleTransaction.testScenario6_8 |
| EZShop+DAO+SaleTransaction+ProductType | integrationTests.testUC6ManageSaleTransaction.testScenario6_9 |
| EZShop+DAO+SaleTransaction+ProductType | integrationTests.testUC6ManageSaleTransaction.testScenario6_10 |
| EZShop+DAO+SaleTransaction | integrationTests.testUC6ManageSaleTransaction.testScenario6_11 |

# Scenarios


Additional scenarios are reported here: 

## Scenario UC1.4

| Scenario |  Create product type X by not logged user |
| ------------- |:-------------:| 
|  Precondition     | No employee logged in |
|  Post condition     |  Return error |
| Step#        | Description  |
|  1    |  C inserts new product description |  
|  2    |  C inserts new bar code |
|  3    |  C inserts new price per unit |
|  4    |  C inserts new product notes |
|  5    |  C enters location of X |
|  6    |  C confirms the entered data |

## Scenario UC1.5

| Scenario |  Create product type X by NOT AUTHORIZED user |
| ------------- |:-------------:| 
|  Precondition     | User C exists and is logged in |
|  Post condition     | Return error  |
| Step#        | Description  |
|  1    |  C inserts new product description |  
|  2    |  C inserts new bar code |
|  3    |  C inserts new price per unit |
|  4    |  C inserts new product notes |
|  5    |  C enters location of X |
|  6    |  C confirms the entered data |

## Scenario UC1.6
| Scenario |  Create product type X with invalid barCode |
| ------------- |:-------------:| 
|  Precondition     | User C exists and is logged in |
|  Post condition     | Return error  |
| Step#        | Description  |
|  1    |  C inserts new product description |  
|  2    |  C inserts new INVALID bar code |
|  3    |  C inserts new price per unit |
|  4    |  C inserts new product notes |
|  5    |  C enters location of X |
|  6    |  C confirms the entered data |

## Scenario UC1.7
| Scenario |  Create product type X with invalid description |
| ------------- |:-------------:| 
|  Precondition     | User C exists and is logged in |
|  Post condition     | Return error  |
| Step#        | Description  |
|  1    |  C inserts new product INVALID description |  
|  2    |  C inserts new bar code |
|  3    |  C inserts new price per unit |
|  4    |  C inserts new product notes |
|  5    |  C enters location of X |
|  6    |  C confirms the entered data |

## Scenario UC1.8
| Scenario |  Create product type X with invalid location |
| ------------- |:-------------:| 
|  Precondition     | User C exists and is logged in |
|       | location is already in use |
|  Post condition     | Return error  |
| Step#        | Description  |
|  1    |  C inserts new product description |  
|  2    |  C inserts new bar code |
|  3    |  C inserts new price per unit |
|  4    |  C inserts new product notes |
|  5    |  C enters INVALID location of X |
|  6    |  C confirms the entered data |

## Scenario UC1.9
| Scenario |  Create product type X with invalid price per unit |
| ------------- |:-------------:| 
|  Precondition     | User C exists and is logged in |
|  Post condition     | Return error  |
| Step#        | Description  |
|  1    |  C inserts new product description |  
|  2    |  C inserts new bar code |
|  3    |  C inserts new INVALID price per unit |
|  4    |  C inserts new product notes |
|  5    |  C enters location of X |
|  6    |  C confirms the entered data |

## Scenario UC1.10
| Scenario |  Create product type X with invalid barCode |
| ------------- |:-------------:| 
|  Precondition     | User C exists and is logged in |
|       | barCode is already in use |
|  Post condition     | Return error  |
| Step#        | Description  |
|  1    |  C inserts new product description |  
|  2    |  C inserts new bar code |
|  3    |  C inserts new INVALID price per unit |
|  4    |  C inserts new product notes |
|  5    |  C enters location of X |
|  6    |  C confirms the entered data |

## Scenario UC1.11
| Scenario |  Delete product type X|
| ------------- |:-------------:| 
|  Precondition     | User C exists and is logged in |
|       | Product P exists |
|  Post condition     | Product P does not exists  |
| Step#        | Description  |
|  1    |  C inserts P id |  
|  2    |  C confirms deletion of P|

## Scenario UC1.12
| Scenario |  Delete product type X which does not exist|
| ------------- |:-------------:| 
|  Precondition     | User C exists and is logged in |
|       | Product P DOES NOT exist |
|  Post condition     | Return error |
| Step#        | Description  |
|  1    |  C inserts P id |  
|  2    |  C confirms deletion of P|

## Scenario UC2.4
| Scenario |  Delete user with no permission |
| ------------- |:-------------:| 
|  Precondition     | ShopManager E exists and is logged in |
|  | Account X exists |
|  Post condition     | Return error |
| Step#        | Description  |
|  1    |  E selects account X  |
|  2    |  E deletes X |

## Scenario UC2.5
| Scenario |  Get users list |
| ------------- |:-------------:| 
|  Precondition     | Admin A exists and is logged in |
|  | Accounts X, Y, Z exist |
|  Post condition     |  |
| Step#        | Description  |
|  1    |  A selects all accounts  |
|  2    |  A gets users from the system |

## Scenario UC2.6

| Scenario |  Create user and define rights with already present username |
| ------------- |:-------------:| 
|  Precondition     | Admin A exists and is logged in |
|     | Username of user X already exits |
|  Post condition     | Return error |
| Step#        | Description  |
|  1    |  A defines the credentials of the new Account X |  
|  2    |  A selects the access rights for the new account X |
|  3    |  A confirms the inserted data |

## Scenario UC3.1

| Scenario |  ProductType for the issued order does not exist |
| ------------- |:-------------:| 
|  Precondition     | Admin A exists and is logged in |
|     | Product type does not exist |
|  Post condition     | Return error |
| Step#        | Description  |
|  1    |  A creates an order for a INVALID product Type|  
|  2    |  A issues the order|

## Scenario UC3.6

| Scenario |  NotEnoughBalance |
| ------------- |:-------------:| 
|  Precondition     | Admin A exists and is logged in |
|     | Product type does not exist |
|  Post condition     | Return error |
| Step#        | Description  |
|  1    |  A creates an order for a product Type|  
|  2    |  A gets all orders|
|  3	| A tries to pay the order|

## Scenario UC4.7

| Scenario |  Add Points |
| ------------- |:-------------:| 
|  Precondition     | Admin A exists and is logged in |
|     | customer exists with a card|
|  Post condition     |  |
| Step#        | Description  |
|  1    |  A modifies points to the card| 
|  2    |  A gets all Customer| 
|  3  |  A checks information of the Customer| 



## Scenario UC5.3
| Scenario |  inexistent user tries to log in |
| ------------- |:-------------:| 
|  Precondition     | User A does not exists |
|  Post condition     | Return error |
| Step#        | Description  |
|  1    |  User inserts a username |  
|  2    |  User inserts a password |
|  3    |  Exit with error |

## Scenario UC5.4
| Scenario |  user tries logs in after another user has logged in |
| ------------- |:-------------:| 
|  Precondition     | User A exists is logged in |
|  Post condition     | User B is logged in |
| Step#        | Description  |
|  1    |  User B inserts a username |  
|  2    |  User B inserts a password |
|  3    |  User A is logged out and User B is logged in |

## Scenario UC5.5
| Scenario |  user tries to log out when there's no one logged in |
| ------------- |:-------------:| 
|  Precondition     | No one is logged in |
|  Post condition     | Return error |
| Step#        | Description  |
|  1    |  User tries to log out |  
|  2    |  Exit with error |


## Scenario UC6.7
| Scenario |  not enough quantity for a product in a sale transaction |
| ------------- |:-------------:| 
|  Precondition     | Cashier C exists and is logged in |
|                   | Product type X exists and has not enough units to complete the sale |
|  Post condition   | product not added in sale transaction |
| Step#        | Description  |
|  1    |  C creates new sale transaction |  
|  2    |  C adds a product to the sale transaction |
|  3    |  there is not enough quantity for the product |


## Scenario UC6.8
| Scenario |  try to add a product code that does not exist in a sale transaction |
| ------------- |:-------------:| 
|  Precondition     | Cashier C exists and is logged in |
|                   | Product type X doesn't exist  |
|  Post condition   | product not added in sale transaction |
| Step#        | Description  |
|  1    |  C creates new sale transaction |  
|  2    |  C adds a product that does not exist to the sale transaction |
|  3    |  product not added |


## Scenario UC6.9
| Scenario |  add a product code in a sale transaction and then remove it|
| ------------- |:-------------:| 
|  Precondition     | Cashier C exists and is logged in |
|                   | Product type X exists  |
|  Post condition   | product not added in sale transaction |
| Step#        | Description  |
|  1    |  C creates new sale transaction |  
|  2    |  C adds a product to the sale transaction |
|  3    |  C removes the product |


## Scenario UC6.10
| Scenario |  remove a product from sale transaction even though it is not there|
| ------------- |:-------------:| 
|  Precondition     | Cashier C exists and is logged in |
|                   | Product type X not in sale transaction  |
|  Post condition   | product not removed from sale transaction |
| Step#        | Description  |
|  1    |  C creates new sale transaction |  
|  2    |  C removes a product from the sale transaction |
|  3    |  nothing happens |


## Scenario UC6.10-2
| Scenario |  try to remove a quantity of a product > quantity on sale transaction 
| ------------- |:-------------:| 
|  Precondition     | Cashier C exists and is logged in |
|                   | Product type X exists  |
|  Post condition   | product not removed from sale transaction |
| Step#        | Description  |
|  1    |  C creates new sale transaction |  
|  2    |  C adds Q quantity of a product to the sale transaction |
|  3    |  C removes Q2>Q quantity of that product from the sale transaction |
|  4    |  removal does not happen |


## Scenario UC6.11
| Scenario |  add some discounts
| ------------- |:-------------:| 
|  Precondition     | Cashier C exists and is logged in |
|                   | Product type X exists  |
|  Post condition   | discount applied |
| Step#        | Description  |
|  1    |  C creates new sale transaction |  
|  2    |  C adds Q a product to the sale transaction |
|  3    |  C applies discount to that product |
|  4    |  C adds new product to the sale transaction |
|  5    |  C applies discount to the whole transaction |


## Scenario UC6.12
| Scenario |  wrong sale ID
| ------------- |:-------------:| 
|  Precondition     | Cashier C exists and is logged in |
|                   | sale transaction X does not exist  |
|  Post condition   | error |
| Step#        | Description  |
|  1    |  C tries to get a closed sale transaction that does not exist |  
|  2    |  error  |



## Scenario UC7.5

| Scenario |  Invalid Ticket ID |
| ------------- |:-------------:| 
|  Precondition     | Ticket ID not valid  |
|  Post condition     |   |
| Step#        | Description  |
|  1    |  Check validity of ticket id, not valid |
|  2    |  Exit with error |




# Coverage of Scenarios and FR




| Scenario ID | Functional Requirements covered | JUnit  Test(s) | 
| ----------- | ------------------------------- | ----------- | 
|  UC1 scenarios        | FR4.1->4.2                   |     testUC1ManageProducts      |  
|  UC2 scenarios        | FR1                     |     testUC2ManageUsersNRights     |  
|  UC3 scenarios        | FR4.3->4.7                     |     testUC3ManageOrderTest      |             
|  UC4 scenarios        | FR5                            |     testUC4CheckManageCustomersandCards     |             
|  UC5 scenarios        | FR1.5                          |     testUC5AuthenticateAuthorize        |             
|  UC6 scenarios        | FR6                            |     testUC6ManageSaleTransaction       |             
|  UC7 scenarios        | FR7.1->7.2                     |     testUC7ManagePayment       |    
|  UC7 scenarios        | FR8.1->8.2                     |     testUC8ManageReturnTransaction        |   
|  UC9 scenarios        | FR8.3->8.4                     |     testUC9Accounting       | 
                        



# Coverage of Non-Functional Requirements


### 

| Non Functional Requirement | Test name |
| -------------------------- | --------- |
|       NFR1                     |     testUC6ManageSaleTransaction, testUC5AuthenticateAuthorize, testUC7ManagePayment      |
|       NFR2                     |     testUC6ManageSaleTransaction, testUC5AuthenticateAuthorize, testUC7ManagePayment      |
|       NFR3                     |    testUC1ManageProducts      |
|       NFR4                     |     testUC7ManagePayment, testUC8ManageReturnTransaction       |
|       NFR5                     |     testUC7ManagePayment, testUC8ManageReturnTransaction       |
|       NFR6                     |    testUC4CheckManageCustomersandCards       |



