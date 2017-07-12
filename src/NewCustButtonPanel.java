import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;



public class NewCustButtonPanel extends JPanel {
	
	private JButton insertButton;
	private JButton exitButton;
	private Connection dbConnection;
	private NewCustPanel custPanel;
	private CustomerEntry parentFrame;
	
	public NewCustButtonPanel(CustomerEntry parentFrame, NewCustPanel custPanel, Connection dbConnection)
	{
		this.dbConnection=dbConnection;
		if(this.dbConnection==null)
		{
			JOptionPane.showMessageDialog(null, "Missing Database Connection: Contact IT");
		}
		else
		{
		this.parentFrame=parentFrame;
		this.custPanel=custPanel;
		this.dbConnection=dbConnection;
		insertButton=new JButton("Create Customer Account");
		this.add(insertButton);	
		exitButton=new JButton("Exit");
		this.add(exitButton);
		insertButton.addActionListener(new ButtonListener());
		//exitButton.addActionListener(new ExitButtonListener());//TODO
		
		}
		
		
	}
	
	private class ButtonListener implements ActionListener{

		private String name;
		private String street;
		private String city;
		private String state;
		private String zip;
		private String phone;
		//private double balance;//DEFAULT 0 in sql
		private BigDecimal creditLimit; //hard code
		//repNum and CustNum have triggers
		private int repNum;
		private PreparedStatement insertCust;
		
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			insertCust=null;
			
			//ResultSet resultSet=null;
			try{
				insertCust=dbConnection.prepareStatement("Insert into Customer(cust_name, cust_street, cust_City, cust_state,cust_zip,credit_limit,rep_num,phone,cust_login,cust_balance)"+
					"Values(?,?,?,?,?,?,?,?,?,DEFAULT)");
			}
			catch(SQLException sqlex){
				sqlex.printStackTrace();
			}
			NewCustPanel ncp=custPanel;
			name=ncp.getNameText().getText();
			street=ncp.getStreet().getText();
			city=ncp.getCity().getText();
			state=ncp.getSelectedState();
			zip=ncp.getZip().getText();
			creditLimit=new BigDecimal(5000);
			repNum=Integer.parseInt(ncp.getRepNum().getText());
			phone=ncp.getPhone().getText();
			
			try{
				//cust num is identity
				insertCust.setString(1, name);
				insertCust.setString(2, street);
				insertCust.setString(3, city);
				insertCust.setString(4, state);
				insertCust.setString(5,zip);
				insertCust.setBigDecimal(6,creditLimit);
				insertCust.setInt(7, repNum);
				insertCust.setString(8, phone);
				insertCust.setString(9, null);
			
				
				int result=insertCust.executeUpdate();
			}
			catch(SQLException sqlex){
				sqlex.printStackTrace();
				try{
					dbConnection.rollback();
					
				}
				catch(SQLException sqlexc){
					sqlexc.printStackTrace();
				}
			}
			
			try {
				dbConnection.commit();
			} catch (SQLException sqlex) {
				sqlex.printStackTrace();
			}
			
		}
		
	}

}
