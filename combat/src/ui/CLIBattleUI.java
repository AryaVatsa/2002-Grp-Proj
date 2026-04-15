package ui;

import actions.*;
import combatants.*;
import items.Item;
import java.util.List;
import java.util.Scanner;

public class CLIBattleUI implements BattleUI {

    private final Scanner scanner;

    public CLIBattleUI(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void display(String message) {
        System.out.println(message);
    }

    @Override
    public void displayBattleState(Player player, List<Combatant> enemies, int round) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("  ROUND " + round);
        System.out.println("=".repeat(60));

        String smoke = player.isSmokeBombActive()
                ? " [SMOKE BOMB: " + player.getSmokeBombTurnsRemaining() + "]" : "";
        String defend = player.getTempDefenseBonus() > 0
                ? " [DEFENDING +" + player.getTempDefenseBonus() + "]" : "";
        System.out.printf("  %-20s HP: %d/%d%s%s%n",
                player.getName(), player.getCurrentHp(), player.getMaxHp(), smoke, defend);
        System.out.printf("  ATK: %-5d DEF: %-5d SPD: %-5d%n",
                player.getAttack(), player.getEffectiveDefense(), player.getSpeed());

        if (!player.getInventory().isEmpty()) {
            StringBuilder inv = new StringBuilder("  Items: ");
            for (Item it : player.getInventory()) inv.append("[").append(it.getName()).append("] ");
            System.out.println(inv);
        } else {
            System.out.println("  Items: (none)");
        }

        if (player.isSpecialSkillReady()) {
            System.out.println("  Special Skill: READY");
        } else {
            System.out.println("  Cooldown: " + player.getSpecialSkillCooldown() + " round(s)");
        }

        System.out.println("  " + "-".repeat(56));

        for (Combatant e : enemies) {
            String stun = e.hasEffect("Stun") ? " [STUNNED]" : "";
            if (e.isAlive()) {
                System.out.printf("  %-20s HP: %d/%d%s%n",
                        e.getName(), e.getCurrentHp(), e.getMaxHp(), stun);
            } else {
                System.out.printf("  %-20s ELIMINATED%n", e.getName());
            }
        }
        System.out.println("=".repeat(60));
    }

    @Override
    public Action selectPlayerAction(Player player, List<Combatant> enemies, List<Combatant> allCombatants) {
        while (true) {
            System.out.println("\nChoose action:");
            System.out.println("  1. Basic Attack");
            System.out.println("  2. Defend");

            boolean hasItems = player.hasItems();
            boolean skillReady = player.isSpecialSkillReady();

            if (hasItems) System.out.println("  3. Use Item");
            if (skillReady) {
                System.out.println("  4. " + player.getSpecialSkillName() + " (Special Skill)");
            } else {
                System.out.println("  4. " + player.getSpecialSkillName() + " [COOLDOWN: " + player.getSpecialSkillCooldown() + "]");
            }

            System.out.print("Enter: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1" -> {
                    Combatant target = selectTarget(enemies);
                    return new BasicAttackAction(target);
                }
                case "2" -> { return new DefendAction(); }
                case "3" -> {
                    if (!hasItems) { System.out.println("No items!"); continue; }
                    Item chosen = selectItem(player.getInventory());
                    return new ItemAction(player, chosen);
                }
                case "4" -> {
                    if (!skillReady) { System.out.println("Skill not ready!"); continue; }
                    return buildSpecialSkill(player, enemies, allCombatants);
                }
                default -> System.out.println("Invalid, try again.");
            }
        }
    }

    private Action buildSpecialSkill(Player player, List<Combatant> enemies, List<Combatant> allCombatants) {
        if (player instanceof Warrior w) {
            System.out.println("Shield Bash - pick a target:");
            Combatant t = selectTarget(enemies);
            return new ShieldBashAction(w, t);
        } else if (player instanceof Wizard wiz) {
            return new ArcaneBlastAction(wiz, false);
        }
        throw new IllegalStateException("Unknown player type");
    }

    @Override
    public Combatant selectTarget(List<Combatant> targets) {
        while (true) {
            System.out.println("Select target:");
            for (int i = 0; i < targets.size(); i++) {
                Combatant t = targets.get(i);
                System.out.printf("  %d. %s (HP: %d/%d)%n", i + 1, t.getName(), t.getCurrentHp(), t.getMaxHp());
            }
            System.out.print("Enter: ");
            try {
                int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (idx >= 0 && idx < targets.size()) return targets.get(idx);
            } catch (NumberFormatException e) {}
            System.out.println("Invalid.");
        }
    }

    @Override
    public Item selectItem(List<Item> inventory) {
        while (true) {
            System.out.println("Select item:");
            for (int i = 0; i < inventory.size(); i++) {
                System.out.printf("  %d. %s%n", i + 1, inventory.get(i).getName());
            }
            System.out.print("Enter: ");
            try {
                int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (idx >= 0 && idx < inventory.size()) return inventory.get(idx);
            } catch (NumberFormatException e) {}
            System.out.println("Invalid.");
        }
    }

    @Override
    public void showVictory(Player player, int rounds) {
        System.out.println("\n" + "*".repeat(60));
        System.out.println("  YOU WIN! All enemies defeated!");
        System.out.printf("  HP Remaining: %d / %d%n", player.getCurrentHp(), player.getMaxHp());
        System.out.printf("  Rounds: %d%n", rounds);
        if (!player.getInventory().isEmpty()) {
            System.out.print("  Items left: ");
            for (Item i : player.getInventory()) {
                System.out.print("[" + i.getName() + "] ");
            }
            System.out.println();
        }
        System.out.println("*".repeat(60));
    }

    @Override
    public void showDefeat(List<Combatant> remainingEnemies, int rounds) {
        int alive = 0;
        for (Combatant c : remainingEnemies) {
            if (c.isAlive()) alive++;
        }
        System.out.println("\n" + "X".repeat(60));
        System.out.println("  GAME OVER. You were defeated!");
        System.out.println("  Enemies Still Alive: " + alive);
        System.out.printf("  Rounds Survived: %d%n", rounds);
        System.out.println("X".repeat(60));
    }

    @Override
    public String showEndMenu() {
        System.out.println("\nWhat now?");
        System.out.println("  1. Replay same settings");
        System.out.println("  2. New game");
        System.out.println("  3. Exit");
        System.out.print("Enter: ");
        while (true) {
            String in = scanner.nextLine().trim();
            switch (in) {
                case "1" -> { return "replay"; }
                case "2" -> { return "new"; }
                case "3" -> { return "exit"; }
                default  -> System.out.print("Invalid. Enter 1, 2 or 3: ");
            }
        }
    }
}
