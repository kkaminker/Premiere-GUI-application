

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;



public class ModifyOrder extends JFrame implements WindowListener{
	private Connection dbConnection;
	private JPanel getIDPanel;
	private JButton getIDButton;
	private JLabel instructions;
	private JTextField id;
	private int inputID;
	private PreparedStatement orderStatement;
	private JPanel tablePanel;
	private JButton updateButton;

	public ModifyOrder(Connection dbConnection){
	//store the reference to the database --- back end
	 this.dbConnection = dbConnection;
	 tablePanel=new JPanel();
	 tablePanel.setLayout(new BorderLayout());
	 updateButton=new JButton("Update orders");
		 
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
		
			query = "select * from SalesRepOrderLineData where order_num=?";
		
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
			
			JTable table= new JTable(new DefaultTableModel(data, colNames));
			updateButton.addActionListener(new UpdateListener(table));
			tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
			tablePanel.add(updateButton, BorderLayout.SOUTH);
			add(tablePanel, BorderLayout.SOUTH);
			//so tech can edit orderNum but won't send that to DB
			
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// set up layout of the window
		pack();
		setVisible(true);
	}

	private class UpdateListener implements ActionListener{
		
		private String partNum;
		private int qty;
		private BigDecimal price;
		private java.sql.Date mod_date;
		private JTable table;
		private String query;
		private CallableStatement proc;
	
		
		public UpdateListener(JTable table) {
			this.table=table;
		}

		@Override
		public void actionPerformed(ActionEvent arg0){
			
			int rows=table.getRowCount();
			for(int index=0; index<rows; index++){
				//(index, 0) has order_num, not letting change
				partNum=(String) table.getValueAt(index, 1);
				qty=Short.parseShort(table.getValueAt(index, 2).toString());//apparently this is a small int which corresponds to short
				price=new BigDecimal(table.getValueAt(index, 3).toString());
				//(index, 4) is subtotal calculated field can not modify
			    mod_date= java.sql.Date.valueOf(LocalDate.now());
				
				
			    query="{call sp_modifySROrderLine(?,?,?,?,?)}";
				try {
					proc=dbConnection.prepareCall(query);
					
					proc.setString(1, partNum);
					proc.setInt(2, qty);
					proc.setBigDecimal(3, price);
					proc.setDate(4, mod_date);
					proc.setInt(5, inputID);
					proc.execute();
					dbConnection.commit();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
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


	





