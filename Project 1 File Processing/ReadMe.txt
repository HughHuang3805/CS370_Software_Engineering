Grocery Store Database Inventory


Requirement:
- you should already have a datase created in MySQL
- provide the name of this database
- database of a "localhost"
- when doing insert or update, all columns must be filled (will be enhanced in the future)
- when inserting, data format MUST strictly follow "yyyy-mm-dd"


How to use the program:
- on the login screen of the program provide the name of the database along with the username and password 
- (FOR FIRST TIME USE ONLY) create the necessary table for this grocery store ivnentory by going to the constructor of 
  Controller class and uncomment "myDatabase.createTable()". After the first use, comment out "myDatabase.createTable()"
  again since tables are already created in the database and it might cause exceptions if left alone. (New ability will 
  be added in the future for creating tables dynamically.
- once established connection with the data, a filechooser will show up, and you have the ability to select a CSV file to
  populate the database with
- you can also skip the file choosing option and go into the program and choose "Functions" including "Insert", "Update",
  "Delete", "Find"(will be added in the future)
- initially, you can use the Book1.csv provided to populate the database


Features:
- shows the inventory 
- insert, delete, update, find(will be added) records
