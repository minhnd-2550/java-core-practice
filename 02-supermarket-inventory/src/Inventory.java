import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

// DSHH in the assignment. The backing store is an array, not an ArrayList.
public class Inventory {
    private Product[] products = new Product[10];
    private int size;

    public void add(Product product) {
        Objects.requireNonNull(product, "Product is required.");
        for (int i = 0; i < size; i++) {
            if (products[i].getCode().equalsIgnoreCase(product.getCode())) {
                throw new IllegalArgumentException("Duplicate product code: " + product.getCode());
            }
        }
        if (size == products.length) products = Arrays.copyOf(products, products.length * 2);
        products[size++] = product;
    }

    public Product[] getProducts() { return Arrays.copyOf(products, size); }

    public long totalQuantity(Class<? extends Product> type) {
        long total = 0;
        for (int i = 0; i < size; i++) if (type.isInstance(products[i])) total += products[i].getQuantity();
        return total;
    }

    public BigDecimal totalVat(Class<? extends Product> type) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < size; i++) {
            if (type.isInstance(products[i])) total = total.add(products[i].getInventoryVat());
        }
        return total;
    }
}
