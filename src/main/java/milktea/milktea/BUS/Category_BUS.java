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
    public static void addCategoryLocal(Category category){
        arrCategory.add(category);
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

    public static String autoID() {
        if (arrCategory.isEmpty()) {
            return "C001";
        } else {
            int max = 0;
            for (Category category : arrCategory) {
                int id = Integer.parseInt(category.getId().substring(1));
                if (id > max) {
                    max = id;
                }
            }
            return "C" + String.format("%03d", max + 1);
        }
    }

    public static void editCategoryLocal(Category newCategory) {
        for (Category category : arrCategory) {
            if (category.getId().equals(newCategory.getId())) {
                category.setName(newCategory.getName());
                break;
            }
        }
    }

    public static boolean deleteCategory(String id) {
        return Category_DAO.deleteCategory(id);
    }

    public static void deleteCategoryLocal(String id) {
        arrCategory.removeIf(category -> category.getId().equals(id));
    }
}
