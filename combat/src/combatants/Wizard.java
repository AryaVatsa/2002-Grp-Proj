package combatants;

public class Wizard extends Player {

    public Wizard() {
        super("Wizard", 200, 50, 10, 20);
    }

    public void applyArcaneBlastKillBonus() {
        this.attack += 10;
    }

    @Override
    public String getSpecialSkillName() {
        return "Arcane Blast";
    }

    @Override
    public String toString() {
        return super.toString() + " [Wizard]";
    }
}
