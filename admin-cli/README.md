# Administrative Interface
<<<<<<< Updated upstream

To launch: DATABASE_URI="jdbc:postgresql://aws-0-us-west-1.pooler.supabase.com:5432/postgres?user=postgres.ixqgeaxiniwbvakpabpy&password=the-buzz-123" mvn exec:java

The supabase password is: "the-buzz-123"
=======
To launch: DATABASE_URI="jdbc:postgresql://aws-0-us-west-1.pooler.supabase.com:5432/postgres?user=postgres.ixqgeaxiniwbvakpabpy&password=the-buzz-123" mvn exec:java
The supabase password is: "the-buzz-123"

>>>>>>> Stashed changes

IDEAD FIELD:
This admin CLI program allows users to manage a PostgreSQL database through a command-line interface. Users can create, drop, insert, update, and delete rows in the database. It includes features like querying for specific rows or all rows, and updating or deleting them by ID. The CLI provides a simple way to interact with the database, ensuring efficient data handling through structured commands

TESTING:
The AppTest.java file is a unit test class designed to validate the functionality of the App.java file, which handles the command-line interface for interacting with the database. Using JUnit as the testing framework, AppTest.java includes tests that simulate user input to ensure that commands such as creating tables, querying rows, and inserting data behave as expected. It verifies that the application processes input correctly and returns the appropriate results, ensuring the reliability of the core CLI functionality.

DEBUGGING:
During the development of the admin CLI program, I actively contributed to testing the integration of different components, ensuring that the database interactions (creating, updating, querying, and deleting rows) worked seamlessly with the CLI interface. I identified and fixed bugs related to database connection issues, as well as mismatches in package structures. Additionally, I participated in diagnosing problems with Maven builds and addressed errors in the code, improving the overall functionality and ensuring the tests passed successfully.