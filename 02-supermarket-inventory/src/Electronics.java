import java.math.BigDecimal;
import java.time.LocalDate;

public class Electronics extends Product {
    private final int warrantyMonths;
    private final double powerKw;

    public Electronics(String code, String name, int quantity, BigDecimal price,
                       int warrantyMonths, double powerKw) {
        super(code, name, quantity, price);
        if (warrantyMonths < 0 || !Double.isFinite(powerKw) || powerKw < 0) {
            throw new IllegalArgumentException("Warranty and power must be nonnegative and finite.");
        }
        this.warrantyMonths = warrantyMonths;
        this.powerKw = powerKw;
    }

    @Override
    public BigDecimal getVatRate() { return new BigDecimal("0.10"); }

    @Override
    public String evaluateConsumption(LocalDate today) {
        return getQuantity() < 3 ? "SELLING_WELL" : "NOT_EVALUATED";
    }

    @Override
    public String toString() {
        return super.toString() + " warrantyMonths=" + warrantyMonths + ", powerKw=" + powerKw;
    }
}
