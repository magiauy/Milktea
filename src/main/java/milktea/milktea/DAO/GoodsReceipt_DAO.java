package milktea.milktea.DAO;

import milktea.milktea.DTO.GoodsReceipt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GoodsReceipt_DAO extends Connect{
    public static ArrayList<GoodsReceipt> getAllGoodsReceipt(){
        ArrayList<GoodsReceipt> arrGoodsReceipt = new ArrayList<>();
        if(openConnection("GoodsReceipt")){
            try{
                String sql = "Select * " +
                        "from goodsreceipt gr";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    GoodsReceipt goodsReceipt = GoodsReceipt.builder()
                            .id(rs.getString("id"))
                            .providerId(rs.getString("providerId"))
                            .employeeId(rs.getString("employeeId"))
                            .date(rs.getDate("date").toLocalDate())
                            .total(rs.getBigDecimal("total"))

                            .build();
                    arrGoodsReceipt.add(goodsReceipt);
                }
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }
        return arrGoodsReceipt;
    }
    public static boolean addGoodsReceipt(GoodsReceipt goodsReceipt){
        boolean result = false;
        if(openConnection("GoodsReceipt")){
            try{
                String sql = "Insert into goodsreceipt values(?,?,?,?,?)";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1,goodsReceipt.getId());
                stmt.setString(2,goodsReceipt.getProviderId());
                stmt.setString(3,goodsReceipt.getEmployeeId());
                stmt.setDate(4,java.sql.Date.valueOf(goodsReceipt.getDate()));
                stmt.setBigDecimal(5,goodsReceipt.getTotal());

                if(stmt.executeUpdate()>=1){
                    result = true;
                }

            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                closeConnection();
            }
        }
        return result;
    }


}
