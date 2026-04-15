package effects;

import combatants.Combatant;
import java.util.List;

public class StunEffect implements StatusEffect {

    private int turnsLeft;

    public StunEffect(int turns) {
        this.turnsLeft = turns;
    }

    @Override
    public void tick(Combatant target, List<String> log) {
        turnsLeft--;
        if (turnsLeft <= 0) {
            log.add(target.getName() + "'s Stun expired.");
        } else {
            log.add(target.getName() + " is STUNNED (" + turnsLeft + " turn(s) left).");
        }
    }

    public boolean isActive() { return turnsLeft > 0; }

    @Override
    public boolean isExpired() { return turnsLeft <= 0; }

    @Override
    public String getName() { return "Stun"; }

    public int getTurnsRemaining() { return turnsLeft; }
}
