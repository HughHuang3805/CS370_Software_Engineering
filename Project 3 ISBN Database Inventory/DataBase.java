package Phase3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import javax.swing.JOptionPane;


public class DataBase {

	String driver = "com.mysql.jdbc.Driver";
	String url1 = "jdbc:mysql://localhost:3306/";
	//String url2 = "?autoReconnect=true&useSSL=false";
	String databaseName;
	String tableName;
	BufferedReader br;
	Connection conn;
	Properties properties = new Properties();

	/*establish connection with database with user inputting userName, password, and name of database*/
	public Connection getConnection(String userName, String password, String database, GUI myGui) throws Exception{//connection to the database
		try{
			/*these properties are set for getConnection(), better :)*/
			databaseName = database;
			properties.setProperty("user", userName);
			properties.setProperty("password", password);
			properties.setProperty("useSSL", "false");
			properties.setProperty("autoReconnect", "true");

			Class.forName(driver);
			/*try to connect to local host first*/
			conn = DriverManager.getConnection(url1, properties);

			/*if successful connecting to local host, create database 
			and table try creating below, if fails, go to catch*/
			createDatabase(myGui);
			createTable(myGui);

			/*establish connection with the newly created database*/
			conn = DriverManager.getConnection(url1 + database, properties);

			JOptionPane.showMessageDialog(myGui, "Successfully Connected.", "Connected", JOptionPane.INFORMATION_MESSAGE);
			System.out.println("Connected");	
			return conn;
		} catch(SQLException e){
			try{

				//if cannot create the base -> already exists, try to establish connection with it
				//if cannot establish connection, return null, this null will make sure login screen is repaint
				conn = DriverManager.getConnection(url1 + database, properties);

			} catch(Exception e2){
				return null;
			}

			//this shows up if database cannot be created -> already exists
			//then returns the connection established above
			JOptionPane.showMessageDialog(myGui, "Establishing connection to existing database...", "Info", JOptionPane.INFORMATION_MESSAGE);
			//System.out.println(e);
			return conn;
		} 
	}

	/*creates a database in client database*/
	public void createDatabase(GUI myGui){
		try{
		PreparedStatement createStatement = conn.prepareStatement("CREATE DATABASE " + myGui.getDatabaseName());
		createStatement.executeUpdate();
		JOptionPane.showMessageDialog(myGui, "Database created!", "Info", JOptionPane.INFORMATION_MESSAGE);
		System.out.println("Database created.");
		} catch(SQLException se){
			
		}
	}

	/*creates inventory table in client's database*/
	public void createTable(GUI myGui) throws SQLException{
		PreparedStatement createStatement = conn.prepareStatement("CREATE TABLE `" + myGui.getDatabaseName() + "`.`inventory` ("
				+ "`ISBN_10` VARCHAR(20) NOT NULL,"
				+ "`Title` VARCHAR(45) NULL DEFAULT 'N/A',"
				+ "`Quantity` INT NULL DEFAULT 1,"
				+ "`Author_Firstname` VARCHAR(45) NULL DEFAULT 'N/A',"
				+ "`Author_Lastname` VARCHAR(45) NULL DEFAULT 'N/A',"
				+ "`Publisher` VARCHAR(45) NULL DEFAULT 'N/A',"
				+ "`Year_Published` VARCHAR(45) NULL DEFAULT 'N/A',"
				+ "`Date_Entered` VARCHAR(45) NULL DEFAULT 'N/A',"
				+ "`Date_Purchased` VARCHAR(45) NULL DEFAULT 'N/A',"
				+ "`Date_Last_Modified` VARCHAR(45) NULL DEFAULT 'N/A',"
				+ "`Condition_of_Book` VARCHAR(45) NULL DEFAULT 'N/A',"
				+ "`Collectible_Features` VARCHAR(45) NULL DEFAULT 'N/A',"
				+ "PRIMARY KEY (`ISBN_10`),"
				+ "UNIQUE INDEX `ISBN_10_UNIQUE` (`ISBN_10` ASC));");
		createStatement.executeUpdate();
		System.out.println("Table created.");
	}

	/*selection from database*/
	public ResultSet selection() throws SQLException{
		PreparedStatement selectionStatement = conn.prepareStatement("select * from `" + databaseName + "`.inventory");
		ResultSet myRs = selectionStatement.executeQuery();
		return myRs;
	}

	public void insert(String isbn, GUI myGui){
		/*insert statement for a list of ISBN from input file*/
		PreparedStatement insertStatement;
		try {
			insertStatement = conn.prepareStatement("insert into `" + databaseName + "`.inventory "
					+ "(ISBN_10) values (?) on duplicate key update QUANTITY = QUANTITY + 1");
			insertStatement.setString(1, isbn);
			insertStatement.executeUpdate();

			//JOptionPane.showMessageDialog(myGui, insertStatement.toString());
			try {
				FileProcessing.writeToLog(insertStatement);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*insert to database, regular insert statement with all parameters included*/
	public void insert1(String isbn, String title, int quantity, String author_FirstName, String author_LastName, String publisher, 
			String year_Published, String date_Purchased, String condition_of_Book, 
			String collectible_Features, GUI myGui) throws SQLException, ParseException{

		PreparedStatement findStatement = conn.prepareStatement("select * from " + databaseName + ".inventory where ISBN_10 = ?");
		findStatement.setString(1, isbn);

		/*check if the specified record already exists, if it does, insert not allowed. Return.*/
		/*ResultSet myRS = findStatement.executeQuery();//check if a prodcut_ID is already existed in the table
		 * if(myRS.next()){
			JOptionPane.showMessageDialog(myGui, "ISBN " + myRS.getString(1) + " already exists, please double check", "WARNING", JOptionPane.WARNING_MESSAGE);
			return;
		}*/

		/*insert statement*/
		PreparedStatement insertStatement = conn.prepareStatement("insert into `" + databaseName + "`.inventory "
				+ "(ISBN_10, TITLE, QUANTITY, AUTHOR_FIRSTNAME, AUTHOR_LASTNAME, PUBLISHER, YEAR_PUBLISHED, DATE_ENTERED, DATE_PURCHASED, DATE_LAST_MODIFIED, CONDITION_OF_BOOK, COLLECTIBLE_FEATURES) "
				+ "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )"
				+ "on duplicate key update quantity = values(quantity) + 1");

		DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date today1 = Calendar.getInstance().getTime();
		String date_Entered = df1.format(today1);
		
		DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date today2 = Calendar.getInstance().getTime();
		String date_Last_Modified = df2.format(today2);
		
		insertStatement.setString(1, isbn);//insert each data item
		insertStatement.setString(2, title);
		insertStatement.setInt(3, quantity);
		insertStatement.setString(4, author_FirstName);
		insertStatement.setString(5, author_LastName);
		insertStatement.setString(6, publisher);
		insertStatement.setString(7, year_Published);
		insertStatement.setString(8, date_Entered);
		insertStatement.setString(9, date_Purchased);
		insertStatement.setString(10, date_Last_Modified);
		insertStatement.setString(11, condition_of_Book);
		insertStatement.setString(12, collectible_Features);	

		try {// writes to log
			FileProcessing.writeToLog(insertStatement);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		insertStatement.executeUpdate();
		//System.out.println("Insert Complete.");
	}
	
	/*update specified record, regular update with all parameters included*/
	public void update1(String isbn, String title, int quantity, String author_FirstName, String author_LastName, String publisher, 
			String year_Published, String date_Purchased, String condition_of_Book, String collectible_Features, GUI myGui) 
					throws SQLException, ParseException{

		
		PreparedStatement findStatement = conn.prepareStatement("select * from inventory where ISBN_10 = ?");
		findStatement.setString(1, isbn);
		ResultSet myRS = findStatement.executeQuery();//check if a prodcut_ID is already existed in the table

		if(myRS.next()){//check the record specified already exists
			int reply = JOptionPane.showConfirmDialog(myGui, "Confirm update ISBN-10 " + isbn + "?", "Confirm ...", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(reply == 0){
				//update employees set email = ? where idemployees = ?
				PreparedStatement insertStatement = conn.prepareStatement("update `" + databaseName + "`.inventory "
						+ "set TITLE = ?, QUANTITY = QUANTITY + 1, AUTHOR_FIRSTNAME = ?, AUTHOR_LASTNAME = ?, PUBLISHER = ?, " 
						+ "YEAR_PUBLISHED = ?, DATE_ENTERED = ?, DATE_PURCHASED = ?, DATE_LAST_MODIFIED = ?, CONDITION_OF_BOOK = ?, COLLECTIBLE_FEATURES = ? "
						+ "where ISBN_10 = ?");
				
				DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");//change current time to string
				Date today1 = Calendar.getInstance().getTime();
				String date_Entered = df1.format(today1);
				
				DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");//change current time to string
				Date today2 = Calendar.getInstance().getTime();
				String date_Last_Modified = df2.format(today2);
				
				insertStatement.setString(1, title);
				//insertStatement.setInt(2, quantity);
				insertStatement.setString(2, author_FirstName);
				insertStatement.setString(3, author_LastName);
				insertStatement.setString(4, publisher);
				insertStatement.setString(5, year_Published);
				insertStatement.setString(6, date_Entered);
				insertStatement.setString(7, date_Purchased);
				insertStatement.setString(8, date_Last_Modified);
				insertStatement.setString(9, condition_of_Book);
				insertStatement.setString(10, collectible_Features);	
				insertStatement.setString(11, isbn);
				
				try {//writes to log
					FileProcessing.writeToLog(insertStatement);
					//System.out.println("hi");
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				insertStatement.executeUpdate();
				
				JOptionPane.showMessageDialog(myGui, "Update complete.");
				//System.out.println("Insert Complete.");
				return;
			} else {
				JOptionPane.showMessageDialog(myGui, "Update cancelled.", "CANCELLED", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
	}

	public void update2(String isbn, String title, int quantity, String author_FirstName, String author_LastName, String publisher, 
			String year_Published, String date_Purchased, String condition_of_Book, String collectible_Features, GUI myGui) 
					throws SQLException, ParseException{

		
		PreparedStatement findStatement = conn.prepareStatement("select * from inventory where ISBN_10 = ?");
		findStatement.setString(1, isbn);
		ResultSet myRS = findStatement.executeQuery();//check if a prodcut_ID is already existed in the table

		if(myRS.next()){//check the record specified already exists
			int reply = JOptionPane.showConfirmDialog(myGui, "Confirm update ISBN-10 " + isbn + "?", "Confirm ...", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(reply == 0){
				//update employees set email = ? where idemployees = ?
				PreparedStatement insertStatement = conn.prepareStatement("update `" + databaseName + "`.inventory "
						+ "set TITLE = ?, QUANTITY = ?, AUTHOR_FIRSTNAME = ?, AUTHOR_LASTNAME = ?, PUBLISHER = ?, " 
						+ "YEAR_PUBLISHED = ?, DATE_ENTERED = ?, DATE_PURCHASED = ?, DATE_LAST_MODIFIED = ?, CONDITION_OF_BOOK = ?, COLLECTIBLE_FEATURES = ? "
						+ "where ISBN_10 = ?");
				
				DateFormat df1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date today1 = Calendar.getInstance().getTime();
				String date_Entered = df1.format(today1);
				
				DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
				Date today2 = Calendar.getInstance().getTime();
				String date_Last_Modified = df2.format(today2);
				
				//insertStatement.setString(1, isbn);
				insertStatement.setString(1, title);
				insertStatement.setInt(2, quantity);
				insertStatement.setString(3, author_FirstName);
				insertStatement.setString(4, author_LastName);
				insertStatement.setString(5, publisher);
				insertStatement.setString(6, year_Published);
				insertStatement.setString(7, date_Entered);
				insertStatement.setString(8, date_Purchased);
				insertStatement.setString(9, date_Last_Modified);
				insertStatement.setString(10, condition_of_Book);
				insertStatement.setString(11, collectible_Features);	
				insertStatement.setString(12, isbn);
				
				try {//writes to log
					FileProcessing.writeToLog(insertStatement);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				insertStatement.executeUpdate();
				return;
			} else {
				return;
			}
		}
	}
	
	/* - deletes record*/
	public boolean delete(String product_ID) throws SQLException{
		PreparedStatement deleteStatement = conn.prepareStatement("delete from inventory where ISBN_10 = ?");
		deleteStatement.setString(1, product_ID);
		
		try {// writes to log
			FileProcessing.writeToLog(deleteStatement);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int rowAffected = deleteStatement.executeUpdate();
		if(rowAffected == 0){
			return false;
		}
		
		System.out.println("Rows affected: " + rowAffected);
		System.out.println("Deletion Complete.");
		return true;
	}

	/* - processes input .txt file*/
	public void processFile(File inputFile, GUI myGui){
		try{
			br = new BufferedReader(new FileReader(inputFile));
			String line = "";
			while((line = br.readLine()) != null){
				insert(line, myGui);	
			}
			myGui.jsp.setVisible(false);//make initial jsp insert panel invisible
			myGui.setDisplay(this);//new jsp
			myGui.repaint();
			
		} catch(IOException e){
			System.out.println("Exception in process file." + e);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
