package combatants;

import effects.StatusEffect;
import java.util.ArrayList;
import java.util.List;

public abstract class Combatant {

    protected String name;
    protected int maxHp;
    protected int currentHp;
    protected int attack;
    protected int defense;
    protected int speed;

    private int tempDefenseBonus;
    private int tempDefenseTurns;

    private List<StatusEffect> statusEffects = new ArrayList<>();

    public Combatant(String name, int hp, int attack, int defense, int speed) {
        this.name = name;
        this.maxHp = hp;
        this.currentHp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.tempDefenseBonus = 0;
        this.tempDefenseTurns = 0;
    }

    public void takeDamage(int damage) {
        currentHp = Math.max(0, currentHp - damage);
    }

    public void heal(int amount) {
        currentHp = Math.min(maxHp, currentHp + amount);
    }

    public boolean isAlive() {
        return currentHp > 0;
    }

    public void applyDefendBonus() {
        this.tempDefenseBonus = 10;
        this.tempDefenseTurns = 2;
    }

    public int getEffectiveDefense() {
        return defense + tempDefenseBonus;
    }

    public void tickDefendBonus() {
        if (tempDefenseTurns > 0) {
            tempDefenseTurns--;
            if (tempDefenseTurns == 0) {
                tempDefenseBonus = 0;
            }
        }
    }

    public void addStatusEffect(StatusEffect effect) {
        statusEffects.add(effect);
    }

    public void removeStatusEffect(StatusEffect effect) {
        statusEffects.remove(effect);
    }

    public List<StatusEffect> getStatusEffects() {
        return statusEffects;
    }

    public boolean hasEffect(String effectName) {
        for (StatusEffect e : statusEffects) {
            if (e.getName().equalsIgnoreCase(effectName)) return true;
        }
        return false;
    }

    public List<String> processStatusEffects() {
        List<String> log = new ArrayList<>();
        List<StatusEffect> expired = new ArrayList<>();
        for (StatusEffect effect : statusEffects) {
            effect.tick(this, log);
            if (effect.isExpired()) expired.add(effect);
        }
        statusEffects.removeAll(expired);
        return log;
    }

    public String getName()      { return name; }
    public int getMaxHp()        { return maxHp; }
    public int getCurrentHp()    { return currentHp; }
    public int getAttack()       { return attack; }
    public int getDefense()      { return defense; }
    public int getSpeed()        { return speed; }
    public int getTempDefenseBonus() { return tempDefenseBonus; }

    public void setAttack(int val) { this.attack = val; }

    @Override
    public String toString() {
        return String.format("%s [HP: %d/%d | ATK: %d | DEF: %d(+%d) | SPD: %d]",
                name, currentHp, maxHp, attack, defense, tempDefenseBonus, speed);
    }
}
