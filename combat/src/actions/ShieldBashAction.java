package actions;

import combatants.Combatant;
import combatants.Warrior;
import effects.StunEffect;
import java.util.List;

public class ShieldBashAction implements Action {

    private final Warrior warrior;
    private final Combatant target;

    public ShieldBashAction(Warrior warrior, Combatant target) {
        this.warrior = warrior;
        this.target = target;
    }

    @Override
    public void execute(Combatant actor, List<Combatant> targets, List<String> log) {
        int dmg = Math.max(0, actor.getAttack() - target.getEffectiveDefense());
        int before = target.getCurrentHp();
        target.takeDamage(dmg);
        int after = target.getCurrentHp();

        String note = target.isAlive() ? "" : " X ELIMINATED";
        log.add(String.format("%s -> Shield Bash -> %s: HP %d -> %d (dmg: %d-%d=%d)%s",
                actor.getName(), target.getName(), before, after,
                actor.getAttack(), target.getEffectiveDefense(), dmg, note));

        if (target.isAlive()) {
            target.addStatusEffect(new StunEffect(2));
            log.add(target.getName() + " is STUNNED for 2 turns!");
        }

        warrior.setSpecialSkillCooldown(3);
        log.add("Shield Bash cooldown: 3 rounds.");
    }

    @Override
    public String getName() { return "Shield Bash (Special Skill)"; }
}
