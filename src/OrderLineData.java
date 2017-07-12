
public class OrderLineData {
	
	private String id;
	private int qty;
	private double price; 
	
	public OrderLineData(String id, int qty, double price){
		this.id=id;
		this.qty=qty;
		this.price=price;
	}

	public String getPartID() {
		return id;
	}

	public int getQty() {
		return qty;
	}

	public double getPrice() {
		return price;
	}

}
