//package JavaPremiereDBSQLSecurityApp;
import javax.security.auth.*;
import javax.security.auth.login.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class MainMenuFrame extends JFrame implements WindowListener{
	
	
	private JMenuBar premiereMenuBar;//the bar to hold drop down menus
	
	private JMenu salesMenu, employeeMenu,customerMenu,purchaseMenu; //the drop down menus
	//menuItems for salesMenu
	private JMenuItem addSalesItem,modifySalesItem, cancelSalesItem,viewSalesItem,viewSalesOrdersItem;
	//menuItems for employeeMenu
	private JMenuItem addEmployeeMenuItem,modifyEmployeeMenuItem,modifySalesRepMenuItem,addSalesRepMenuItem,
	                  viewEmployeeMenuItem,listEmployeesMenuItem;
	//MenuItems for customerMenu
	private JMenuItem addcustomerMenuItem,modifyCustomerMenuItem,listCustomersMenuItem;
	//MenuItem for purchaseMenu
	private JMenuItem purchasesMenuItem;
	
	private Connection dbConnection;
	
	private Roles allRoles; //an enum
	
	public MainMenuFrame(Connection dbConnection){
		
		
		this.setSize(700,700);
		this.setTitle("Premiere Application");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//first display LoginFrame
		
		this.dbConnection = dbConnection;
		ArrayList <String> databaseRoles ;
		databaseRoles = getDatabaseRoles(); //all the roles the current user belongs to
		setUpMenuBar(databaseRoles);
		this.setJMenuBar(premiereMenuBar);   //add menu bar to the window
		pack();
		this.setVisible(true);
	}

    
     private ArrayList<String> getDatabaseRoles(){
    	 /*
			 * USER_NAME() - database username - a.k.a USER and CURRENT_USER

             * SUSER_SNAME() - login identification name - supersedes SUSER_NAME() 
		 */
			
    	 ArrayList<String> databaseRoles = new ArrayList<String>(); //to store the roles that the current Login has been assigned
    	 String currentLogin;
		//set up a Statement that can query the sql server
		Statement sqlStatement =null;
			
		//set up the query to get the SQL Server login if using SQL Server authentication
		String query =
					"select suser_sname()";   
		//now create an instance of Statement
		//and use it to execute the query
		try{
				sqlStatement = dbConnection.createStatement();
				//execute the query
				ResultSet rs = sqlStatement.executeQuery(query);
				//move to first record in the resultset
				rs.next();
				//retrieve data from first column in the resultset
				//there should be only one row and one column
				currentLogin = rs.getString(1);
				 
				 
				System.out.println("current login " + currentLogin);
				
				  //now get the user and the role(s) associated with the
				 //user that is associated with this sql server login
				 query =
					
				
				"select u.name, r.name " +
				"from [PREMIERE].sys.database_role_members as m " +
				"inner join [PREMIERE].sys.database_principals r "+
				"on m.role_principal_id = r.principal_id "+
				"inner join [PREMIERE].sys.database_principals as u "+
				"on u.principal_id = m.member_principal_id "+
				"where u.name = "+
				//access sql logins instead of windows logins
				"(select u.name from [PREMIERE].sys.sql_logins  l "+
				"inner join [PREMIERE].sys.sysusers  u "+
				"on l.sid = u.sid "+
				"where l.name = "+ "'" + currentLogin + "')";
				   
						
				//now execute this query
				rs = sqlStatement.executeQuery(query);
				//now check the results that came back
				if (!rs.next()){
					 return null;   //no database roles were assigned
				}
				else{
			    //iterate through the resultset
				do{	
				//get the data from the first row
				String currentUser	=	
						rs.getString(1);
			    String currentDBRole =
			    		rs.getString(2);
			    if (!(currentDBRole == null)){
			    
			    	System.out.println("current user" + currentUser + " current role" + currentDBRole);
			    	databaseRoles.add(currentDBRole);
			    }
				}  while (rs.next());  //go to next row
				}
			}
			catch(SQLException ex){
				JOptionPane.showMessageDialog(null, "couldnt retrieve data");
				ex.printStackTrace();
				return null;
			}
			
			
		    	 
    	 return databaseRoles;    //if no roles were assigned to this particular login
    	                 //it must be a guest
     }
     
     
     
     private void setUpMenuBar(ArrayList<String>databaseRoles){
    	 //set up the menu bar
    	 this.premiereMenuBar = new JMenuBar();
    	 //set up and add the main menus to the menu bar
    	 if (databaseRoles != null && databaseRoles.isEmpty() || databaseRoles == null ){
    		 //no roles were assigned to user that is currently logged in
    		 System.out.println("not authorized to use this system.... contact IT");
    		 this.dispose();   //close this window
    		 System.exit(1);  //end the application
    	 }
    	 //purchases menu
    	 if(databaseRoles.contains(Roles.PR_BuyerRole.name())){
    		 this.purchaseMenu=new JMenu("Purchases");
    		 premiereMenuBar.add(purchaseMenu);
    		 this.purchasesMenuItem=new JMenuItem("View Purchases");
    		 this.purchaseMenu.add(purchasesMenuItem);
    		 this.purchasesMenuItem.addActionListener(new ViewPurchasesListener());
    	 }
    	 //Sales Menu
    	 if (databaseRoles.contains(Roles.PR_SalesRole.name())){
    		 //current user was assigned SalesRole
    	     this.salesMenu = new JMenu("Sales");
    	     premiereMenuBar.add(salesMenu);
    	   //set up each menu item and add the item to the menu on the menu bar
        	 this.addSalesItem = new JMenuItem("Add Sales Order");
        	 this.modifySalesItem = new JMenuItem("Modify Sales Order");
        	 this.cancelSalesItem = new JMenuItem("Cancel Sales Order");
        	 
        	 this.salesMenu.add(addSalesItem);
        	 this.salesMenu.add(modifySalesItem);
        	 this.salesMenu.add(cancelSalesItem);
        	 this.salesMenu.addSeparator();   //add separate between modify operations and list operations
        	 this.viewSalesItem = new JMenuItem ("View Sales Order");
        	 this.viewSalesOrdersItem = new JMenuItem("View All Sales Orders");
        	 
        	 this.salesMenu.add(viewSalesItem);
        	 this.salesMenu.add(viewSalesOrdersItem);
        	 
        	 //connect each menu item to the appropriate action listener
        	 //TODO the actionListener code bodies
			this.addSalesItem.addActionListener(new AddSalesOrderListener());
			this.modifySalesItem.addActionListener(new ModifySalesListener());
			this.cancelSalesItem.addActionListener(new CancelSalesListener());
			this.viewSalesItem.addActionListener(new ViewSaleListener(databaseRoles));
			this.viewSalesOrdersItem.addActionListener(new ViewAllSalesListener(databaseRoles));
      	     
    	 }
    	 //Customer Menu
    	 if (databaseRoles.contains(Roles.PR_CustomerRole.name())
    			 || databaseRoles.contains(Roles.PR_OfficeRole.name())
    			 || databaseRoles.contains(Roles.PR_AccountantRole.name()))
    	 { 
    	   this.customerMenu = new JMenu("Customer");
	       premiereMenuBar.add(customerMenu);
    	   this.addcustomerMenuItem = new JMenuItem("Add Customer") ;
    	   this.modifyCustomerMenuItem = new JMenuItem("Modify Customer");
    	   
    	   //add submenus to the main menu
    	   this.customerMenu.add(addcustomerMenuItem);
    	   this.customerMenu.add(modifyCustomerMenuItem);
    	   
    	   //TODO actionListener code bodies
    	   this.addcustomerMenuItem.addActionListener(new AddCustomerListener());
    	   this.modifyCustomerMenuItem.addActionListener(new ModifyCustomerListener(databaseRoles));
    	   
			if (databaseRoles.contains(Roles.PR_AccountantRole.name())) 
			{
				this.customerMenu.addSeparator(); // add separate between modify operations and list operations
				this.listCustomersMenuItem = new JMenuItem("List Customers");
				this.customerMenu.add(listCustomersMenuItem);
				
				// connect menu item to the appropriate ActionListener
				this.listCustomersMenuItem.addActionListener(new ListCustomersListener());
				 

			}
			if(databaseRoles.contains(Roles.PR_CustomerRole.name())){
				this.salesMenu=new JMenu("Orders");
				this.premiereMenuBar.add(salesMenu);
				this.viewSalesItem=new JMenuItem("Find an order");
				this.viewSalesOrdersItem=new JMenuItem("View Order History");
			
				
				salesMenu.add(viewSalesItem);
				salesMenu.add(viewSalesOrdersItem);
				this.viewSalesOrdersItem.addActionListener(new ViewAllSalesListener(databaseRoles));
				viewSalesItem.addActionListener(new ViewSaleListener(databaseRoles));
				
			
			}
			if(databaseRoles.contains(Roles.PR_OfficeRole.name())){
    		    this.modifyEmployeeMenuItem = new JMenuItem("Modify Employee Data");
    			this.employeeMenu.add(modifyEmployeeMenuItem);
    			//TODO event code body
    			this.modifyEmployeeMenuItem.addActionListener(new ModifyEmployeeListener());
			}
    	    
    	    
    	 }
    	 
    	 //Employee Menu
    	 if (!databaseRoles.contains(Roles.PR_CustomerRole.name())){
    		 //display employee related menu
    		 this.employeeMenu = new JMenu("Employee");
    		 premiereMenuBar.add(employeeMenu);
    		 if (databaseRoles.contains(Roles.PR_OfficeRole.name())){
    			 this.addEmployeeMenuItem = new JMenuItem("Add Employee");
    			 this.employeeMenu.add(addEmployeeMenuItem);
    			 //TODO event code body
    			 this.addEmployeeMenuItem.addActionListener(new AddEmployeeListener());
    			 this.listEmployeesMenuItem = new JMenuItem("List Employees");
    			    
        		 this.employeeMenu.add(listEmployeesMenuItem);
        		 this.listEmployeesMenuItem.addActionListener(new ListEmployeesListener());
    		 }
    		 
    		 }
    		
    		 if (databaseRoles.contains(Roles.PR_AccountantRole.name())){
    			 //Accountant can modify the commission rate
    			 this.modifySalesRepMenuItem = new JMenuItem("Modify SalesRep Data");
    			 this.employeeMenu.add(modifySalesRepMenuItem);
    			 //TODO event code body
    			 this.modifySalesRepMenuItem.addActionListener(new ModifySalesRepListener());
    			 this.listEmployeesMenuItem = new JMenuItem("List Employees");
    			 this.addEmployeeMenuItem=new JMenuItem("Add Employee");
    			 this.addEmployeeMenuItem.addActionListener(new AddEmployeeListener());
    			 this.employeeMenu.add(this.addEmployeeMenuItem);
        		 this.employeeMenu.add(listEmployeesMenuItem);
        		 this.listEmployeesMenuItem.addActionListener(new ListEmployeesListener());
        		
 				this.addSalesRepMenuItem=new JMenuItem("Add Sales Rep");
 				this.employeeMenu.add(addSalesRepMenuItem);
 				this.addSalesRepMenuItem.addActionListener(new AddSalesRepListener());
    		 }
    		 if(this.employeeMenu==null){
    			 employeeMenu=new JMenu("Employee");
    		 }
    		 this.employeeMenu.addSeparator();   //add separate between modify operations and list operations
    		 this.viewEmployeeMenuItem = new JMenuItem("View Employee Data");
    		 this.employeeMenu.add(viewEmployeeMenuItem);
    		 //TODO event code body
    		 this.viewEmployeeMenuItem.addActionListener(new ViewEmployeeDataListener());
    		/*//THIS IS NOT ALLOWED FOR EMPLOYEES, RIGHT?
    		 * this.listEmployeesMenuItem = new JMenuItem("List Employees");
       		 this.employeeMenu.add(listEmployeesMenuItem);
    		 this.listEmployeesMenuItem.addActionListener(new ListEmployeesListener());*/
    				 
    	
     }
    	 
    @Override
	public void windowActivated(WindowEvent e) {
	
		
	}


	@Override
	public void windowClosed(WindowEvent e) {
		System.exit(0);   //window close , exit the application
	}


	@Override
	public void windowClosing(WindowEvent e) {
		try{
			dbConnection.close();  //close connection to database
			dispose(); //close and dispose of this window
		}
		catch(SQLException sqlexception){
			JOptionPane.showMessageDialog(null, "couldn't close connection to database");
		}
		
	}


	@Override
	public void windowDeactivated(WindowEvent e) {
		
		
	}


	@Override
	public void windowDeiconified(WindowEvent e) {
		
		
	}


	@Override
	public void windowIconified(WindowEvent e) {
		
		
	}


	@Override
	public void windowOpened(WindowEvent e) {
		
		
	}
	
	private class ListCustomersListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			new ListCustomers(dbConnection);
			
		}
		 
	}
	
    private class AddSalesOrderListener implements ActionListener{
    	

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
		
			OrderEntry oe= new OrderEntry(dbConnection);
			
			
			
		}
    	
    }
    
    private class ModifySalesListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			new ModifyOrder(dbConnection);
			
		}
    	
    }
    

    private class CancelSalesListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			new CancelOrder(dbConnection);
			
		}
    	
    }
    
    private class AddCustomerListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
		
			CustomerEntry ce=new CustomerEntry(dbConnection);
		
			
		}
    	
    }
    
    private class ModifyCustomerListener implements ActionListener{

    	private ArrayList<String>dbRoles;
    	public ModifyCustomerListener(ArrayList<String>dbRoles){
    		this.dbRoles=dbRoles;	
    	}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			new ModifyCust(dbConnection, dbRoles);
			
		}
    	
    }
    private class AddEmployeeListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
		
			NewEmployeeFrame ne=new NewEmployeeFrame(dbConnection);
			
			
		}
    	
    }
    
    private class ModifyEmployeeListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
					
			new EditEmployee(dbConnection);
			
		}
    	
    }
    
    private class ViewEmployeeDataListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			new ViewEmployee(dbConnection);
			
		}
    	
    }
    
    private class ListEmployeesListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
	
			new ListEmployees(dbConnection);
			
		}
    	
    }
    
    private class ModifySalesRepListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			new ModifySalesRep(dbConnection);
			
		}
    	
    }
    
    private class ViewPurchasesListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
		
			new ViewAllPurchases(dbConnection);
			
		}
    	
    }
    private class ViewSaleListener implements ActionListener{

    	private ArrayList<String>databaseRoles;
    	public ViewSaleListener(ArrayList databaseRoles){
    		this.databaseRoles=databaseRoles;
    		
    	}
    	@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
    		new ViewAnOrder(dbConnection, databaseRoles);
    		
			
		}
    	
    }
    private class ViewAllSalesListener implements ActionListener{

    	private ArrayList<String>databaseRoles;
    	public ViewAllSalesListener(ArrayList databaseRoles){
    		this.databaseRoles=databaseRoles;
    		
    	}
		@Override
		public void actionPerformed(ActionEvent e) {
			ViewAllOrders vao=new ViewAllOrders(dbConnection, databaseRoles);
			
		}
    	
    }
    private class AddSalesRepListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			NewEmployeeFrame emp=new NewEmployeeFrame(dbConnection);
		//will have option in employee frame to add additional fields for the sales rep
			
			
		}
    	
    }
   
    
    
}//CLASS
