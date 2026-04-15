package items;

import combatants.Combatant;
import combatants.Player;
import java.util.List;

public interface Item {
    void use(Player player, List<Combatant> targets, List<String> log);
    String getName();
}
