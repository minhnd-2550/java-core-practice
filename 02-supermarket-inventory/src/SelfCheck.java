import java.math.BigDecimal;
import java.time.LocalDate;

public class SelfCheck {
    public static void run() {
        LocalDate today = LocalDate.of(2026, 9, 21);
        BigDecimal price = new BigDecimal("100.00");
        Inventory inventory = new Inventory();
        Food food = new Food("F1", "Milk", 2, price, today.minusDays(2), today.minusDays(1), "Supplier");
        inventory.add(food);
        assert food.evaluateConsumption(today).equals("HARD_TO_SELL");
        assert food.getUnitVat().compareTo(new BigDecimal("5")) == 0;
        assert inventory.totalVat(Food.class).compareTo(new BigDecimal("10")) == 0;
        assert inventory.totalQuantity(Food.class) == 2;
        assert new Food("F2", "Milk", 1, price, today, today, "Supplier")
                .evaluateConsumption(today).equals("NOT_EVALUATED");
        assert new Food("F3", "Milk", 0, price, today.minusDays(2), today.minusDays(1), "Supplier")
                .evaluateConsumption(today).equals("NOT_EVALUATED");
        for (int quantity : new int[]{0, 2, 3}) {
            Electronics item = new Electronics("E" + quantity, "Fan", quantity, price, 0, 0);
            assert item.evaluateConsumption(today).equals(quantity < 3 ? "SELLING_WELL" : "NOT_EVALUATED");
            assert item.getUnitVat().compareTo(new BigDecimal("10")) == 0;
            inventory.add(item);
        }
        for (int quantity : new int[]{50, 51}) {
            for (int days : new int[]{10, 11}) {
                Crockery item = new Crockery("C" + quantity + days, "Plate", quantity, price,
                        "Maker", today.minusDays(days));
                assert item.evaluateConsumption(today).equals(quantity > 50 && days > 10 ? "SLOW_SELLING" : "NOT_EVALUATED");
                assert item.getUnitVat().compareTo(new BigDecimal("10")) == 0;
            }
        }
        rejects(() -> inventory.add(new Electronics("f1", "Fan", 1, price, 1, 1)));
        rejects(() -> new Food("F", "Food", 1, price, today, today.minusDays(1), "Supplier"));
        rejects(() -> new Electronics("E", "Fan", -1, price, 1, 1));
        rejects(() -> new Electronics("E", "Fan", 1, price.negate(), 1, 1));
        rejects(() -> new Electronics("E", "Fan", 1, price, -1, 1));
        rejects(() -> new Electronics("E", "Fan", 1, price, 1, Double.NaN));
        for (int i = 0; i < 20; i++) inventory.add(new Electronics("Extra" + i, "Fan", 1, price, 0, 0));
        assert inventory.getProducts().length == 24;
        Product[] snapshot = inventory.getProducts();
        snapshot[0] = null;
        assert inventory.getProducts()[0] == food;
        System.out.println("Inventory checks passed.");
    }

    private static void rejects(Runnable action) {
        try { action.run(); } catch (IllegalArgumentException expected) { return; }
        throw new AssertionError("Expected invalid input to be rejected.");
    }
}
