import java.math.BigDecimal;
import java.time.LocalDate;

public class Crockery extends Product {
    private final String manufacturer;
    private final LocalDate arrivedOn;

    public Crockery(String code, String name, int quantity, BigDecimal price,
                    String manufacturer, LocalDate arrivedOn) {
        super(code, name, quantity, price);
        this.manufacturer = requireText(manufacturer, "Manufacturer");
        if (arrivedOn == null) throw new IllegalArgumentException("Arrival date is required.");
        this.arrivedOn = arrivedOn;
    }

    @Override
    public BigDecimal getVatRate() { return new BigDecimal("0.10"); }

    @Override
    public String evaluateConsumption(LocalDate today) {
        return getQuantity() > 50 && arrivedOn.isBefore(today.minusDays(10))
                ? "SLOW_SELLING" : "NOT_EVALUATED";
    }

    @Override
    public String toString() {
        return super.toString() + " manufacturer=" + manufacturer + ", arrivedOn=" + arrivedOn;
    }
}
