package milktea.milktea.DAO;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.Gender;
import milktea.milktea.DTO.Status;
import milktea.milktea.DTO.Unit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;

@Slf4j
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
        public static boolean openConnection() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL+DATABASE, DATABASE_USER, DATABASE_PASSWORD);
            return true;
        } catch (SQLException e) {
            log.error("Connection failed: ", e);
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
        assert is != null;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        // Use ";" as a delimiter for each request
        String line;
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
        log.error(String.valueOf(e));
    } finally {
        closeConnection();
    }
}
    public static Gender getGender(@NonNull String gender){
        if(gender.equals("MALE")){
            return Gender.MALE;
        } else if (gender.equals("FEMALE")) {
            return Gender.FEMALE;
        }
        return Gender.OTHER;
    }

    @NonNull
    public static Status getStatus(@NonNull String status) {
        return switch (status) {
            case "ACTIVE" -> Status.ACTIVE;
            case "INACTIVE" -> Status.INACTIVE;
            default -> throw new IllegalStateException("Unexpected value: " + status);
        };
    }

    @NonNull
    public static Unit getUnit(@NonNull String unit) {
        return switch (unit) {
            case "KILOGRAM" -> Unit.KILOGRAM;
            case "GRAM" -> Unit.GRAM;
            case "LITER" -> Unit.LITER;
            case "MILLILITER" -> Unit.MILLILITER;
            default -> throw new IllegalStateException("Unexpected value: " + unit);
        };
    }

    @SuppressWarnings("SqlSourceToSinkFlow")
    protected static boolean executeUpdate(String sql, Object... params) {
        boolean result = false;
        if (openConnection()) {
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
                result = stmt.executeUpdate() >= 1;
            } catch (SQLException e) {
                log.error("Error: ", e);
            } finally {
                closeConnection();
            }
        }
        return result;
    }
}
