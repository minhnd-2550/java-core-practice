import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class Product {
    private final String code;
    private final String name;
    private final int quantity;
    private final BigDecimal unitPrice;

    protected Product(String code, String name, int quantity, BigDecimal unitPrice) {
        this.code = requireText(code, "Code");
        this.name = requireText(name, "Name");
        if (quantity < 0) throw new IllegalArgumentException("Quantity must be >= 0.");
        if (unitPrice == null || unitPrice.signum() < 0) {
            throw new IllegalArgumentException("Price must be >= 0.");
        }
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    protected static String requireText(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required.");
        return value.strip();
    }

    public String getCode() { return code; }
    public int getQuantity() { return quantity; }
    public abstract BigDecimal getVatRate();
    public abstract String evaluateConsumption(LocalDate today);

    public BigDecimal getUnitVat() { return unitPrice.multiply(getVatRate()); }
    public BigDecimal getInventoryVat() { return getUnitVat().multiply(BigDecimal.valueOf(quantity)); }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{code=" + code + ", name=" + name
                + ", quantity=" + quantity + ", unitPrice=" + unitPrice + "}";
    }
}
