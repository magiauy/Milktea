package milktea.milktea.BUS;

import milktea.milktea.DAO.GoodsReceiptDetail_DAO;
import milktea.milktea.DTO.GoodsReceiptDetail;

import java.util.ArrayList;
public class GoodsReceiptDetail_BUS {

    private static ArrayList<GoodsReceiptDetail> arrGoodsReceiptDetail = new ArrayList<>();

    public static void getLocalData() {
        arrGoodsReceiptDetail = GoodsReceiptDetail_DAO.getAllGoodsReceiptDetail();
    }
        public static ArrayList<GoodsReceiptDetail> getAllGoodsReceiptDetail() {
        return arrGoodsReceiptDetail;
    }

    public static boolean addGoodsReceiptDetail(ArrayList<GoodsReceiptDetail> goodsReceiptDetails) {
        return GoodsReceiptDetail_DAO.addGoodsReceiptDetail(goodsReceiptDetails);
    }

    public static boolean hasIngredient(String id) {
        for (GoodsReceiptDetail goodsReceiptDetail : arrGoodsReceiptDetail) {
            if (goodsReceiptDetail.getIngredientId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
