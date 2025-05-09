package milktea.milktea.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
    private String roleId;
    private String roleName;
    private int access;
    @Override
    public String toString() {
        return roleName;
    }
}
