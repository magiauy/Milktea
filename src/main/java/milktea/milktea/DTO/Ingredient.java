package milktea.milktea.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {
    private String id; // Mã nguyên liệu
    private String name; // Tên nguyên liệu
    private BigDecimal price; // Giá
    private float quantity; // Số lượng
    private Unit unit; // Đơn vị tính
    private Status status; // Trạng thái
}

