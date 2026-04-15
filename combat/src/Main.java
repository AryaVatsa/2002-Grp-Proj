import battle.BattleEngine;
import battle.Level;
import battle.Level.Difficulty;
import combatants.*;
import items.*;
import strategy.SpeedBasedTurnOrder;
import ui.BattleUI;
import ui.CLIBattleUI;
import java.util.Scanner;

public class Main {

    private static Scanner scanner = new Scanner(System.in);
    private static BattleUI ui;

    public static void main(String[] args) {
        ui = new CLIBattleUI(scanner);
        showTitle();

        boolean running = true;
        Player lastPlayer = null;
        Difficulty lastDifficulty = null;

        while (running) {
            Player player;
            Difficulty difficulty;

            if (lastPlayer != null) {
                String choice = ui.showEndMenu();
                switch (choice) {
                    case "replay" -> {
                        player = copyPlayer(lastPlayer);
                        difficulty = lastDifficulty;
                    }
                    case "new" -> {
                        player = pickPlayer();
                        difficulty = pickDifficulty();
                    }
                    default -> {
                        ui.display("Thanks for playing!");
                        running = false;
                        continue;
                    }
                }
            } else {
                player = pickPlayer();
                difficulty = pickDifficulty();
            }

            pickItems(player);

            lastPlayer = player;
            lastDifficulty = difficulty;

            Level level = new Level(difficulty);
            BattleEngine engine = new BattleEngine(player, level, new SpeedBasedTurnOrder(), ui);
            engine.run();
        }
    }

    private static void showTitle() {
        System.out.println("==============================");
        System.out.println("   TURN-BASED COMBAT ARENA");
        System.out.println("==============================");
        System.out.println();
        System.out.println("=== PLAYER CLASSES ===");
        System.out.println("  1. Warrior");
        System.out.println("     HP: 260 | ATK: 40 | DEF: 20 | SPD: 30");
        System.out.println("     Special: Shield Bash - damage + stun target 2 turns");
        System.out.println();
        System.out.println("  2. Wizard");
        System.out.println("     HP: 200 | ATK: 50 | DEF: 10 | SPD: 20");
        System.out.println("     Special: Arcane Blast - hit all enemies; +10 ATK per kill");
        System.out.println();
        System.out.println("=== ENEMIES ===");
        System.out.println("  Goblin  HP: 55 | ATK: 35 | DEF: 15 | SPD: 25");
        System.out.println("  Wolf    HP: 40 | ATK: 45 | DEF: 5  | SPD: 35");
        System.out.println();
        System.out.println("=== DIFFICULTY ===");
        System.out.println("  Easy   - 3 Goblins");
        System.out.println("  Medium - 1 Goblin + 1 Wolf  | Backup: 2 Wolves");
        System.out.println("  Hard   - 2 Goblins          | Backup: 1 Goblin + 2 Wolves");
        System.out.println();
        System.out.println("=== ITEMS (pick 2) ===");
        System.out.println("  Potion      - Heal 100 HP");
        System.out.println("  Power Stone - Trigger special skill free");
        System.out.println("  Smoke Bomb  - Block all enemy damage for 2 turns");
        System.out.println("==============================");
        System.out.println();
    }

    private static Player pickPlayer() {
        System.out.println("Choose your class:");
        System.out.println("  1. Warrior");
        System.out.println("  2. Wizard");
        System.out.print("Enter: ");
        while (true) {
            String in = scanner.nextLine().trim();
            switch (in) {
                case "1" -> { return new Warrior(); }
                case "2" -> { return new Wizard(); }
                default  -> System.out.print("Invalid. Enter 1 or 2: ");
            }
        }
    }

    private static Difficulty pickDifficulty() {
        System.out.println("\nChoose difficulty:");
        System.out.println("  1. Easy   (3 Goblins)");
        System.out.println("  2. Medium (1 Goblin + 1 Wolf)");
        System.out.println("  3. Hard   (2 Goblins + backup wave)");
        System.out.print("Enter: ");
        while (true) {
            String in = scanner.nextLine().trim();
            switch (in) {
                case "1" -> { return Difficulty.EASY; }
                case "2" -> { return Difficulty.MEDIUM; }
                case "3" -> { return Difficulty.HARD; }
                default  -> System.out.print("Invalid. Enter 1, 2, or 3: ");
            }
        }
    }

    private static void pickItems(Player player) {
        System.out.println("\nPick 2 items (duplicates allowed):");
        for (int i = 1; i <= 2; i++) {
            System.out.println("Item " + i + ":");
            System.out.println("  1. Potion      (Heal 100 HP)");
            System.out.println("  2. Power Stone (Free special skill)");
            System.out.println("  3. Smoke Bomb  (Block enemy dmg for 2 turns)");
            System.out.print("Enter: ");
            while (true) {
                String in = scanner.nextLine().trim();
                switch (in) {
                    case "1" -> { player.addItem(new Potion());          break; }
                    case "2" -> { player.addItem(new PowerStone(ui));    break; }
                    case "3" -> { player.addItem(new SmokeBomb());       break; }
                    default  -> { System.out.print("Invalid. Enter 1-3: "); continue; }
                }
                break;
            }
        }
        System.out.print("Your items: ");
        for (int i = 0; i < player.getInventory().size(); i++) {
            System.out.print("[" + player.getInventory().get(i).getName() + "] ");
        }
        System.out.println();
    }

    private static Player copyPlayer(Player original) {
        if (original instanceof Warrior) return new Warrior();
        if (original instanceof Wizard)  return new Wizard();
        throw new IllegalStateException("Unknown player type");
    }
}
