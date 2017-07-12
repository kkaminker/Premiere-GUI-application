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
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


public class ModifySalesRep extends JFrame{
	
	private Connection dbConnection;
	private PreparedStatement getSalesRep;
	private JPanel labelPanel;
	private JPanel resultPanel;
	private int inputID;
	private JButton updateButton;
	private JButton getIDButton;
	private JPanel getIDPanel;
	private JTextField id;
	private JLabel instructions;
	private CallableStatement proc;
	private JTextField rateText;
	
	
	public ModifySalesRep(Connection dbConnection){
		
		this.dbConnection=dbConnection;
		this.setLayout(new BorderLayout());
		this.setSize(500,500);
		this.setTitle("Modify Sales Rep");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		labelPanel=new JPanel();
		resultPanel=new JPanel();
		
		//set up EmpPanel
		getIDPanel=new JPanel();
		getIDPanel.setLayout(new FlowLayout());
		getIDButton=new JButton("Go");
		getIDButton.addActionListener(new getEmpListener());
		instructions=new JLabel("Enter Sales Rep ID you wish to modify");
		id=new JTextField();
		id.setColumns(5);
		getIDPanel.add(instructions);
		getIDPanel.add(id);
		getIDPanel.add(getIDButton);
		
		//add to this frame
		add(getIDPanel, BorderLayout.NORTH);
		pack();
		setVisible(true);
		
	}
	
	private class getEmpListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			inputID=Integer.parseInt(id.getText());
			setUpView();
			
		}
		
	}
	private void setUpView(){
		
		try {
			getSalesRep=dbConnection.prepareStatement("select * from viewSRFunc (?)");
			getSalesRep.setInt(1, inputID);
			
			ResultSet rs=getSalesRep.executeQuery();
			
		
	
			JTextField textField=new JTextField();
		
			int numCols=0;
			try {
				
				
				labelPanel.setLayout(new GridLayout(numCols,1));
				resultPanel.setLayout(new GridLayout(numCols,1));
				labelPanel.add(new JLabel("Rate"));
			
				
				
				if(rs.next()){
					rateText=new JTextField(rs.getString(1));
					rateText.setEditable(true);
					resultPanel.add(rateText);
		
				}
				
				
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			add(resultPanel,BorderLayout.EAST);
			add(labelPanel, BorderLayout.WEST);
			updateButton=new JButton("Update Employee");
			updateButton.addActionListener(new ModifyListener());
			add(updateButton, BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
}
	
	private class ModifyListener implements ActionListener{

		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			try {
				proc=dbConnection.prepareCall("{call sp_modifySalesRep(?,?)}");
				proc.setBigDecimal(1, new BigDecimal(rateText.getText()));
				proc.setInt(2,inputID);
				proc.execute();
				dbConnection.commit();
			} 
			catch (SQLException e) {
		
				e.printStackTrace();
			}
	
			
		}
		
	}
	

	

}


