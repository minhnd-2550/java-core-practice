import java.time.Year;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public abstract class Vehicle {
    public static final List<String> MANUFACTURERS = List.of("Honda", "Yamaha", "Toyota", "Suzuki");
    private final String number;
    private final String manufacturer;
    private final int year;
    private final String color;
    private final Owner owner;

    protected Vehicle(String number, String manufacturer, int year, String color, Owner owner) {
        this.number = normalizeNumber(number);
        this.manufacturer = normalizeManufacturer(manufacturer);
        if (year <= 2000 || year > Year.now().getValue()) {
            throw new IllegalArgumentException("Year must be > 2000 and <= current year.");
        }
        this.year = year;
        this.color = requireText(color, "Color");
        this.owner = Objects.requireNonNull(owner, "Owner is required.");
    }

    public static String requireText(String value, String field) {
        if (value == null || value.isBlank()) throw new IllegalArgumentException(field + " is required.");
        return value.strip();
    }

    public static String normalizeNumber(String value) {
        String number = requireText(value, "Vehicle number").toUpperCase(Locale.ROOT);
        if (number.codePointCount(0, number.length()) != 5 || number.codePoints().anyMatch(Character::isWhitespace)) {
            throw new IllegalArgumentException("Vehicle number must contain exactly 5 non-space characters.");
        }
        return number;
    }

    public static String normalizeManufacturer(String value) {
        String name = requireText(value, "Manufacturer");
        for (String allowed : MANUFACTURERS) if (allowed.equalsIgnoreCase(name)) return allowed;
        throw new IllegalArgumentException("Manufacturer must be Honda, Yamaha, Toyota or Suzuki.");
    }

    protected static double requirePositive(double value, String field) {
        if (!Double.isFinite(value) || value <= 0) throw new IllegalArgumentException(field + " must be finite and > 0.");
        return value;
    }

    public String getNumber() { return number; }
    public String getManufacturer() { return manufacturer; }
    public Owner getOwner() { return owner; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{number=" + number + ", manufacturer=" + manufacturer
                + ", year=" + year + ", color=" + color + ", owner=" + owner + "}";
    }
}
