package Phase3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame{

	private static final long serialVersionUID = 1L;
	JMenuItem[] menuItems = new JMenuItem[6];
	JTextArea textArea = new JTextArea();
	JPanel loginPanel = new JPanel();
	JButton fileChooseButton;
	JButton connectButton;
	JButton insertToTableButton, updateToTableButton;
	JTextField userText;
	JTextField databaseText;
	JPasswordField passwordText;
	JTable myTable;
	JScrollPane jsp, jspForInsertPanel, jspForUpdatePanel;
	JPanel panel;
	JMenuItem item1, item2, item3, item4, item5, item6;
	JMenu menu1;
	JOptionPane incorrectMessage = new JOptionPane();
	GridLayout myGrid = new GridLayout(3,3);
	int integerISBN;

	public GUI(){
		setTitle("ISBN Inventory");
		setSize(1250, 800);
		//setExtendedState(JFrame.MAXIMIZED_BOTH); 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMenuItems();
		setLocationRelativeTo(null);
		setLayout(new GridBagLayout());
		askForDatabaseNameUsernamePassword();
		//setDisplayPanel();
		//setConnectButton();
		//setFileChooseButton();
		//setJTable();
		getContentPane().setBackground(new Color(212, 175, 55));
		setVisible(true);
	}

	/*public void setDisplayPanel(){
		textArea.setBackground(Color.WHITE);
		textArea.setEditable(false);
		add(new JScrollPane(textArea));
	}*/

	//different menu items
	public void setMenuItems(){
		JMenuBar menuBar = new JMenuBar();
		menu1 = new JMenu("Functions");

		item1 = new JMenuItem("Choose a file");
		item2 = new JMenuItem("Insert");
		item3 = new JMenuItem("Update");
		item4 = new JMenuItem("Delete");
		item5 = new JMenuItem("Exit");
		item6 = new JMenuItem("Output to File");
		
		menuItems[0] = item1;
		menuItems[1] = item2;
		menuItems[2] = item3;
		menuItems[3] = item4;
		menuItems[4] = item5;
		menuItems[5] = item6;
		
		menu1.add(item1);
		menu1.addSeparator();
		menu1.add(item6);
		menu1.addSeparator();
		menu1.add(item2);
		menu1.addSeparator();
		menu1.add(item3);
		menu1.addSeparator();
		menu1.add(item4);
		menu1.addSeparator();
		menu1.add(item5);

		menuBar.add(menu1);
		//menuBar.add(menu2);

		menu1.setEnabled(false);
		setJMenuBar(menuBar);
	}

	//adds listeners to each of the buttons
	public void setButtonListener(ActionListener a){
		//fileChooseButton.addActionListener(a);
		connectButton.addActionListener(a);
		for(JMenuItem x : menuItems){
			x.addActionListener(a);
		}
	}

	//set up the insert panel when "Insert" is clicked
	public JTable setInsertPanel(ActionListener a){
		String[] columnNames = {"ISBN-10", "Title", "Quantity", "Author Firstname", "Author Lastname", "Publisher" , "Year Published",
				"Date Purchased", "Condition of Book", "Collectible Feature"};
		String[][] data = {{"", "", "", "", "", "", "", "", "", ""}};
		JTable myTable = new JTable(data, columnNames);
		myTable.setPreferredScrollableViewportSize(new Dimension(50,50));
		myTable.setFillsViewportHeight(true);
		jspForInsertPanel = new JScrollPane(myTable);
		insertToTableButton = new JButton("INSERT TO TABLE");
		insertToTableButton.addActionListener(a);

		//insertButton.setLayout(new GridLayout());
		//insertToTableButton.setPreferredSize(new Dimension(150, 50));
		insertToTableButton.setAlignmentX(Component.BOTTOM_ALIGNMENT);
		insertToTableButton.setBackground(Color.ORANGE);
		insertToTableButton.setOpaque(true);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(jspForInsertPanel, BorderLayout.NORTH);
		panel.add(insertToTableButton, BorderLayout.SOUTH);
		//insertPanel.getRootPane().setDefaultButton(insertToTableButton);
		panel.repaint();

		add(panel, BorderLayout.SOUTH);	
		setVisible(true);
		return myTable;
	}

	//set up the update panel when "Update" is clicked
	public JTable setUpdatePanel(ActionListener a, Connection conn){
		String[] columnNames = {"ISBN-10", "Title", "Quantity", "Author Firstname", "Author Lastname", "Publisher" , "Year Published",
				"Date Purchased", "Condition of Book", "Collectible Feature"};

		/*first ask for isbn for update, check if exits, then 
		display the selected isbn on update panle, then get the updated value and do things with it, disable edible on isbn*/
		String isbnToBeUpdated = "";
		isbnToBeUpdated = JOptionPane.showInputDialog(this, "Enter an ISBN-10 to be updated: ", "Update", JOptionPane.INFORMATION_MESSAGE);
		
		PreparedStatement findStatement;
		try {
			while(true){
				if(isbnToBeUpdated == null)
					return null;
				
				findStatement = conn.prepareStatement("select * from inventory where ISBN_10 = ?");//check if the isbn is valid
				findStatement.setString(1, isbnToBeUpdated);
				ResultSet myRs = findStatement.executeQuery();
				if(myRs.next()){
					int numberOfRows;
					myRs.last();
					numberOfRows = myRs.getRow(); //gets the number of rows from the result set
					String[][] data = new String[numberOfRows][10];//only 10 columns because date entered and date modified are not important
					
					int i = 0;
					myRs.beforeFirst();//goes back to the beginning of the result set
					while(myRs.next()){
						for(int j = 0; j < 10; j++){
							switch(j){

							case 0:
								data[i][j] = myRs.getString("ISBN_10");
								break;
							case 1:
								data[i][j] = myRs.getString("TITLE");
								break;
							case 2:
								data[i][j] = Integer.toString(myRs.getInt("QUANTITY"));
								break;
							case 3:
								data[i][j] = myRs.getString("AUTHOR_FIRSTNAME");
								break;
							case 4:
								data[i][j] = myRs.getString("AUTHOR_LASTNAME");
								break;
							case 5:
								data[i][j] = myRs.getString("PUBLISHER");
								break;
							case 6:
								data[i][j] = myRs.getString("YEAR_PUBLISHED");
								break;
							case 7:
								data[i][j] = myRs.getString("DATE_PURCHASED");
								break;
							case 8:
								data[i][j] = myRs.getString("CONDITION_OF_BOOK");
								break;
							case 9:
								data[i][j] = myRs.getString("COLLECTIBLE_FEATURES");
								break;
							default:
								break;
							}
						}
						i++;
					}
					myTable = new JTable(data, columnNames);//creates a new table based on the data retrieved and columnNames for display purpose
					break;
				} else{
					isbnToBeUpdated = JOptionPane.showInputDialog(this, "Wrong ISBN-10, try again: ", "ERROR", JOptionPane.ERROR_MESSAGE);
				}
			} 
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		myTable.setPreferredScrollableViewportSize(new Dimension(50, 50));
		myTable.setFillsViewportHeight(true);
		jspForUpdatePanel = new JScrollPane(myTable);
		updateToTableButton = new JButton("UPDATE TABLE");
		updateToTableButton.addActionListener(a);

		//insertButton.setLayout(new GridLayout());
		//insertToTableButton.setPreferredSize(new Dimension(150, 50));
		updateToTableButton.setAlignmentX(Component.BOTTOM_ALIGNMENT);
		updateToTableButton.setBackground(Color.GREEN);
		//updateToTableButton.setOpaque(true);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(jspForUpdatePanel, BorderLayout.NORTH);
		panel.add(updateToTableButton, BorderLayout.SOUTH);
		//insertPanel.getRootPane().setDefaultButton(insertToTableButton);
		panel.repaint();

		add(panel, BorderLayout.SOUTH);	
		setVisible(true);
		isbnToBeUpdated = "";
		return myTable;
		
	}

	public void clearInsertPanel(){
		panel.setVisible(false);
	}

	public void setFileChooseButton(){//file choose button
		fileChooseButton = new JButton("Choose a file ...");
		fileChooseButton.setPreferredSize(new Dimension(150, 50));
		fileChooseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(fileChooseButton);
		setVisible(true);
	}

	public void askForDatabaseNameUsernamePassword(){//username and password are asked and set at the login screen
		loginPanel.setLayout(new FlowLayout());

		JLabel userLabel = new JLabel("User");
		userLabel.setBounds(10, 10, 80, 25);
		loginPanel.add(userLabel);

		userText = new JTextField(20);
		userText.setBounds(100, 10, 160, 25);
		loginPanel.add(userText);

		JLabel passwordLabel = new JLabel("Password");
		//passwordLabel.setBounds(10, 40, 80, 25);
		loginPanel.add(passwordLabel);

		passwordText = new JPasswordField(20);
		//passwordText.setBounds(100, 40, 160, 25);
		loginPanel.add(passwordText);

		JLabel databaseLabel = new JLabel("Database Name");
		//userLabel.setBounds(10, 10, 20, 25);
		loginPanel.add(databaseLabel);

		databaseText = new JTextField(20);
		//databaseText.setBounds(100, 10, 160, 25);
		loginPanel.add(databaseText);

		connectButton = new JButton("Connect to database");
		//connectButton.setBounds(10, 80, 80, 25);
		loginPanel.add(connectButton);

		getRootPane().setDefaultButton(connectButton);
		add(loginPanel);
		setVisible(true);
	}

	public String getUserName(){
		//get rid of any space in the username
		return userText.getText().replaceAll("\\s+","");
	}

	public String getPassword(){
		String passText = new String(passwordText.getPassword());
		return passText;
	}

	public String getDatabaseName(){
		//get rid of any space in the database name
		return databaseText.getText().replaceAll("\\s+", "");
	}

	//set up the file chooser display
	public void setJFileChooser(DataBase myDatabase){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose a file to populate database ...");
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileNameExtensionFilter filter1 = new FileNameExtensionFilter("text file", "txt", "text");
		fileChooser.setFileFilter(filter1);
		int result = fileChooser.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION){
			File selectedFile = fileChooser.getSelectedFile();
			//process the selected file
			myDatabase.processFile(selectedFile, this);
		}
	}

	//set up the display panel 
	public void setDisplay(DataBase myDatabase) throws SQLException{
		String[] columnNames = {"ISBN-10", "Title Name", "Quantity", "Author Firstname", "Author Lastname", "Publisher" , "Year Published", "Date Entered", 
				"Date Purchased", "Date Last Modified", "Condition of Book", "Collectible Feature"};
		
		int numOfRows;
		ResultSet myRs = myDatabase.selection();
		myRs.last();
		numOfRows = myRs.getRow();
		//System.out.println("Number of rows: " + numOfRows);
		String[][] data = new String[numOfRows][12];
		int i = 0;
		myRs.beforeFirst();
		
		while(myRs.next()){
			for(int j = 0; j < 12; j++){
				switch(j){

				case 0:
					data[i][j] = myRs.getString("ISBN_10");
					break;
				case 1:
					data[i][j] = myRs.getString("TITLE");
					break;
				case 2:
					data[i][j] = Integer.toString(myRs.getInt("QUANTITY"));
					break;
				case 3:
					data[i][j] = myRs.getString("AUTHOR_FIRSTNAME");
					break;
				case 4:
					data[i][j] = myRs.getString("AUTHOR_LASTNAME");
					break;
				case 5:
					data[i][j] = myRs.getString("PUBLISHER");
					break;
				case 6:
					data[i][j] = myRs.getString("YEAR_PUBLISHED");
					break;
				case 7:
					data[i][j] = myRs.getString("DATE_ENTERED");
					break;
				case 8:
					data[i][j] = myRs.getString("DATE_PURCHASED");
					break;
				case 9:
					data[i][j] = myRs.getString("DATE_LAST_MODIFIED");
					break;
				case 10:
					data[i][j] = myRs.getString("CONDITION_OF_BOOK");
					break;
				case 11:
					data[i][j] = myRs.getString("COLLECTIBLE_FEATURES");
					break;
				default:
					break;
				}
			}
			i++;
		}
		myTable = new JTable(data, columnNames);
		myTable.setFillsViewportHeight(true);
		setLayout(new BorderLayout());
		jsp = new JScrollPane(myTable);
		jsp.setAlignmentX(Component.CENTER_ALIGNMENT);
		//myRs.close();
		add(jsp);	
		revalidate();
		repaint();
		setVisible(true);
	}

	//Get ready to insert to database with values come from the insert panel
	public void setInsert(DataBase myDatabase, JTable myTable, ActionListener a) throws NumberFormatException, SQLException, ParseException{

		int nRow = myTable.getRowCount();
		int nCol = myTable.getColumnCount();
		String[][] tableData = new String[nRow][nCol];

		for(int i = 0; i < nRow; i++){
			for(int j = 0; j < nCol; j++){
				tableData[i][j] = (String) myTable.getValueAt(i, j);
			}
		}
		if(tableData[0][2] == ""){
			myDatabase.insert1(tableData[0][0], tableData[0][1], 1, tableData[0][3], tableData[0][4], 
					tableData[0][5], tableData[0][6], tableData[0][7], tableData[0][8], tableData[0][9], this);
		} else {
			myDatabase.insert1(tableData[0][0], tableData[0][1], Integer.parseInt(tableData[0][2]), tableData[0][3], tableData[0][4], 
					tableData[0][5], tableData[0][6], tableData[0][7], tableData[0][8], tableData[0][9], this);
		}
		panel.setVisible(false);
		repaint();
		setVisible(true);
	}

	//Get ready to update to database with values come from the update panel
	public void setUpdate(DataBase myDatabase, JTable myTable, ActionListener a) throws NumberFormatException, SQLException, ParseException{

		int nRow = myTable.getRowCount();
		int nCol = myTable.getColumnCount();
		String[][] tableData = new String[nRow][nCol];

		for(int i = 0; i < nRow; i++){
			for(int j = 0; j < nCol; j++){
				tableData[i][j] = (String) myTable.getValueAt(i, j);
			}
		}
		myDatabase.update1(tableData[0][0], tableData[0][1], Integer.parseInt(tableData[0][2]), 
				tableData[0][3], tableData[0][4], tableData[0][5], tableData[0][6], tableData[0][7], 
				tableData[0][8], tableData[0][9], this);

		panel.setVisible(false);
		repaint();
		setVisible(true);
	}

	//show message if user name or password is incorrect
	public void showIncorrectUNorPWDialog(){
		JOptionPane.showMessageDialog(this, "Incorrect username or password", "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void clearGUI(){
		textArea.setText(null);
	}

}
