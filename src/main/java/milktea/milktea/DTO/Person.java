package milktea.milktea.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public abstract class Person {
    private String id; // Mã
    private String firstName; // Họ
    private String lastName; // Tên
    private Gender gender; // Giới tính
    private String phoneNumber; // Số điện thoại

}
