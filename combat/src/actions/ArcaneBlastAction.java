package actions;

import combatants.Combatant;
import combatants.Enemy;
import combatants.Wizard;
import java.util.List;

public class ArcaneBlastAction implements Action {

    private final Wizard wizard;
    private final boolean fromPowerStone;

    public ArcaneBlastAction(Wizard wizard, boolean fromPowerStone) {
        this.wizard = wizard;
        this.fromPowerStone = fromPowerStone;
    }

    @Override
    public void execute(Combatant actor, List<Combatant> allCombatants, List<String> log) {
        log.add(actor.getName() + " unleashes Arcane Blast on all enemies!");

        for (Combatant t : allCombatants) {
            if (t instanceof Enemy && t.isAlive()) {
                int dmg = Math.max(0, actor.getAttack() - t.getEffectiveDefense());
                int before = t.getCurrentHp();
                t.takeDamage(dmg);
                int after = t.getCurrentHp();

                String note = t.isAlive() ? "" : " X ELIMINATED";
                log.add(String.format("  %s: HP %d -> %d (dmg: %d-%d=%d)%s",
                        t.getName(), before, after,
                        actor.getAttack(), t.getEffectiveDefense(), dmg, note));

                if (!t.isAlive()) {
                    wizard.applyArcaneBlastKillBonus();
                    log.add("  Kill bonus! Wizard ATK -> " + wizard.getAttack());
                }
            }
        }

        if (!fromPowerStone) {
            wizard.setSpecialSkillCooldown(3);
            log.add("Arcane Blast cooldown: 3 rounds.");
        } else {
            log.add("Arcane Blast via Power Stone - cooldown unchanged.");
        }
    }

    @Override
    public String getName() { return "Arcane Blast (Special Skill)"; }
}
