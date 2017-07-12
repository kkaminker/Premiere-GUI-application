//package JavaPremiereDBSQLSecurityApp;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginFrame extends JFrame{
	private JTextField loginField; //txtbx
	private JPasswordField passwordField; //txtbx of type password
	private JPanel loginPanel; //the panel/window/form on this "JFrame"
	private JButton loginButton; //button to login
	private JButton cancelButton; //button to cancel
	private Connection dbConnection; //the connection to the database
	
	public LoginFrame(){
		this.setSize(500,500);
		this.setTitle("Premiere Application Login");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//set up the login panel
		loginPanel = setUpPanel();//the loginPanel is a GridLayout
		this.setLayout(new BorderLayout());//this frame is BorderLayout
		//add the login panel to the (this) window at center position
		this.add(loginPanel, BorderLayout.CENTER);
		
		//set up the command buttons
		loginButton = new JButton("LOGIN");
		cancelButton = new JButton ("CANCEL");
		
		loginButton.addActionListener(new LoginListener(this));
		
		
		
		cancelButton.addActionListener (new CancelListener(this));
		
		this.add(loginButton,BorderLayout.EAST);
		this.add(cancelButton,BorderLayout.WEST);
		
		this.pack();
		this.setVisible(true);
		
		
	}
	
	private JPanel setUpPanel(){
		JPanel loginPanel = new JPanel();
		loginPanel.setLayout(new GridLayout(2,2));
		loginPanel.add(new JLabel("SQL Login:"));
		loginField = new JTextField(20);
		loginPanel.add(loginField);
		loginPanel.add(new JLabel("Password:"));
		passwordField = new JPasswordField();
		loginPanel.add(passwordField);
		return loginPanel;
		
	}
	
	
	 

	  
	private class LoginListener implements ActionListener{
		private JFrame parentWindow;

		public LoginListener(JFrame parentWindow){//takes a parentWindow which is THIS form/frame thing (LoginFrame)
			 this.parentWindow = parentWindow;
		}
		@Override
		public void actionPerformed(ActionEvent e) {//when this button clicked
			//retrieve data that user entered on the screen
			String login = loginField.getText();
			//this statement is a security vulnerability
			char[] code = passwordField.getPassword();
			
			
			connectToDatabase(login, code);
			this.parentWindow.dispose();
			
		}
		
		 private void connectToDatabase(String login, char[] code){
			 
			  String password = new String(code);
		
	   	      //testing this by connecting to sql server using SQL Server Authentication on the local machine
			
			  final String DATABASE_URL = "jdbc:sqlserver://127.0.0.1:58420;" +
	 			     "databaseName=PREMIERE";
		
	 		  
	 		
	 	      
	 	       try{
	 	    	   
	 	    	   //establish connection to database
	 	    	   //for computer SQL Server 2008
	 	    	   System.out.println("connecting to database");
	 	    	   //for sql server authentication
	 	    	   dbConnection = DriverManager.getConnection(DATABASE_URL,login,password);
	 	    	   
	 	    	   //for windows authentication
	 	    	   //dbConnection =DriverManager.getConnection(DATABASE_URL);
	 	    	   
	 	    	   //to enable transaction processing do not
	 	    	   //automatically commit
	 	    	   dbConnection.setAutoCommit(false);
	 	    	   
	 	    	   
	 	    	   
	 	    	   
	 	    	   //now that we connected to SQL Server and the database, display the main menu window
	 	    	   new MainMenuFrame(dbConnection);
	 	       }
	 	     catch(SQLException sqlException){
		    	  JOptionPane.showMessageDialog(null,"couldn't connect to premiere database ");
		    	   sqlException.printStackTrace();
		    	   
		       }
	}
	}   //end LoginListener class
	
	private class CancelListener implements ActionListener{
		private JFrame parentWindow;
		public CancelListener(JFrame parentWindow){
			this.parentWindow = parentWindow;
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//close the login window
			JOptionPane.showMessageDialog(null, "login cancelled");
			parentWindow.dispose();
			
		}
		
	}
	
	public static void main(String[] args){
		new LoginFrame();
	}
	}  //end LoginFrame class
	
	


