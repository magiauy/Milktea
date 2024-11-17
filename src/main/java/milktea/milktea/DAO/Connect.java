package milktea.milktea.DAO;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.Gender;
import milktea.milktea.DTO.MySQLConfig;
import milktea.milktea.DTO.Status;
import milktea.milktea.DTO.Unit;

import java.io.*;
import java.sql.*;

@Slf4j
public class Connect {

    private static String DATABASE ;
    private static String DATABASE_URL ;
    private static String DATABASE_USER ;
    private static String DATABASE_PASSWORD = "";
    protected static Connection connection = null;
    private static final String filePath = "connect.json";

    static {
        loadConfig();
    }

public static boolean loadConfig() {
    ObjectMapper objectMapper = new ObjectMapper();
    InputStream is = null;
    try {
        // Try to load the configuration from the resource path
        is = Connect.class.getClassLoader().getResourceAsStream(filePath);
        if (is == null) {
            log.error("Resource not found: {}", filePath);
            return false;
        }
        JsonNode rootNode = objectMapper.readTree(is);
        JsonNode mysqlNode = rootNode.path("mysql");
        MySQLConfig config = objectMapper.treeToValue(mysqlNode, MySQLConfig.class);
        DATABASE = config.getDatabase();
        DATABASE_URL = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/";
        DATABASE_USER = config.getUser();
        DATABASE_PASSWORD = config.getPassword();
        connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        closeConnection();
        return true;
    } catch (IOException | SQLException e) {
        log.error("Failed to load database configuration from resource, trying external file", e);
        try {
            // If resource is not found or connection fails, try to load from an external file
            File externalFile = new File(filePath);
            if (externalFile.exists()) {
                is = new FileInputStream(externalFile);
                JsonNode rootNode = objectMapper.readTree(is);
                JsonNode mysqlNode = rootNode.path("mysql");
                MySQLConfig config = objectMapper.treeToValue(mysqlNode, MySQLConfig.class);
                DATABASE = config.getDatabase();
                DATABASE_URL = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/";
                DATABASE_USER = config.getUser();
                DATABASE_PASSWORD = config.getPassword();
                connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
                closeConnection();
                return true;
            } else {
                log.error("External file not found: {}", filePath);
                return false;
            }
        } catch (IOException | SQLException ex) {
            log.error("Failed to load database configuration from external file", ex);
            return false;
        }
    } finally {
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                log.error("Failed to close InputStream", e);
            }
        }
    }
}

    public static boolean checkConnection(MySQLConfig config) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/", config.getUser(), config.getPassword());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
    public static void updateConfig(MySQLConfig newConfig) {
        ObjectMapper objectMapper = new ObjectMapper();
        try (InputStream is = Connect.class.getClassLoader().getResourceAsStream(filePath)) {
            if (is == null) {
                log.error("Resource not found: " + filePath);
                return;
            }
            JsonNode rootNode = objectMapper.readTree(is);
            JsonNode mysqlNode = rootNode.path("mysql");
            DATABASE_URL = "jdbc:mysql://" + newConfig.getHost() + ":" + newConfig.getPort() + "/";
            DATABASE_USER = newConfig.getUser();
            DATABASE_PASSWORD = newConfig.getPassword();
            ((ObjectNode) mysqlNode).put("host", newConfig.getHost());
            ((ObjectNode) mysqlNode).put("port", newConfig.getPort());
            ((ObjectNode) mysqlNode).put("user", newConfig.getUser());
            ((ObjectNode) mysqlNode).put("password", newConfig.getPassword());

            try (OutputStream os = new FileOutputStream(filePath)) {
                objectMapper.writeValue(os, rootNode);
            }
        } catch (IOException e) {
            log.error("Failed to update database configuration", e);
        }
    }

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

    public static boolean checkExistDatabase() {
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
    public static void createDatabase() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
            if (!checkExistDatabase()) {
                Statement stmt = connection.createStatement();
                stmt.executeUpdate("CREATE DATABASE " + DATABASE);
                String sql = "SET GLOBAL max_allowed_packet = 16 * 1024 * 1024"; // 16MB
                stmt.execute(sql);
                closeConnection();
                loadSQLFile("/milktea/milktea/DAO/"+DATABASE+".sql");
                loadSQLFile("/milktea/milktea/DAO/"+DATABASE+"2.sql");

                System.out.println("Created database " + DATABASE);
            }
        } catch (SQLException ignored) {
        }
    }
public static void loadSQLFile(String filePath) {
    openConnection();
    try {
        // Create a Statement
        Statement stmt = connection.createStatement();

        // Read the SQL script
        InputStream is = Connect.class.getResourceAsStream(filePath);
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
                return false;
            } finally {
                closeConnection();
            }
        }
        return result;
    }

}
