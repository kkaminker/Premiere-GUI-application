import java.awt.BorderLayout;
import java.sql.Connection;

import javax.swing.*;

public class OrderEntry extends JFrame{

	private Connection dbConnection;
	private OrderPanel orderPanel;
	private OrderLine orderLinePanel;
	private ButtonPanel buttonPanel;
	
	public OrderEntry(Connection dbConnection){
		this.dbConnection=dbConnection;
		if(this.dbConnection==null){
			JOptionPane.showMessageDialog(null, "Missing Database Connection: Contact IT");
		}
		else{
			setTitle("Premiere Order Entry Form");
			setSize(600,600);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLayout(new BorderLayout());
			orderPanel=new OrderPanel();//TODO create
			add (orderPanel, BorderLayout.WEST);
			orderLinePanel=new OrderLine();
			add(orderLinePanel, BorderLayout.CENTER);
			buttonPanel=new ButtonPanel(this,orderPanel,orderLinePanel,dbConnection);
			add(buttonPanel, BorderLayout.SOUTH);
			pack();
			setVisible(true);
			
		}
	}
	
	
	
	
	
}
