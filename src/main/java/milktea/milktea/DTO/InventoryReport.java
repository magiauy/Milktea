package milktea.milktea.DTO;

import java.time.LocalDate;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryReport {
    private String id;
    private String employeeId;
    private LocalDate date;

}
