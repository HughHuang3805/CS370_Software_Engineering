ISBN Database Inventory


Requirement:
- you should already have a datase created in MySQL
- provide the name of this database
- database of a "localhost"
- when doing insert or update, all columns must be filled (will be enhanced in the future)
- when inserting, data format MUST strictly follow "yyyy-mm-dd"
- must have jdbc installed

How to use the program:
- On the login screen of the program provide the name of the database along with the username and password 
- It will automatically create the necessary table for this grocery store ivnentory by going to the constructor of 
  Controller class and uncomment "myDatabase.createTable()"
- If command line arguments are included at the time of running the program, the program will process the ISBN in the
  input file, creates a file containing all the html lines and starts to retrieve information from them. Once finished,
  information corresponding to the ISBN will be added to the database
- If command line arguments are included at the time of running the program, the program will output a file containing
  all the data entries from the database
- Choose a file
  choose a file to analyze the ISBN and store information to the database based on amazon.com
- Output to file
  outputs the data entries to a name specified .txt file
- Insert
  insert a new entry to the database
- Update
  update an existing entry in the database
- Delete
  delete a row from the database
- Exit
  exit the program
- Given an ISBN, the program goes to the corresponding amazon.com, downloads that particular amazon website, then reads it
  to retrieve appropriate information

Features:
- shows the inventory 
- insert, delete, update, file choosing, file output
- every time a transaction is executed, it will log to "Transactional Log.txt" file