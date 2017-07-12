
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;



public class CancelOrder extends JFrame implements WindowListener{

	private Connection dbConnection;

	private JPanel getIDPanel;
	private JButton getIDButton;
	private JLabel instructions;
	private JTextField id;
	private int inputID;
	private CallableStatement orderStatement;
	private JPanel tablePanel;

	public CancelOrder(Connection dbConnection){
	//store the reference to the database --- back end
	 this.dbConnection = dbConnection;
	 tablePanel=new JPanel();
	 tablePanel.setLayout(new BorderLayout());
		 
	//verify that a database connection exists
	if (this.dbConnection == null){
		   JOptionPane.showMessageDialog(null,"missing database connection --- contact IT");
		   
	}
	else{ //continue with this process
		   
		   	setTitle("Order Cancellation");
		   	//set window size
		   	setSize(600, 600);
		   	// Specify an action for the close button.
		   	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		   	this.setLayout(new BorderLayout());
		   	getIDPanel=new JPanel();
			getIDPanel.setLayout(new FlowLayout());
			getIDButton=new JButton("Go");
			getIDButton.addActionListener(new GetIDListener());
			instructions=new JLabel("Enter the order id you wish to cancel");
			id=new JTextField();
			id.setColumns(5);
			getIDPanel.add(instructions);
			getIDPanel.add(id);
			getIDPanel.add(getIDButton);
			
			add(getIDPanel);
			
			pack();
			setVisible(true);
	}
	}

	

	private class GetIDListener implements ActionListener{
		
		

		@Override
		public void actionPerformed(ActionEvent arg0) {
			inputID=Integer.parseInt(id.getText());
			try {
				orderStatement=dbConnection.prepareCall("{call sp_deleteSROrder(?)}");
				orderStatement.setInt(1, inputID);
				orderStatement.execute();
				dbConnection.commit();
				add(new JLabel("Order canceled"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		}
		
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


	





