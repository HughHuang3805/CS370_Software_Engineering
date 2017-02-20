package Phase1;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

public class Controller implements ActionListener{

	GUI myGui;
	DataBase myDatabase = new DataBase();
	JTable myTable;

	public Controller(GUI g) throws Exception{
		myGui = g;
		myGui.setButtonListener(this);
		//myDatabase.createSchema();
		//myDatabase.createTable();
	}

	@Override
	public void actionPerformed(ActionEvent e){
		String buttonName = e.getActionCommand();

		switch(buttonName){

		case "Connect to database":
			try {
				Connection conn = myDatabase.getConnection(myGui.getUserName(), myGui.getPassword(), myGui.getDatabaseName(), myGui);
				if(conn == null){
					myGui.showIncorrectUNorPWDialog();
				} else{
					myGui.loginPanel.setVisible(false);
					myGui.setJFileChooser(myDatabase);
					myGui.setDisplay(myDatabase);
					myGui.menu1.setEnabled(true);
					myGui.getRootPane().setDefaultButton(null);
					myGui.panel = new JPanel();
					myDatabase.createDatabase();
					//change to a new field
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				//e1.printStackTrace();
			}
			break;
			
		case "Choose a file":
			myGui.setJFileChooser(myDatabase);
			break;
			
		case "Insert":
			myGui.panel.setVisible(false);
			myGui.repaint();
			myTable = myGui.setInsertPanel(this);
			myGui.panel.setVisible(true);
			myGui.repaint();
			break;
			
		case "Update":
			myGui.panel.setVisible(false);
			myGui.repaint();
			myTable = myGui.setUpdatePanel(this);
			myGui.panel.setVisible(true);
			myGui.repaint();
			break;

		case "INSERT TO TABLE":
			try {
				myGui.setInsert(myDatabase, myTable, this);//insert to the table
				myGui.jsp.setVisible(false);//make initial jsp insert panel invisible
				myGui.setDisplay(myDatabase);//new jsp
				myTable = myGui.setInsertPanel(this);//new insert panel
				myGui.repaint();
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			break;
			
		case "Delete":
			String id = JOptionPane.showInputDialog("ID of product to be deleted: ");
			try{
				int intID = Integer.parseInt(id);
				int reply = JOptionPane.showConfirmDialog(myGui, "Confirm delete?", "Confirm ...", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				boolean success = myDatabase.delete(intID);
				if(success && reply == 0)
					JOptionPane.showMessageDialog(myGui, "Delete complete.");
				else
					JOptionPane.showMessageDialog(myGui, "Delete not successful.");
				myGui.jsp.setVisible(false);//make initial jsp insert panel invisible
				myGui.panel.setVisible(false);
				myGui.repaint();
				myGui.setDisplay(myDatabase);
				myGui.jsp.setVisible(true);
				myGui.jsp.repaint();
				myGui.repaint();
			} catch(Exception e2){
				JOptionPane.showMessageDialog(myGui, "Wrong ID.");
			}
			break;

		case "Exit":
			try {
				myDatabase.br.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			System.exit(0);
			break;
			
		case "UPDATE TABLE":
			try {
				myGui.setUpdate(myDatabase, myTable, this);//insert to the table
				myGui.jsp.setVisible(false);//make initial jsp insert panel invisible
				myGui.setDisplay(myDatabase);
				myTable = myGui.setUpdatePanel(this);//new insert panel
				myGui.repaint();
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			break;
			
		default:
			break;

		}
	}
}
