package milktea.milktea.DAO;

import lombok.extern.slf4j.Slf4j;
import milktea.milktea.DTO.Product;
import milktea.milktea.DTO.Status;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
@Slf4j
public class Product_DAO extends Connect {
    public static ArrayList<Product> getAllProduct() {
        ArrayList<Product> arrProduct = new ArrayList<>();
        if (openConnection("Product")) {
            try {
                String sql = "Select * " +
                        "from product";

                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    Product product = Product.builder()
                            .productId(rs.getString("productId"))
                            .name(rs.getString("name"))
                            .categoryId(rs.getString("categoryId"))
                            .price(rs.getBigDecimal("price"))
                            .status(getStatus(rs.getString("status")))
                            .build();
                    arrProduct.add(product);
                }
            } catch (SQLException e) {
                log.error("Error: ", e);
            } finally {
                closeConnection();
            }
        }
        return arrProduct;
    }

    public static boolean addProduct(Product product) {
        boolean result = false;
        if (openConnection("Product")) {
            try {
                String sql = "Insert into product values(?,?,?,?,?)";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1, product.getProductId());
                stmt.setString(2, product.getName());
                stmt.setString(3, product.getCategoryId());
                stmt.setBigDecimal(4, product.getPrice());
                stmt.setString(5, product.getStatus().toString());

                if (stmt.executeUpdate() >= 1) {
                    result = true;
                }

            } catch (SQLException e) {
                log.error("Error: ", e);
            } finally {
                closeConnection();
            }
        }
        return result;
    }

    public static boolean editProduct(Product product) {
        boolean result = false;
        if (openConnection("Product")) {
            try {
                String sql = "Update product set name = ?, categoryId = ?, price = ? where productId = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1, product.getName());
                stmt.setString(2, product.getCategoryId());
                stmt.setBigDecimal(3, product.getPrice());
                stmt.setString(4, product.getProductId());

                if (stmt.executeUpdate() >= 1) {
                    result = true;
                }

            } catch (SQLException e) {
                log.error("Error: ", e);
            } finally {
                closeConnection();
            }
        }
        return result;
    }

    public static Product getProductByName(String key) {
        Product product = null;
        if (openConnection("Product")) {
            try {
                String sql = "Select * " +
                        "from product " +
                        "where name = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setString(1, key);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    product = Product.builder()
                            .productId(rs.getString("productId"))
                            .name(rs.getString("name"))
                            .categoryId(rs.getString("categoryId"))
                            .price(rs.getBigDecimal("price"))
                            .status(getStatus(rs.getString("status")))
                            .build();
                }
            } catch (SQLException e) {
                log.error("Error: ", e);
            } finally {
                closeConnection();
            }
        }
        return product;
    }


    public static boolean deleteProduct(String productId) {
        boolean result = false;
        if (openConnection("Product")) {
            try {
                String sql = "Delete from product where productId = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1, productId);

                if (stmt.executeUpdate() >= 1) {
                    result = true;
                }

            } catch (SQLException e) {
                log.error("Error: ", e);
            } finally {
                closeConnection();
            }
        }
        return result;
    }


    public static boolean changeStatusProduct(String productId, Status status) {
        boolean result = false;
        if (openConnection("Product")) {
            try {
                String sql = "Update product set status = ? where productId = ?";

                PreparedStatement stmt = connection.prepareStatement(sql);

                stmt.setString(1, status.toString());
                stmt.setString(2, productId);

                if (stmt.executeUpdate() >= 1) {
                    result = true;
                }

            } catch (SQLException e) {
                log.error("Error: ", e);
            } finally {
                closeConnection();
            }
        }
        return result;
    }
    
}
