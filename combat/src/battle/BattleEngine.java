package battle;

import actions.Action;
import actions.BasicAttackAction;
import combatants.Combatant;
import combatants.Enemy;
import combatants.Player;
import effects.StatusEffect;
import effects.StunEffect;
import strategy.TurnOrderStrategy;
import ui.BattleUI;
import java.util.ArrayList;
import java.util.List;

public class BattleEngine {

    private final Player player;
    private final Level level;
    private final TurnOrderStrategy turnOrder;
    private final BattleUI ui;

    private final List<Combatant> allEnemies = new ArrayList<>();
    private final List<Combatant> activeEnemies = new ArrayList<>();

    private int round = 0;

    public BattleEngine(Player player, Level level, TurnOrderStrategy turnOrder, BattleUI ui) {
        this.player = player;
        this.level = level;
        this.turnOrder = turnOrder;
        this.ui = ui;

        for (Enemy e : level.getInitialWave()) {
            allEnemies.add(e);
            activeEnemies.add(e);
        }
    }

    public boolean run() {
        ui.display("\n--- Battle Start! ---");
        announceEnemies();

        while (true) {
            round++;
            ui.display("\n--- ROUND " + round + " ---");

            checkBackupSpawn();

            List<Combatant> order = buildTurnOrder();

            for (Combatant actor : order) {
                if (!actor.isAlive()) continue;

                actor.tickDefendBonus();

                if (isStunned(actor)) {
                    ui.display(actor.getName() + " is STUNNED - turn skipped!");
                    List<String> stunLog = actor.processStatusEffects();
                    for (String s : stunLog) ui.display(s);
                    continue;
                }

                List<String> effectLog = actor.processStatusEffects();
                for (String s : effectLog) ui.display(s);

                if (actor instanceof Player p) {
                    ui.displayBattleState(p, allEnemies, round);

                    List<Combatant> alive = new ArrayList<>();
                    for (Combatant c : activeEnemies) {
                        if (c.isAlive()) alive.add(c);
                    }

                    if (alive.isEmpty()) break;

                    List<Combatant> all = new ArrayList<>(activeEnemies);
                    all.add(p);

                    Action action = ui.selectPlayerAction(p, alive, all);
                    List<String> log = new ArrayList<>();
                    action.execute(p, all, log);
                    for (String s : log) ui.display(s);

                    p.tickCooldown();

                } else if (actor instanceof Enemy enemy) {
                    List<Combatant> targets = new ArrayList<>();
                    targets.add(player);
                    List<String> log = new ArrayList<>();
                    Action atk = new BasicAttackAction(player);
                    atk.execute(enemy, targets, log);
                    for (String s : log) ui.display(s);
                }

                if (checkPlayerDefeated()) return false;
                if (checkAllEnemiesDefeated()) return true;
            }

            showEndOfRound();

            if (checkPlayerDefeated()) return false;
            if (checkAllEnemiesDefeated()) return true;
        }
    }

    private List<Combatant> buildTurnOrder() {
        List<Combatant> all = new ArrayList<>(activeEnemies);
        all.add(player);
        List<Combatant> alive = new ArrayList<>();
        for (Combatant c : all) {
            if (c.isAlive()) alive.add(c);
        }
        return turnOrder.determine(alive);
    }

    private void checkBackupSpawn() {
        if (!level.isBackupSpawned() && level.hasBackup()) {
            boolean allDead = true;
            for (Combatant c : activeEnemies) {
                if (c.isAlive()) { allDead = false; break; }
            }
            if (allDead && !activeEnemies.isEmpty()) {
                level.markBackupSpawned();
                ui.display("\nBACKUP SPAWN! Reinforcements arrive!");
                for (Enemy e : level.getBackupWave()) {
                    allEnemies.add(e);
                    activeEnemies.add(e);
                    ui.display("  " + e.getName() + " joins the battle! (HP: " + e.getCurrentHp() + ")");
                }
            }
        }
    }

    private boolean checkPlayerDefeated() {
        if (!player.isAlive()) {
            ui.display("\n" + player.getName() + " has been defeated!");
            ui.showDefeat(allEnemies, round);
            return true;
        }
        return false;
    }

    private boolean checkAllEnemiesDefeated() {
        boolean allDead = true;
        for (Combatant c : activeEnemies) {
            if (c.isAlive()) { allDead = false; break; }
        }
        boolean backupDone = level.isBackupSpawned() || !level.hasBackup();
        if (allDead && backupDone) {
            ui.display("\nAll enemies defeated!");
            ui.showVictory(player, round);
            return true;
        }
        return false;
    }

    private boolean isStunned(Combatant c) {
        for (StatusEffect e : c.getStatusEffects()) {
            if (e instanceof StunEffect && ((StunEffect) e).isActive()) return true;
        }
        return false;
    }

    private void announceEnemies() {
        ui.display("Enemies:");
        for (Combatant e : activeEnemies) {
            ui.display("  " + e.getName()
                    + " [HP: " + e.getCurrentHp()
                    + " | ATK: " + e.getAttack()
                    + " | DEF: " + e.getDefense()
                    + " | SPD: " + e.getSpeed() + "]");
        }
    }

    private void showEndOfRound() {
        ui.display("\n-- End of Round " + round + " --");
        ui.display("  " + player.getName() + " HP: " + player.getCurrentHp() + "/" + player.getMaxHp());
        for (Combatant e : allEnemies) {
            if (e.isAlive()) {
                String stun = isStunned(e) ? " [STUNNED]" : "";
                ui.display("  " + e.getName() + " HP: " + e.getCurrentHp() + "/" + e.getMaxHp() + stun);
            } else {
                ui.display("  " + e.getName() + " HP: dead");
            }
        }
        if (!player.getInventory().isEmpty()) {
            String inv = "  Items: ";
            for (int i = 0; i < player.getInventory().size(); i++) {
                inv += "[" + player.getInventory().get(i).getName() + "] ";
            }
            ui.display(inv);
        }
        ui.display("  Cooldown: " + player.getSpecialSkillCooldown() + " round(s)");
    }

    public int getRound() { return round; }
}
