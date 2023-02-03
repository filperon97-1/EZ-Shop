package it.polito.ezshop.model;
import it.polito.ezshop.exceptions.*;
public class Customer implements it.polito.ezshop.data.Customer, java.io.Serializable{
    private String name;
    private LoyaltyCard card;
    private Integer id;

    public Customer(Integer id,String name, String card) throws IllegalArgumentException{
        if(!checkId(id)){
            throw new IllegalArgumentException("Not a valid customer ID");
        }
        if(!checkString(name)){
            throw new IllegalArgumentException("Not a valid customer name");
        }/*
        if(!checkString(card)){
            throw new IllegalArgumentException("Not a valid loyalty card");
        }*/
        setId(id);
        setCustomerName(name);
        setCustomerCard(card);

    }

    @Override
    public String getCustomerName(){
        return this.name;
    }

    @Override
    public void setCustomerName(String customerName){
        if(!checkString(customerName)){
            return;
        }
        this.name=customerName;
    }

    @Override
    public String getCustomerCard(){
        if(this.card != null) {
            return this.card.getId();
        } else {
            return null;
        }
    }

    @Override
    public void setCustomerCard(String customerCard){
        if(!checkString(customerCard)){
            return;
        }
        else{
            try{
                this.card=new LoyaltyCard(customerCard,0);
            }
            catch(InvalidCustomerCardException e){
                return;
            }

        }
    }

    @Override
    public Integer getId(){
        return this.id;
    }

    @Override
    public void setId(Integer id){
        if(!checkId(id)){
            return;
        }
       this.id=id;
    }

    @Override
    public Integer getPoints(){
        return this.card.getPoint();
    }

    @Override
    public void setPoints(Integer points){
        this.card.setPoint(points);
    }

    private boolean checkId(int id){
        if(id<0){
            return false;
        }
        else{
            return true;
        }
    }

    private boolean checkString(String s){
        if(s==null || s.length()<=0){
            return false;
        }
        else{
            return true;
        }
    }

}