package milktea.milktea.BUS;

import milktea.milktea.DAO.Connect;
import milktea.milktea.DTO.MySQLConfig;

public class Connect_BUS {
    public static boolean loadConfig(){
        return Connect.loadConfig();
    }
    public static boolean checkConnection(MySQLConfig config){
        return Connect.checkConnection(config);
    }
    public static void updateConfig(MySQLConfig newConfig){
        Connect.updateConfig(newConfig);
    }

    public static void createDatabase(){
        Connect.createDatabase();
    }
}
