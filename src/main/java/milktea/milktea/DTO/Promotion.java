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
}
