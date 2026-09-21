public record Owner(String identityNumber, String fullName, String email) {
    public Owner {
        if (identityNumber == null || !identityNumber.matches("[0-9]{12}")) {
            throw new IllegalArgumentException("Identity number must contain exactly 12 digits.");
        }
        fullName = Vehicle.requireText(fullName, "Owner name");
        email = Vehicle.requireText(email, "Email");
        // Basic exercise validation; not a full RFC email parser.
        if (!email.matches("[^\\s@]+@[^\\s@.]+(?:\\.[^\\s@.]+)+")) {
            throw new IllegalArgumentException("Invalid email format.");
        }
    }
}
