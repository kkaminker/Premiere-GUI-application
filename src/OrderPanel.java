
import java.awt.GridLayout;
import java.util.Date;

import javax.swing.*;

public class OrderPanel extends JPanel{

	private JLabel orderIdLabel;
	private JLabel dateLabel;
	private JLabel custIdLabel;
	private JTextField orderIdText;
	private JTextField dateText;
	private JTextField custIdText;
	
	
	public OrderPanel(){
		
		orderIdLabel=new JLabel("Order ID:");
		dateLabel=new JLabel("Date:");
		custIdLabel=new JLabel("Customer ID:");
		orderIdText=new JTextField(10);
		dateText=new JTextField(10);
		custIdText=new JTextField(10);
		setLayout(new GridLayout(3,2));
		this.add(orderIdLabel);
		this.add(orderIdText);
		this.add(dateLabel);
		this.add(dateText);
		this.add(custIdLabel);
		this.add(custIdText);
	}
	
	public String getOrderID(){
		return this.orderIdText.getText();
	}
	
	public String getCustomerID(){
		return this.custIdText.getText();
	}
	
	
	public String getDate(){
		return (this.dateText.getText());
	}
	
	
}
