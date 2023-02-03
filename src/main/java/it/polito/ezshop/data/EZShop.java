package it.polito.ezshop.data;

import it.polito.ezshop.exceptions.*;
import it.polito.ezshop.model.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


public class EZShop implements EZShopInterface {

    private User userLogged;
    private DAO dao;
    // lista di saleTransaction e returnTransaction aperte. Si memorizzano su DB solo una volta chiuse
    private ArrayList<it.polito.ezshop.model.SaleTransaction> openSaleTransactions;
    private ArrayList<ReturnTransaction> openReturnTransactions;

    public EZShop() {
        this.userLogged = null;
        try {
            this.dao = DAO.getInstance();
            openSaleTransactions= new ArrayList<>();
            openReturnTransactions= new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void reset() {
        this.userLogged = null;
        try {
            this.dao.resetData();
            openSaleTransactions.clear();
            openReturnTransactions.clear();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
        int id;
        try {
            do {
                id = Math.abs(Math.abs(new Random().nextInt()));
            } while(dao.getUser(id) != null);

            if(this.dao.getUsers().stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null) != null) {
            	return -1;
            }

            it.polito.ezshop.model.User u = new it.polito.ezshop.model.User(id, username, password, role);
            dao.addUser(u);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        isAuthorized("Administrator");
        if(id == null || id <= 0){
            throw new InvalidUserIdException("User id provided is not good");
        }
        try {
            return this.dao.deleteUser(id);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
        isAuthorized("Administrator");
        List<User> list = new ArrayList<>();
        try {
            list = new ArrayList<>(this.dao.getUsers());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
        isAuthorized("Administrator");
        User u = null;
        try {
            if (id == null || id <= 0) {
                throw new InvalidUserIdException("id is less than or equal to zero or if it is null");
            }
            u = this.dao.getUser(id);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return u;
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
        isAuthorized("Administrator");
        try {
            if (role == null ||
                    role.isEmpty() || !(
                            role.equals("Administrator") ||
                            role.equals("Cashier") ||
                            role.equals("ShopManager")
            )
            ) {
                throw new InvalidRoleException("Role is null or empty or not in the list");
            }
            it.polito.ezshop.model.User u = (it.polito.ezshop.model.User) this.getUser(id);
            if(u == null) {
                return false;
            }
            u.setRole(role);

            this.dao.updateUser(u);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
        if(username == null || username.isEmpty()){
            throw new InvalidUsernameException("The username is empty or null");
        }
        if(password == null || password.isEmpty()) {
            throw new InvalidPasswordException("The password is empty or null");
        }

        try {
            this.userLogged = this.dao.getUsers().stream().filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password)).findFirst().orElse(null);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            this.userLogged = null;
        }

        return this.userLogged;
    }

    @Override
    public boolean logout() {
        if(this.userLogged == null) {
            return false;
        } else {
            this.userLogged = null;
            return true;
        }
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        int id;
        if(productCode == null || productCode.isEmpty() || !it.polito.ezshop.model.ProductType.checkBarcode(productCode)) {
            throw new InvalidProductCodeException("Product code provided is not valid");
        }
        if(description == null || description.isEmpty()) {
            throw new InvalidProductDescriptionException("Product description provided is not valid");
        }
        if(pricePerUnit <= 0) {
            throw new InvalidPricePerUnitException("Product price per unit provided is not valid");
        }
        try {
            do {
                id = Math.abs(new Random().nextInt());
            } while (dao.getProductType(id) != null);
            if (dao.getProductType(productCode) != null) {
                return -1;
            }
            it.polito.ezshop.model.ProductType pt = new it.polito.ezshop.model.ProductType(id, 0, "", note, description, productCode, pricePerUnit);
            this.dao.addProductType(pt);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote)
            throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        if(id == null || id <= 0){
            throw new InvalidProductIdException("Product id provided is not good");
        }
        if(newDescription == null || newDescription.isEmpty()){
            throw new InvalidProductDescriptionException("Product description provided is not good");
        }
        if(newCode == null || !it.polito.ezshop.model.ProductType.checkBarcode(newCode) || newCode.isEmpty()){
            throw new InvalidProductCodeException("Product barcode provided is not good");
        }
        if(newPrice <= 0){
            throw new InvalidPricePerUnitException("Product price per unit provided is not good");
        }

        it.polito.ezshop.model.ProductType pt;
        try {
            pt = this.dao.getProductType(id);
            if(pt == null) {
                return false;
            }
            it.polito.ezshop.model.ProductType ptCheck = this.dao.getProductType(newCode);
            if(ptCheck != null && !ptCheck.getId().equals(pt.getId())) {
                return false;
            }
            pt.setProductDescription(newDescription);
            pt.setBarCode(newCode);
            pt.setPricePerUnit(newPrice);
            pt.setNote(newNote);

            this.dao.updateProductType(pt);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        if(id == null || id <= 0){
            throw new InvalidProductIdException("Product id provided is not good");
        }
        try {
            ProductType pt = this.dao.getProductType(id);
            if (pt == null) {
                return false;
            }
            this.dao.deleteProductType(id);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        List<ProductType> list = new ArrayList<>();
        try {
            list = new ArrayList<>(this.dao.getProductTypes());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        if(barCode == null || !it.polito.ezshop.model.ProductType.checkBarcode(barCode) || barCode.isEmpty()){
            throw new InvalidProductCodeException("Product barcode provided is not good");
        }
        ProductType pt = null;
        try {
           pt = this.dao.getProductType(barCode);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return pt;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        List<ProductType> list = new ArrayList<>();
        if(description == null) {
            description = "";
        }
        try {
            String finalDescription = description;
            list = this.dao.getProductTypes().stream()
                    .filter(pt -> pt.getProductDescription().contains(finalDescription)).collect(Collectors.toList());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        if(productId == null || productId <= 0){
            throw new InvalidProductIdException("Product id provided is not good");
        }
        try {
            it.polito.ezshop.model.ProductType pt = this.dao.getProductType(productId);
            if (pt == null || pt.getQuantity() + toBeAdded < 0) {
                return false;
            }
            pt.setQuantity(pt.getQuantity() + toBeAdded);
            this.dao.updateProductType(pt);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        if(productId == null || productId <= 0){
            throw new InvalidProductIdException("Product id provided is not good");
        }
        if(!newPos.matches("^[0-9]+-[A-Za-z0-9]+-[0-9]+")) {
            throw new InvalidLocationException("Product location provided is not good");
        }
        try {
            it.polito.ezshop.model.ProductType pt = this.dao.getProductType(productId);
            if (pt == null) {
                return false;
            }
            if (this.getAllProductTypes()
                    .stream().filter(p -> p.getLocation().equals(newPos))
                    .findFirst()
                    .orElse(null)
                    != null
            ) {
                return false;
            }
            pt.setLocation(newPos);
            this.dao.updateProductType(pt);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        if(productCode == null || !it.polito.ezshop.model.ProductType.checkBarcode(productCode) || productCode.isEmpty()){
            throw new InvalidProductCodeException("Product barcode provided is not good");
        }
        if(quantity <= 0) {
            throw new InvalidQuantityException("Quantity provided is too low");
        }
        if(pricePerUnit <= 0) {
            throw new InvalidPricePerUnitException("Price per unit is too low");
        }
        if(this.getProductTypeByBarCode(productCode) == null) {
            return -1;
        }
        int id;
        try {
            do {
                id = Math.abs(new Random().nextInt());
            } while (this.dao.getOrder(id) != null);
            Integer balanceId = null;
            it.polito.ezshop.model.Order o = new it.polito.ezshop.model.Order(id, balanceId, productCode, pricePerUnit, quantity, "issued");
            o.issueWarningReorder();
            this.dao.addOrder(o);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        if(productCode == null || !it.polito.ezshop.model.ProductType.checkBarcode(productCode) || productCode.isEmpty()){
            throw new InvalidProductCodeException("Product barcode provided is not good");
        }
        if(quantity <= 0) {
            throw new InvalidQuantityException("Quantity provided is too low");
        }
        if(pricePerUnit <= 0) {
            throw new InvalidPricePerUnitException("Price per unit is too low");
        }
        if(this.computeBalance() < pricePerUnit*quantity) {
            return -1;
        }
        if(this.getProductTypeByBarCode(productCode) == null) {
            return -1;
        }
        int id;
        try {
            do {
                id = Math.abs(new Random().nextInt());
            } while (dao.getOrder(id) != null);
            Integer balanceId = null;
            it.polito.ezshop.model.Order o = new it.polito.ezshop.model.Order(id, balanceId, productCode, pricePerUnit, quantity, "ISSUED");
            int idbo;
            do {
                idbo = Math.abs(new Random().nextInt());
            } while (dao.getBalanceOperation(idbo) != null);
            it.polito.ezshop.model.BalanceOperation bo = new it.polito.ezshop.model.BalanceOperation(idbo, LocalDate.now(), pricePerUnit * quantity, "debit");
            o.sendAndPay();
            o.setBalanceId(idbo);
            this.dao.addBalanceOperation(bo);
            this.dao.addOrder(o);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        if(orderId == null || orderId <= 0){
            throw new InvalidOrderIdException("Order id provided is not good");
        }
        try {
            it.polito.ezshop.model.Order o = this.dao.getOrder(orderId);
            if(o == null) {
                return false;
            }
            if(!o.getStatus().equals("ISSUED")) {
                return false;
            }
            if(this.computeBalance() < o.getPricePerUnit()*o.getQuantity()) {
                return false;
            }
            int idbo;
            do {
                idbo = Math.abs(new Random().nextInt());
            } while (dao.getBalanceOperation(idbo) != null);
            it.polito.ezshop.model.BalanceOperation bo = new it.polito.ezshop.model.BalanceOperation(idbo, LocalDate.now(), o.getPricePerUnit()*o.getQuantity(), "debit");
            o.payIssuedReorderedWarning();
            o.setBalanceId(idbo);
            this.dao.addBalanceOperation(bo);
            this.dao.updateOrder(o);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        isAuthorized("Administrator", "ShopManager");
        if(orderId == null || orderId <= 0){
            throw new InvalidOrderIdException("Order id provided is not good");
        }
        try {
            it.polito.ezshop.model.Order o = this.dao.getOrder(orderId);
            if(o == null) {
                return false;
            }
            if(!(o.getStatus().equals("PAYED") || o.getStatus().equals("ISSUED"))) {
                return false;
            }
            it.polito.ezshop.model.ProductType pt = this.dao.getProductType(o.getProductCode());
            if(pt.getLocation() == null || pt.getLocation().isEmpty()) {
                throw new InvalidLocationException("Product type location is not valid");
            }
            o.setStatus("COMPLETED");
            pt.setQuantity(pt.getQuantity()+o.getQuantity());
            this.dao.updateOrder(o);
            this.dao.updateProductType(pt);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean recordOrderArrivalRFID(Integer orderId, String RFIDfrom) throws InvalidOrderIdException, UnauthorizedException, 
InvalidLocationException, InvalidRFIDException {
        isAuthorized("Administrator", "ShopManager");
        if(orderId == null || orderId <= 0){
            throw new InvalidOrderIdException("Order id provided is not good");
        }
        if(RFIDfrom == null || RFIDfrom.isEmpty() || !(RFIDfrom.matches("^[0-9]{12}"))) {
            throw new InvalidRFIDException("RFID provided is not good");
        }
        try {
            it.polito.ezshop.model.Order o = this.dao.getOrder(orderId);
            if(o == null) {
                return false;
            }
            if(!(o.getStatus().equals("PAYED") || o.getStatus().equals("ISSUED"))) {
                return false;
            }
            if(o.getStatus().equals("COMPLETED")) {
                return true;
            }
            it.polito.ezshop.model.ProductType pt = this.dao.getProductType(o.getProductCode());
            if(pt.getLocation() == null || pt.getLocation().isEmpty()) {
                throw new InvalidLocationException("Product type location is not valid");
            }
            int RFIDfromInt = Integer.parseInt(RFIDfrom);
            List<Product> lp = pt.getProductList();
            for(int i = 0; i<o.getQuantity(); i++) {
                String RFID = String.valueOf(RFIDfromInt+i);
                int fill = 12-RFID.length();
                for(int j = 0; j<fill; j++) {
                    RFID = "0".concat(RFID);
                }
                Product p = new Product(RFID);
                lp.add(p);
            }
            pt.setProductList(lp);
            pt.setQuantity(pt.getQuantity()+o.getQuantity());
            o.setStatus("COMPLETED");
            this.dao.updateOrder(o);
            this.dao.updateProductType(pt);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        List<Order> list = new ArrayList<>();
        try {
            list = new ArrayList<>(this.dao.getOrders());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(customerName == null || customerName.isEmpty()){
            throw new InvalidCustomerNameException("Customer name provided is not good");
        }
        int id;
        try {
            do {
                id = Math.abs(new Random().nextInt());
            } while (dao.getCustomer(id) != null);
            it.polito.ezshop.model.Customer c = new it.polito.ezshop.model.Customer(id, customerName, null);
            this.dao.addCustomer(c);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(id == null || id <= 0){
            throw new InvalidCustomerIdException("Customer id provided is not good");
        }
        if(newCustomerName == null || newCustomerName.isEmpty()){
            throw new InvalidCustomerNameException("Customer name provided is not good");
        }
        if(newCustomerCard != null && !newCustomerCard.isEmpty() && !LoyaltyCard.isValidCard(newCustomerCard)) {
            throw new InvalidCustomerCardException("Customer card provided is not good");
        }
        try {
            it.polito.ezshop.model.Customer c = this.dao.getCustomer(id);
            if(c == null) {
                return false;
            }
            c.setCustomerName(newCustomerName);
            if(newCustomerCard != null) {
                if(
                        this.dao.getCustomers().stream()
                                .filter(cust -> cust.getCustomerCard() != null && cust.getCustomerCard().equals(newCustomerCard))
                                .findFirst()
                                .orElse(null)
                        != null

                ) {
                    return false;
                }
                if(newCustomerCard.isEmpty()) {
                    this.dao.deleteCard(c.getCustomerCard());
                } else {
                    it.polito.ezshop.model.LoyaltyCard card = new it.polito.ezshop.model.LoyaltyCard(newCustomerCard);
                    c.setCustomerCard(card.getId());
                    this.dao.addCard(card, c);
                }
            }
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(id == null || id <= 0){
            throw new InvalidCustomerIdException("Customer id provided is not good");
        }
        try {
            return this.dao.deleteCustomer(id);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(id == null || id <= 0){
            throw new InvalidCustomerIdException("Customer id provided is not good");
        }
        it.polito.ezshop.model.Customer c = null;
        try {
            c = this.dao.getCustomer(id);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        List<Customer> list = new ArrayList<>();
        try {
            list = new ArrayList<>(this.dao.getCustomers());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public String createCard() throws UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        String cardNumber = "";
        try {
            StringBuilder salt;
            String SALTCHARS = "1234567890";
            do {
                Random rand = new Random();
                salt = new StringBuilder();
                for (int i = 0; i < 10; i++) {
                    salt.append(SALTCHARS.charAt(rand.nextInt(10)));
                }
            } while (this.dao.getCard(cardNumber) != null);
            cardNumber = salt.toString();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cardNumber;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(customerId == null || customerId <= 0){
            throw new InvalidCustomerIdException("Customer id provided is not good");
        }
        if(!LoyaltyCard.isValidCard(customerCard)){
            throw new InvalidCustomerCardException("Customer card provided is not good");
        }
        try {
            if(this.dao.getCard(customerCard) != null) {
                return false;
            }
            it.polito.ezshop.model.Customer cust = this.dao.getCustomer(customerId);
            if(cust == null) {
                return false;
            }
            LoyaltyCard card = new LoyaltyCard(customerCard);

            this.dao.addCard(card, cust);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(!LoyaltyCard.isValidCard(customerCard)){
            throw new InvalidCustomerCardException("Customer card provided is not good");
        }
        try {
            LoyaltyCard card = this.dao.getCard(customerCard);
            if(card == null) {
                return false;
            }
            if(card.getPoint()+pointsToBeAdded < 0) {
                return false;
            }
            card.setPoint(card.getPoint()+pointsToBeAdded);

            this.dao.updateCard(card);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        // NON CONSIDERO IL TICKET (UPDATE: VA CONSIDERATO IN QUALCHE MODO PER GLI INTEGRATION TEST)
        
        isAuthorized("Administrator", "ShopManager", "Cashier");
        int id;
        try {
            do {
                id = Math.abs(new Random().nextInt());
            } while (dao.getBalanceOperation(id) != null && isInOpenTransaction(id));
            it.polito.ezshop.model.SaleTransaction s = new it.polito.ezshop.model.SaleTransaction(id, LocalDate.now());
            
            // per i test, assegno un ticket number uguale al ID della transazione, in modo che mi venga "ritornato" in mirroring
            s.setTicketNumber(id);

            openSaleTransactions.add(s);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            id = -1;
        }
        return id;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(transactionId==null || transactionId<=0){
            throw new InvalidTransactionIdException("Transaction ID provided is not good");
        }
        if(amount<=0){
            throw new InvalidQuantityException("Amount provided is not good");
        }
        if(productCode==null || productCode.length()==0 || !it.polito.ezshop.model.ProductType.checkBarcode(productCode)){//|| it.polito.ezshop.model.ProductType.checkBarcode(productCode)){
            throw new InvalidProductCodeException("Product provided is not good");
        }
        try {
            it.polito.ezshop.model.ProductType prod=this.dao.getProductType(productCode);
            if(prod==null || prod.getQuantity()<amount){
                return false;
            }
            it.polito.ezshop.model.SaleTransaction s=getOpenSaleTransaction(transactionId);
            s.addProduct(prod,amount);
            prod.setQuantity(prod.getQuantity()-amount);    // update available quantity
            this.dao.updateProductType(prod);               // update product type
        }
        catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean addProductToSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException{
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(transactionId==null || transactionId<=0){
            throw new InvalidTransactionIdException("Transaction ID provided is not good");
        }
        if(RFID==null || RFID.length()!=12){
            throw new InvalidRFIDException("RFID provided is not good");
        }
        try {
            Product prod=null;
            it.polito.ezshop.model.ProductType prodType=null;
            boolean trov=false;
            for(it.polito.ezshop.model.ProductType pType:this.dao.getProductTypes()){
                for(Product p:pType.getProductList()){
                    if(p.getRFID().equals(RFID)){
                        prod=p;
                        prodType=pType;
                        trov=true;
                        break;
                    }
                }
                if(trov){
                    break;
                }
            }
            if(!trov || prod==null || prod.getStatus()== Product.Status.BUYED || prodType.getQuantity()<1){
                return false;
            }
            it.polito.ezshop.model.SaleTransaction s=getOpenSaleTransaction(transactionId);
            s.addProductByRFID(prodType,prod);
            prodType.setQuantity(prodType.getQuantity()-1);    // update available quantity
            prodType.setStatusByRFID(RFID, Product.Status.BUYED);
            this.dao.updateProductType(prodType);               // update product type
        }
        catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(transactionId==null || transactionId<=0){
            throw new InvalidTransactionIdException("Transaction ID provided is not good");
        }
        if(amount<=0){
            throw new InvalidQuantityException("Amount provided is not good");
        }
        if(productCode==null || productCode.length()==0 ||!it.polito.ezshop.model.ProductType.checkBarcode(productCode) ){
            throw new InvalidProductCodeException("Product provided is not good");
        }
        try {
            it.polito.ezshop.model.ProductType prod=this.dao.getProductType(productCode);
            it.polito.ezshop.model.SaleTransaction s=getOpenSaleTransaction(transactionId);
            if(!s.removeProduct(prod,amount)){
                return false;       // quantity to delete>actual quantity on sale Transaction
            }
            prod.setQuantity(prod.getQuantity()+amount);
            this.dao.updateProductType(prod);           // update product type
            return true;
        }
        catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean deleteProductFromSaleRFID(Integer transactionId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, InvalidQuantityException, UnauthorizedException{
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(transactionId==null || transactionId<=0){
            throw new InvalidTransactionIdException("Transaction ID provided is not good");
        }
        if(RFID==null || RFID.length()!=12){
            throw new InvalidRFIDException("RFID provided is not good");
        }
        try {
            Product prod=null;
            it.polito.ezshop.model.ProductType prodType=null;
            boolean trov=false;
            for(it.polito.ezshop.model.ProductType pType:this.dao.getProductTypes()){
                for(Product p:pType.getProductList()){
                    if(p.getRFID().equals(RFID)){
                        prod=p;
                        prodType=pType;
                        trov=true;
                        break;
                    }
                }
                if(trov){
                    break;
                }
            }
            if(!trov || prod==null || prod.getStatus()== Product.Status.AVAILABLE){
                return false;
            }
            it.polito.ezshop.model.SaleTransaction s=getOpenSaleTransaction(transactionId);
            if(!s.removeProductByRFID(prodType,prod)){
                return false;
            }
            prodType.setQuantity(prodType.getQuantity()+1);
            prodType.setStatusByRFID(RFID, Product.Status.AVAILABLE);
            this.dao.updateProductType(prodType);
            return true;
        }
        catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(transactionId==null || transactionId<=0){
            throw new InvalidTransactionIdException("Transaction ID provided is not good");
        }
        if(discountRate<=0 || discountRate>=1){
            throw new InvalidDiscountRateException("Discount rate provided is not good");
        }
        if(productCode==null || productCode.length()==0 ||!it.polito.ezshop.model.ProductType.checkBarcode(productCode)){
            throw new InvalidProductCodeException("Product provided is not good");
        }
        try {
            it.polito.ezshop.model.SaleTransaction s=getOpenSaleTransaction(transactionId);
            it.polito.ezshop.model.ProductType prod=this.dao.getProductType(productCode);
            return(s.applyDiscountToProduct(prod,discountRate));
        }
        catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(transactionId==null || transactionId<=0){
            throw new InvalidTransactionIdException("Transaction ID provided is not good");
        }
        if(discountRate<=0 || discountRate>=1){
            throw new InvalidDiscountRateException("Discount rate provided is not good");
        }
        try {
            it.polito.ezshop.model.SaleTransaction s=getOpenSaleTransaction(transactionId);
            s.setDiscountRate(discountRate);
            return true;
        }
        catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(transactionId==null || transactionId<=0){
            throw new InvalidTransactionIdException("Transaction ID provided is not good");
        }
        try {
            it.polito.ezshop.data.SaleTransaction s;
            if(isInOpenTransaction(transactionId)){
                s=getOpenSaleTransaction(transactionId);
            }
            else{
                s=getSaleTransaction(transactionId);
            }
            if(s==null){
                throw new IllegalArgumentException();
            }
            double totalAmount=s.getPrice();
            return (int)totalAmount/10;
        }
        catch (IllegalArgumentException e) {
            return -1;
        }
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(transactionId==null || transactionId<=0){
            throw new InvalidTransactionIdException("Transaction ID provided is not good");
        }
        try {
            it.polito.ezshop.model.SaleTransaction s=getOpenSaleTransaction(transactionId);
            openSaleTransactions.remove(s);
            this.dao.addBalanceOperation(s);
            return true;
        }
        catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(saleNumber==null || saleNumber<=0){
            throw new InvalidTransactionIdException("Transaction saleNumber provided is not good");
        }
        try {
            it.polito.ezshop.model.BalanceOperation b=this.dao.getBalanceOperation(saleNumber);
            if(!b.getType().equalsIgnoreCase("transaction")){
                throw new InvalidTransactionIdException("Transaction saleNumber provided is not good");
            }
            it.polito.ezshop.model.SaleTransaction s=(it.polito.ezshop.model.SaleTransaction) b;
            resetSaleTransaction(s);
            this.dao.deleteBalanceOperation(saleNumber);
            return true;
        }
        catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            return false;
        }
    }

    private void resetSaleTransaction(it.polito.ezshop.model.SaleTransaction s) throws InvalidTransactionIdException, UnauthorizedException {
        try{
            ArrayList<String> barcodes=s.getProductList();
            int i=0;
            while(i<barcodes.size()){
                ProductType p=this.dao.getProductType(barcodes.get(i));
                updateQuantity(p.getId(),s.getProductQuantity((it.polito.ezshop.model.ProductType) p));
                i++;
            }
        }
       catch(InvalidProductIdException | IOException | ClassNotFoundException e ){
            throw new InvalidTransactionIdException();
       }
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(transactionId==null || transactionId<=0){
            throw new InvalidTransactionIdException("Transaction saleNumber provided is not good");
        }
        try {
            it.polito.ezshop.model.BalanceOperation b=this.dao.getBalanceOperation(transactionId);
            if(b==null){
                return null;
            }
            if(!b.getType().equalsIgnoreCase("transaction")){
                throw new InvalidTransactionIdException("Transaction saleNumber provided is not good");
            }
            return (it.polito.ezshop.model.SaleTransaction) b;
        }
        catch (IOException | ClassNotFoundException | IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(saleNumber==null || saleNumber<=0){
            throw new InvalidTransactionIdException("Transaction saleNumber provided is not good");
        }
        try{
            it.polito.ezshop.model.BalanceOperation b=this.dao.getBalanceOperation(saleNumber);
            if(!b.getType().equalsIgnoreCase("transaction")){
                throw new InvalidTransactionIdException("Transaction saleNumber provided is not good");
            }
            int id;
            do {
                id = Math.abs(new Random().nextInt());
            } while (dao.getBalanceOperation(id) != null && isInOpenReturnTransaction(id));
            ReturnTransaction r=new ReturnTransaction(id,saleNumber,LocalDate.now());
            openReturnTransactions.add(r);
            return id;
        }
        catch(IOException | ClassNotFoundException | IllegalArgumentException e){
            return -1;
        }
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidQuantityException, InvalidProductCodeException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(returnId==null || returnId<=0){
            throw new InvalidTransactionIdException("Return Id provided is not good");
        }
        if(productCode==null || productCode.length()==0 ||!it.polito.ezshop.model.ProductType.checkBarcode(productCode)){
            throw new InvalidProductCodeException("Product provided is not good");
        }
        if(amount<=0){
            throw new InvalidQuantityException();
        }
        try{
            if(!isInOpenReturnTransaction(returnId)){
                return false;
            }
            ReturnTransaction r=getOpenReturnTransaction(returnId);
            //1. Find corresponding saleTransaction
            int saleNumber=r.getSaleNumber();
            it.polito.ezshop.model.BalanceOperation b=this.dao.getBalanceOperation(saleNumber);
            if(!b.getType().equalsIgnoreCase("transaction")){
                throw new InvalidTransactionIdException("Transaction saleNumber provided is not good");
            }
            it.polito.ezshop.model.SaleTransaction s=(it.polito.ezshop.model.SaleTransaction) b;
            //2. Get all products from saleTransaction
            ArrayList<String> barcodes=s.getProductList();
            it.polito.ezshop.model.ProductType p=this.dao.getProductType(productCode);
            
            int i = 0;
            boolean check = false;
            while (i < barcodes.size()) {
                if(barcodes.get(i).equals(p.getBarCode()))
                {
                	check = true;
                	i = barcodes.size();
                }
                i++;
            }
            if(!check){
                return false;       // this porductType wasn't in the saleTransaction
            }

            if(r.getProductQuantity(p)+amount> s.getProductQuantity(p)){
                return false;       // amount too big
            }
            r.addProduct(p,amount);
            return true;
        }
        catch(IOException | ClassNotFoundException | IllegalArgumentException e){
            return false;
        }
    }

    @Override
    public boolean returnProductRFID(Integer returnId, String RFID) throws InvalidTransactionIdException, InvalidRFIDException, UnauthorizedException 
    {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(returnId==null || returnId<=0){
            throw new InvalidTransactionIdException("Return Id provided is not good");
        }
        if(RFID==null || RFID.length()!=12){
            throw new InvalidRFIDException("RFID provided is not good");
        }
        try{
            Product prod=null;
            it.polito.ezshop.model.ProductType prodType=null;
            boolean trov=false;
            for(it.polito.ezshop.model.ProductType pType:this.dao.getProductTypes()){
                for(Product p:pType.getProductList()){
                    if(p.getRFID().equals(RFID)){
                        prod=p;
                        prodType=pType;
                        trov=true;
                        break;
                    }
                }
                if(trov){
                    break;
                }
            }
            if(!trov || prod==null || prod.getStatus()== Product.Status.AVAILABLE){
                return false;
            }
            if(!isInOpenReturnTransaction(returnId)){
                return false;
            }
            ReturnTransaction r=getOpenReturnTransaction(returnId);
            //1. Find corresponding saleTransaction
            int saleNumber=r.getSaleNumber();
            it.polito.ezshop.model.BalanceOperation b=this.dao.getBalanceOperation(saleNumber);
            if(!b.getType().equalsIgnoreCase("transaction")){
                throw new InvalidTransactionIdException("Transaction saleNumber provided is not good");
            }
            it.polito.ezshop.model.SaleTransaction s=(it.polito.ezshop.model.SaleTransaction) b;
            //2. Get all productTypes from saleTransaction
            ArrayList<String> barcodes=s.getProductList();

            int i = 0;
            boolean check = false;
            while (i < barcodes.size()) {
                if(barcodes.get(i).equals(prodType.getBarCode()))
                {
                    check = true;
                    i = barcodes.size();
                }
                i++;
            }
            if(!check){
                return false;       // this porductType wasn't in the saleTransaction
            }

            // 3.check if RFID was in saleTransaction
            ArrayList<String> RFIDS=s.getRFIDs();
            i=0;
            check=false;
            while(i<RFIDS.size()){
                if(RFIDS.get(i).equals(RFID)){
                    check=true;
                    i=RFIDS.size();
                }
                i++;
            }
            if(!check){
                return false;       // this RFID wasn't in the saleTransaction
            }

            if(r.getProductQuantity(prodType)+1> s.getProductQuantity(prodType)){
                return false;       // amount too big
            }
            r.addProductByRFID(prodType,prod);
            return true;
        }
        catch(IOException | ClassNotFoundException | IllegalArgumentException e){
            return false;
        }
    }


    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(returnId==null || returnId<=0){
            throw new InvalidTransactionIdException("Return Id provided is not good");
        }
        try{
            if(!isInOpenReturnTransaction(returnId)){
                throw new InvalidTransactionIdException("Return Id provided is not good");
            }
            ReturnTransaction r=getOpenReturnTransaction(returnId);
            if(!commit){
                openReturnTransactions.remove(r);
            }
            else{
                // for each productType in Return Transaction update its quantity
                ArrayList<it.polito.ezshop.model.ProductType> prodList=r.getProductList();
                for (it.polito.ezshop.model.ProductType p : prodList) {
                    it.polito.ezshop.model.ProductType pr = this.dao.getProductType(p.getBarCode());
                    pr.setQuantity(pr.getQuantity() + r.getProductQuantity(p));
                    //this.dao.updateProductType(pr);
                }
                // for each product in Return Transaction update its status
                ArrayList<Product> pList=r.getProductRFIDList();
                for(Product prod: pList){
                    boolean trov=false;
                    for(it.polito.ezshop.model.ProductType pType:this.dao.getProductTypes()){
                        for(Product p:pType.getProductList()){
                            if(prod.getRFID().equals(p.getRFID())){
                                pType.setStatusByRFID(p.getRFID(), Product.Status.AVAILABLE);
                                this.dao.updateProductType(pType);
                                trov=true;
                                break;
                            }
                        }
                        if(trov){
                            break;
                        }
                    }
                }
                openReturnTransactions.remove(r);
                r.setStatus(ReturnTransaction.Status.ENDED);
                this.dao.addBalanceOperation(r);
            }
            return true;
        }
        catch(IOException | ClassNotFoundException | IllegalArgumentException e){
            return false;
        }
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(returnId==null || returnId<=0){
            throw new InvalidTransactionIdException("Return Id provided is not good");
        }
        try{
            it.polito.ezshop.model.BalanceOperation b=this.dao.getBalanceOperation(returnId);
            if(!b.getType().equalsIgnoreCase("return")){
                throw new InvalidTransactionIdException("Transaction saleNumber provided is not good");
            }
            this.dao.deleteBalanceOperation(returnId);
            return true;
        }
        catch(IOException | ClassNotFoundException | IllegalArgumentException e){
            return false;
        }
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        double change;
        if(ticketNumber == null || ticketNumber <= 0) {
            throw new InvalidTransactionIdException("Transaction id provided is not valid");
        }
        if(cash <= 0) {
            throw new InvalidPaymentException("Cash provided is not valid");
        }
        try {
            it.polito.ezshop.model.SaleTransaction st = this.dao.getBalanceOperations().stream()
                    .filter(bo -> bo.getType().equals("transaction"))
                    .map(bo -> (it.polito.ezshop.model.SaleTransaction) bo)
                    .filter(sale -> sale.getTicketNumber().equals(ticketNumber))
                    .findFirst()
                    .orElse(null);
            if(st == null) {
                change = -1;
            } else {
                if(cash < st.getPrice()) {
                    change = -1;
                } else {
                    st.setStatus(it.polito.ezshop.model.SaleTransaction.Status.PAYED);
                    this.dao.updateBalanceOperation(st);
                    change = cash - st.getPrice();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            change = -1;
        }
        return change;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(ticketNumber == null || ticketNumber <= 0) {
            throw new InvalidTransactionIdException("Transaction id provided is not valid");
        }
        if(creditCard == null || creditCard.length() != 16 || !this.checkLuhn(creditCard)) {
            throw new InvalidCreditCardException("Credit card provided is not valid");
        }
        try {
            it.polito.ezshop.model.SaleTransaction st = this.dao.getBalanceOperations().stream()
                    .filter(bo -> bo.getType().equals("transaction"))
                    .map(bo -> (it.polito.ezshop.model.SaleTransaction) bo)
                    .filter(sale -> sale.getTicketNumber().equals(ticketNumber))
                    .findFirst()
                    .orElse(null);
            CreditCard card = this.dao.getCreditCard(creditCard);
            if(st == null || card == null) {
                return false;
            } else {
                if(card.getAmount() < st.getPrice()) {
                    return false;
                } else {
                    st.setStatus(it.polito.ezshop.model.SaleTransaction.Status.PAYED);
                    card.setAmount(card.getAmount()-st.getPrice());
                    this.dao.updateBalanceOperation(st);
                    this.dao.updateCreditCard(card);
                    return true;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        double money;
        if(returnId == null || returnId <= 0) {
            throw new InvalidTransactionIdException("Return transaction id provided is not valid");
        }
        try {
            it.polito.ezshop.model.ReturnTransaction rt = this.dao.getBalanceOperations().stream()
                    .filter(bo -> bo.getType().equals("return"))
                    .map(bo -> (it.polito.ezshop.model.ReturnTransaction) bo)
                    .filter(ret -> ret.getBalanceId() == returnId && ret.getStatus().equals(ReturnTransaction.Status.ENDED))
                    .findFirst()
                    .orElse(null);
            if(rt == null) {
                money = -1;
            } else {
                if(rt.getMoney() > this.computeBalance()) {
                    money = -1;
                } else {
                    rt.setStatus(it.polito.ezshop.model.ReturnTransaction.Status.PAYED);
                    this.dao.updateBalanceOperation(rt);
                    money = rt.getMoney();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            money = -1;
        }
        return money;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        isAuthorized("Administrator", "ShopManager", "Cashier");
        if(returnId == null || returnId <= 0) {
            throw new InvalidTransactionIdException("Return transaction id provided is not valid");
        }
        if(creditCard == null || creditCard.length() != 16 || !this.checkLuhn(creditCard)) {
            throw new InvalidCreditCardException("Credit card provided is not valid");
        }
        double money;
        try {
            it.polito.ezshop.model.ReturnTransaction rt = this.dao.getBalanceOperations().stream()
                    .filter(bo -> bo.getType().equals("return"))
                    .map(bo -> (it.polito.ezshop.model.ReturnTransaction) bo)
                    .filter(ret -> ret.getBalanceId() == returnId)
                    .findFirst()
                    .orElse(null);
            CreditCard card = this.dao.getCreditCard(creditCard);
            if(rt == null || card == null) {
                money = -1;
            } else {
                if(card.getAmount() < rt.getMoney()) {
                    money = -1;
                } else {
                    rt.setStatus(it.polito.ezshop.model.ReturnTransaction.Status.PAYED);
                    card.setAmount(card.getAmount()-rt.getMoney());
                    this.dao.updateBalanceOperation(rt);
                    this.dao.updateCreditCard(card);
                    money = rt.getMoney();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            money = -1;
        }
        return money;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        try {
            int id;
            do {
                id = Math.abs(new Random().nextInt());
            } while (dao.getBalanceOperation(id) != null);
            if(this.computeBalance()+toBeAdded < 0) {
                return false;
            }
            it.polito.ezshop.model.BalanceOperation bo;
            if(toBeAdded < 0) {
                bo = new Debit(id, LocalDate.now(), toBeAdded, "debit");
            } else {
                bo = new Credit(id, LocalDate.now(), toBeAdded, "credit");
            }
            this.dao.addBalanceOperation(bo);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
        isAuthorized("Administrator", "ShopManager");
        List<BalanceOperation> list = new ArrayList<>();

        try {
            List<BalanceOperation> ops = new ArrayList<>(this.dao.getBalanceOperations());
            list = new ArrayList<>(ops);
            if (from != null && to != null){
                if(from.isAfter(to)) {
                    // Fix date order
                    LocalDate tmp = from;
                    from = to;
                    to = tmp;
                }
                LocalDate finalTo = to;
                LocalDate finalFrom = from;
                list = ops.stream()
                        .filter(bo -> bo.getDate().isAfter(finalFrom) && bo.getDate().isBefore(finalTo)).collect(Collectors.toList());
                //filter between two dates
            } else if(from == null && to != null) {
                //filter before "to" date
                LocalDate finalTo = to;
                list = ops.stream().filter(bo -> bo.getDate().isBefore(finalTo)).collect(Collectors.toList());
            } else if(from != null && to == null) {
                //filter after "from" date
                LocalDate finalFrom = from;
                list = ops.stream().filter(bo -> bo.getDate().isAfter(finalFrom)).collect(Collectors.toList());
            }
            //Return value is ALL
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
         isAuthorized("Administrator", "ShopManager", "Cashier");
        double balance = 0.0;
        try {
            List<BalanceOperation> ops = new ArrayList<>(this.dao.getBalanceOperations());
            balance = ops.stream()
                    .mapToDouble(bo -> {
                        double money = 0;
                        switch (bo.getType()) {
                            case "transaction":
                                // It's a sale transaction
                                it.polito.ezshop.model.SaleTransaction st = (it.polito.ezshop.model.SaleTransaction) bo;
                                if (st.getStatus().equals(it.polito.ezshop.model.SaleTransaction.Status.PAYED)) {
                                    money = st.getMoney();
                                }
                                break;
                            case "return":
                                // It's a return transaction
                                ReturnTransaction rt = (ReturnTransaction) bo;
                                money = rt.getMoney();
                                break;
                            case "debit":
                                // It's a return transaction
                                // ReturnTransaction rt = (ReturnTransaction) bo;
                                money = bo.getMoney();
                                if(money > 0) {
                                    money = -money;
                                }
                                break;
                            default:
                                // It's a balance operation
                                money = bo.getMoney();
                                break;
                        }
                        return money;
                    })
                    .sum();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return balance;
    }

    private void isAuthorized(String... roles) throws UnauthorizedException {
        if(userLogged == null) {
            throw new UnauthorizedException("No logged user");
        }
        for(String role : roles) {
            if(userLogged.getRole().equals(role)) {
                return;
            }
        }
        throw new UnauthorizedException("No rights to access to this function. Only "+ Arrays.toString(roles) +" roles are accepted");
    }

    private boolean isInOpenTransaction(int id){
        int i=0;
        boolean trov=false;
        while(!trov && i<openSaleTransactions.size()){
            if(id==openSaleTransactions.get(i).getBalanceId()){
                trov=true;
            }
            i++;
        }
        return trov;
    }

    private it.polito.ezshop.model.SaleTransaction getOpenSaleTransaction(int id) throws IllegalArgumentException{
        int i=0;
        while(i<openSaleTransactions.size()){
            if(id==openSaleTransactions.get(i).getBalanceId()){
                return openSaleTransactions.get(i);
            }
            i++;
        }
        throw new IllegalArgumentException();
    }

    private boolean isInOpenReturnTransaction(int id){
        int i=0;
        boolean trov=false;
        while(!trov && i<openReturnTransactions.size()){
            if(id==openReturnTransactions.get(i).getBalanceId()){
                trov=true;
            }
            i++;
        }
        return trov;
    }

    private ReturnTransaction getOpenReturnTransaction(int id) throws IllegalArgumentException {
        int i=0;
        while(i<openReturnTransactions.size()){
            if(id==openReturnTransactions.get(i).getBalanceId()){
                return openReturnTransactions.get(i);
            }
            i++;
        }
        throw new IllegalArgumentException();
    }

    private boolean checkLuhn(String cardNo) {
        int nDigits = cardNo.length();

        int nSum = 0;
        boolean isSecond = false;
        for (int i = nDigits - 1; i >= 0; i--)
        {

            int d = cardNo.charAt(i) - '0';

            if (isSecond)
                d = d * 2;

            // We add two digits to handle
            // cases that make two digits
            // after doubling
            nSum += d / 10;
            nSum += d % 10;

            isSecond = !isSecond;
        }
        return (nSum % 10 == 0);
    }
}
