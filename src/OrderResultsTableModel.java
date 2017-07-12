
//package JavaPremiereDBSQLSecurityApp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
	import java.sql.ResultSetMetaData;
	import java.sql.SQLException;
	import java.sql.Statement;

	import javax.swing.table.AbstractTableModel;



public class OrderResultsTableModel  extends AbstractTableModel{
	    Connection dbConnection;
		private PreparedStatement statement;
		private ResultSet resultSet;
		private ResultSetMetaData metaData;
		private int numberOfRows;
		
		
		public OrderResultsTableModel(Connection dbConnection, PreparedStatement statement)throws SQLException{
			this.dbConnection = dbConnection;
			
			//this query is read only, user can view data but can't modify any of the data
			this.statement = statement;
			//set query and execute it

			
			resultSet = this.statement.executeQuery();
			metaData = resultSet.getMetaData();
			
			//determine number of rows in the resultset
			resultSet.last();  //move to last row
			numberOfRows = resultSet.getRow();
			
			//notify JTable that model has changed
			fireTableStructureChanged();  
			
		}
		@Override
		public int getColumnCount()throws IllegalStateException {
			
			if (dbConnection == null)
			{
				throw new IllegalStateException("not connected to the database");
			}
			
			try{
			return metaData.getColumnCount();
			}
			catch(SQLException sqlException){
				sqlException.printStackTrace();
			}
			
			
			return 0;  //problem occurred, return 0
		}

		@Override
		public int getRowCount() throws IllegalStateException {
			if (dbConnection == null){
				throw new IllegalStateException("not connected to database");
			}
			return numberOfRows;
			
		}

		//this method will be invoked when the JTable is set up so that the 
		//column headings appear in the table
		public String getColumnName(int column)throws IllegalStateException{
			if (dbConnection == null){
				throw new IllegalStateException("not connected to database");
			}
			try{
				return metaData.getColumnName(column +1);
			}
			catch (SQLException sqlException){
				sqlException.printStackTrace();
			}
			return null;  //problem occurred , nothing to return
		}
		
		//returns the data type of a particular column, including this method makes retrieval of 
		//numeric columns more efficient.
		public Class getColumnClass (int column)throws IllegalStateException{
			if (dbConnection == null){
				throw new IllegalStateException();
			}
			try {
				String className = metaData.getColumnClassName(column +1);
				return Class.forName(className);
			}
			catch (Exception exception){
				exception.printStackTrace();
			}
			return Object.class;  //if problem occurred
		}
		
		@Override
		public Object getValueAt(int row, int column)throws IllegalStateException{
			if (dbConnection == null){
				throw new IllegalStateException("not connected to database");
			}
			try{
				resultSet.absolute(row +1);   //resultset rows and columns start from 1
				return resultSet.getObject(column +1);
			}
			catch (SQLException ex){
				 ex.printStackTrace();
			}
			return null;   //if problem
		}
		
		
		public void disconnectFromDatabase(){
			
			try{
				resultSet.close();
				statement.close();
			}
			catch(SQLException sqlException){
				sqlException.printStackTrace();
			}
		}

	
	
	
	

}
