public class Car extends Vehicle {
    private final int seats;
    private final String engineType;

    public Car(String number, String manufacturer, int year, String color, Owner owner, int seats, String engineType) {
        super(number, manufacturer, year, color, owner);
        if (seats <= 0) throw new IllegalArgumentException("Seats must be > 0.");
        this.seats = seats;
        this.engineType = requireText(engineType, "Engine type");
    }

    @Override
    public String toString() { return super.toString() + " seats=" + seats + ", engineType=" + engineType; }
}
