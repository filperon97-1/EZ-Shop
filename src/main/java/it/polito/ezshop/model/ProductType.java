package it.polito.ezshop.model;

import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductType implements it.polito.ezshop.data.ProductType, Serializable {

	private Integer quantity;
	private String location;
	private String note;
	private String productDescription;
	private String barCode;
	private Double pricePerUnit;
	private Integer id;

	private List<Product> productList;

	public ProductType(Integer id, Integer quantity, String location, String note,
					   String productDescription, String barCode, Double pricePerUnit)
			throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException
	{
		if (quantity<0 || productDescription.isEmpty() || barCode.isEmpty() || pricePerUnit<0 || id<0 ) {
			throw new IllegalArgumentException("Argument error");
		}
		setQuantity(quantity);
		setLocation(location);
		setNote(note);
		setProductDescription(productDescription);
		setBarCode(barCode);
		setPricePerUnit(pricePerUnit);
		setId(id);

		setProductList(new ArrayList<>());
	}

	@Override
	public Integer getQuantity() {
		return this.quantity;
		//return this.getProductList().size();
	}

	@Override
	public void setQuantity(Integer quantity) {
		if(quantity < 0)
		{
			//throw new InvalidQuantityException("Quantity should be a positive number.");
			return;
		}
		this.quantity = quantity;
	}

	@Override
	public String getLocation() {
		return location;
	}

	@Override
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String getNote() {
		return note;
	}

	@Override
	public void setNote(String note) {
		this.note = note;
	}

	@Override
	public String getProductDescription() {
		return productDescription;
	}

	@Override
	public void setProductDescription(String productDescription) {
		if(productDescription.isEmpty())
		{
			return;
		}
		this.productDescription = productDescription;
	}

	@Override
	public String getBarCode() {
		return barCode;
	}

	@Override
	public void setBarCode(String barCode) {
		if(checkBarcode(barCode)) {
			this.barCode = barCode;
		}
	}

	@Override
	public Double getPricePerUnit() {
		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(Double pricePerUnit) {
		if(pricePerUnit < 0)
		{
			return;
		}
		this.pricePerUnit = pricePerUnit;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		if(id <= 0)
		{
			//throw new InvalidProductIdException("ID should be a valid ID number.");
			return;
		}
		this.id = id;
	}

	public static boolean checkBarcode(String barCode) {
		if(barCode.isEmpty() || barCode.length() < 12 || barCode.length() > 14) {
			return false;
		} else {
			try {
				int size = barCode.length();
				int [] arr = new int [size];
				int m = 3;
				int sum = 0;
				for(int i=size-1; i>=0; i--) {
					arr[i] = Integer.parseInt(String.valueOf(barCode.charAt(i)));
					if(i < size-1) {
						arr[i] = arr[i]*m;
						if(m == 1) {
							m = 3;
						} else {
							m = 1;
						}
						sum += arr[i];
					}
				}
				int diff = ( ((sum/10)*10)+10 ) - sum;

				if(diff != arr[size-1]) {
					return false;
				}

				} catch(NumberFormatException e) {
					return false;
				}


		}
		return true;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public void setStatusByRFID(String RFID, Product.Status status){
		int i=0;
		boolean trov=false;
		while(i<productList.size() && !trov){
			if(RFID.equals(productList.get(i).getRFID())){
				productList.get(i).setStatus(status);
				trov=true;
			}
			i++;
		}
	}
}
