package Phase1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends JFrame{

	private static final long serialVersionUID = 1L;
	JMenuItem[] menuItems = new JMenuItem[5];
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
	JMenuItem item1, item2, item3, item4, item5;
	JMenu menu1;
	JOptionPane incorrectMessage = new JOptionPane();
	GridLayout myGrid = new GridLayout(3,3);

	public GUI(){
		setTitle("Grocery Inventory");
		setSize(1150, 700);
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

	public void setMenuItems(){
		JMenuBar menuBar = new JMenuBar();
		menu1 = new JMenu("Functions");

		item1 = new JMenuItem("Choose a file");
		item2 = new JMenuItem("Insert");
		item3 = new JMenuItem("Update");
		item4 = new JMenuItem("Delete");
		item5 = new JMenuItem("Exit");

		//menuItems[0] = item1;
		menuItems[0] = item1;
		menuItems[1] = item2;
		menuItems[2] = item3;
		menuItems[3] = item4;
		menuItems[4] = item5;

		menu1.add(item1);
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

	public void setButtonListener(ActionListener a){
		//fileChooseButton.addActionListener(a);
		connectButton.addActionListener(a);
		for(JMenuItem x : menuItems){
			x.addActionListener(a);
		}
	}

	public JTable setInsertPanel(ActionListener a){
		String[] columnNames = {"Product ID", "Product Name", "Selling Price", "Threshhold Quantity", "Cost Paid", "Previous Order Date" , "Quantity"};
		String[][] data = {{"", "", "", "", "", "", ""}};
		JTable myTable = new JTable(data, columnNames);
		//myTable.setModel(new DefaultTableModel());
		//myTable.setSize(100, 50);
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

	public JTable setUpdatePanel(ActionListener a){
		String[] columnNames = {"Product ID", "Product Name", "Selling Price", "Threshhold Quantity", "Cost Paid", "Previous Order Date" , "Quantity"};
		String[][] data = {{"", "", "", "", "", "", ""}};
		JTable myTable = new JTable(data, columnNames);
		//myTable.setModel(new DefaultTableModel());
		//myTable.setSize(100, 50);
		myTable.setPreferredScrollableViewportSize(new Dimension(50,50));
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
		return myTable;
	}

	public void clearInsertPanel(){
		panel.setVisible(false);
	}

	/*public void displayResult(Map<String, Integer> map){
		for(String key : map.keySet()){
			textArea.append(key + ": " + map.get(key) + "\n");
		}
	}*/

	public void setFileChooseButton(){
		fileChooseButton = new JButton("Choose a file ...");
		fileChooseButton.setPreferredSize(new Dimension(150, 50));
		fileChooseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(fileChooseButton);
		setVisible(true);
	}

	public void setConnectButton(){
		connectButton = new JButton("Click here to connect to a database ...");
		connectButton.setPreferredSize(new Dimension(300, 50));
		connectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(connectButton);
		setVisible(true);
	}

	public void askForDatabaseNameUsernamePassword(){
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
		return userText.getText().replaceAll("\\s+","");
	}

	public String getPassword(){
		String passText = new String(passwordText.getPassword());
		return passText;
	}

	public String getDatabaseName(){
		return databaseText.getText().replaceAll("\\s+", "");
	}

	public void setJFileChooser(DataBase myDatabase){
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Choose a file to populate data ...");
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		FileNameExtensionFilter filter1 = new FileNameExtensionFilter("csv file", "csv");
		fileChooser.setFileFilter(filter1);
		int result = fileChooser.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION){
			File selectedFile = fileChooser.getSelectedFile();
			myDatabase.processFile(selectedFile, this);
			//do something after selecting file
		}
	}

	public void setDisplay(DataBase myDatabase) throws SQLException{
		String[] columnNames = {"Product ID", "Product Name", "Selling Price", "Threshhold Quantity", "Cost Paid", "Previous Order Date" , "Quantity"};
		int numOfRows;
		ResultSet myRs = myDatabase.selection();
		myRs.last();
		numOfRows = myRs.getRow();
		System.out.println(numOfRows);
		String[][] data = new String[numOfRows][7];
		int i = 0;
		myRs.beforeFirst();
		while(myRs.next()){
			for(int j = 0; j < 7; j++){
				switch(j){

				case 0:
					data[i][j] = Integer.toString(myRs.getInt("product_ID"));
					break;
				case 1:
					data[i][j] = myRs.getString("product_Name");
					break;
				case 2:
					data[i][j] = Double.toString(myRs.getDouble("selling_Price"));
					break;
				case 3:
					data[i][j] = Integer.toString(myRs.getInt("threshhold_Quantity"));
					break;
				case 4:
					data[i][j] = Double.toString(myRs.getDouble("cost_Paid"));
					break;
				case 5:
					Date date = myRs.getDate("previous_Ordered_Date");
					DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					String text = df.format(date);
					data[i][j] = text;
					break;
				case 6:
					data[i][j] = Integer.toString(myRs.getInt("quantity"));
					break;
				default:
					break;
				}
			}
			i++;
		}
		myTable = new JTable(data, columnNames);
		//myTable.setPreferredSize(new Dimension(500, 500));
		//myTable.setPreferredScrollableViewportSize(new Dimension(500,500));
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

	public void setInsert(DataBase myDatabase, JTable myTable, ActionListener a) throws NumberFormatException, SQLException, ParseException{

		int nRow = myTable.getRowCount();
		int nCol = myTable.getColumnCount();
		String[][] tableData = new String[nRow][nCol];

		for(int i = 0; i < nRow; i++){
			for(int j = 0; j < nCol; j++){
				tableData[i][j] = (String) myTable.getValueAt(i, j);
			}
		}
		myDatabase.insert(Integer.parseInt(tableData[0][0]), tableData[0][1], Double.parseDouble(tableData[0][2]), Integer.parseInt(tableData[0][3]), Double.parseDouble(tableData[0][4]), tableData[0][5], Integer.parseInt(tableData[0][6]), this);

		panel.setVisible(false);
		repaint();
		setVisible(true);
	}

	public void setUpdate(DataBase myDatabase, JTable myTable, ActionListener a) throws NumberFormatException, SQLException, ParseException{

		int nRow = myTable.getRowCount();
		int nCol = myTable.getColumnCount();
		String[][] tableData = new String[nRow][nCol];

		for(int i = 0; i < nRow; i++){
			for(int j = 0; j < nCol; j++){
				tableData[i][j] = (String) myTable.getValueAt(i, j);
			}
		}
		myDatabase.update(Integer.parseInt(tableData[0][0]), tableData[0][1], Double.parseDouble(tableData[0][2]), Integer.parseInt(tableData[0][3]), Double.parseDouble(tableData[0][4]), tableData[0][5], Integer.parseInt(tableData[0][6]), this);

		panel.setVisible(false);
		repaint();
		setVisible(true);
	}

	public void showIncorrectUNorPWDialog(){
		JOptionPane.showMessageDialog(this, "Incorrect username, password, database name, or database is not running try again.", "Error", JOptionPane.ERROR_MESSAGE);
	}

	public void clearGUI(){
		textArea.setText(null);
	}

}
