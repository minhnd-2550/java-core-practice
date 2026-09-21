import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VehicleRegistry {
    private final List<Vehicle> vehicles = new ArrayList<>();

    public void add(Vehicle vehicle) {
        Objects.requireNonNull(vehicle, "Vehicle is required.");
        if (findByNumber(vehicle.getNumber()) != null) throw new IllegalArgumentException("Duplicate vehicle number.");
        Owner existing = findOwner(vehicle.getOwner().identityNumber());
        if (existing != null && !existing.equals(vehicle.getOwner())) {
            throw new IllegalArgumentException("This identity number already belongs to an owner with different details.");
        }
        vehicles.add(vehicle);
    }

    public Vehicle findByNumber(String number) {
        String normalized = Vehicle.normalizeNumber(number);
        for (Vehicle vehicle : vehicles) if (vehicle.getNumber().equals(normalized)) return vehicle;
        return null;
    }

    public Owner findOwner(String identityNumber) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.getOwner().identityNumber().equals(identityNumber)) return vehicle.getOwner();
        }
        return null;
    }

    public List<Vehicle> findByOwner(String identityNumber) {
        return vehicles.stream().filter(v -> v.getOwner().identityNumber().equals(identityNumber)).toList();
    }

    public int deleteByManufacturer(String manufacturer) {
        String normalized = Vehicle.normalizeManufacturer(manufacturer);
        int before = vehicles.size();
        vehicles.removeIf(v -> v.getManufacturer().equals(normalized));
        return before - vehicles.size();
    }

    public Map<String, Long> countByManufacturer() {
        Map<String, Long> counts = new LinkedHashMap<>();
        for (String manufacturer : Vehicle.MANUFACTURERS) counts.put(manufacturer, 0L);
        for (Vehicle vehicle : vehicles) counts.merge(vehicle.getManufacturer(), 1L, Long::sum);
        return counts;
    }

    public List<String> mostCommonManufacturers() {
        if (vehicles.isEmpty()) return List.of();
        Map<String, Long> counts = countByManufacturer();
        long max = counts.values().stream().mapToLong(Long::longValue).max().orElse(0);
        return counts.entrySet().stream().filter(e -> e.getValue() == max).map(Map.Entry::getKey).toList();
    }

    public List<Vehicle> sortedByNumberDescending() {
        return vehicles.stream().sorted(Comparator.comparing(Vehicle::getNumber).reversed()).toList();
    }

    public Map<String, Long> countByType() {
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("Car", 0L);
        counts.put("Motorcycle", 0L);
        counts.put("Truck", 0L);
        for (Vehicle vehicle : vehicles) counts.merge(vehicle.getClass().getSimpleName(), 1L, Long::sum);
        return counts;
    }
}
