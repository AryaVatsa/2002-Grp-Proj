# Turn-Based Combat Arena — SC2002 Group Assignment

## Project Structure

```
src/
├── Main.java                        ← Entry point
├── actions/
│   ├── Action.java                  ← Interface (OCP/DIP)
│   ├── BasicAttackAction.java
│   ├── DefendAction.java
│   ├── ShieldBashAction.java        ← Warrior special skill
│   ├── ArcaneBlastAction.java       ← Wizard special skill
│   └── ItemAction.java
├── battle/
│   ├── BattleEngine.java            ← Core game loop (SRP/DIP)
│   └── Level.java                   ← Enemy spawn config
├── combatants/
│   ├── Combatant.java               ← Abstract base (LSP)
│   ├── Player.java                  ← Abstract player
│   ├── Warrior.java
│   ├── Wizard.java
│   ├── Enemy.java                   ← Abstract enemy
│   ├── Goblin.java
│   └── Wolf.java
├── effects/
│   ├── StatusEffect.java            ← Interface (OCP)
│   └── StunEffect.java
├── items/
│   ├── Item.java                    ← Interface (OCP/ISP)
│   ├── Potion.java
│   ├── PowerStone.java
│   └── SmokeBomb.java
├── strategy/
│   ├── TurnOrderStrategy.java       ← Interface (OCP/DIP)
│   └── SpeedBasedTurnOrder.java
└── ui/
    ├── BattleUI.java                ← Interface (DIP/SRP)
    └── CLIBattleUI.java
```


## SOLID Principles Applied

| Principle | Where |
|-----------|-------|
| SRP | Each class has one job: BattleEngine orchestrates, CLIBattleUI handles I/O, Level manages spawns |
| OCP | Add new Action/Item/StatusEffect by implementing the interface — BattleEngine never changes |
| LSP | Warrior/Wizard usable as Player; Goblin/Wolf usable as Enemy; all usable as Combatant |
| ISP | Action, Item, StatusEffect, TurnOrderStrategy are small, focused interfaces |
| DIP | BattleEngine depends on Action, BattleUI, TurnOrderStrategy interfaces — not concrete classes |

## Requirements Java 17+
