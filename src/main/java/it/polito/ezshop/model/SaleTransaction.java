package it.polito.ezshop.model;
import it.polito.ezshop.data.TicketEntry;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SaleTransaction extends Credit implements it.polito.ezshop.data.SaleTransaction, java.io.Serializable {

    private Integer ticketNumber;
    private List<TicketEntry> entries;
    private List<Product> prodList;
    private double discountRate;
    private double price;
    private double money;



    public enum Status {
        PAYED,
        PENDING
    }

    private Status status;

    public SaleTransaction(int balanceID, LocalDate date){
        super(balanceID,date,0,"transaction");
        entries=new ArrayList<TicketEntry>();
        prodList=new ArrayList<Product>();
        status=Status.PENDING;
        setTicketNumber(balanceID);
    }

    public SaleTransaction(int balanceID, LocalDate date, String type, Integer ticketNumber, List<TicketEntry> entries, double discountRate, double price, Status status) {
        super(balanceID, date, price, type);
        if (ticketNumber<1 || entries==null || discountRate<0 || price<0) {
			throw new IllegalArgumentException("Argument error");
		}
        setTicketNumber(ticketNumber);
        setEntries(entries);
        setDiscountRate(discountRate);
        setPrice(price);
        setStatus(status);
        entries=new ArrayList<TicketEntry>();
        prodList=new ArrayList<Product>();
    }

    @Override
    public Integer getTicketNumber() {
        return ticketNumber;
    }

    @Override
    public void setTicketNumber(Integer ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    @Override
    public List<TicketEntry> getEntries() {
        return entries;
    }

    @Override
    public void setEntries(List<TicketEntry> entries) {
        this.entries = entries;
    }

    @Override
    public double getDiscountRate() {

        return this.discountRate;
    }

    @Override
    public void setDiscountRate(double discountRate) {
        if(discountRate < 0 || discountRate >= 1)
        {
            return;
        }
        this.discountRate = discountRate;
    }

    @Override
    public double getPrice() {
        return getTotalAmount();
    }

    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void addProduct(ProductType p, int quantity){
        int i=searchProduct(p);
        if(i<0){
            TicketEntry e=new it.polito.ezshop.model.TicketEntry
                    (p.getBarCode(),p.getProductDescription(),quantity,p.getPricePerUnit(),0);
            entries.add(e);
        }
        else{
            entries.get(i).setAmount(quantity+entries.get(i).getAmount());
        }
    }

    public boolean removeProduct(ProductType p,int quantity){
        if(quantity<0){
            return false;
        }
        int i=searchProduct(p);
        if(i<0 || quantity>entries.get(i).getAmount()){
            return false;
        }
        else{
            entries.get(i).setAmount(entries.get(i).getAmount()-quantity);
            return true;
        }
    }

    public void addProductByRFID(ProductType prodType,Product prod){
        addProduct(prodType,1);
        prodList.add(prod);
    }

    public boolean removeProductByRFID(ProductType prodType,Product prod){
        int i=0;
        boolean trov=false;
        int pos=-1;
        while(i<prodList.size() && !trov){
            if(prod.getRFID().equals(prodList.get(i).getRFID())){
                trov=true;
                pos=i;
            }
            i++;
        }
        if(pos<0){
            return false;
        }
        prodList.remove(pos);
        return removeProduct(prodType,1);
    }

    public boolean applyDiscountToProduct(ProductType p,double d){
        if(d<0){
            return false;
        }
        int i=searchProduct(p);
        if(i<0){
            return false;
        }
        entries.get(i).setDiscountRate(d);
        return true;
    }

    public double getTotalAmount(){
        double tot=0,relativeAmount=0;
        int i=0, quantity=0;
        while(i<entries.size()){
            relativeAmount=entries.get(i).getPricePerUnit();
            quantity=entries.get(i).getAmount();
            relativeAmount*=quantity;
            relativeAmount=relativeAmount-(entries.get(i).getDiscountRate()*relativeAmount);
            tot= tot+relativeAmount;
            i++;
        }
        tot=tot-tot*discountRate;
        return tot;
    }

    private int searchProduct(ProductType p){
        int i=0;
        boolean trov=false;
        int pos=-1;
        while(i<entries.size() && !trov){
            if(p.getBarCode().equals(entries.get(i).getBarCode())){
                trov=true;
                pos=i;
            }
            i++;
        }
        return pos;
    }

    public int getProductQuantity(ProductType p){
        int i=searchProduct(p);
        if(i<0){
            return 0;
        }
        return entries.get(i).getAmount();
    }

    public ArrayList<String> getProductList(){
        ArrayList<String> p=new ArrayList<String>();
        int i=0;
        while(i<entries.size()){
            p.add(entries.get(i).getBarCode());
            i++;
        }
        return p;
    }

    public ArrayList<String> getRFIDs(){
        ArrayList<String> p=new ArrayList<String>();
        int i=0;
        while(i<prodList.size()){
            p.add(prodList.get(i).getRFID());
            i++;
        }
        return p;
    }

    public double getMoney(){
        return getTotalAmount();
    }

}
