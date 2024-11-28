package milktea.milktea.DTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@ToString(callSuper = true)
public class Customer extends Person {
    private BigDecimal point; // Point

    public Customer(Customer customer) {
        super(customer);
        this.point = customer.point;
    }
}
