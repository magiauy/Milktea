package milktea.milktea.DTO;

import lombok.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionProgram {
    private String promotionProgramId;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
}
