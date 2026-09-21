import java.time.Year;
import java.util.List;

public class SelfCheck {
    public static void run() {
        Owner owner = new Owner("000000000001", "Demo Owner", "demo@example.com");
        int year = Year.now().getValue();
        VehicleRegistry registry = new VehicleRegistry();
        assert registry.mostCommonManufacturers().isEmpty();
        assert registry.countByType().get("Car") == 0;
        Car car = new Car("A0001", "toyota", year, "White", owner, 4, "Petrol");
        registry.add(car);
        registry.add(new Motorcycle("B0001", "Honda", 2001, "Black", owner, 125));
        registry.add(new Truck("C0001", "Honda", year, "Blue", owner, 2));
        assert registry.findByNumber("a0001") == car;
        assert registry.findByNumber("X0001") == null;
        assert registry.findByOwner(owner.identityNumber()).size() == 3;
        assert registry.mostCommonManufacturers().equals(List.of("Honda"));
        assert registry.sortedByNumberDescending().stream().map(Vehicle::getNumber).toList()
                .equals(List.of("C0001", "B0001", "A0001"));
        assert registry.countByType().values().stream().allMatch(count -> count == 1);
        rejects(() -> registry.add(new Car("a0001", "Toyota", year, "White", owner, 4, "Petrol")));
        Owner conflicting = new Owner(owner.identityNumber(), "Different Owner", "other@example.com");
        rejects(() -> registry.add(new Car("D0001", "Toyota", year, "White", conflicting, 4, "Petrol")));
        assert registry.findByOwner(owner.identityNumber()).size() == 3;
        rejects(() -> new Owner("123", "Name", "demo@example.com"));
        rejects(() -> new Owner("000000000001", "Name", "invalid@localhost"));
        rejects(() -> new Car("1234", "Toyota", year, "White", owner, 4, "Petrol"));
        rejects(() -> new Car("123456", "Toyota", year, "White", owner, 4, "Petrol"));
        rejects(() -> new Car("A0002", "Ford", year, "White", owner, 4, "Petrol"));
        rejects(() -> new Car("A0002", "Toyota", 2000, "White", owner, 4, "Petrol"));
        rejects(() -> new Car("A0002", "Toyota", year + 1, "White", owner, 4, "Petrol"));
        rejects(() -> new Motorcycle("A0002", "Honda", year, "White", owner, Double.NaN));
        registry.add(new Car("D0001", "Toyota", year, "White", owner, 4, "Petrol"));
        assert registry.mostCommonManufacturers().equals(List.of("Honda", "Toyota"));
        assert registry.deleteByManufacturer("honda") == 2;
        assert registry.findByOwner(owner.identityNumber()).size() == 2;
        assert registry.countByType().get("Motorcycle") == 0;
        assert registry.deleteByManufacturer("Honda") == 0;
        assert registry.deleteByManufacturer("Toyota") == 2;
        assert registry.mostCommonManufacturers().isEmpty();
        System.out.println("Vehicle registry checks passed.");
    }

    private static void rejects(Runnable action) {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected invalid input to be rejected.");
    }
}
