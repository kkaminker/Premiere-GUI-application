import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;


public class EditEmployee extends JFrame{
	
	private Connection dbConnection;
	private PreparedStatement getEmp;
	private JPanel labelPanel;
	private JPanel resultPanel;
	private int inputEmpID;
//	private JPanel getEmpIDPanel;
	private String first;
	private String last;
	private String street;
	private String city;
	private String state;
	private String zip;
	private String phone;
	private ArrayList<JTextField>textFields;
	private JButton updateButton;
	private JButton getEmpButton;
	private JPanel getEmpPanel;
	private JTextField empID;
	private JLabel instructions;
	private CallableStatement proc;
	
	
	public EditEmployee(Connection dbConnection){
		
		this.dbConnection=dbConnection;
		this.setLayout(new BorderLayout());
		this.setSize(500,500);
		this.setTitle("Modify Employee");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		labelPanel=new JPanel();
		resultPanel=new JPanel();
		textFields=new ArrayList<JTextField>();
		getEmpPanel=new JPanel();
		getEmpPanel.setLayout(new FlowLayout());
		getEmpButton=new JButton("Go");
		getEmpButton.addActionListener(new getEmpListener());
		instructions=new JLabel("Enter Employee ID you wish to modify");
		empID=new JTextField();
		empID.setColumns(5);
		getEmpPanel.add(instructions);
		getEmpPanel.add(empID);
		getEmpPanel.add(getEmpButton);
		
		//add to this frame
		add(getEmpPanel, BorderLayout.NORTH);
		pack();
		setVisible(true);
		
	}
	
	private class getEmpListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			inputEmpID=Integer.parseInt(empID.getText());
			setUpView();
			
		}
		
	}
	private void setUpView(){
		
		try {
			getEmp=dbConnection.prepareStatement("select * from viewEmpDataFunc (?)");
			getEmp.setInt(1, inputEmpID);
			
			ResultSet rs=getEmp.executeQuery();
			
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
			updateButton=new JButton("Update Employee");
			updateButton.addActionListener(new ModifyListener(numCols));
			add(updateButton, BorderLayout.SOUTH);
		
		pack();
		setVisible(true);
		
	} catch (SQLException e) {
		e.printStackTrace();
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
				proc=dbConnection.prepareCall("{call sp_modifyEmpData(?,?,?,?,?,?,?,?)}");
				int index;
				for(int i=1;i<=numCols;i++){
					index=i-1;
					proc.setString(i,textFields.get(index).getText());
				}
				proc.setInt(numCols+1,inputEmpID);
				proc.execute();
				dbConnection.commit();
			} 
			catch (SQLException e) {
		
				e.printStackTrace();
			}
	
			
		}
		
	}
	

	

}
