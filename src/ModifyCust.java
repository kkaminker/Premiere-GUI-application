import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


public class ModifyCust extends JFrame{
	
	private Connection dbConnection;
	private PreparedStatement getCust;
	private JPanel labelPanel;
	private JPanel resultPanel;
	private int inputCustID;
	private JLabel custBalanceLabel;
	private JLabel RepNumLabel;
	private JTextField repNumText;
	private JTextField balanceText;
	//credit_limit
	//rep_num
	private ArrayList<JTextField>textFields;
	private JButton updateButton;
	private JButton getIDButton;
	private JPanel getIDPanel;
	private JTextField IDText;
	private JLabel instructions;
	private CallableStatement proc;
	private ArrayList<String> roles;
	
	
	public ModifyCust(Connection dbConnection, ArrayList<String>roles){
		
		this.roles=roles;
		this.dbConnection=dbConnection;
		this.setLayout(new BorderLayout());
		this.setSize(500,500);
		this.setTitle("Modify Customer");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		labelPanel=new JPanel();
		resultPanel=new JPanel();
		textFields=new ArrayList<JTextField>();
		//set up EmpPanel
		getIDPanel=new JPanel();
		getIDPanel.setLayout(new FlowLayout());
		getIDButton=new JButton("Go");
		getIDButton.addActionListener(new getIDListener());
		instructions=new JLabel("Enter Customer ID you wish to modify");
		IDText=new JTextField();
		IDText.setColumns(5);
		getIDPanel.add(instructions);
		getIDPanel.add(IDText);
		getIDPanel.add(getIDButton);
		
		//add to this frame
		add(getIDPanel, BorderLayout.NORTH);
		pack();
		setVisible(true);
		
	}
	
	private class getIDListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			inputCustID=Integer.parseInt(IDText.getText());
			if(roles.contains(Roles.PR_AccountantRole.name())){
				setUpAccountView();
			}
			else
			setUpView();
			
		}
		
	}
	
	private void setUpAccountView(){
		
		try {
			getCust=dbConnection.prepareStatement("select * from viewCustAccountingFunc (?)");
			getCust.setInt(1, inputCustID);
			
			ResultSet rs=getCust.executeQuery();
			
			//ResultSetMetaData md=rs.getMetaData();
	
			JTextField textField=new JTextField();
		
			int numCols=0;
			try {
				this.custBalanceLabel=new JLabel("Customer Balance");
				this.RepNumLabel= new JLabel("Rep number");
				
				if(rs.next()){
					this.balanceText=new JTextField(rs.getString(1));
					this.repNumText=new JTextField(rs.getString(2));
					balanceText.setEditable(true);
					repNumText.setEditable(true);
					resultPanel.add(custBalanceLabel);
					resultPanel.add(balanceText);
					resultPanel.add(RepNumLabel);
					resultPanel.add(repNumText);
						
				}
				
				
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			add(resultPanel,BorderLayout.EAST);
			add(labelPanel, BorderLayout.WEST);
			updateButton=new JButton("Update Customer");
			updateButton.addActionListener(new ModifyAccountListener());
			add(updateButton, BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
		
	}
	private void setUpView(){
		
		try {
			getCust=dbConnection.prepareStatement("select * from viewCustAddressFunc (?)");
			getCust.setInt(1, inputCustID);
			
			ResultSet rs=getCust.executeQuery();
			
			ResultSetMetaData md=rs.getMetaData();
	
			JTextField textField=new JTextField();
		
			int numCols=0;
			try {
				numCols = md.getColumnCount();
				
				labelPanel.setLayout(new GridLayout(numCols,1));
				resultPanel.setLayout(new GridLayout(numCols,1));
				for(int i=1;i<=numCols;i++){
					labelPanel.add(new JLabel(md.getColumnName(i)));
					System.out.println(md.getColumnClassName(i));

				}
				
				
				if(rs.next()){
					
					for(int i=1; i<=numCols;i++){
						textField=new JTextField();
						textField.setEditable(true);
						textField.setText(rs.getString(i));
						resultPanel.add(textField);
						textFields.add(textField);
					}	
				}
				
				
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			add(resultPanel,BorderLayout.EAST);
			add(labelPanel, BorderLayout.WEST);
			updateButton=new JButton("Update Customer");
			updateButton.addActionListener(new ModifyListener(numCols));
			add(updateButton, BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
}
	
	private class ModifyAccountListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub

			try {
				String query;
				query="{call sp_modifyCustAccounting(?,?,?)}";
				proc=dbConnection.prepareCall(query);
				BigDecimal custBalance=new BigDecimal(balanceText.getText());
				proc.setBigDecimal(1, custBalance);
				proc.setInt(2, Integer.parseInt(repNumText.getText()));
				proc.setInt(3, inputCustID);
				proc.execute();
				dbConnection.commit();
			} 
			catch (SQLException e) {
		
				e.printStackTrace();
			}
			
		}
		
	}
	private class ModifyListener implements ActionListener{

		private int numCols;
		public ModifyListener(int numCols){
			this.numCols=numCols;
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			try {
				String query;
				query="{call sp_modifyCustAddress(?,?,?,?,?)}";
				proc=dbConnection.prepareCall(query);
				int index;
				for(int i=1;i<=numCols;i++){
					index=i-1;
					proc.setString(i,textFields.get(index).getText());
				}
				proc.setInt(numCols+1,inputCustID);
				proc.execute();
				dbConnection.commit();
			} 
			catch (SQLException e) {
		
				e.printStackTrace();
			}
	
			
		}
		
	}
	

	

}

