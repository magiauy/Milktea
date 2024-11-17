package milktea.milktea.DTO;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    private String productId;
    private String name;
    private String categoryId;
    private BigDecimal price;
    private Status status;

    public Product(String productId, String name, String categoryId, BigDecimal price, Status status) {
        this.productId = productId;
        this.name = name;
        this.categoryId = categoryId;
        this.price = price;
        this.status = status;
    }
    //Sub attributes
    private int quantity;
    private BigDecimal total;

    public Product(Product productById) {
        this.productId = productById.getProductId();
        this.name = productById.getName();
        this.categoryId = productById.getCategoryId();
        this.price = productById.getPrice();
        this.status = productById.getStatus();
    }


    @Override
    public String toString(){
        return name;
    }

    public BigDecimal getPrice() {
        return price.setScale(0, RoundingMode.HALF_UP);
    }
}
