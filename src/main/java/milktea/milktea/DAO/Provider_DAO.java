package milktea.milktea.DAO;

import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.Provider;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Slf4j
public class Provider_DAO extends Connect{
    public static ArrayList<Provider> getAllProvider(){
        ArrayList<Provider> arrProvider = new ArrayList<>();
        if(openConnection("Provider")){
            try{
                String sql = "Select * " +
                        "from provider";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    Provider provider = Provider.builder()
                            .id(rs.getString("id"))
                            .name(rs.getString("name"))
                            .address(rs.getString("address"))
                            .phone(rs.getString("phone"))
                            .email(rs.getString("email"))
                            .build();
                    arrProvider.add(provider);
                }
            }catch(SQLException e){
                log.error("Error: ", e);
            }finally{
                closeConnection();
            }
        }
        return arrProvider;
    }
    public static boolean addProvider(Provider provider){
        boolean result = false;
        if(openConnection("Provider")){
            try{
                String sql = "Insert into provider values(?,?,?,?,?)";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1,provider.getId());
                stmt.setString(2,provider.getName());
                stmt.setString(3,provider.getAddress());
                stmt.setString(4,provider.getPhone());
                stmt.setString(5,provider.getEmail());

                if(stmt.executeUpdate()>=1){
                    result = true;
                }

            }catch(SQLException e){
                log.error("Error: ", e);
            }finally{
                closeConnection();
            }
        }
        return result;
    }

    public static boolean editProvider(Provider provider){
        boolean result = false;
        if(openConnection("Provider")){
            try{
                String sql = "Update provider set name = ?, address = ?, phone = ?, email = ? where id = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1,provider.getName());
                stmt.setString(2,provider.getAddress());
                stmt.setString(3,provider.getPhone());
                stmt.setString(4,provider.getEmail());
                stmt.setString(5,provider.getId());

                if(stmt.executeUpdate()>=1){
                    result = true;
                }

            }catch(SQLException e){
                log.error("Error: ", e);
            }finally{
                closeConnection();
            }
        }
        return result;
    }

    public static boolean deleteProvider(String id) {
        boolean result = false;
        if(openConnection()){
            try{
                String sql = "Delete from provider where id = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1,id);

                if(stmt.executeUpdate()>=1){
                    result = true;
                }

            }catch(SQLException e){
                log.error("Error: ", e);
            }finally{
                closeConnection();
            }
        }
        return result;
    }
}
