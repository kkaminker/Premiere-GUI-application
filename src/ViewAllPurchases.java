import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class ViewAllPurchases extends JFrame{
	
	private ResultsTableModel tableModel;
	private Connection dbConnection;
	

	public ViewAllPurchases(Connection dbConnection){
	//store the reference to the database --- back end
	 this.dbConnection = dbConnection;
	
		 
	//verify that a database connection exists
	if (this.dbConnection == null){
		   JOptionPane.showMessageDialog(null,"missing database connection --- contact IT");
		   
	}
	else{ //continue with this process
		   
		   	setTitle("Purchase History");
		   	//set window size
		   	setSize(200, 1000);
		   	// Specify an action for the close button.
		   	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		   	this.setLayout(new BorderLayout());
		   	
		   	
		   	String query = "select * from purchase";
		   
		   
		   			
		   			
		   			
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
			   setSize(600,450);
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
}
