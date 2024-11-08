package milktea.milktea.BUS;

import javafx.beans.property.ReadOnlyProperty;
import lombok.NonNull;
import milktea.milktea.DAO.Product_DAO;
import milktea.milktea.DTO.Product;

import java.util.ArrayList;
import java.util.HashMap;

public class Product_BUS {

    private static final ArrayList<Product> arrProducts = new ArrayList<>();
    private static final HashMap<String, Product> mapProducts = new HashMap<>();

    public static void getLocalProductData() {
        arrProducts.clear();
        arrProducts.addAll(Product_DAO.getAllProduct());
        for (Product product : arrProducts) {
            mapProducts.put(product.getProductId(), product);
        }
    }
    public static ArrayList<Product> getAllProduct() {
        return arrProducts;
    }

    public static boolean addProduct(Product product) {
        return Product_DAO.addProduct(product);
    }

    public static boolean editProduct(Product product) {
        return Product_DAO.editProduct(product);
    }


    public static Product getProductByName(String key) {
        return Product_DAO.getProductByName(key);
    }

    public static Product getProductById(@NonNull String key) {
        for (Product product : arrProducts) {
            if (product.getProductId().equals(key)) {
                return product;
            }
        }
        return null;
    }

    public static Product quickGetProductById( String id) {
        return mapProducts.get(id);
    }

    public static ArrayList<Product> searchProduct(String key) {
        ArrayList<Product> result = new ArrayList<>();
        for (Product product : arrProducts) {
            if (product.getName().toLowerCase().contains(key.toLowerCase())|| product.getProductId().toLowerCase().contains(key.toLowerCase())) {
                result.add(product);
            }
        }
        return result;
    }

    public static ArrayList<Product> searchProductByCategory(String key) {
        ArrayList<Product> result = new ArrayList<>();
        for (Product product : arrProducts) {
            if (product.getCategoryId().toLowerCase().contains(key.toLowerCase())) {
                result.add(product);
            }
        }
        return result;
    }
    public static boolean checkQuantityBelowZero(ArrayList<Product> arrProducts,String productId) {
        for (Product product : arrProducts) {
            if (product.getProductId().equals(productId)) {
                return product.getQuantity() > 0;
            }
        }
        return false;
    }
    public static int getQuantityById(ArrayList<Product> arrProducts,String productId) {
        for (Product product : arrProducts) {
            if (product.getProductId().equals(productId)) {
                return product.getQuantity();
            }else {
                return 0;
            }
        }
        return 0;

    }
}
