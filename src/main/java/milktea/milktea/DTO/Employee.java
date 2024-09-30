package milktea.milktea.DTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@AllArgsConstructor
public class Employee extends Person{
    private String username; // Tên đăng nhập
    private String password; // Mật khẩu
    private Role role; // Vai trò
    private Status status; // Trạng thái
}
