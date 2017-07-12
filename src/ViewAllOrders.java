import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;



public class ViewAllOrders extends JFrame implements WindowListener{
	private ResultsTableModel tableModel;
	private Connection dbConnection;
	private ArrayList<String> dbRoles;

	public ViewAllOrders(Connection dbConnection, ArrayList dbRoles){
	//store the reference to the database --- back end
	 this.dbConnection = dbConnection;
	 this.dbRoles=dbRoles;
		 
	//verify that a database connection exists
	if (this.dbConnection == null){
		   JOptionPane.showMessageDialog(null,"missing database connection --- contact IT");
		   
	}
	else{ //continue with this process
		   
		   	setTitle("Order History");
		   	//set window size
		   	setSize(600, 600);
		   	// Specify an action for the close button.
		   	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		   	this.setLayout(new BorderLayout());
		   	
		   	
		   	String query = null;
		   	if(dbRoles.contains(Roles.PR_CustomerRole.name())){
		   		query="select * from CustomerOrderLineData";
		   	}
		   	else if(dbRoles.contains(Roles.PR_SalesRole.name())){
		   		query="select * from SalesRepOrderLineData";
		   	}
		   
		   			
		   			
		   			
			try{
				//instantiate the model which will automatically fire off the 
				//execution of the query in String, query.
			    tableModel = new ResultsTableModel(this.dbConnection,query);
			   
			    
			   //create JTable based on the tableModel
			    //the table holds the data that will be displayed on the screen
			   JTable resultTable = new JTable(tableModel);

	            //place components in the window
			   //place table in a scrollpane so that user can scroll through the 
			   //contents of the table
			   this.add(new JScrollPane(resultTable),BorderLayout.CENTER);
			
			   //adding a row sorter will allow the user to click on any column heading
			   //in order to resort the rows by the data in that column
			   final TableRowSorter<TableModel> sorter =
					    new TableRowSorter<TableModel> (tableModel);
			   resultTable.setRowSorter(sorter);
		
			   //set window size and make it and its components visible on the screen
			   setSize(500,250);
			   setVisible(true);

	         }
			catch(SQLException sqlException){
				JOptionPane.showMessageDialog(null, sqlException.getMessage());
				tableModel.disconnectFromDatabase();
				
			}
		}
		   	
		   	//set up layout of the window 			   	
		   	pack();
		   	setVisible(true);
		   	
	
	
	
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}


	



