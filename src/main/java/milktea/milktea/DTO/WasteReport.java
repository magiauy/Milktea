package milktea.milktea.DTO;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WasteReport {
    private String id;
    private String employeeId;
    private LocalDate date;
    private BigDecimal total;
}
