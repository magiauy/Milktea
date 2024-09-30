package milktea.milktea.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {
    private String inventoryId; // Inventory ID
    private String ingredientId; // Ingredient ID
    private double quantityInStock; // Quantity in stock
    private LocalDate lastUpdatedDate; // Last updated date
    private LocalDate expirationDate; // Expiration date
    private BigDecimal price; // Price
    private Status status; // Status
}
