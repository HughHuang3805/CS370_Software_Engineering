Grocery Store Database Inventory

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
- no command line input file is added nor outputfile is added (will add this feature in the future)

Features:
- shows the inventory 
- insert, delete, update, find(will be added) records