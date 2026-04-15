package actions;

import combatants.Combatant;
import java.util.List;

public class DefendAction implements Action {

    @Override
    public void execute(Combatant actor, List<Combatant> targets, List<String> log) {
        actor.applyDefendBonus();
        log.add(actor.getName() + " takes a defensive stance! DEF +10 for this round and next.");
    }

    @Override
    public String getName() { return "Defend"; }
}
