package items;

import actions.ArcaneBlastAction;
import combatants.Combatant;
import combatants.Enemy;
import combatants.Player;
import combatants.Warrior;
import combatants.Wizard;
import effects.StunEffect;
import ui.BattleUI;
import java.util.ArrayList;
import java.util.List;

public class PowerStone implements Item {

    private final BattleUI ui;

    public PowerStone(BattleUI ui) {
        this.ui = ui;
    }

    @Override
    public void use(Player player, List<Combatant> targets, List<String> log) {
        log.add(player.getName() + " uses Power Stone - Special Skill triggered for free!");

        if (player instanceof Warrior warrior) {
            List<Combatant> enemies = new ArrayList<>();
            for (Combatant t : targets) {
                if (t instanceof Enemy && t.isAlive()) enemies.add(t);
            }
            if (enemies.isEmpty()) {
                log.add("No enemies to target!");
                return;
            }
            Combatant target = ui.selectTarget(enemies);
            int dmg = Math.max(0, warrior.getAttack() - target.getEffectiveDefense());
            int before = target.getCurrentHp();
            target.takeDamage(dmg);
            int after = target.getCurrentHp();
            String elim = target.isAlive() ? "" : " X ELIMINATED";
            log.add(String.format("  Shield Bash (Power Stone) -> %s: HP %d -> %d (dmg: %d-%d=%d)%s",
                    target.getName(), before, after,
                    warrior.getAttack(), target.getEffectiveDefense(), dmg, elim));
            if (target.isAlive()) {
                target.addStatusEffect(new StunEffect(2));
                log.add("  " + target.getName() + " is STUNNED!");
            }

        } else if (player instanceof Wizard wizard) {
            ArcaneBlastAction blast = new ArcaneBlastAction(wizard, true);
            blast.execute(player, targets, log);
        }
    }

    @Override
    public String getName() { return "Power Stone"; }
}
