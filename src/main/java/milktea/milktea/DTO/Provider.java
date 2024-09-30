package milktea.milktea.DTO;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Provider {
    private String id;
    private String name;
    private String address;
    private String phone;
    private String email;
}
