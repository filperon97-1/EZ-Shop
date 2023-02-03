package it.polito.ezshop.model;

import java.time.LocalDate;

public class Debit extends BalanceOperation{
    public Debit(int id, LocalDate date, double money, String type) throws IllegalArgumentException {
        super(id, date, money, type);
    }
}
