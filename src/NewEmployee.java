import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Date;

import javax.swing.*;

public class NewEmployee extends JPanel{
	
	
	//private JLabel empIDLabel; this is a trigger in db
	private JLabel FNameLabel;
	private JLabel LNameLabel;
	private JLabel streetLabel;
	private JLabel cityLabel;
	private JComboBox<String> stateList;
	private JLabel zipLabel;
	private JLabel phoneLabel;
	private JLabel stateLabel;
	private JLabel bDayLabel;
	private JLabel hireDateLabel;
	private JLabel empTypeLabel;
	
	private JTextField FNameText;
	private JTextField LNameText;
	private JTextField streetText;
	private JTextField cityText;
	private JTextField zipText;
	private JTextField phoneText;
	private JTextField bDayText;
	private JTextField hireDateText;
	private String[]states;
	private JTextField empTypeText;
	
	private Connection dbConnection;
	private int empID;
	private JCheckBox salesrep;
	

	
	private JButton insertButton;
	
	public NewEmployee(Connection DBConnection){
		this.dbConnection=DBConnection;
		
		states=new String[]{"AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA","HI","ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY"};
		stateList=new JComboBox<String>(states);
		FNameLabel=new JLabel("First Name: ");
		LNameLabel=new JLabel("Last Name: ");
		streetLabel=new JLabel("Address: ");
		cityLabel=new JLabel("City: ");
		zipLabel=new JLabel("ZipCode: ");
		stateLabel=new JLabel("State: ");
		phoneLabel=new JLabel("Phone Number (NO symbols or spaces)");
		bDayLabel=new JLabel("Birthdate: ");
		hireDateLabel=new JLabel("Hire date: ");
		this.empTypeLabel=new JLabel("Employee Type ID");
		
		
		FNameText= new JTextField(40);
		LNameText=new JTextField(40);
		streetText= new JTextField(30);
		cityText= new JTextField(25);
		stateList=new JComboBox<String>(states);
		zipText=new JTextField(9);
		phoneText=new JTextField(10);
		bDayText=new JTextField();
		hireDateText=new JTextField();
		this.empTypeText=new JTextField();
		this.salesrep=new JCheckBox("This is a sales rep");
		
		this.insertButton=new JButton("Create Employee");
		
		setLayout(new GridLayout(11,2));
		add(FNameLabel);
		add(FNameText);
		add(LNameLabel);
		add(LNameText);
		add(streetLabel);
		add(streetText);
		add(cityLabel);
		add(cityText);
		add(stateLabel);
		add(stateList);
		add(zipLabel);
		add(zipText);
		add(phoneLabel);
		add(phoneText);
		add(this.empTypeLabel);
		add(this.empTypeText);
		add(this.bDayLabel);
		add(this.bDayText);
		add(this.hireDateLabel);
		add(this.hireDateText);
		add(insertButton);
		insertButton.addActionListener(new InsertButtonListener());
		add(salesrep);
		

	}
	
//	public int getEmpID(){
//		return this.empID;
//	}
	
	private class InsertButtonListener implements ActionListener{

		private LocalDate bdate;
		private LocalDate hdate;
		private String firstName;
		private String lastName;
		private String street;
		private String city;
		private String zip;
		private String state;
		private String phone;
		private int empTypeID;
		
		private PreparedStatement insertEmp=null;
		private PreparedStatement getEmpID=null;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			try{
				insertEmp=dbConnection.prepareStatement("Insert into Employee (FirstName, LastName,Street,City,EmpState,ZipCode,PhoneNumber,BirthDate,HireDate,EmpTypeID)"+
			"Values(?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			}
			catch(SQLException sqle){
				sqle.printStackTrace();
			}
		
			 String[] bdateParts = bDayText.getText().split("/");
			 String[]hdateParts=hireDateText.getText().split("/");
			  if (bdateParts.length !=3 || hdateParts.length!=3){
				  //user made an error entering the date
				  JOptionPane.showMessageDialog(null,"incorrect date format");
				  return;
			  }
			  else
			  { 
				int month;
			    int day;
			    int year;
			    
				month = Integer.parseInt(bdateParts[0]);
				day = Integer.parseInt(bdateParts[1]);
				year = Integer.parseInt(bdateParts[2]);
				
				bdate = LocalDate.of(year, month, day);
			
				
				java.sql.Date sqlbDate = java.sql.Date.valueOf(bdate);
				
				month = Integer.parseInt(hdateParts[0]);
				day = Integer.parseInt(hdateParts[1]);
				year = Integer.parseInt(hdateParts[2]);
				hdate=LocalDate.of(year, month, day);
				
				java.sql.Date sqlhDate=java.sql.Date.valueOf(hdate);
				
				this.firstName=FNameText.getText();
				this.lastName=LNameText.getText();
				this.street=streetText.getText();
				this.city=cityText.getText();
				this.state=stateList.getSelectedItem().toString();
				this.zip=zipText.getText();
				this.phone=phoneText.getText();
				this.empTypeID=Integer.parseInt(empTypeText.getText());
				try{
					insertEmp.setString(1,firstName);
					insertEmp.setString(2,lastName);
					insertEmp.setString(3, street);
					insertEmp.setString(4, city);
					insertEmp.setString(5, state);
					insertEmp.setString(6, zip);
					insertEmp.setString(7, phone);
					insertEmp.setDate(8, sqlbDate);
					insertEmp.setDate(9, sqlhDate);
					insertEmp.setInt(10, empTypeID);
					
					int result=insertEmp.executeUpdate();
					String query="Select EmpID from Employee where FirstName=? and LastName=? and PhoneNumber=?";
					getEmpID=dbConnection.prepareStatement(query);
					getEmpID.setString(1, firstName);
					getEmpID.setString(2, lastName);
					getEmpID.setString(3, phone);
					ResultSet rs1=getEmpID.executeQuery();
					if(rs1.next()){
						 empID=rs1.getInt("EmpID");
						System.out.println(empID);
					}
					
					
					
					
					
					
				}
				catch(SQLException sqlex){
					sqlex.printStackTrace();
					try{
						dbConnection.rollback();
						
					}
					catch(SQLException sqlexc){
						sqlexc.printStackTrace();
					}
				}
				
				try {
					dbConnection.commit();
				} catch (SQLException sqlex) {
					sqlex.printStackTrace();
				}
			  }
			
			  if(salesrep.isSelected()){
					new NewSalesRep(dbConnection, empID);
				}
			
		}
		
	}

}
