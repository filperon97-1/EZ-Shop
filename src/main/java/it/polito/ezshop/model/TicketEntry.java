package it.polito.ezshop.model;

public class TicketEntry implements it.polito.ezshop.data.TicketEntry,java.io.Serializable {

	private String barCode;
	private String productDescription;
	private int amount;
	private double pricePerUnit;
	private double discountRate;

	public TicketEntry(String barCode, String productDescription, int amount, double pricePerUnit, double discountRate)
	{
		if (amount<0  || productDescription=="" || barCode=="" || pricePerUnit<0 || discountRate<0) {
			throw new IllegalArgumentException("Argument error");
		}
		setBarCode(barCode);
		setProductDescription(productDescription);
		setAmount(amount);
		setPricePerUnit(pricePerUnit);
		setDiscountRate(discountRate);
	}

	@Override
	public String getBarCode() {
		return barCode;
	}

	@Override
	public void setBarCode(String barCode) {
		if(barCode.equals("") || barCode.length() < 12 || barCode.length() > 14)
		{
			//throw new InvalidProductCodeException ("Bar code is a number on 12 to 14  digits, compliant to GTIN specifications, see  https://www.gs1.org/services/how-calculate-check-digit-manually");
			return;
		}
		this.barCode = barCode;
	}

	@Override
	public String getProductDescription() {
		return productDescription;
	}

	@Override
	public void setProductDescription(String productDescription) {
		if(productDescription.equals(""))
		{
			//throw new InvalidProductDescriptionException("Product description should not be empty.");
			return;
		}
		this.productDescription = productDescription;
	}

	@Override
	public int getAmount() {
		return amount;
	}

	@Override
	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Override
	public double getPricePerUnit() {
		return pricePerUnit;
	}

	@Override
	public void setPricePerUnit(double pricePerUnit) {
		if(discountRate < 0)
		{
			//throw new InvalidPricePerUnitException("Price per unit should be a positive number.");
			return;
		}
		this.pricePerUnit = pricePerUnit;
	}

	@Override
	public double getDiscountRate() {
		return discountRate;
	}

	@Override
	public void setDiscountRate(double discountRate) {
		if(discountRate < 0 || discountRate >= 1)
		{
			//throw new InvalidDiscountRateException("Discount rate should be a valid percentage between 0 and 100");
			return;
		}
		this.discountRate = discountRate;
	}
}
