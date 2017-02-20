package Phase3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

public class Controller implements ActionListener{

	GUI myGui;
	DataBase myDatabase = new DataBase();
	JTable myTable;
	Connection conn;
	Scanner initialScannerForInputFile; //scanner of input file from initial command line argument
	String outputFile; //name of output file

	public Controller(GUI g) throws Exception{//constructor for no command line arguments
		myGui = g;
		myGui.setButtonListener(this);
	}

	public Controller(GUI g, Scanner myScanner1, String outfileName) throws Exception{//constructor for yes command line arguments
		myGui = g;
		initialScannerForInputFile = myScanner1;
		myGui.setButtonListener(this);
		outputFile = outfileName;
	}

	@Override
	public void actionPerformed(ActionEvent e){
		String buttonName = e.getActionCommand();

		switch(buttonName){

		case "Connect to database":
			try {
				//initial attempt to connect to database
				conn = myDatabase.getConnection(myGui.getUserName(), myGui.getPassword(), myGui.getDatabaseName(), myGui);

				//if fails, due to incorrect user name or password, repaint the log in screen
				if(conn == null){
					myGui.showIncorrectUNorPWDialog();//error message
					myGui.loginPanel.setVisible(false);//hide log in screen of incorrect username or password
					myGui.loginPanel.removeAll();
					myGui.askForDatabaseNameUsernamePassword();               //ask again for username and password
					myGui.setButtonListener(this);                            //set button listener for "connect to database"
					myGui.getRootPane().setDefaultButton(myGui.connectButton);//"Enter" to connect
					myGui.loginPanel.setVisible(true);		                  //repaint loginPanel 
					//try new connection with newly entered user name and password
					conn = myDatabase.getConnection(myGui.getUserName(), myGui.getPassword(), myGui.getDatabaseName(), myGui); 
				} else{
					myGui.loginPanel.setVisible(false);
					//myGui.setJFileChooser(myDatabase);
					try{// see if there are command line arguments, if not, continue the program
						FileProcessing.insertFile(initialScannerForInputFile, myDatabase, myGui);//insert command line input file
					} catch(Exception e2){
						System.out.println("No input file.");
					}
					try{
						FileProcessing.writeToInfoFile(myDatabase, outputFile);//command line output file
					} catch(Exception e3){
						System.out.println("No output file.");
					}
					myGui.setDisplay(myDatabase);
					myGui.menu1.setEnabled(true);
					myGui.getRootPane().setDefaultButton(null);
					myGui.panel = new JPanel();
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

		case "Output to File"://output information to an output file any time during the program
			outputFile = JOptionPane.showInputDialog(myGui, "Name of output file: (.txt)");
			try{
				FileProcessing.writeToInfoFile(myDatabase, outputFile);
			} catch(Exception e3){
				System.out.println("No output file.");
			}
			JOptionPane.showMessageDialog(myGui, "Output Complete.");
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
			myTable = myGui.setUpdatePanel(this, conn);
			if(myTable!= null){
				myGui.panel.setVisible(true);
				myGui.repaint();
			}
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

				int reply = JOptionPane.showConfirmDialog(myGui, "Confirm delete?", "Confirm ...", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				boolean success = myDatabase.delete(id);
				if(success && reply == 0)
					JOptionPane.showMessageDialog(myGui, "Delete complete.", "DELETE", JOptionPane.INFORMATION_MESSAGE);
				else
					JOptionPane.showMessageDialog(myGui, "Delete not successful.", "DELETE", JOptionPane.ERROR_MESSAGE);
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
			/*try {
				myDatabase.br.close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}*/
			System.exit(0);
			break;

		case "UPDATE TABLE":
			try {
				myGui.setUpdate(myDatabase, myTable, this);//insert to the table
				myGui.jsp.setVisible(false);//make initial jsp insert panel invisible
				myGui.setDisplay(myDatabase);
				//myTable = myGui.setUpdatePanel(this, conn);//new insert panel
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
