# Requirements Document 

Authors: Andrea Colli Vignarelli, Roberto Comella, Stefano Palmieri, Filippo Peron

Date: April 21st 2021

Version: 0.1

# Contents

- [Essential description](#essential-description)
- [Stakeholders](#stakeholders)
- [Context Diagram and interfaces](#context-diagram-and-interfaces)
	+ [Context Diagram](#context-diagram)
	+ [Interfaces](#interfaces) 
	
- [Stories and personas](#stories-and-personas)
- [Functional and non functional requirements](#functional-and-non-functional-requirements)
	+ [Functional Requirements](#functional-requirements)
	+ [Non functional requirements](#non-functional-requirements)
- [Use case diagram and use cases](#use-case-diagram-and-use-cases)
	+ [Use case diagram](#use-case-diagram)
	+ [Use cases](#use-cases)
    	+ [Relevant scenarios](#relevant-scenarios)
- [Glossary](#glossary)
- [System design](#system-design)
- [Deployment diagram](#deployment-diagram)

# Essential description

Small shops require a simple application to support the owner or manager. A small shop (ex a food shop) occupies 50-200 square meters, sells 500-2000 different item types, has one or a few cash registers 
EZShop is a software application to:
* manage sales
* manage inventory
* manage customers
* support accounting

# Stakeholders

| Stakeholder name  | Description | 
| ----------------- |:-----------:|
|   Manager     |	Manage application, check accounting, manage orders	|
|	Employee	|	Manage income transactions	|
|	Cashier		|	Manage income transactions, register new subscriber customer|
|	Customer	| 	Register as new subscriber, purchase items	|
|	POS			|	Manage card payments	|
|	Barcode Reader	|	Scan barcode |

# Context Diagram and interfaces

## Context Diagram
```plantuml
top to bottom direction
actor Manager as m
actor Employee as e
actor Cashier as csh
actor Customer as cus
actor POS as pos
actor BarcodeReader as bcr
m -up-|> e
csh -up-|> e
e -> (ComputerShop)
cus -> (ComputerShop)
pos -> (ComputerShop)
bcr -> (ComputerShop)
```

## Interfaces

| Actor | Logical Interface | Physical Interface  |
| ------------- |:-------------:| -----:|
|   Manager		|	Application GUI	|	PC input system	|
|	Cashier		|	Application GUI	|	PC input system	|
|	Customer	|	Application GUI	|	On screen input system |
|	POS			|	API	|	USB (wired)	  |
|	Barcode Reader	|	API	|	USB (wired)	  |

# Stories and personas

Davide is 76, he has no experience on computers and he is not practiced with shopping center or online market, so he prefers to go in a small shop where he can ask to the cashier advices on what he needs.

Marco is 38, he works as a Microsoft employee and he is passionate of computer hardware, that's why he loves buying computer hardware items in small shops like EZShop, as he prefers small entities rather than big ones like he is used to.

Enzo Pessegatto is 45 and he is the owner of the little family's computer shop. Enzo is very busy: since it is a small shop he often does not only work as a manager but also as a cashier. For this reason he has very little time to manage orders and he needs a simple and intuitive way to do them: that's why he chose EZShop as his shop's software application.

Tiberia is 23, she is very unsure about her future and in her last three years she has been dealing with her heroin addiction. She left rehab a couple weeks ago and she is currently searching for a job. EZShop's familiar environment is the perfect choice for her, and with its intuitive cashier's interface learning the new job will be as easy as eating an apple.

# Functional and non functional requirements

## Functional Requirements

| ID        | Description  |
| ------------- | ------------- | 
|	FR1     |	Manage subscriptions	|
|	FR1.1	|	Manage data insertion and checking for all fields to be filled in	|
|	FR1.2	|	Manage 'go back' button	|
|	FR1.3	|	Store subscriber's data in db	|
|	FR1.4	|	Create unique ID for subscriber	|
|	FR1.5	|	Manage fidelity card creation	|
|	FR1.6	|	Manage subscriber's deletion	|
|	FR2     |	Manage inventory	|
|   FR2.1   |   Check item quantities per item descriptor	|
|   FR2.2   |   Perform orders |
|   FR2.3   |   Set customer items prices	|
|	FR2.4	|	Set an item available	|
|	FR3     |	Manage purchases	| 
|	FR3.1 	| 	Set the ID of the item to sell	|
|	FR3.2 	|	Evaluate the amount	|
|	FR3.3 	|	Adding the subscription card of the client to the payment	|
|	FR3.4 	|	Make a discount	|
|	FR3.5 	|	Select the type of payment	|
|	FR3.6	|	Manage payment	|
|	FR4		|	Check accounting	|
|	FR4.1	|	Show transactions balance on user determined period of time	|
|	FR4.2	|	Show specific item balance (sold unit vs stock ones)	|
|	FR4.3	|	Show subscriber purchase rate	|
|	FR4.4	|	Show per day balance	|
|   FR5   	|   Login as authorized employee	|


### Access right, actor vs function

| Function | Manager | Employee | Customer |
| ------------- |------------- | -- | -- |
| FR1 | yes | yes | yes|
| FR2 | yes | yes | no |
| FR3 | yes | yes | no |
| FR4 |	yes	| no  | no |
| FR5 | yes | yes | no |

## Non Functional Requirements

| ID        | Type (efficiency, reliability, ..)           | Description  | Refers to |
| ------------- | ------------- | ----- | ----- |
|  NFR1     |	Usability	| Application should be used with no specific training for the users | All FR |
|  NFR2     |	Performance	| All functions should complete in < 1 sec  | All FR |
|  NFR3     |	Privacy	| Customers and users data should not be disclosed publicly	| All FR |
|  NFR4     |   Size	| Possible number of items in transaction <= 500  | FR2 FR3  |
|  NFR5		|	Performance	| Screen refresh time < 1 sec 		| All FR	|
|  NFR6		|	Robustness	| Time to restart after failure < 1 min	| All FR	|


# Use case diagram and use cases


## Use case diagram

```plantuml
top to bottom direction
actor Manager as m
actor Employee as e
actor Cashier as ca
actor Customer as cu
actor POS as p
actor BarcodeReader as bcr
m-up->e
ca-up->e
e->(Manage subscriptions)
cu->(Manage subscriptions)
e->(Manage inventory)
e->(Manage purchases)
(Manage purchases).>(Manage payment):include
(Manage inventory).>(Perform orders):include
m->(Check accounting)
e->(Set an item available)
p->(Manage payment)
bcr->(Manage purchases)
```

### Use case 1, UC1 - Make an order
| Actors Involved	|   Employee   |
| ------------- | ------------- | 
|  Precondition     |	Items for a specific item descriptor are not available anymore in the inventory	|  
|  Post condition	|	Items for a specific item descriptor are now available again in the inventory	|
|  Nominal Scenario	|	Employee uses the tool for ordering a certain amount of items of a specific item descriptor from the supplier	|
|  Variants	|  the total volume of the item exceed the total volume available in the inventory   |

##### Scenario 1.1
| Scenario	|   Items fit in the shop	|
| ------------- | ------------- | 
|  Precondition     |			|  
|  Post condition	|	 Items are added in the inventory	|
|  Step#	|	Description	|
|  1	| Employee confirms the order |
|  2	| the total volume of the items fit in the inventory |
|  3	| the order succeedes |
|  4    | Items are added in the inventory |


##### Scenario 1.2
| Scenario	|   Items do not fit in the shop	|
| ------------- | ------------- | 
|  Precondition     |			|  
|  Post condition	|		|
|  Step#	|	Description	|
|  1	| Employee confirms the order |
|  2	| the total volume of the items does not fit in the inventory |
|  3	| the order failed |
|  4    | employee decrements the amout of items in the order |
|  5    | repeat order process |

### Use case 2, UC2 - Check accounting
| Actors Involved	|   Manager   |
| ------------- | ------------- | 
|  Precondition     |	-	|  
|  Post condition	|	-	|
|  Nominal Scenario	|	Manager uses the tool for checking the current shop balance and accounting information	|
|  Variants	| - |

### Use case 3, UC3 - Customer self subscribe
| Actors Involved	 |   Customer   |
| ------------- | ------------- | 
|  Precondition     |	Customer does not exist in the registered customers database	|  
|  Post condition	|	Customer is now a registered customer	|
|  Nominal Scenario	|	Customer uses the tool to subscribe by inserting the required data (name, surname, CODICE FISCALE) 	|
|  Variants	| Subscribers are uniquely identified by their CODICE FISCALE |

### Use case 4, UC4 - Subscribe a customer
| Actors Involved	 |   Employee   |
| ------------- | ------------- | 
|  Precondition     |	Customer does not exist in the registered customers database	|  
|  Post condition	|	Customer is now a registered customer	|
|  Nominal Scenario	|	Employee uses the tool to register a customer by inserting the required data (name, surname, CODICE FISCALE) 	|
|  Variants	| Subscribers are uniquely identified by their CODICE FISCALE |

### Use case 5, UC5 - Purchase items
| Actors Involved	 |   Employee   |
| ------------- | ------------- | 
|  Precondition     |	Items are stocked in the inventory	| 
|					|	Items are available in the shop	|
|  Post condition	|	Item get removed from the inventory	|
|  					|	accounting is updated	|
|  Nominal Scenario	|	Employee uses the tool to perform the transaction for all items selected by the customer. Payment executes successfully.	|
|  Variants	| Payment could not execute successfully |

##### Scenario 5.1
| Scenario	 |	Payment succeeded	 |
| ------------- | ------------- | 
|  Precondition     |  -  |
|  Post condition     |   Items in inventory decrease - Income amount increases  |
| Step#        | Description  |
|  1	|  Customer confirms the payment  |  
|  2	|  Payment succeedes  |
|  3	|  Items amount in inventory reduces	|
|  4	|  Income amount increases	|

##### Scenario 5.2
| Scenario	 |	Payment failed	 |
| ------------- | ------------- | 
|  Precondition     |  -  |
|  Post condition     |  -  |
| Step#        | Description  |
|  1	|  Customer confirms the payment  |  
|  2	|  Payment fails  |
|  3	|  Repeat payment process	|

##### Scenario 5.3
| Scenario	|   Printer out of paper	|
| ------------- | ------------- | 
|  Precondition     |	Transaction has been closed		|  
|  Post condition	|	Application stop waiting for paper recharge	|
|  Step#	|	Description	|
|  1	| Employee confirms the transaction adn payment |
|  2	| Application return alert message with the error and stuck |

### Use case 6, UC6 - Remove an item

| Actors Involved	|   Employee   |
| ------------- | ------------- | 
|  Precondition     |	Items are available in the inventory	|  
|  Post condition	|	Items are removed from the inventory	|
|  Nominal Scenario	|	Employee uses the tool for remove an item from the inventory	|
|  Variants	|  -  |

### Use case 7, UC7 - Set an item available

| Actors Involved	|   Employee   |
| ------------- | ------------- | 
|  Precondition     |	Items are available in the inventory	|  
|  Post condition	|	Items are available in the store	|
|  Nominal Scenario	|	Employee uses the tool to set an item available	|
|  Variants	|  -  |

### Use case 8, UC8 - Employee deletes customer as subscriber
| Actor Involved	|	Customer	|
| ------------- | ------------- | 
|  Precondition     |	Customer is registered as subscriber	|  
|  Post condition	|	Customer is not registered as subscriber	|
| Step#        | Description  |
|  1	|  Employee logs into the platform   |  
|  2	|  Employee looks for customer's data	|
|  3	|  Employee deletes customer as a subscriber  |

### Use case 9, UC9 - Employee login
| Actor Involved	|	Customer	|
| ------------- | ------------- | 
|  Precondition     |	-	|  
|  Post condition	|	Employee enters into the dashboard	|
| Step#        | Description  |
|  1	|  Employee insert username and password   |  
|  2	|  Employee confirm the data	|

##### Scenario 9.1
| Scenario	|   Login Error	|
| ------------- | ------------- | 
|  Precondition     |	-		|  
|  Post condition	|	Employee return to login page	|
|  Step#	|	Description	|
|  1	|  Employee insert the wrong username and password   |  
|  2	|  Employee confirm the data	|
|  3	|  Employee return to the login page with error shown	|

# Glossary
```plantuml
class EZShop {
 name
 location
}
class Inventory {
 volumeMax
}
class Catalogue
class Item {
 id
 sellingPrice
 available
}
class ItemDescriptor {
 id
 name
}
class Supplier
class Transaction {
 amount
 date
 cashOrCard
 inOut
}
class Order {
 quantity
}
class Purchase {
 id
 discount
}
class SubscriptionCard {
 id
 creditAmount
}
class Customer
class Subscriber {
 id
 name
 surname
 address
}
class Employee {
 id
 role
}
class Accounting {
 balance
}

Subscriber -up-|> Customer
Purchase -up-|> Transaction
Order -up-|> Transaction
EZShop -- "*" Subscriber
Subscriber --  SubscriptionCard
Customer -- "*" Purchase
Employee -- "*" Transaction
Accounting -- "*" Transaction
EZShop -- Inventory
EZShop -- Catalogue
Inventory -- "*" Item
Catalogue -- "*" ItemDescriptor
ItemDescriptor -- "*" Item
ItemDescriptor -- "0..*" Order
Supplier -- "*" Order
```

# System Design

Not really meaningful in this case.  Only software components are needed.

# Deployment Diagram 

Simple front-end / back-end application. Employees can use it on a PC, while customers have limited access to functions on a touchscreen device.

```plantuml
artifact "EZShop Application" as ezshopapp
node "database" as db
node "PC manager" as pcman
node "Tablet customer" as tablet
node "PC cashier" as pccash
db -- ezshopapp
ezshopapp -- pcman
ezshopapp -- tablet
ezshopapp -- "*" pccash
```


