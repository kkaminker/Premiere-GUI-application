import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;


public class ViewEmployee extends JFrame{

	private Connection dbConnection;
	private PreparedStatement getEmp;
	private JPanel labelPanel;
	private JPanel resultPanel;
	
	
	public ViewEmployee(Connection dbConnection){
		this.dbConnection=dbConnection;
		try {
			getEmp=dbConnection.prepareStatement("select * from EmployeeData");
			ResultSet rs=getEmp.executeQuery();
			ResultSetMetaData md=rs.getMetaData();
			setLayout(new BorderLayout());

		
		
			List<String> cols=new ArrayList<String>();
			List<Object> data=new ArrayList<Object>();
			labelPanel=new JPanel();
			resultPanel=new JPanel();
			
			JTextField textField=new JTextField();
		
			int numCols=0;
			try {
				numCols = md.getColumnCount();
				
				labelPanel.setLayout(new GridLayout(numCols,1));
				resultPanel.setLayout(new GridLayout(numCols,1));
				for(int i=1;i<=numCols;i++){
					labelPanel.add(new JLabel(md.getColumnName(i)));
				

				}
				if(rs.next()){
					for(int i=1; i<=numCols;i++){
						textField=new JTextField();
						textField.setEditable(false);
						textField.setText(rs.getString(i));
						resultPanel.add(textField);
						
						//System.out.println(textField.getText());
						
					}
					
				
					
				}
				
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
			add(resultPanel,BorderLayout.EAST);
			add(labelPanel, BorderLayout.WEST);
		
		pack();
		setVisible(true);
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
}
	

	
}
