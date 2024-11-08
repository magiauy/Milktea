package milktea.milktea.DAO;

import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.GoodsReceiptDetail;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

@Slf4j
public class GoodsReceiptDetail_DAO extends Connect{
    public static ArrayList<GoodsReceiptDetail> getAllGoodsReceiptDetail(){
        ArrayList<GoodsReceiptDetail> arrGoodsReceiptDetail = new ArrayList<>();
        if(openConnection("GoodsReceiptDetail")){
            try{
                String sql = "Select * " +
                        "from goodsreceiptdetail grd";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while(rs.next()){
                    GoodsReceiptDetail goodsReceiptDetail = GoodsReceiptDetail.builder()
                            .goodsReceiptId(rs.getString("goodsReceiptId"))
                            .ingredientId(rs.getString("ingredientId"))
                            .quantity(rs.getDouble("quantity"))
                            .price(rs.getBigDecimal("price"))
                            .total(rs.getBigDecimal("total"))
                            .build();
                    arrGoodsReceiptDetail.add(goodsReceiptDetail);
                }
            }catch(SQLException e){
                log.error("e: ", e);
            }finally{
                closeConnection();
            }
        }
        return arrGoodsReceiptDetail;
    }
    public static boolean addGoodsReceiptDetail(ArrayList<GoodsReceiptDetail> goodsReceiptDetails){
        boolean result = false;
        if(openConnection("GoodsReceiptDetail")){
            try{
                for (GoodsReceiptDetail goodsReceiptDetail : goodsReceiptDetails) {
                    String sql = "Insert into goodsreceiptdetail values(?,?,?,?,?)";

                    PreparedStatement stmt = connection.prepareStatement(sql);

                    stmt.setString(1,goodsReceiptDetail.getGoodsReceiptId());
                    stmt.setString(2,goodsReceiptDetail.getIngredientId());
                    stmt.setDouble(3,goodsReceiptDetail.getQuantity());
                    stmt.setBigDecimal(4,goodsReceiptDetail.getPrice());
                    stmt.setBigDecimal(5,goodsReceiptDetail.getTotal());

                    if(stmt.executeUpdate()>=1){
                        result = true;
                    }
                }

            }catch(SQLException e){
                log.error("e: ", e);
            }finally{
                closeConnection();
            }
        }
        return result;
    }
}
