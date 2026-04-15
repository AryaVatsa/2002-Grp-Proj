package strategy;

import combatants.Combatant;
import java.util.List;

public interface TurnOrderStrategy {
    List<Combatant> determine(List<Combatant> combatants);
}
