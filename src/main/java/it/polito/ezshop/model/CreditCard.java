package it.polito.ezshop.model;

import java.io.Serializable;

public class CreditCard implements Serializable {
    private String creditCard;
    private double amount;

    public CreditCard(String creditCard, double amount) {
    	if ( creditCard=="" || amount<0 ) {
			throw new IllegalArgumentException("Argument error");
		}
        this.creditCard = creditCard;
        this.amount = amount;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
