package milktea.milktea.DAO;

import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.Promotion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
@Slf4j
public class Promotion_DAO extends Connect{
    public static ArrayList<Promotion> getAllPromotion(){
        ArrayList<Promotion> arrPromotion = new ArrayList<>();
        if(openConnection("Promotion")){
            try{
                String sql = "Select * " +
                        "from promotion";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    Promotion promotion = Promotion.builder()
                            .promotionProgramId(rs.getString("promotionProgramId"))
                            .promotionId(rs.getString("promotionId"))
                            .discount(rs.getBigDecimal("discount"))
                            .minimumPrice(rs.getBigDecimal("minimumPrice"))
                            .build();
                    arrPromotion.add(promotion);
                }
            }catch(SQLException e){
                log.error("Error: ", e);
            }finally{
                closeConnection();
            }
        }
        return arrPromotion;
    }
    public static boolean addPromotion(Promotion promotion){
        boolean result = false;
        if(openConnection("Promotion")){
            try{
                String sql = "Insert into promotion values(?,?,?,?)";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1,promotion.getPromotionProgramId());
                stmt.setString(2,promotion.getPromotionId());
                stmt.setBigDecimal(3,promotion.getDiscount());
                stmt.setBigDecimal(4,promotion.getMinimumPrice());

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
    public static boolean editPromotion(Promotion promotion){
        boolean result = false;
        if(openConnection("Promotion")){
            try{
                String sql = "Update promotion set minimumPrice = ?, discount = ? where promotionId = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setBigDecimal(1,promotion.getMinimumPrice());
                stmt.setBigDecimal(2,promotion.getDiscount());
                stmt.setString(3,promotion.getPromotionId());

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
