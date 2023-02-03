package it.polito.ezshop.model;
import java.time.LocalDate;

public class BalanceOperation implements it.polito.ezshop.data.BalanceOperation, java.io.Serializable{
    private int balanceId;
    private LocalDate date;
    private double money;
    private String type;

    public BalanceOperation(int id,LocalDate date, double money, String type)throws IllegalArgumentException{
        if(!checkId(id)){
            throw new IllegalArgumentException("Not a valid balance ID");
        }
        if(!checkType(type)){
            throw new IllegalArgumentException("Not a valid balance type");
        }
        if(date==null){
            throw new IllegalArgumentException("Not a valid argument");
        }
        setBalanceId(id);
        setType(type);
        setMoney(money);
        setDate(date);
    }

    @Override
    public int getBalanceId(){
        return this.balanceId;
    }

    @Override
    public void setBalanceId(int balanceId){
        if(!checkId(balanceId)){
            return;
        }
        this.balanceId=balanceId;
    }

    @Override
    public LocalDate getDate(){
        return this.date;
    }

    @Override
    public void setDate(LocalDate date){
        if(date==null){
            date=LocalDate.now();
        }
        this.date=date;
    }

    @Override
    public double getMoney(){
        return this.money;
    }

    @Override
    public void setMoney(double money){
        this.money=money;
    }

    @Override
    public String getType(){
        return this.type;
    }

    @Override
    public void setType(String type) {
        if(!checkType(type)){
            return;
        }
        this.type = type;
    }

    private boolean checkId(int id){
        if(id<0){
            return false;
        }
        else{
            return true;
        }
    }

    private boolean checkType(String type){
        if (!type.equalsIgnoreCase("debit") && !type.equalsIgnoreCase("credit") && !type.equalsIgnoreCase("order")
                && !type.equalsIgnoreCase("return") && !type.equalsIgnoreCase("transaction")) {
            return false;
        }
        else{
            return true;
        }
    }

}
