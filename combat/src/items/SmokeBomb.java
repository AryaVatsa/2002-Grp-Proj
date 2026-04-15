package items;

import combatants.Combatant;
import combatants.Player;
import java.util.List;

public class SmokeBomb implements Item {

    @Override
    public void use(Player player, List<Combatant> targets, List<String> log) {
        player.applySmokeBomb();
        log.add(player.getName() + " uses Smoke Bomb! Enemy attacks deal 0 damage this turn and next.");
    }

    @Override
    public String getName() { return "Smoke Bomb"; }
}
