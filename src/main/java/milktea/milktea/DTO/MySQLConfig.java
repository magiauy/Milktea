package milktea.milktea.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MySQLConfig {
    private String host;
    private int port;
    private String database;
    private String user;
    private String password;
    private int maxConnection;

}
