package eatyourbeets.utilities;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.powers.PowerHelper;

public enum PowerTarget
{
    Normal,
    Enemies,
    Player,
    Source,
    Random,
    RandomEnemy,
    ALL;

    public void ApplyPowers(PowerHelper powerHelper, AbstractCreature source, int amount)
    {
        ApplyPowers(powerHelper, source, source, amount);
    }

    public void ApplyPowers(PowerHelper power, AbstractCreature source, AbstractCreature target, int amount)
    {
        switch (this)
        {
            case ALL:
                for (AbstractCreature t : GameUtilities.GetAllCharacters(true))
                {
                    GameActions.Bottom.StackPower(source, power.Create(t, source, amount));
                }
                break;

            case Enemies:
                for (AbstractCreature t : GameUtilities.GetAllEnemies(true))
                {
                    GameActions.Bottom.StackPower(source, power.Create(t, source, amount));
                }
                break;

            case Player:
                GameActions.Bottom.StackPower(source, power.Create(AbstractDungeon.player, source, amount));
                break;

            case Normal:
                GameActions.Bottom.StackPower(source, power.Create(target, source, amount));
                break;

            case Source:
                GameActions.Bottom.StackPower(source, power.Create(source, source, amount));
                break;

            case RandomEnemy:
                GameActions.Bottom.StackPower(source, power.Create(GameUtilities.GetRandomEnemy(true), source, amount));
                break;

            case Random:
                GameActions.Bottom.StackPower(source, power.Create(GameUtilities.GetRandomCharacter(true), source, amount));
                break;
        }
    }
}
