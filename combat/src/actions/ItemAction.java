package actions;

import combatants.Combatant;
import combatants.Player;
import items.Item;
import java.util.List;

public class ItemAction implements Action {

    private final Player player;
    private final Item item;

    public ItemAction(Player player, Item item) {
        this.player = player;
        this.item = item;
    }

    @Override
    public void execute(Combatant actor, List<Combatant> targets, List<String> log) {
        item.use(player, targets, log);
        player.removeItem(item);
    }

    @Override
    public String getName() { return "Use Item: " + item.getName(); }
}
