package it.polito.ezshop.model;

import java.time.LocalDate;
import java.util.ArrayList;

public class ReturnTransaction extends Debit {
    
    class ProductInfo implements java.io.Serializable {
        ProductType productType;
        int quantity;
        ProductInfo(ProductType p, int q){
            this.productType=p;
            this.quantity=q;
        }
    }
    private ArrayList<ProductInfo> productList;
    private ArrayList<Product> prods;

    private int saleNumber;

    public enum Status {
        PAYED,
        PENDING,
        ENDED
    }

    private Status status;

    public ReturnTransaction(int balanceID, int saleNumber, LocalDate date){
        super(balanceID,date,0,"return");
        if (saleNumber<0) {
        	throw new IllegalArgumentException("Not a valid argument");
        }
        this.setSaleNumber(saleNumber);
        productList=new ArrayList<ProductInfo>();
        prods=new ArrayList<Product>();
        status= ReturnTransaction.Status.PENDING;
    }

    public ReturnTransaction(int id,int saleNumber, LocalDate date, double money, String type, Status status) throws IllegalArgumentException {
        super(id, date, money, type);
        if (status==null|| saleNumber<0) {
        	throw new IllegalArgumentException("Not a valid argument status or saleNumber");
        }
        productList=new ArrayList<ProductInfo>();
        prods=new ArrayList<Product>();
        this.setSaleNumber(saleNumber);
    }

    public int getProductQuantity(ProductType p){
        int i=searchProduct(p);
        if(i<0){
            return 0;
        }
        return productList.get(i).quantity;
    }

    public void addProduct(ProductType p,int quantity){
        int i=searchProduct(p);
        if(i<0){
            ProductInfo info=new ProductInfo(p,quantity);
            productList.add(info);
        }
        else{
            productList.get(i).quantity=(quantity+productList.get(i).quantity);
        }
        this.setMoney(p.getPricePerUnit()*quantity);
    }

    public void addProductByRFID(ProductType p, Product prod){
        addProduct(p,1);
        prods.add(prod);
    }

    private int searchProduct(ProductType p){
        int i=0;
        boolean trov=false;
        int pos=-1;
        while(i<productList.size() && !trov){
            if(p.getId()==productList.get(i).productType.getId()){
                trov=true;
                pos=i;
            }
            i++;
        }
        return pos;
    }

    public int getSaleNumber(){
        return this.saleNumber;
    }

    public ArrayList<ProductType> getProductList(){
        ArrayList<ProductType> p=new ArrayList<ProductType>();
        int i=0;
        while(i<productList.size()){
            p.add(productList.get(i).productType);
            i++;
        }
        return p;
    }

    public ArrayList<Product> getProductRFIDList(){
        ArrayList<Product> p=new ArrayList<Product>();
        int i=0;
        while(i<prods.size()){
            p.add(prods.get(i));
            i++;
        }
        return p;
    }

    public Status getStatus() {
        return status;
    }

    public void setSaleNumber(int saleNumber) {
        this.saleNumber = saleNumber;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
