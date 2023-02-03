package it.polito.ezshop.model;

import java.time.LocalDate;

public class Credit extends BalanceOperation {
	
	public Credit(int balanceID, LocalDate date, double money, String type) 
	{
		super(balanceID, date, money, type);
	}
}
