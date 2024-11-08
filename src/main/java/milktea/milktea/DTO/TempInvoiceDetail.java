package milktea.milktea.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TempInvoiceDetail {
    private InvoiceDetail invoiceDetail;
    private int sugar;
    private int ice;
    private String note;
    private HashMap<String,Integer> topping;

}
