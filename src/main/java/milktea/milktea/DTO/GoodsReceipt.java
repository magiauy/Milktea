package milktea.milktea.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoodsReceipt {
    private String id;
    private String providerId;
    private String employeeId;
    private LocalDate date;
    private BigDecimal total;
}
