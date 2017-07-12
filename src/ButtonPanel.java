import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javax.swing.*;

public class ButtonPanel extends JPanel{
	
	private JButton insertOrderButton;
	private JButton exitButton;
	private Connection dbConnection;
	private OrderPanel orderPanel;
	private OrderLine orderLinePanel;
	private OrderEntry parentFrame;

	public ButtonPanel(OrderEntry parentFrame, OrderPanel orderPanel, OrderLine orderLinePanel, Connection dbConnection)
	{
		this.dbConnection=dbConnection;
		if(this.dbConnection==null)
		{
			JOptionPane.showMessageDialog(null, "Missing Database Connection: Contact IT");
		}
		else
		{
			this.orderLinePanel=orderLinePanel;
			this.orderPanel=orderPanel;
			this.parentFrame=parentFrame;
			insertOrderButton=new JButton("Insert Order");
			exitButton=new JButton("Exit");
			this.add(insertOrderButton);
			this.add(exitButton);
			insertOrderButton.addActionListener(new ButtonListener());
			exitButton.addActionListener(new ExitButtonListener());
			
		}
	}
	
	private class ButtonListener implements ActionListener{
		
	       private String orderID;
	       private String custID;
	       private LocalDate orderDate;
	       private String partID;
	       private int qty;
	       private double price;
	       private double total;
	       
	       
	       private PreparedStatement insertOrder;
	       private PreparedStatement insertOrderLine;
	       
	  public ButtonListener(){
		    orderDate =null;
	  }     
	  public void actionPerformed(ActionEvent e){
		   System.out.println("ready to process the button");
		
	      insertOrder = null;   //query statement
	      insertOrderLine = null;
	      ResultSet resultSet = null;   //manages results
	       try{
	    	   
	    	//Let it know it is getting values (the amount there are '?'s)
	    	   insertOrder = dbConnection.prepareStatement("Insert into SalesRepOrderData (Order_Num, Order_Date,Cust_Num) "+
	    	        "VALUES (?,?,? )");
	           insertOrderLine = dbConnection.prepareStatement("Insert into SalesRepOrderLineData (Order_Num,Part_Num,QTY_Ordered,Quoted_Price, modified_date)" +
	        		"VALUES (?,?,?,?,?)");   
	       }
	       catch(SQLException sqlException){
	    	   sqlException.printStackTrace();
	    	   
	       }
		
	     
		  //collect data from the user interface
	      
	      OrderPanel orderInfo = ButtonPanel.this.orderPanel;
	      //data about parts that were ordered
	      OrderLine orderLineInfo = orderLinePanel;
	      
		  System.out.println("access to order panel");
		  String orderNum;
		  //get orderid that user enter in the JTextField on the order entry form
		  orderID = orderInfo.getOrderID();
		  //orderID = Integer.parseInt(orderNum);
		  System.out.println("Order " + orderID);
		  //get date that user entered on order entry form
		  String date = orderInfo.getDate();
		  java.sql.Date sqlDate= null;
		  String[] dateParts = date.split("/");
		  if (dateParts.length !=3){
			  //user made an error entering the date
			  JOptionPane.showMessageDialog(null,"incorrect date format");
			  return;
		  }
		  else
		  { 
			int month;
		    int day;
		    int year;
			month = Integer.parseInt(dateParts[0]);
			day = Integer.parseInt(dateParts[1]);
			year = Integer.parseInt(dateParts[2]);
			System.out.println(month + " " + day + " " + year);
			orderDate = LocalDate.of(year, month, day);
		
			
			sqlDate = java.sql.Date.valueOf(orderDate);
			
		
			
			
			//get custid from order entry form
			custID = orderInfo.getCustomerID();
			
			//now we have all the data necessary to add a new order record
			//create Statement for querying database
			//now give that prepared statement the values to fill the '?'s 
	    	  try{ 
			  insertOrder.setString(1, orderID);
			  
			  insertOrder.setDate(2,sqlDate);
			  insertOrder.setString(3,custID);
	    	  
	    	   //query database
			  //execute the preparedStatement
	    	   int result = insertOrder.executeUpdate();
	    	   System.out.println("executed insert statement");
	    	   
	    	   
	    	  }
	    	  catch(SQLException sqlExcept){
	    		  sqlExcept.printStackTrace();
	    		  //if get an exception, rollback
	    		  try{
	   	    	   dbConnection.rollback();}
	   	    	   catch(SQLException sqlEx){
	   	    		   sqlEx.printStackTrace();
	   	    		   
	   	    	   }
	   	    	   
	    		  
	    	  }
	    	  //now proceed to insert parts of the order
	    	  try{
	    		  System.out.println("enter part order to order line table ");
	    		 //loop through orderline information
	    		  for (int i=0;i<orderLineInfo.partData.length;i++) //number of rows in the grid
	    		   //retrieve data from one row at a time
	    		  { 
	    			  OrderLineData rowData = orderLineInfo.getOrderLineData(i);
	    			   //now prepare record to insert 
	    			  if (rowData == null ){
	    				  if (i==0){
	    				     System.out.println("details not provided, rollback the order");
	    				     try{
	    					     dbConnection.rollback();
	    					     return; //end this method
	    				     }
	    				     catch(SQLException ex){
	    					    ex.printStackTrace();
	    				     }
	    			     }
	    			     else { //no more details, commit the order
	    				  break;  //leave the loop
	    			      }
	    			  }
	    			  else{  //insert the details
	    			  System.out.println("part order " +
	    					  orderID + " " +
		    				  rowData.getPartID() + " " +
		    				  rowData.getQty() + " " +
		    				  rowData.getPrice());
	    			  
	    			   insertOrderLine.setString(1, orderID);
	    		       insertOrderLine.setString(2,rowData.getPartID());
	    		       insertOrderLine.setInt(3, rowData.getQty());
	    		       insertOrderLine.setDouble(4, rowData.getPrice());
	    		      java.sql.Date sqlDate2=java.sql.Date.valueOf(LocalDate.now());
	    		       insertOrderLine.setDate(5, sqlDate2);
	    		       //now execute the statement to insert into database
	    		       int result = insertOrderLine.executeUpdate();
	    	               
	    		   }
	    		  }
	    	  }
	    	 
	    	  catch(SQLException ex){
	    		  ex.printStackTrace();
	    		  try{
	    		  dbConnection.rollback();}
	    		  catch(SQLException sqlE){
	    			  sqlE.printStackTrace();
	    			  
	    		  }
	    	  }
	    	  try{
	    	  dbConnection.commit();}
	    	  catch(SQLException except){
	    		  except.printStackTrace();
	    		  try{
	    			  dbConnection.rollback();
	    			  
	    		  }
	    		  catch(SQLException sqle){
	    			  sqle.printStackTrace();
	    			  
	    		  }
	    	  }
			  
		  }
	  }
	  
}	private class ExitButtonListener implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e){
			parentFrame.dispose();
		}
	}
}
