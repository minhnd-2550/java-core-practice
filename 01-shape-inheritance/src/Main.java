import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Shape shape = new Shape(3, 4);
        Rectangle rectangle = new Rectangle(5, 8);
        Circle circle = new Circle(3);

        System.out.println(shape);

        System.out.println(rectangle);
        System.out.printf(Locale.ROOT, "Area: %.2f%n", rectangle.getArea());
        System.out.printf(Locale.ROOT, "Perimeter: %.2f%n", rectangle.getPerimeter());

        System.out.println(circle);
        System.out.printf(Locale.ROOT, "Area: %.2f%n", circle.getArea());
        System.out.printf(Locale.ROOT, "Circumference: %.2f%n", circle.getCircumference());

        // Run with -ea to enable these small correctness checks.
        assert rectangle.getArea() == 40 : "Incorrect rectangle area";
        assert rectangle.getPerimeter() == 26 : "Incorrect rectangle perimeter";
        assert Math.abs(circle.getArea() - 28.274333882308138) < 1e-9
                : "Incorrect circle area";
        assert Math.abs(circle.getCircumference() - 18.84955592153876) < 1e-9
                : "Incorrect circle circumference";
    }
}
