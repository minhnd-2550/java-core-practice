import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--check")) { SelfCheck.run(); return; }
        Inventory inventory = new Inventory();
        try (Scanner input = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n1. Add food  2. Add electronics  3. Add crockery  4. Report  0. Exit");
                if (!input.hasNextLine()) return;
                String choice = input.nextLine().strip();
                if (choice.equals("0")) return;
                if (choice.equals("4")) { report(inventory); continue; }
                if (!List.of("1", "2", "3").contains(choice)) { System.out.println("Invalid choice."); continue; }
                try {
                    String code = read(input, "Code");
                    String name = read(input, "Name");
                    int quantity = Integer.parseInt(read(input, "Quantity"));
                    BigDecimal price = new BigDecimal(read(input, "Unit price before VAT"));
                    Product product = switch (choice) {
                        case "1" -> new Food(code, name, quantity, price,
                                LocalDate.parse(read(input, "Manufactured on (yyyy-MM-dd)")),
                                LocalDate.parse(read(input, "Expires on (yyyy-MM-dd)")), read(input, "Supplier"));
                        case "2" -> new Electronics(code, name, quantity, price,
                                Integer.parseInt(read(input, "Warranty months")), Double.parseDouble(read(input, "Power kW")));
                        default -> new Crockery(code, name, quantity, price, read(input, "Manufacturer"),
                                LocalDate.parse(read(input, "Arrived on (yyyy-MM-dd)")));
                    };
                    inventory.add(product);
                    System.out.println("Added: " + product.getCode());
                } catch (IllegalArgumentException | java.time.DateTimeException e) {
                    System.out.println("Invalid input: " + e.getMessage() + " Please add the product again.");
                } catch (java.util.NoSuchElementException e) { return; }
            }
        }
    }

    private static String read(Scanner input, String label) {
        System.out.print(label + ": ");
        return input.nextLine().strip();
    }

    private static void report(Inventory inventory) {
        LocalDate today = LocalDate.now();
        for (Product product : inventory.getProducts()) {
            System.out.println(product + " | " + product.evaluateConsumption(today)
                    + " | unit VAT=" + product.getUnitVat() + " | inventory VAT=" + product.getInventoryVat());
        }
        for (Class<? extends Product> type : List.of(Food.class, Electronics.class, Crockery.class)) {
            System.out.println(type.getSimpleName() + ": quantity=" + inventory.totalQuantity(type)
                    + ", VAT=" + inventory.totalVat(type));
        }
    }
}
