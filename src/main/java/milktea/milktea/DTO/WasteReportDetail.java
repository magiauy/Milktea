package milktea.milktea.DTO;

import java.math.BigDecimal;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WasteReportDetail {
    private String id;
    private String inventoryId;
    private double quantity;
    private BigDecimal price;
    private BigDecimal total;
}
