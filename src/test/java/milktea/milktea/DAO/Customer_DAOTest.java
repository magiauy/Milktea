package milktea.milktea.DAO;

import milktea.milktea.DTO.Customer;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.*;

class Customer_DAOTest {

    @Test
    void getAllCustomer() {
        Customer_DAO customer_dao = new Customer_DAO();
        Customer firstCustomer = customer_dao.getAllCustomer().getFirst();
        assertNotNull(firstCustomer.toString()); // Ensure toString() is not null
        System.out.println(firstCustomer);
    }


}