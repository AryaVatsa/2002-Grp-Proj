package ui;

import actions.Action;
import combatants.Combatant;
import combatants.Player;
import items.Item;
import java.util.List;

public interface BattleUI {
    void display(String message);
    void displayBattleState(Player player, List<Combatant> enemies, int round);
    Action selectPlayerAction(Player player, List<Combatant> enemies, List<Combatant> allCombatants);
    Combatant selectTarget(List<Combatant> targets);
    Item selectItem(List<Item> inventory);
    void showVictory(Player player, int rounds);
    void showDefeat(List<Combatant> remainingEnemies, int rounds);
    String showEndMenu();
}
