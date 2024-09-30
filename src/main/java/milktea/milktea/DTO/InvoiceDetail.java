package milktea.milktea.DTO;

import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDetail {
    private String invoiceId;
    private String productId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}
