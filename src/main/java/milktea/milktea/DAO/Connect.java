package milktea.milktea.DAO;

import milktea.milktea.DTO.Gender;
import milktea.milktea.DTO.Role;
import milktea.milktea.DTO.Status;
import milktea.milktea.DTO.Unit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

public class Connect {

    private static final String DATABASE = "milktea";
    private static final String DATABASE_URL = "jdbc:mysql://localhost/";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "";
    protected static Connection connection = null;

    public static boolean openConnection(String name) {
        try {
            connection = DriverManager.getConnection(DATABASE_URL+DATABASE, DATABASE_USER, DATABASE_PASSWORD);
            System.out.println("Connected to the "+name+" database successfully!");
            return true;
        } catch (SQLException e) {
            System.out.println("Connection to the "+ name + "database failed: " + e.getMessage());
            return false;
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Failed to close connection: " + e.getMessage());
        }
    }

    public boolean checkExistDatabase() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL+DATABASE, DATABASE_USER, DATABASE_PASSWORD);
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW DATABASES");
            while(rs.next()) {
                if(DATABASE.equals(rs.getString(1))) {
                     return true;
                }
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }
    public void createDatabase() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            if (!checkExistDatabase()) {
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("CREATE DATABASE " + DATABASE);
                loadSQLFile("/milktea/milktea/DAO/"+DATABASE+".sql");
                System.out.println("Created database " + DATABASE);
            }
        } catch (SQLException ignored) {
        }
    }
public void loadSQLFile(String filePath) {
    try {
        // Create a Statement
        Statement stmt = connection.createStatement();

        // Read the SQL script
        InputStream is = getClass().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        // Use ";" as a delimiter for each request
        String line = "";
        StringBuilder sb = new StringBuilder();
        while((line = reader.readLine()) != null) {
            // Ignore SQL comments
            if(line.startsWith("--") || line.startsWith("//") || line.startsWith("/*")) {
                continue;
            }
            sb.append(line);
            // If the line ends with a ";", execute it
            if(line.endsWith(";")) {
                stmt.execute(sb.toString());
                sb = new StringBuilder();
            }
        }
        // Close the reader
        reader.close();

        // Close the statement
        stmt.close();
    } catch (IOException | SQLException e) {
        System.out.println(e);
    } finally {
        closeConnection();
    }
}
    public static Gender getGender(String gender){
        if(gender.equals("MALE")){
            return Gender.MALE;
        } else if (gender.equals("FEMALE")) {
            return Gender.FEMALE;
        }
        return Gender.OTHER;
    }
        public static Role getRole(String role) {
        return switch (role) {
            case "ADMIN" -> Role.ADMIN;
            case "STAFF" -> Role.MANAGER;
            case "EMPLOYEE" -> Role.EMPLOYEE;
            default -> null;
        };
    }
    public static Status getStatus(String status) {
        return switch (status) {
            case "ACTIVE" -> Status.ACTIVE;
            case "INACTIVE" -> Status.INACTIVE;
            default -> null;
        };
    }
    public Status getStatutorily(String status){
        return switch (status) {
            case "ACTIVE" -> Status.ACTIVE;
            case "INACTIVE" -> Status.INACTIVE;
            case "DISPOSED" -> Status.DISPOSED;
            case "OUTDATED" -> Status.OUTDATED;
            default -> null;
        };
    }
    public static Unit getUnit(String unit) {
        return switch (unit) {
            case "KILOGRAM" -> Unit.KILOGRAM;
            case "GRAM" -> Unit.GRAM;
            case "LITER" -> Unit.LITER;
            case "MILLILITER" -> Unit.MILLILITER;
            default -> null;
        };
    }
}
