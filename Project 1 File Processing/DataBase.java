package Phase1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

public class DataBase {

	String driver = "com.mysql.jdbc.Driver";
	String url1 = "jdbc:mysql://localhost:3305/";
	String url2 = "?autoReconnect=true&useSSL=false";
	String databaseName;
	String tableName;
	BufferedReader br;
	Connection conn;

	
	/*establish connection with database with user inputting userName, password, and name of database*/
	public Connection getConnection(String userName, String password, String database, GUI myGui) throws Exception{//connection to the database
		try{
			databaseName = database;
			String url3 = url1 + database + url2;
			Class.forName(driver);
			conn = DriverManager.getConnection(url3, userName, password);
			JOptionPane.showMessageDialog(myGui, "Successfully Connected.", "Connected", JOptionPane.INFORMATION_MESSAGE);

			System.out.println("Connected");	

			return conn;
		} catch(Exception e){
			System.out.println(e);
		}
		return null;
	}

	/*creates schema in client database, use is not recommended*/
	public void createDatabase() throws SQLException{
		PreparedStatement createStatement = conn.prepareStatement("create database store");
		createStatement.executeUpdate();
		System.out.println("Schema created.");
	}

	/*creates inventory table in client's database*/
	public void createTable() throws SQLException{
		PreparedStatement createStatement = conn.prepareStatement("CREATE TABLE `grocerystore`.`inventory` (" 
				+ "`product_ID` INT NOT NULL AUTO_INCREMENT,"
				+ "`product_Name` VARCHAR(45) NOT NULL,"
				+ "`selling_Price` DOUBLE NOT NULL,"
				+ "`threshhold_Quantity` INT NOT NULL,"
				+ "`cost_Paid` DOUBLE NOT NULL,"
				+ "`previous_Ordered_Date` DATE NOT NULL,"
				+ "`quantity` INT NOT NULL,"
				+ "PRIMARY KEY (`product_ID`))");
		createStatement.executeUpdate();
		System.out.println("Table created.");
	}

	/*selection from database*/
	public ResultSet selection() throws SQLException{
		PreparedStatement selectionStatement = conn.prepareStatement("select * from inventory");
		ResultSet myRs = selectionStatement.executeQuery();
		return myRs;
	}

	/* - insert to database
	 * - expect user to input the right type (will be enhanced in the future)*/
	public void insert(int product_ID, String product_Name, double selling_Price, int threshhold_Quantity, double cost_Paid, String previous_Ordered_Date, int quantity, GUI myGui) 
			throws SQLException, ParseException{
		
		PreparedStatement findStatement = conn.prepareStatement("select * from inventory where product_ID = ?");
		findStatement.setInt(1, product_ID);
		ResultSet myRS = findStatement.executeQuery();//check if a prodcut_ID is already existed in the table
		
		if(myRS.next()){//check if specified record already exists
			int reply = JOptionPane.showConfirmDialog(myGui, "Record " + myRS.getInt(1) + " already exists, insert anyway?", "Confirm ...", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if(reply == 0){
				PreparedStatement insertStatement = conn.prepareStatement("insert into `" + databaseName + "`.inventory "
						+ "(product_ID, product_Name, selling_Price, threshhold_Quantity, cost_Paid, previous_Ordered_Date, quantity) "
						+ "values (?, ?, ?, ?, ?, ?, ?)"
						+ "on duplicate key update product_ID = values(product_ID), product_Name = values(product_Name), "
						+ "selling_Price = values(selling_Price), threshhold_Quantity = values(threshhold_Quantity), "
						+ "cost_Paid = values(cost_Paid), previous_Ordered_Date = values(previous_Ordered_Date), quantity = values(quantity)");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
				LocalDate date = LocalDate.parse(previous_Ordered_Date, formatter);
				Date newDate  = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
				java.sql.Date sql_StartDate = new java.sql.Date(newDate.getTime() );

				insertStatement.setInt(1, product_ID);
				insertStatement.setString(2, product_Name);
				insertStatement.setDouble(3, selling_Price);
				insertStatement.setInt(4, threshhold_Quantity);
				insertStatement.setDouble(5, cost_Paid);
				insertStatement.setDate(6, sql_StartDate);
				insertStatement.setInt(7, quantity);
				insertStatement.executeUpdate();
				JOptionPane.showMessageDialog(myGui, "Insert complete.");
				//System.out.println("Insert Complete.");
				return;
			} else {
				JOptionPane.showMessageDialog(myGui, "Insert cancelled.", "CANCELLED", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
		PreparedStatement insertStatement = conn.prepareStatement("insert into `" + databaseName + "`.inventory "
				+ "(product_ID, product_Name, selling_Price, threshhold_Quantity, cost_Paid, previous_Ordered_Date, quantity) "
				+ "values (?, ?, ?, ?, ?, ?, ?)"
				+ "on duplicate key update product_ID = values(product_ID), product_Name = values(product_Name), "
				+ "selling_Price = values(selling_Price), threshhold_Quantity = values(threshhold_Quantity), "
				+ "cost_Paid = values(cost_Paid), previous_Ordered_Date = values(previous_Ordered_Date), quantity = values(quantity)");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		LocalDate date = LocalDate.parse(previous_Ordered_Date, formatter);
		Date newDate  = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		java.sql.Date sql_StartDate = new java.sql.Date(newDate.getTime() );

		insertStatement.setInt(1, product_ID);
		insertStatement.setString(2, product_Name);
		insertStatement.setDouble(3, selling_Price);
		insertStatement.setInt(4, threshhold_Quantity);
		insertStatement.setDouble(5, cost_Paid);
		insertStatement.setDate(6, sql_StartDate);
		insertStatement.setInt(7, quantity);
		insertStatement.executeUpdate();
		JOptionPane.showMessageDialog(myGui, "Insert complete.");
		//System.out.println("Insert Complete.");
	}
	
	/* - update specified record
	 * - expect user to input the right data type(will be enhanced in the future)*/
	public void update(int product_ID, String product_Name, double selling_Price, int threshhold_Quantity, double cost_Paid, String previous_Ordered_Date, int quantity, GUI myGui) 
			throws SQLException, ParseException{
		
		PreparedStatement findStatement = conn.prepareStatement("select * from inventory where product_ID = ?");
		findStatement.setInt(1, product_ID);
		ResultSet myRS = findStatement.executeQuery();//check if a prodcut_ID is already existed in the table
		
		if(myRS.next()){//check the record specified already exists
			int reply = JOptionPane.showConfirmDialog(myGui, "Confirm update?", "Confirm ...", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			if(reply == 0){
				PreparedStatement insertStatement = conn.prepareStatement("insert into `" + databaseName + "`.inventory "
						+ "(product_ID, product_Name, selling_Price, threshhold_Quantity, cost_Paid, previous_Ordered_Date, quantity) "
						+ "values (?, ?, ?, ?, ?, ?, ?)"
						+ "on duplicate key update product_ID = values(product_ID), product_Name = values(product_Name), "
						+ "selling_Price = values(selling_Price), threshhold_Quantity = values(threshhold_Quantity), "
						+ "cost_Paid = values(cost_Paid), previous_Ordered_Date = values(previous_Ordered_Date), quantity = values(quantity)");
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
				LocalDate date = LocalDate.parse(previous_Ordered_Date, formatter);
				Date newDate  = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
				java.sql.Date sql_StartDate = new java.sql.Date(newDate.getTime() );

				insertStatement.setInt(1, product_ID);
				insertStatement.setString(2, product_Name);
				insertStatement.setDouble(3, selling_Price);
				insertStatement.setInt(4, threshhold_Quantity);
				insertStatement.setDouble(5, cost_Paid);
				insertStatement.setDate(6, sql_StartDate);
				insertStatement.setInt(7, quantity);
				insertStatement.executeUpdate();
				JOptionPane.showMessageDialog(myGui, "Update complete.");
				//System.out.println("Insert Complete.");
				return;
			} else {
				JOptionPane.showMessageDialog(myGui, "Update cancelled.", "CANCELLED", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}
		PreparedStatement insertStatement = conn.prepareStatement("insert into `" + databaseName + "`.inventory "
				+ "(product_ID, product_Name, selling_Price, threshhold_Quantity, cost_Paid, previous_Ordered_Date, quantity) "
				+ "values (?, ?, ?, ?, ?, ?, ?)"
				+ "on duplicate key update product_ID = values(product_ID), product_Name = values(product_Name), "
				+ "selling_Price = values(selling_Price), threshhold_Quantity = values(threshhold_Quantity), "
				+ "cost_Paid = values(cost_Paid), previous_Ordered_Date = values(previous_Ordered_Date), quantity = values(quantity)");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
		LocalDate date = LocalDate.parse(previous_Ordered_Date, formatter);
		Date newDate  = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		java.sql.Date sql_StartDate = new java.sql.Date(newDate.getTime() );

		insertStatement.setInt(1, product_ID);
		insertStatement.setString(2, product_Name);
		insertStatement.setDouble(3, selling_Price);
		insertStatement.setInt(4, threshhold_Quantity);
		insertStatement.setDouble(5, cost_Paid);
		insertStatement.setDate(6, sql_StartDate);
		insertStatement.setInt(7, quantity);
		insertStatement.executeUpdate();
		JOptionPane.showMessageDialog(myGui, "Insert complete.");
		//System.out.println("Insert Complete.");
	}
	
	/* - deletes record*/
	public boolean delete(int product_ID) throws SQLException{
		PreparedStatement deleteStatement = conn.prepareStatement("delete from inventory where product_ID = ?");
		deleteStatement.setInt(1, product_ID);
		int rowAffected = deleteStatement.executeUpdate();
		if(rowAffected == 0){
			return false;
		}
		System.out.println("Rows affected: " + rowAffected);
		System.out.println("Deletion Complete.");
		return true;
	}

	/* - processes input CSV file*/
	public void processFile(File inputFile, GUI myGui){
		try{
			br = new BufferedReader(new FileReader(inputFile));
			String line = "";
			String splitter = ",";
			line = br.readLine();
			while((line = br.readLine()) != null){
				String[] data = line.split(splitter);
				insert(Integer.parseInt(data[0]), data[1], Double.parseDouble(data[2]), Integer.parseInt(data[3]), Double.parseDouble(data[4]), data[5], Integer.parseInt(data[6]), myGui);			
			}
			
		} catch(IOException e){
			System.out.println("Exception in process file." + e);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
