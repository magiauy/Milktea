package milktea.milktea.BUS;

import milktea.milktea.DAO.GoodsReceipt_DAO;
import milktea.milktea.DTO.GoodsReceipt;

import java.util.ArrayList;
public class GoodsReceipt_BUS {

    private static ArrayList<GoodsReceipt> arrGoodsReceipt = new ArrayList<>();

    public static void getLocalData() {
        arrGoodsReceipt = GoodsReceipt_DAO.getAllGoodsReceipt();
    }

        public static ArrayList<GoodsReceipt> getAllGoodsReceipt() {
        return arrGoodsReceipt;
    }

    public static boolean addGoodsReceipt(GoodsReceipt goodsReceipt) {
        return GoodsReceipt_DAO.addGoodsReceipt(goodsReceipt);
    }

}
