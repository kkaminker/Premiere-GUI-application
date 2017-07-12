
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;



public class ViewAnOrder extends JFrame implements WindowListener{

	private Connection dbConnection;
	private ArrayList<String> dbRoles;
	private JPanel getIDPanel;
	private JButton getIDButton;
	private JLabel instructions;
	private JTextField id;
	private int inputID;
	private PreparedStatement orderStatement;
	private JPanel tablePanel;

	public ViewAnOrder(Connection dbConnection, ArrayList dbRoles){
	//store the reference to the database --- back end
	 this.dbConnection = dbConnection;
	 this.dbRoles=dbRoles;
	 tablePanel=new JPanel();
	 tablePanel.setLayout(new BorderLayout());
		 
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
		   	getIDPanel=new JPanel();
			getIDPanel.setLayout(new FlowLayout());
			getIDButton=new JButton("Go");
			getIDButton.addActionListener(new GetIDListener());
			instructions=new JLabel("Enter the order id");
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

	private void setUpView() {

		String query = null;
		if (dbRoles.contains(Roles.PR_CustomerRole.name())) {
			query = "select * from CustomerOrderLineData where order_num=?";
		} else if (dbRoles.contains(Roles.PR_SalesRole.name())) {
			query = "select * from SalesRepOrderLineData where order_num=?";
		}
		try {
			orderStatement = dbConnection.prepareStatement(query);
			orderStatement.setInt(1, inputID);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// TODO make table now
		try {
			ResultSet rs=orderStatement.executeQuery();
			ResultSetMetaData md=rs.getMetaData();
			Vector<String>colNames = new Vector<String>();
			Vector<Vector<Object>> data = new Vector<Vector<Object>>();
			int numCols=md.getColumnCount();
			for(int index=1; index<=numCols; index++){
				colNames.add(md.getColumnName(index));
				
			}
			while(rs.next()){
				Vector<Object> vector= new Vector<Object>();
				for(int c=1; c<=numCols; c++){
					vector.add(rs.getObject(c));
				}
				data.add(vector);
			}
			
			JTable table= new JTable(new DefaultTableModel(data, colNames)){
				public boolean isCellEditable(int row, int col){
					return false;
				}
			};
			
			tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
			add(tablePanel, BorderLayout.SOUTH);
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// set up layout of the window
		pack();
		setVisible(true);
	}

	private class GetIDListener implements ActionListener{
		
		

		@Override
		public void actionPerformed(ActionEvent arg0) {
			inputID=Integer.parseInt(id.getText());
			setUpView();
			
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


	




