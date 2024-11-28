package milktea.milktea.API;

import org.springframework.web.bind.annotation.*;

import milktea.milktea.DTO.Employee;
import milktea.milktea.BUS.Employee_BUS;

import milktea.milktea.DTO.Product;
import milktea.milktea.BUS.Product_BUS;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/employee")
    public List<Employee> getUsers() {
        List<Employee> users = new ArrayList<>();
        users.addAll(Employee_BUS.getAllEmployee());
        // Thêm logic để lấy danh sách người dùng
        return users;
    }

    @GetMapping("/product")
    public List<Product> getProducts() {
        List<Product> products = new ArrayList<>();
        Product_BUS.getLocalData();
        products.addAll(Product_BUS.getAllProduct());
        // Thêm logic để lấy danh sách sản phẩm
        return products;
    }

    @PostMapping("/product")
    public boolean addProduct(@RequestBody Product product) {
        return Product_BUS.addProduct(product);
    }

    // Thêm các phương thức tương tự cho các đối tượng khác

}