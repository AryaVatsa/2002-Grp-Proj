package combatants;

public class Wolf extends Enemy {

    private static int count = 0;

    public Wolf() {
        super("Wolf " + (char)('A' + count++), 40, 45, 5, 35);
    }

    public static void resetCounter() { count = 0; }
}
