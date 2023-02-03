package it.polito.ezshop.model;
import java.io.Serializable;
import it.polito.ezshop.exceptions.InvalidRFIDException;

public class Product implements Serializable{
    private String RFID;
    public enum Status {
        AVAILABLE,
        BUYED
    }
    private Status status;

    public Product(String RFID){
        setRFID(RFID);
        status=Status.AVAILABLE;
    }

    public String getRFID(){
        return this.RFID;
    }
    public void setRFID(String RFID){
        this.RFID=RFID;
    }
    public Status getStatus(){
        return this.status;
    }
    public void setStatus(Status status){
        this.status=status;
    }
}
