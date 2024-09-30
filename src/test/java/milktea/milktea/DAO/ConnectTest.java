package milktea.milktea.DAO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConnectTest {

    @Test
    void checkExistTable() {
        Connect connect = new Connect();
        connect.createDatabase();
        assertNotNull(connect.connection);
    }
}