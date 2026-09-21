import java.math.BigDecimal;
import java.time.LocalDate;

public class Food extends Product {
    private final LocalDate manufacturedOn;
    private final LocalDate expiresOn;
    private final String supplier;

    public Food(String code, String name, int quantity, BigDecimal price,
                LocalDate manufacturedOn, LocalDate expiresOn, String supplier) {
        super(code, name, quantity, price);
        if (manufacturedOn == null || expiresOn == null || expiresOn.isBefore(manufacturedOn)) {
            throw new IllegalArgumentException("Expiry date must be on or after manufacture date.");
        }
        this.manufacturedOn = manufacturedOn;
        this.expiresOn = expiresOn;
        this.supplier = requireText(supplier, "Supplier");
    }

    @Override
    public BigDecimal getVatRate() { return new BigDecimal("0.05"); }

    @Override
    public String evaluateConsumption(LocalDate today) {
        return getQuantity() > 0 && expiresOn.isBefore(today) ? "HARD_TO_SELL" : "NOT_EVALUATED";
    }

    @Override
    public String toString() {
        return super.toString() + " manufacturedOn=" + manufacturedOn
                + ", expiresOn=" + expiresOn + ", supplier=" + supplier;
    }
}
