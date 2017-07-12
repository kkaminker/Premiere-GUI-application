import java.awt.GridLayout;

import javax.swing.*;

public class OrderLine extends JPanel{

	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	JTextField[][] partData;
	
	public OrderLine(){
		
		setLayout(new GridLayout (6,3));
		label1=new JLabel("Part ID");
		label2=new JLabel("Quantity");
		label3=new JLabel("Price");
		add(label1);
		add(label2);
		add(label3);
		partData=new JTextField[5][3];
		
		for(int row=0;row<5;row++){
			for(int col=0;col<3;col++){
				partData[row][col]=new JTextField(5);
				add(partData[row][col]);
			}
		}
		
		
		
	}
	
	public OrderLineData getOrderLineData(int row){
		
		String partID;
		int qty;
		double price;
		if(partData[row][0].getText().equals("")||partData[row][1].getText().equals("")||
				partData[row][2].getText().equals("")) return null;
		partID=partData[row][0].getText();
		qty=Integer.parseInt(partData[row][1].getText());
		price=Double.parseDouble(partData[row][2].getText());
		OrderLineData temp=new OrderLineData(partID, qty, price);
		return temp;
				
		
	}
	
	
	
	
}
