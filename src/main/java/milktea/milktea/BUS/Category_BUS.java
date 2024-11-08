package milktea.milktea.BUS;

import milktea.milktea.DAO.Category_DAO;
import milktea.milktea.DTO.Category;

import java.util.ArrayList;
import java.util.List;

public class Category_BUS {
    private static ArrayList<Category> arrCategory = new ArrayList<>();

    public static void getLocalData(){
        arrCategory = Category_DAO.getAllCategory();
    }
    public static ArrayList<Category> getAllCategory() {
        return arrCategory;
    }

    public static boolean addCategory(Category category) {
        return Category_DAO.addCategory(category);
    }

    public static boolean editCategory(Category category) {
        return Category_DAO.editCategory(category);
    }

    public static List<String> getAllCategoryName() {
        List<String> result = new ArrayList<>();
        for (Category category : arrCategory) {
            result.add(category.getName());
        }
        result.removeIf(s -> s.equals("Topping"));
        return result;
    }

    public static String getCategoryIdByName(String key) {
        for (Category category : arrCategory) {
            if (category.getName().equals(key)) {
                return category.getId();
            }
        }
        return null;
    }

    public static Category getCategoryById(String categoryId) {
        for (Category category : arrCategory) {
            if (category.getId().equals(categoryId)) {
                return category;
            }
        }
        return null;
    }
}
