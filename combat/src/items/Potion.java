package items;

import combatants.Combatant;
import combatants.Player;
import java.util.List;

public class Potion implements Item {

    private static final int HEAL = 100;

    @Override
    public void use(Player player, List<Combatant> targets, List<String> log) {
        int before = player.getCurrentHp();
        player.heal(HEAL);
        int after = player.getCurrentHp();
        log.add(String.format("%s uses Potion: HP %d -> %d (+%d)",
                player.getName(), before, after, after - before));
    }

    @Override
    public String getName() { return "Potion"; }
}
