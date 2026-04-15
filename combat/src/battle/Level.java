package battle;

import combatants.Enemy;
import combatants.Goblin;
import combatants.Wolf;
import java.util.ArrayList;
import java.util.List;

public class Level {

    public enum Difficulty { EASY, MEDIUM, HARD }

    private final Difficulty difficulty;
    private final List<Enemy> initialWave;
    private final List<Enemy> backupWave;
    private boolean backupSpawned = false;

    public Level(Difficulty difficulty) {
        this.difficulty = difficulty;
        this.initialWave = new ArrayList<>();
        this.backupWave = new ArrayList<>();
        buildWaves();
    }

    private void buildWaves() {
        Goblin.resetCounter();
        Wolf.resetCounter();

        switch (difficulty) {
            case EASY -> {
                initialWave.add(new Goblin());
                initialWave.add(new Goblin());
                initialWave.add(new Goblin());
            }
            case MEDIUM -> {
                initialWave.add(new Goblin());
                initialWave.add(new Wolf());
                backupWave.add(new Wolf());
                backupWave.add(new Wolf());
            }
            case HARD -> {
                initialWave.add(new Goblin());
                initialWave.add(new Goblin());
                backupWave.add(new Goblin());
                backupWave.add(new Wolf());
                backupWave.add(new Wolf());
            }
        }
    }

    public List<Enemy> getInitialWave()  { return initialWave; }
    public List<Enemy> getBackupWave()   { return backupWave; }
    public boolean hasBackup()           { return !backupWave.isEmpty(); }
    public boolean isBackupSpawned()     { return backupSpawned; }
    public void markBackupSpawned()      { backupSpawned = true; }
    public Difficulty getDifficulty()    { return difficulty; }
}
