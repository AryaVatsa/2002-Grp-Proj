package combatants;

import items.Item;
import java.util.ArrayList;
import java.util.List;

public abstract class Player extends Combatant {

    private List<Item> inventory = new ArrayList<>();
    private int skillCooldown = 0;
    private int smokeBombTurns = 0;

    public Player(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
    }

    public void addItem(Item item) { inventory.add(item); }
    public List<Item> getInventory() { return inventory; }
    public boolean hasItems() { return !inventory.isEmpty(); }
    public void removeItem(Item item) { inventory.remove(item); }

    public boolean isSpecialSkillReady() { return skillCooldown == 0; }
    public int getSpecialSkillCooldown() { return skillCooldown; }
    public void setSpecialSkillCooldown(int turns) { this.skillCooldown = turns; }

    public void tickCooldown() {
        if (skillCooldown > 0) skillCooldown--;
    }

    public void applySmokeBomb() { smokeBombTurns = 2; }
    public boolean isSmokeBombActive() { return smokeBombTurns > 0; }
    public void tickSmokeBomb() { if (smokeBombTurns > 0) smokeBombTurns--; }
    public int getSmokeBombTurnsRemaining() { return smokeBombTurns; }

    public abstract String getSpecialSkillName();
}
