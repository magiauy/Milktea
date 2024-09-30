package milktea.milktea.DTO;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    private String invoiceId; // Invoice ID
    private String employeeId; // Employee ID
    private String customerId; // Customer ID
    private String promotionId; // Promotion ID
    private LocalDate issueDate; // Issue date
    private BigDecimal discount; // Discount
    private BigDecimal total;
}
