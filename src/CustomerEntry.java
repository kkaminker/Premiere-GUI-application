import java.awt.BorderLayout;
import java.sql.Connection;

import javax.swing.*;

public class CustomerEntry extends JFrame{
	
	private Connection dbConnection;
	private NewCustPanel custPanel;
	private NewCustButtonPanel buttonPanel;
	
	public CustomerEntry(Connection dbConnection){
		this.dbConnection=dbConnection;
		if(this.dbConnection==null){
			JOptionPane.showMessageDialog(null, "Missing Database Connection: Contact IT");
		}
		else{
			setTitle("New Customer Form");
			setSize(600,600);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setLayout(new BorderLayout());
			custPanel=new NewCustPanel();
			add(custPanel, BorderLayout.CENTER);
			buttonPanel=new NewCustButtonPanel(this, custPanel, dbConnection);
			add(buttonPanel, BorderLayout.SOUTH);
		
			//buttonPanel=new ButtonPanel(this,orderPanel,orderLinePanel,dbConnection);
		
			pack();
			setVisible(true);
		}
	}
	

}
