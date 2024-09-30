package milktea.milktea.BUS;

import milktea.milktea.DAO.Product_DAO;
import milktea.milktea.DTO.Product;

import java.util.ArrayList;
public class Product_BUS {
    public static ArrayList<Product> getAllProduct() {
        return Product_DAO.getAllProduct();
    }

    public static boolean addProduct(Product product) {
        return Product_DAO.addProduct(product);
    }

    public static boolean editProduct(Product product) {
        return Product_DAO.editProduct(product);
    }



}
