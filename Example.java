// A simple JDBC example.

// Remember that you need to put the jdbc postgresql driver in your class path
// when you run this code.
// See /local/packages/jdbc-postgresql on cdf for the driver, another example
// program, and a how-to file.

// To compile and run this program on cdf:
// (1) Compile the code in Example.java.
//     javac Example
// This creates the file Example.class.
// (2) Run the code in Example.class.
// Normally, you would run a Java program whose main method is in a class 
// called Example as follows:
//     java Example
// But we need to also give the class path to where JDBC is, so we type:
//     java -cp /local/packages/jdbc-postgresql/postgresql-9.4.1212.jar: Example
// Alternatively, we can set our CLASSPATH variable in linux.  (See
// /local/packages/jdbc-postgresql/HelloPostgresql.txt on cdf for how.)

import java.sql.*;
import java.io.*;

class Example {
    
    public static void main(String args[]) throws IOException
        {
            String url;
            Connection conn;
            PreparedStatement pStatement;
            
            String queryString;

            try {
                Class.forName("org.postgresql.Driver");
            }
            catch (ClassNotFoundException e) {
                System.out.println("Failed to find the JDBC driver");
            }
            try
            {
                //Connect to database csc343h-leetsz9
                url = "jdbc:postgresql://localhost:5432/csc343h-leetsz9";
                conn = DriverManager.getConnection(url, "leetsz9", "");
                
                //Query #1 
                queryString = "select guess from guesses where age >= ?";
                PreparedStatement ps = conn.prepareStatement(queryString);

                //Prompt user to enter an age
                BufferedReader br = new BufferedReader(new 
                      InputStreamReader(System.in));
                System.out.println("Enter an age? ");
                String age = br.readLine();
                int int_age = Integer.parseInt(age); 
               
                // Insert that age as int into the PreparedStatement and execute it.
                ps.setInt(1, int_age);
                System.out.println(ps);
                ResultSet rs = ps.executeQuery();

                // Iterate through the result to find the average
                int sum = 0;
                int count = 0;
                while (rs.next()) {
                    int guess = rs.getInt("guess");
                    sum = sum + guess;
                    count++;
                }
                System.out.println("The average guess is: " + sum/count);
                
                queryString = "select count(distinct name) from guesses";
                PreparedStatement ps2 = conn.prepareStatement(queryString, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs2 = ps2.executeQuery();
                
                int num_of_names = 0;
                while (rs2.next()) {                	
                		num_of_names = rs2.getInt("count");
                }
                              
                System.out.println("Number of names: " + num_of_names);
                
                queryString = "select distinct name from guesses";
                PreparedStatement ps3 = conn.prepareStatement(queryString);
                ResultSet rs3 = ps3.executeQuery();
                
                int position = 0;
                String[] names = new String[num_of_names];
                while (rs3.next()) {
                		names[position] = rs3.getString("name");
                }
                System.out.println(names);
            }
            catch (SQLException se)
            {
                System.err.println("SQL Exception." +
                        "<Message>: " + se.getMessage());
            }

        }
        
}
