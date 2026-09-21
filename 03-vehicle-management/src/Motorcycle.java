public class Motorcycle extends Vehicle {
    private final double capacity;

    public Motorcycle(String number, String manufacturer, int year, String color, Owner owner, double capacity) {
        super(number, manufacturer, year, color, owner);
        this.capacity = requirePositive(capacity, "Capacity");
    }

    @Override
    public String toString() { return super.toString() + " capacity=" + capacity; }
}
