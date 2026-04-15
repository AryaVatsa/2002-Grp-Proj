package actions;

import combatants.Combatant;
import java.util.List;

public interface Action {
    void execute(Combatant actor, List<Combatant> targets, List<String> log);
    String getName();
}
