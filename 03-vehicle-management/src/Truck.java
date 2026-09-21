public class Truck extends Vehicle {
    private final double tonnage;

    public Truck(String number, String manufacturer, int year, String color, Owner owner, double tonnage) {
        super(number, manufacturer, year, color, owner);
        this.tonnage = requirePositive(tonnage, "Tonnage");
    }

    @Override
    public String toString() { return super.toString() + " tonnage=" + tonnage; }
}
