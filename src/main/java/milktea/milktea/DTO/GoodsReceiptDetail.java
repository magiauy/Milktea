package milktea.milktea.DTO;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceiptDetail {
    private String goodsReceiptId;
    private String ingredientId;
    private double quantity;
    private BigDecimal price;
    private BigDecimal total;

}
