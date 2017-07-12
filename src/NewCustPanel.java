import java.awt.GridLayout;

import javax.swing.*;

public class NewCustPanel extends JPanel{
	
	private JLabel nameLabel;
	private JLabel streetLabel;
	private JLabel cityLabel;
	private JComboBox<String> stateList;
	private JLabel zipLabel;
	private JLabel phoneLabel;
	private JLabel stateLabel;
	private JTextField nameText;
	private JTextField streetText;
	private JTextField cityText;
	private JTextField zipText;
	private JTextField phoneText;
	private String[]states;
	private JLabel repNumLabel;
	private JTextField repNumText;
	//TODO custNum, repNum, creditLimit
	
	
	public NewCustPanel(){
		
		states=new String[]{"AL","AK","AZ","AR","CA","CO","CT","DE","FL","GA","HI","ID","IL","IN","IA","KS","KY","LA","ME","MD","MA","MI","MN","MS","MO","MT","NE","NV","NH","NJ","NM","NY","NC","ND","OH","OK","OR","PA","RI","SC","SD","TN","TX","UT","VT","VA","WA","WV","WI","WY"};
		stateList=new JComboBox<String>(states);
		nameLabel=new JLabel("Full Name: ");
		streetLabel=new JLabel("Address: ");
		cityLabel=new JLabel("City: ");
		zipLabel=new JLabel("ZipCode: ");
		stateLabel=new JLabel("State: ");
		phoneLabel=new JLabel("Phone Number (NO symbols or spaces)");
		repNumLabel=new JLabel("Rep Num: ");
		nameText= new JTextField(40);
		streetText= new JTextField(30);
		cityText= new JTextField(25);
		zipText=new JTextField(9);
		phoneText=new JTextField(10);
		repNumText=new JTextField(2);
		setLayout(new GridLayout(7,2));
		add(nameLabel);
		add(nameText);
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
		add(repNumLabel);
		add(repNumText);
	}


	public String getSelectedState() {
		return stateList.getSelectedItem().toString();
	}


	


	public JTextField getNameText() {
		return nameText;
	}


	public JTextField getStreet() {
		return streetText;
	}


	public JTextField getCity() {
		return cityText;
	}


	public JTextField getZip() {
		return zipText;
	}


	public JTextField getPhone() {
		return phoneText;
	}
	
	public JTextField getRepNum(){
		return repNumText;
	}


	

}
