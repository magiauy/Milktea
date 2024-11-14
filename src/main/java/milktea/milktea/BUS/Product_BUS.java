package milktea.milktea.BUS;

import lombok.NonNull;
import milktea.milktea.DAO.Product_DAO;
import milktea.milktea.DTO.Product;
import milktea.milktea.DTO.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class Product_BUS {

    private static final ArrayList<Product> arrProducts = new ArrayList<>();
    private static final HashMap<String, Product> mapProducts = new HashMap<>();

    public static void getLocalData() {
        arrProducts.clear();
        mapProducts.clear();
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
    public static boolean editProductLocal(Product product) {
        for (Product product1 : arrProducts) {
            if (product1.getProductId().equals(product.getProductId())) {
                product1.setName(product.getName());
                product1.setCategoryId(product.getCategoryId());
                product1.setPrice(product.getPrice());
                product1.setStatus(product.getStatus());
                mapProducts.put(product.getProductId(), product);
                return true;
            }
        }
        return false;
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

    public static String autoId() {
        if (arrProducts.isEmpty()) {
            return "P001";
        }
        String lastId = arrProducts.getLast().getProductId();
        int id = Integer.parseInt(lastId.substring(1)) + 1;
        return "P" + String.format("%03d", id);
    }

    public static boolean addProductLocal(Product product) {
        arrProducts.add(product);
        mapProducts.put(product.getProductId(), product);
        return true;
    }

    public static boolean deleteProduct(String productId) {
        return Product_DAO.deleteProduct(productId);
    }

    public static boolean deleteProductLocal(String productId) {
        for (Product product : arrProducts) {
            if (product.getProductId().equals(productId)) {
                arrProducts.remove(product);
                mapProducts.remove(productId);
                return true;
            }
        }
        return false;
    }

    public static boolean changeStatusProduct(String productId, Status status) {
        return Product_DAO.changeStatusProduct(productId, status);
    }
    public static boolean changeStatusProductLocal(String productId, Status status) {
        for (Product product : arrProducts) {
            if (product.getProductId().equals(productId)) {
                product.setStatus(status);
                return true;
            }
        }
        return false;
    }

    public static boolean isCategoryExist(String id) {
        for (Product product : arrProducts) {
            if (product.getCategoryId().equals(id)) {
                return true;
            }
        }
        return false;
    }
}
