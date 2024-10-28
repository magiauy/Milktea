package milktea.milktea.DTO;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDetail {
    private String recipeId;
    private String ingredientId;
    private float quantity;
    private Unit unit;

}
