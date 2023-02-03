package it.polito.ezshop.model;
import it.polito.ezshop.exceptions.*;

import java.io.Serializable;

public class LoyaltyCard implements Serializable {
    private String id;
    private Integer point;

    public LoyaltyCard(String id, Integer point) throws InvalidCustomerCardException{
        if(!isValidCard(id)){
            throw new InvalidCustomerCardException("Not a valid loyalty card");
        }
        setId(id);
        setPoint(point);
    }
    public LoyaltyCard(String id) throws InvalidCustomerCardException{
        if(!isValidCard(id)){
            throw new InvalidCustomerCardException("Not a valid loyalty card");
        }
        setId(id);
        setPoint(0);
    }

    public String getId(){
        return id;
    }
    public void setId(String id) throws  InvalidCustomerCardException{
        if(!isValidCard(id)){
            throw new InvalidCustomerCardException("Not a valid loyalty card");
        }
        this.id=id;
    }
    public Integer getPoint(){
        return this.point;
    }

    public void setPoint(Integer point){
        if(point<0){
            point=0;
        }
        this.point=point;
    }

    public static boolean isValidCard(String customerCard) {
        if( customerCard == null || customerCard.isEmpty() || !customerCard.matches("[0-9]{10}")) {
            return false;
        } else {
            return true;
        }
    }
}
