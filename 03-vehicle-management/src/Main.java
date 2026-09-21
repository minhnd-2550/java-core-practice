import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--check")) { SelfCheck.run(); return; }
        VehicleRegistry registry = new VehicleRegistry();
        try (Scanner input = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1. Add  2. Find number  3. Find owner's vehicles  4. Delete manufacturer's vehicles");
                System.out.println("5. Most common manufacturers  6. Sort numbers descending  7. Type counts  0. Exit");
                if (!input.hasNextLine()) return;
                try {
                    switch (input.nextLine().strip()) {
                        case "0": return;
                        case "1": registry.add(readVehicle(input, registry)); System.out.println("Added."); break;
                        case "2":
                            Vehicle found = registry.findByNumber(read(input, "Vehicle number"));
                            System.out.println(found == null ? "Not found." : found); break;
                        case "3": print(registry.findByOwner(read(input, "Owner's identity number"))); break;
                        case "4": System.out.println("Deleted: " + registry.deleteByManufacturer(read(input, "Manufacturer"))); break;
                        case "5":
                            System.out.println("Most common: " + registry.mostCommonManufacturers());
                            System.out.println(registry.countByManufacturer()); break;
                        case "6": print(registry.sortedByNumberDescending()); break;
                        case "7": System.out.println(registry.countByType()); break;
                        default: System.out.println("Invalid choice.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid input: " + e.getMessage() + " Please try the operation again.");
                } catch (java.util.NoSuchElementException e) { return; }
            }
        }
    }

    private static Vehicle readVehicle(Scanner input, VehicleRegistry registry) {
        String type = read(input, "Type (1=Car, 2=Motorcycle, 3=Truck)");
        if (!List.of("1", "2", "3").contains(type)) throw new IllegalArgumentException("Invalid vehicle type.");
        String number = read(input, "Vehicle number (5 characters)");
        String manufacturer = read(input, "Manufacturer (Honda/Yamaha/Toyota/Suzuki)");
        int year = Integer.parseInt(read(input, "Manufacture year"));
        String color = read(input, "Color");
        String identity = read(input, "Owner's identity number (12 digits)");
        Owner owner = registry.findOwner(identity);
        if (owner == null) owner = new Owner(identity, read(input, "Owner name"), read(input, "Owner email"));
        else System.out.println("Using existing owner: " + owner);
        return switch (type) {
            case "1" -> new Car(number, manufacturer, year, color, owner,
                    Integer.parseInt(read(input, "Seats")), read(input, "Engine type"));
            case "2" -> new Motorcycle(number, manufacturer, year, color, owner,
                    Double.parseDouble(read(input, "Capacity (cc, exercise convention)")));
            default -> new Truck(number, manufacturer, year, color, owner,
                    Double.parseDouble(read(input, "Tonnage (tons)")));
        };
    }

    private static String read(Scanner input, String label) {
        System.out.print(label + ": ");
        return input.nextLine().strip();
    }

    private static void print(List<Vehicle> vehicles) {
        if (vehicles.isEmpty()) System.out.println("No vehicles.");
        else vehicles.forEach(System.out::println);
    }
}
