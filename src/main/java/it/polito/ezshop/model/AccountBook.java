package it.polito.ezshop.model;

import it.polito.ezshop.data.BalanceOperation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AccountBook implements java.io.Serializable {
	
	private double balance;
	private List<BalanceOperation> transactions;
	
	public AccountBook(double currBalance, List<BalanceOperation> currTransactions) 
	{
		if ( currTransactions==null || currBalance<0 ) {
			throw new IllegalArgumentException("Argument error");
		}
		balance = currBalance;
		transactions = currTransactions;
	}
	
	public double showBalance() 
	{

		return balance;
	}
	
	public boolean addTransaction(BalanceOperation transaction) 
	{
		
		transactions.add(transaction);
		balance += transaction.getMoney();
		return true;
	}

	public boolean removeTransaction(int transactionId){
		boolean trovato=false;
		int i=0;
		while(!trovato && i<transactions.size()){
			if(transactions.get(i).getBalanceId()==transactionId){
				transactions.remove(i);
				trovato=true;
			}
			i++;
		}
		return trovato;
	}
	
	public List<BalanceOperation> listAllTransactions() 
	{

		return transactions;
	}
	
	public List<BalanceOperation> listTransactionsInRange(LocalDate dateBegin, LocalDate dateEnd) 
	{
		List<BalanceOperation> trimmedTransactionsList = new ArrayList<>();
		
		for (BalanceOperation t : transactions)
		{
			if(t.getDate().isEqual(dateBegin) || t.getDate().isEqual(dateEnd) ||
			   (t.getDate().isAfter(dateBegin) && t.getDate().isBefore(dateEnd))
			  ) 
			{
				trimmedTransactionsList.add(t);
			}
        }
			
		return trimmedTransactionsList;
	}
}

