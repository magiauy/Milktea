package milktea.milktea.DTO;

import org.junit.jupiter.api.Test;
import milktea.milktea.DTO.Gender;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void builder() {
        Customer customer = Customer.builder()
                .id("KH001")
                .firstName("Nguyen Van")
                .lastName("A")
                .gender(Gender.MALE)
                .phoneNumber("0123456789")
                .point(BigDecimal.valueOf(100))
                .build();

        assertEquals("KH001", customer.getId());
        assertEquals("Nguyen Van", customer.getFirstName());
        assertEquals("A", customer.getLastName());
        assertEquals("0123456789", customer.getPhoneNumber());
        assertEquals(BigDecimal.valueOf(100), customer.getPoint());
        assertEquals(Gender.MALE, customer.getGender());
    }
    @Test
    void EqualsAndHashCode() {
        Customer customer1 = Customer.builder()
                .id("KH001")
                .firstName("Nguyen Van")
                .lastName("A")
                .gender(Gender.MALE)
                .phoneNumber("0123456789")
                .point(BigDecimal.valueOf(100))
                .build();

        Customer customer2 = Customer.builder()
                .id("KH001")
                .firstName("Nguyen Van")
                .lastName("A")
                .gender(Gender.MALE)
                .phoneNumber("0123456789")
                .point(BigDecimal.valueOf(100))
                .build();

        assertEquals(customer1, customer2);
        assertEquals(customer1.hashCode(), customer2.hashCode());
    }
}