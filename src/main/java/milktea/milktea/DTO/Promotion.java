package milktea.milktea.DTO;

import java.math.BigDecimal;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Promotion {
    private String promotionProgramId;
    private String promotionId;
    private BigDecimal discount;
    private BigDecimal minimumPrice;

    public Promotion(Promotion selectedItem) {
        this.promotionProgramId = selectedItem.getPromotionProgramId();
        this.promotionId = selectedItem.getPromotionId();
        this.discount = selectedItem.getDiscount();
        this.minimumPrice = selectedItem.getMinimumPrice();

    }
}
