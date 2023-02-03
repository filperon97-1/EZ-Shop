package it.polito.ezshop.model;
import java.io.Serializable;
public class Order implements it.polito.ezshop.data.Order, Serializable {

    private Integer balanceId;
    private Integer id;
    private String productCode;
    private double pricePerUnit;
    private int quantity;
    private String status;

    public Order(Integer id, Integer balanceId, String productCode, double pricePerUnit, int quantity, String status) throws IllegalArgumentException{
        if(!checkId(id)){
            throw new IllegalArgumentException("Not a valid order id");
        }
        if(pricePerUnit<0){
            throw new IllegalArgumentException("not a valid price per unit");
        }
        if(checkStatus(status)){
            throw new IllegalArgumentException("not a valid order status");
        }
        if(balanceId != null) {
            if (balanceId<0 || productCode.equals("") || quantity<0 ) {
                throw new IllegalArgumentException("not a valid argument");
            }
        } else {
            if (productCode.equals("") || quantity<0 ) {
                throw new IllegalArgumentException("not a valid argument");
            }
        }

        setOrderId(id);
        setBalanceId(balanceId);
        setProductCode(productCode);
        setPricePerUnit(pricePerUnit);
        setQuantity(quantity);
        setStatus(status);
    }

    @Override
    public Integer getBalanceId(){
        return this.balanceId;
    }

    @Override
    public void setBalanceId(Integer balanceId){
        this.balanceId=balanceId;
    }

    @Override
    public String getProductCode(){
        return this.productCode;
    }

    @Override
    public void setProductCode(String productCode){
        if(productCode==null || productCode.length()==0 ){
            return;
        }
        this.productCode=productCode;
    }

    @Override
    public double getPricePerUnit(){
        return this.pricePerUnit;
    }

    @Override
    public void setPricePerUnit(double pricePerUnit){
        if(pricePerUnit<0){
            return;
        }
        this.pricePerUnit=pricePerUnit;
    }

    @Override
    public int getQuantity(){
        return this.quantity;
    }

    @Override
    public void setQuantity(int quantity){
        if(quantity<=0){
            quantity=1;
        }
        this.quantity=quantity;
    }

    @Override
    public String getStatus(){
        return status;
    }

    @Override
    public void setStatus(String status){
        if(checkStatus(status)){
            return;
        }
        this.status=status;
    }

    @Override
    public Integer getOrderId(){
        return id;
    }

    @Override
    public void setOrderId(Integer orderId){
        if(checkId(orderId)) {
            this.id = orderId;
        }
    }

    public void sendAndPay(){
        setStatus("PAYED");
    }

    public void issueWarningReorder(){
        setStatus("ISSUED");
    }

    public void payIssuedReorderedWarning(){
        setStatus("PAYED");
    }

    private boolean checkId(int id){
        return id >= 0;
    }

    private boolean checkStatus(String status){
        if(status==null || status.length()==0 ){
            return true;
        }
        else return !status.equalsIgnoreCase("issued") && !status.equalsIgnoreCase("payed") && !status.equalsIgnoreCase("completed");
    }
}
