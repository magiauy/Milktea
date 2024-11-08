package milktea.milktea.DTO;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String productId;
    private String name;
    private String categoryId;
    private BigDecimal price;
    private Status status;

    //Sub attributes
    private int quantity;

    @Override
    public String toString(){
        return name;
    }
}
