package milktea.milktea.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryReportDetail {
    private String id;
    private String inventoryId;
    private double quantity;
}
