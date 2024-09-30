package milktea.milktea.BUS;

import milktea.milktea.DAO.Category_DAO;
import milktea.milktea.DTO.Category;

import java.util.ArrayList;

public class Category_BUS {
    public static ArrayList<Category> getAllCategory() {
        return Category_DAO.getAllCategory();
    }

    public static boolean addCategory(Category category) {
        return Category_DAO.addCategory(category);
    }

    public static boolean editCategory(Category category) {
        return Category_DAO.editCategory(category);
    }
}
