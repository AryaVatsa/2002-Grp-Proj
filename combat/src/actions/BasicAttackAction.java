package actions;

import combatants.Combatant;
import combatants.Player;
import java.util.List;

public class BasicAttackAction implements Action {

    private final Combatant target;

    public BasicAttackAction(Combatant target) {
        this.target = target;
    }

    @Override
    public void execute(Combatant actor, List<Combatant> targets, List<String> log) {
        if (actor instanceof combatants.Enemy && target instanceof Player p && p.isSmokeBombActive()) {
            log.add(actor.getName() + " -> BasicAttack -> " + target.getName() + ": 0 damage (Smoke Bomb active)");
            p.tickSmokeBomb();
            if (!p.isSmokeBombActive()) log.add("Smoke Bomb wears off.");
            return;
        }

        int dmg = Math.max(0, actor.getAttack() - target.getEffectiveDefense());
        int before = target.getCurrentHp();
        target.takeDamage(dmg);
        int after = target.getCurrentHp();

        String note = target.isAlive() ? "" : " X ELIMINATED";
        log.add(String.format("%s -> BasicAttack -> %s: HP %d -> %d (dmg: %d-%d=%d)%s",
                actor.getName(), target.getName(), before, after,
                actor.getAttack(), target.getEffectiveDefense(), dmg, note));
    }

    @Override
    public String getName() { return "Basic Attack"; }
}
