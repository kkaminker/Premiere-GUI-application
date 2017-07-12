import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.*;



public class NewSalesRep extends JFrame {
	
	private SRPanel salesRepPanel;
	private int empID;
	private Connection dbConnection;

	public NewSalesRep(Connection dbConnection, int empID) {
		this.empID=empID;
		this.dbConnection=dbConnection;
		setTitle("New Sales Rep Form");
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		 salesRepPanel= new SRPanel();// TODO create
		add(salesRepPanel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}
	
	private class SRPanel extends JPanel{
		
		
		private JLabel commission;
		private JLabel rate;
	
		private JTextField commText;
		private JTextField rateText;
		private JButton insertButton;
		
		
		public SRPanel(){
		
			commission=new JLabel("Commission");
			rate=new JLabel("Rate");
			commText=new JTextField();
			rateText=new JTextField();
			insertButton=new JButton("Create SalesRep");
			setLayout(new GridLayout(3,2));
			add(commission);
			add(commText);
			add(rate);
			add(rateText);
			insertButton.addActionListener(new InsertButtonListener());
			add(insertButton);
			
			
		}
		private class InsertButtonListener implements ActionListener{
			
			private BigDecimal commission;
			private double rate;
			
			private PreparedStatement insertRep;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				commission=new BigDecimal(commText.getText());
				rate=Double.parseDouble(rateText.getText());
			
				try {
					insertRep=dbConnection.prepareStatement("Insert into Salesrep(rep_num, commission, rate)"+
					"Values(?,?,?)");
					insertRep.setInt(1, empID);
					insertRep.setBigDecimal(2, commission);
					insertRep.setDouble(3, rate);
					int result=insertRep.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
					try{
						dbConnection.rollback();
						
					}
					catch(SQLException sqlexc){
						sqlexc.printStackTrace();
					}
				}
				try{
					dbConnection.commit();
				}
				catch(SQLException sqle){
					sqle.printStackTrace();
				}
				
			}
			
		}
		
	}
	
	
	
}
