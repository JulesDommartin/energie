package data;

public class Vehicules {
    private static Vehicules ourInstance = new Vehicules();

    public static Vehicules getInstance() {
        return ourInstance;
    }

    private Vehicules() {
    }
}
