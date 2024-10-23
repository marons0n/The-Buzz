# Administrative Interface
<<<<<<< Updated upstream

To launch: DATABASE_URI="jdbc:postgresql://aws-0-us-west-1.pooler.supabase.com:5432/postgres?user=postgres.ixqgeaxiniwbvakpabpy&password=the-buzz-123" mvn exec:java

The supabase password is: "the-buzz-123"
=======
To launch: DATABASE_URI="jdbc:postgresql://aws-0-us-west-1.pooler.supabase.com:5432/postgres?user=postgres.ixqgeaxiniwbvakpabpy&password=the-buzz-123" mvn exec:java
The supabase password is: "the-buzz-123"

>>>>>>> Stashed changes

Idea Field: This admin CLI lets users manage a database table via commands. Users can create tables, add, update, query, and delete rows easily through the interface. It makes database tasks simpler with structured commands.

Unit Testing: AppTest.java uses JUnit to test the commands in App.java. It checks if creating tables, querying, and inserting data work properly by simulating user input by adding invalid commands to then make the user to try again.

Debugging: I helped fix database connection errors, tested functionality, and solved issues with Maven builds.