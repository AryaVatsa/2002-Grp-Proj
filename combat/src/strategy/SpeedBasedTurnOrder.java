package strategy;

import combatants.Combatant;
import java.util.ArrayList;
import java.util.List;

public class SpeedBasedTurnOrder implements TurnOrderStrategy {

    @Override
    public List<Combatant> determine(List<Combatant> combatants) {
        List<Combatant> sorted = new ArrayList<>(combatants);
        for (int i = 0; i < sorted.size() - 1; i++) {
            for (int j = i + 1; j < sorted.size(); j++) {
                if (sorted.get(j).getSpeed() > sorted.get(i).getSpeed()) {
                    Combatant temp = sorted.get(i);
                    sorted.set(i, sorted.get(j));
                    sorted.set(j, temp);
                }
            }
        }
        return sorted;
    }
}
