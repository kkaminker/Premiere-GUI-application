import java.awt.BorderLayout;
import java.sql.Connection;

import javax.swing.*;

public class NewEmployeeFrame extends JFrame {

	private NewEmployee employeePanel;

	public NewEmployeeFrame(Connection dbConnection) {
		setTitle("New Employee Form");
		setSize(600, 600);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout());
		employeePanel = new NewEmployee(dbConnection);// TODO create
		add(employeePanel, BorderLayout.CENTER);
		pack();
		setVisible(true);
	}

//	public int getEmpID(){
//		return employeePanel.getEmpID();
//	}
}
