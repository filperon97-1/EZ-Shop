package it.polito.ezshop.model;

import it.polito.ezshop.exceptions.InvalidCustomerCardException;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class DAO {
	
	private static DAO instance = null;
    public static DAO getInstance() throws IOException, ClassNotFoundException {
        if (instance == null) 
		{
            instance = new DAO();
        }
        return instance;
    }

	private final String pathToDB = "database";
	private final String usersFileName = "users";
	private final String ordersFileName = "orders";
	private final String creditCardsFileName = "creditCards";
	private final String accountBookFileName = "accountBook";
	private final String customersFileName = "customers";
	private final String productTypesFileName = "productTypes";

    private DAO() throws IOException, ClassNotFoundException {
		initDB();
	}
	
    public boolean resetData() throws IOException, ClassNotFoundException {
		FileUtils.cleanDirectory(new File(pathToDB));
		initDB();
		return true;
    }
	
	
	//*** USER ***//	
    public boolean addUser(User u)  throws IOException, ClassNotFoundException {
		List<User> usersList = deserialize(ArrayList.class, usersFileName);
		usersList.add(u);
		serialize(usersList, usersFileName);
        return true;
    }
    public User getUser(Integer id)  throws IOException, ClassNotFoundException {
		List<User> usersList = deserialize(ArrayList.class, usersFileName);
		User u = usersList.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
		/*
		usersList.forEach((u) -> 
		{
            if(u.getId() == id) 
			{
				return u;
			}
        });
        return null;
		 */
		return u;
    }
    public List<User> getUsers()  throws IOException, ClassNotFoundException {
		List<User> usersList = deserialize(ArrayList.class, usersFileName);
        return usersList;
    }
    public boolean deleteUser(Integer id)  throws IOException, ClassNotFoundException {
		List<User> usersList = deserialize(ArrayList.class, usersFileName);
		/*usersList.forEach((u) ->
		{
            if(u.getId().equals(id))
			{
				usersList.remove(u);
				wasFound.set(true);
			}
        });*/
		List<User> usersListNew = usersList.stream()
				.filter(u -> !(u.getId().equals(id)))
				.collect(Collectors.toList());
		serialize(usersListNew, usersFileName);
		return usersListNew.size() < usersList.size();
    }
    public boolean updateUser(User u)  throws IOException, ClassNotFoundException {
		AtomicBoolean wasFound = new AtomicBoolean(false);
		List<User> usersList = deserialize(ArrayList.class, usersFileName);
		usersList.forEach((us) -> 
		{
            if(us.getId().equals(u.getId()))
			{
				us.setUsername(u.getUsername());
				us.setPassword(u.getPassword());
				us.setRole(u.getRole());
				wasFound.set(true);
			}
        });
		serialize(usersList, usersFileName);
        return wasFound.get();
    }
	//*** END USER ***//


	//*** PRODUCT ***//
    public boolean addProductType(ProductType pt)  throws IOException, ClassNotFoundException {
		List<ProductType> productsList = deserialize(ArrayList.class, productTypesFileName);
		productsList.add(pt);
		serialize(productsList, productTypesFileName);
        return false;
    }
    public ProductType getProductType(String barCode)  throws IOException, ClassNotFoundException {
		List<ProductType> productsList = deserialize(ArrayList.class, productTypesFileName);
		AtomicReference<ProductType> pt = new AtomicReference<>();
		productsList.forEach((p) -> 
		{
            if(p.getBarCode().equals(barCode)) 
			{
				pt.set(p);
			}
        });
        return pt.get();
    }
    public ProductType getProductType(Integer id)  throws IOException, ClassNotFoundException {
		List<ProductType> productsList = deserialize(ArrayList.class, productTypesFileName);
		AtomicReference<ProductType> pt = new AtomicReference<>();
		productsList.forEach((p) -> 
		{
            if(p.getId().equals(id))
			{
				pt.set(p);
			}
        });
        return pt.get();
    }
    public List<ProductType> getProductTypes()  throws IOException, ClassNotFoundException {
		List<ProductType> productsList = deserialize(ArrayList.class, productTypesFileName);
		return productsList;
    }
    public boolean deleteProductType(Integer id)  throws IOException, ClassNotFoundException {
        List<ProductType> productsList = deserialize(ArrayList.class, productTypesFileName);
		List<ProductType> productsListNew = productsList.stream()
				.filter(p -> !(p.getId().equals(id)))
				.collect(Collectors.toList());
		serialize(productsListNew, productTypesFileName);
		return productsListNew.size() < productsList.size();
    }
    public boolean updateProductType(ProductType pt)  throws IOException, ClassNotFoundException {
        AtomicBoolean wasFound = new AtomicBoolean(false);
        List<ProductType> productsList = deserialize(ArrayList.class, productTypesFileName);
		productsList.forEach((p) -> 
		{
            if(p.getId().equals(pt.getId()))
			{
				p.setQuantity(pt.getQuantity());
				p.setLocation(pt.getLocation());
				p.setNote(pt.getNote());
				p.setProductDescription(pt.getProductDescription());
				p.setBarCode(pt.getBarCode());
				p.setPricePerUnit(pt.getPricePerUnit());
				p.setProductList(pt.getProductList());
				wasFound.set(true);
			}
        });
		serialize(productsList, productTypesFileName);
        return wasFound.get();
    }	
    public boolean incrementProductTypeQuantity(ProductType pt, Integer amount)  throws IOException, ClassNotFoundException {
        AtomicBoolean wasFound = new AtomicBoolean(false);
        List<ProductType> productsList = deserialize(ArrayList.class, productTypesFileName);
		productsList.forEach((p) -> 
		{
            if(p.getId().equals(pt.getId()))
			{
				Integer newQuantity = p.getQuantity()+amount;			
				p.setQuantity(newQuantity);
				wasFound.set(true);
			}
        });
		serialize(productsList, productTypesFileName);
        return wasFound.get();
    }	
	
	//*** END PRODUCT ***//
	

	//*** CUSTOMER / LOYALTY CARD ***//
    public boolean addCustomer(Customer c)  throws IOException, ClassNotFoundException {
		List<Customer> customersList = deserialize(ArrayList.class, customersFileName);
		customersList.add(c);
		serialize(customersList, customersFileName);
        return false;
    }
    public Customer getCustomer(Integer id)  throws IOException, ClassNotFoundException {
		List<Customer> customersList = deserialize(ArrayList.class, customersFileName);
		Customer c = customersList.stream().filter(cust -> cust.getId().equals(id)).findFirst().orElse(null);
		/*
		customersList.forEach((c) -> 
		{
            if(c.getId() == id) 
			{
				return c;
			}
        });
        return null;
		 */
		return c;
    }
    public List<Customer> getCustomers()  throws IOException, ClassNotFoundException {	
		List<Customer> customersList = deserialize(ArrayList.class, customersFileName);
        return customersList;
    }
    public boolean deleteCustomer(Integer id)  throws IOException, ClassNotFoundException {
		List<Customer> customersList = deserialize(ArrayList.class, customersFileName);
		List<Customer> productsListNew = customersList.stream()
				.filter(c -> !(c.getId().equals(id)))
				.collect(Collectors.toList());
		serialize(productsListNew, customersFileName);
		return productsListNew.size() < customersList.size();
    }/*
    public boolean updateCustomer(Customer c)  throws IOException, ClassNotFoundException {
		AtomicBoolean wasFound = new AtomicBoolean(false);
		List<Customer> customersList = deserialize(ArrayList.class, customersFileName);
		customersList.forEach((cust) -> 
		{
            if(cust.getId().equals(c.getId()))
			{
				cust.setCustomerName(c.getCustomerName());
				cust.setCustomerCard(c.getCustomerCard());
				cust.setPoints(c.getPoints());
				wasFound.set(true);
			}
        });
		serialize(customersList, customersFileName);
        return wasFound.get();
    }
*/

    public boolean addCard(LoyaltyCard lc, Customer c)  throws IOException, ClassNotFoundException {
		AtomicBoolean wasFound = new AtomicBoolean(false);
		List<Customer> customersList = deserialize(ArrayList.class, customersFileName);
		customersList.forEach((cust) -> 
		{
            if(cust.getId().equals(c.getId()))
			{
				cust.setCustomerCard(lc.getId());
				wasFound.set(true);
			}
        });
		serialize(customersList, customersFileName);
        return wasFound.get();
    }
    /*
	public List<LoyaltyCard> getCards()  throws IOException, ClassNotFoundException {
		List<LoyaltyCard> allCards = new ArrayList<>();
		List<Customer> customersList = deserialize(List<Customer>.class, customersFileName);
		customersList.forEach((cust) -> 
		{
			LoyaltyCard card = new LoyaltyCard(cust.getCustomerCard(),cust.getPoints());
            allCards.add(card);
        });
        return allCards;
    }
     */
    public LoyaltyCard getCard(String customerCard)  throws IOException, ClassNotFoundException {
		AtomicReference<LoyaltyCard> card = new AtomicReference<>();
		List<Customer> customersList = deserialize(ArrayList.class, customersFileName);
		/*card = customersList.stream().filter(c -> c.getCustomerCard().equals(customerCard)).findFirst().ifPresent(cust -> {
			try {
				new LoyaltyCard(customerCard, cust.getPoints());
			} catch (InvalidCustomerCardException e) {
				e.printStackTrace();

			}
		}).orElse(null);*/

		customersList.forEach((cust) ->
		{
			if(cust.getCustomerCard() != null && cust.getCustomerCard().equals(customerCard))
			{
				try {
					card.set(new LoyaltyCard(customerCard, cust.getPoints()));
				} catch (InvalidCustomerCardException e) {
					e.printStackTrace();
				}
			}
        });
        return card.get();
    }
    public boolean deleteCard(String customerCard)  throws IOException, ClassNotFoundException {
		AtomicBoolean wasFound = new AtomicBoolean(false);
		List<Customer> customersList = deserialize(ArrayList.class, customersFileName);
		customersList.forEach((cust) -> 
		{
			if(cust.getCustomerCard().equals(customerCard)) 
			{
				cust.setCustomerCard("");
				wasFound.set(true);
			}
        });
		serialize(customersList, customersFileName);
        return wasFound.get();
    }
    public boolean updateCard(LoyaltyCard lc)  throws IOException, ClassNotFoundException {
		AtomicBoolean wasFound = new AtomicBoolean(false);
		List<Customer> customersList = deserialize(ArrayList.class, customersFileName);
		customersList.forEach((cust) -> 
		{
			if(cust.getCustomerCard().equals(lc.getId())) 
			{
				cust.setPoints(lc.getPoint());
				wasFound.set(true);
			}
        });
		serialize(customersList, customersFileName);
        return wasFound.get();
    }
	//*** END CUSTOMER / LOYALTY CARD ***//

	//*** ORDER ***//
	public boolean addOrder(Order u)  throws IOException, ClassNotFoundException {
		List<Order> ordersList = deserialize(ArrayList.class, ordersFileName);
		ordersList.add(u);
		serialize(ordersList, ordersFileName);
		return true;
	}
	public Order getOrder(Integer id)  throws IOException, ClassNotFoundException {
		List<Order> ordersList = deserialize(ArrayList.class, ordersFileName);
		return ordersList.stream().filter(order -> order.getOrderId().equals(id)).findFirst().orElse(null);
		/*
		ordersList.forEach((u) -> 
		{
            if(u.getId() == id) 
			{
				return u;
			}
        });
        return null;
		 */
	}
	public List<Order> getOrders()  throws IOException, ClassNotFoundException {
		List<Order> ordersList = deserialize(ArrayList.class, ordersFileName);
		return ordersList;
	}/*
	public boolean deleteOrder(Integer id)  throws IOException, ClassNotFoundException {
		List<Order> ordersList = deserialize(ArrayList.class, ordersFileName);
		List<Order> ordersListNew = ordersList.stream()
				.filter(o -> !(o.getOrderId().equals(id)))
				.collect(Collectors.toList());
		serialize(ordersListNew, ordersFileName);
		return ordersListNew.size() < ordersList.size();
	}*/
	public boolean updateOrder(Order o)  throws IOException, ClassNotFoundException {
		AtomicBoolean wasFound = new AtomicBoolean(false);
		List<Order> ordersList = deserialize(ArrayList.class, ordersFileName);
		ordersList.forEach((or) ->
		{
			if(or.getOrderId().equals(o.getOrderId()))
			{
				or.setStatus(o.getStatus());
				or.setBalanceId(o.getBalanceId());
				or.setQuantity(o.getQuantity());
				or.setPricePerUnit(o.getPricePerUnit());
				or.setProductCode(o.getProductCode());
				wasFound.set(true);
			}
		});
		serialize(ordersList, ordersFileName);
		return wasFound.get();
	}
	//*** END ORDER ***//

	//*** ACCOUNT BOOK ***//
	public double getBalance()  throws IOException, ClassNotFoundException {
		AccountBook accBook = deserialize(AccountBook.class, accountBookFileName);
		return accBook.showBalance();
	}
    public boolean addBalanceOperation(BalanceOperation bo)  throws IOException, ClassNotFoundException {
		AccountBook accBook = deserialize(AccountBook.class, accountBookFileName);
		accBook.addTransaction(bo);
		serialize(accBook, accountBookFileName);
        return true; 
    }
    public BalanceOperation getBalanceOperation(Integer balanceId)  throws IOException, ClassNotFoundException {
		AccountBook accBook = deserialize(AccountBook.class, accountBookFileName);
		BalanceOperation res = (BalanceOperation) accBook.listAllTransactions().stream().filter(bo -> bo.getBalanceId() == balanceId).findFirst().orElse(null);
		return res;
		/*
		accBook.listAllTransactions().forEach((bo) ->
		{
            if(bo.getBalanceId() == balanceId)
			{
				return bo;
			}
        });
        return null;
        */
    }
    public List<BalanceOperation> getBalanceOperations()  throws IOException, ClassNotFoundException {
		AccountBook accBook = deserialize(AccountBook.class, accountBookFileName);
		return new ArrayList(accBook.listAllTransactions());
    }
    public boolean deleteBalanceOperation(Integer balanceId)  throws IOException, ClassNotFoundException {
		AtomicBoolean wasFound = new AtomicBoolean(false);
		AccountBook accBook = deserialize(AccountBook.class, accountBookFileName);

		int size=accBook.listAllTransactions().size();
		accBook.removeTransaction(balanceId);
		serialize(accBook,accountBookFileName);
		return accBook.listAllTransactions().size()<size;
		/*List<it.polito.ezshop.data.BalanceOperation> listBalanceOperations = accBook.listAllTransactions();List<it.polito.ezshop.data.BalanceOperation> listBalanceOperationsNew = listBalanceOperations.stream()
				.filter(bo -> !(balanceId.equals(bo.getBalanceId())))
				.collect(Collectors.toList());
		serialize(listBalanceOperationsNew, accountBookFileName);
		return listBalanceOperationsNew.size() < listBalanceOperations.size();*/
    }
    public boolean updateBalanceOperation(BalanceOperation bo) throws IOException, ClassNotFoundException {
		AtomicBoolean wasFound = new AtomicBoolean(false);
		AccountBook accBook = deserialize(AccountBook.class, accountBookFileName);

		if(accBook.removeTransaction(bo.getBalanceId())){
			accBook.addTransaction(bo);
			serialize(accBook, accountBookFileName);
			return true;
		}
		else{
			return false;
		}

		
    }
	//*** END ACCOUNT BOOK ***//


	//*** CREDIT CARD ***//
	public CreditCard getCreditCard(String creditCard)  throws IOException, ClassNotFoundException {
		List<CreditCard> creditCardsList = deserialize(ArrayList.class, creditCardsFileName);
		return creditCardsList.stream().filter(cc -> cc.getCreditCard().equals(creditCard)).findFirst().orElse(null);
	}
	public boolean updateCreditCard(CreditCard cc)  throws IOException, ClassNotFoundException {
		AtomicBoolean wasFound = new AtomicBoolean(false);
		List<CreditCard> creditCardsList = deserialize(ArrayList.class, creditCardsFileName);
		creditCardsList.forEach((card) ->
		{
			if(card.getCreditCard().equals(cc.getCreditCard()))
			{
				card.setCreditCard(cc.getCreditCard());
				card.setAmount(cc.getAmount());
				wasFound.set(true);
			}
		});
		serialize(creditCardsList, creditCardsFileName);
		return wasFound.get();
	}
	//*** END CREDIT CARD ***//
	
	
	//*** INTERNAL FILE OPS ***//	
	private void initDB() throws IOException, ClassNotFoundException {
    	File pathDir = new File(pathToDB);
    	if(!pathDir.exists()) {
    		pathDir.mkdir();
		}
		File f = new File(pathToDB+File.separator+usersFileName);
		if(!f.isFile())
		{
			List<User> users = new ArrayList<User>();
			serialize(users, usersFileName);
		}
		f = new File(pathToDB+File.separator+accountBookFileName);
		if(!f.isFile()) 
		{
			List<BalanceOperation> transactions = new ArrayList<BalanceOperation>();
			AccountBook accBook = new AccountBook(0, new ArrayList<>(transactions));
			serialize(accBook, accountBookFileName);
		}
		f = new File(pathToDB+File.separator+customersFileName);
		if(!f.isFile()) 
		{
			List<Customer> customers = new ArrayList<Customer>();
			serialize(customers, customersFileName);
		}
		f = new File(pathToDB+File.separator+productTypesFileName);
		if(!f.isFile()) 
		{
			List<ProductType> productTypes = new ArrayList<ProductType>();
			serialize(productTypes, productTypesFileName);
		}
		f = new File(pathToDB+File.separator+ordersFileName);
		if(!f.isFile())
		{
			List<Order> orders = new ArrayList<Order>();
			serialize(orders, ordersFileName);
		}
		f = new File(pathToDB+File.separator+creditCardsFileName);
		if(!f.isFile())
		{
			List<CreditCard> creditCards = new ArrayList<CreditCard>();
			creditCards.add(new CreditCard("4485370086510891", 150.0));
			creditCards.add(new CreditCard("5100293991053009", 10.0));
			creditCards.add(new CreditCard("4716258050958645", 0.0));
			serialize(creditCards, creditCardsFileName);
		}
	}
	
	private <T> boolean serialize(T o, String fileName) throws IOException {
		FileOutputStream fileOut = null;
		ObjectOutputStream out = null;
    	try {
			fileOut = new FileOutputStream(pathToDB+File.separator+fileName);
			out = new ObjectOutputStream(fileOut);
			out.writeObject(o);
			out.close();
			fileOut.close();
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
    		if(fileOut != null && out != null) {
				out.close();
				fileOut.close();
			}
		}

		return true;
	}
	
	private <T> T deserialize(Class<T> type, String fileName) throws IOException, ClassNotFoundException {
			T retObj = null;
			FileInputStream fileIn= null;
			ObjectInputStream in = null;
			try{
				fileIn = new FileInputStream(pathToDB+File.separator+fileName);
				in = new ObjectInputStream(fileIn);
				retObj = type.cast(in.readObject());
				in.close();
				fileIn.close();
			}
			catch (IOException e){
				throw new IOException(e);
			}
			catch (ClassNotFoundException e){
				throw new ClassNotFoundException();
			}
			finally{
				if(in!=null && fileIn!=null){
					in.close();
					fileIn.close();
				}
			}
			return retObj;
		// System.out.println("Class " + T.getClass().getSimpleName() + " not found");

	}
	//*** END INTERNAL FILE OPS ***//	
}
