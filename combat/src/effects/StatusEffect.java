package effects;

import combatants.Combatant;
import java.util.List;

public interface StatusEffect {
    void tick(Combatant target, List<String> log);
    boolean isExpired();
    String getName();
}
