package milktea.milktea.DTO;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String productId;
    private String recipeId;
    private String name;
    private String categoryId;
    private BigDecimal price;
    private Status status;
}
