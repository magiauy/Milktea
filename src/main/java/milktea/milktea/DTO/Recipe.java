package milktea.milktea.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recipe {
    private String productId;
    private String ingredientId;
    private double quantity;
    private Unit unit;

    private String ingredientName ;

}
