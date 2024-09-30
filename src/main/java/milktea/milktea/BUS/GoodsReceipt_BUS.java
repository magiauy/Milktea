package milktea.milktea.BUS;

import milktea.milktea.DAO.GoodsReceipt_DAO;
import milktea.milktea.DTO.GoodsReceipt;

import java.util.ArrayList;
public class GoodsReceipt_BUS {
    public static ArrayList<GoodsReceipt> getAllGoodsReceipt() {
        return GoodsReceipt_DAO.getAllGoodsReceipt();
    }

    public static boolean addGoodsReceipt(GoodsReceipt goodsReceipt) {
        return GoodsReceipt_DAO.addGoodsReceipt(goodsReceipt);
    }

}
