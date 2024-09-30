package milktea.milktea.BUS;

import milktea.milktea.DAO.GoodsReceiptDetail_DAO;
import milktea.milktea.DTO.GoodsReceiptDetail;

import java.util.ArrayList;
public class GoodsReceiptDetail_BUS {
    public static ArrayList<GoodsReceiptDetail> getAllGoodsReceiptDetail() {
        return GoodsReceiptDetail_DAO.getAllGoodsReceiptDetail();
    }

    public static boolean addGoodsReceiptDetail(ArrayList<GoodsReceiptDetail> goodsReceiptDetails) {
        return GoodsReceiptDetail_DAO.addGoodsReceiptDetail(goodsReceiptDetails);
    }
}
