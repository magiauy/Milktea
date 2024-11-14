package milktea.milktea.DAO;

import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.PromotionProgram;

import java.sql.*;
import java.util.ArrayList;
@Slf4j
public class PromotionProgram_DAO extends Connect{
    public static ArrayList<PromotionProgram> getAllPromotionProgram(){
        ArrayList<PromotionProgram> arrPromotionProgram = new ArrayList<>();
        if(openConnection("PromotionProgram")){
            try{
                String sql = "Select * " +
                        "from PromotionProgram";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    PromotionProgram promotionProgram = PromotionProgram.builder()
                            .promotionProgramId(rs.getString("promotionProgramId"))
                            .name(rs.getString("name"))
                            .startDate(rs.getDate("startDate").toLocalDate())
                            .endDate(rs.getDate("endDate").toLocalDate())
                            .build();
                    arrPromotionProgram.add(promotionProgram);
                }
            }catch(SQLException e){
                log.error("Error: ", e);
            }finally{
                closeConnection();
            }
        }
        return arrPromotionProgram;
    }
    public static boolean addPromotionProgram(PromotionProgram promotionProgram){
        boolean result = false;
        if(openConnection("PromotionProgram")){
            try{
                String sql = "Insert into PromotionProgram values(?,?,?,?)";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,promotionProgram.getPromotionProgramId());
                stmt.setString(2,promotionProgram.getName());
                stmt.setDate(3, Date.valueOf(promotionProgram.getStartDate()));
                stmt.setDate(4, Date.valueOf(promotionProgram.getEndDate()));

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

    public static boolean editPromotionProgram(PromotionProgram promotionProgram){
        boolean result = false;
        if(openConnection("PromotionProgram")){
            try{
                String sql = "Update PromotionProgram set name = ?, startDate = ?, endDate = ? where promotionProgramId = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,promotionProgram.getName());
                stmt.setDate(2, Date.valueOf(promotionProgram.getStartDate()));
                stmt.setDate(3, Date.valueOf(promotionProgram.getEndDate()));
                stmt.setString(4,promotionProgram.getPromotionProgramId());

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

    public static boolean deletePromotionProgram(String promotionProgramId) {
        boolean result = false;
        if(openConnection("PromotionProgram")){
            try{
                String sql = "Delete from PromotionProgram where promotionProgramId = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1,promotionProgramId);

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
