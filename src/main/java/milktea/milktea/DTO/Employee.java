package milktea.milktea.DTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee extends Person{
    private String username; // Tên đăng nhập
    private String password; // Mật khẩu
    private String role; // Vai trò
    private Status status; // Trạng thái

    private int Permission;
}
