package pinacolada.powers.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import eatyourbeets.utilities.ColoredString;
import pinacolada.actions.special.ElementalMasteryAction;
import pinacolada.effects.SFX;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;

public class ElementalMasteryPower extends PCLPower implements OnChannelOrbSubscriber
{
    public static final String POWER_ID = CreateFullID(ElementalMasteryPower.class);
    public static final float MULTIPLIER = 5;
    public static final int MAX_TURNS = 2;
    public int secondaryAmount;


    public ElementalMasteryPower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        this.amount = amount;
        if (this.amount >= 9999)
        {
            this.amount = 9999;
        }
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.secondaryAmount = MAX_TURNS;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = FormatDescription(0, secondaryAmount, MULTIPLIER);
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString(secondaryAmount, Color.WHITE, c.a);
    }

    @Override
    public void playApplyPowerSfx()
    {
        PCLActions.Top.SFX(SFX.HEAL_3);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        super.stackPower(stackAmount);
        updateDescription();
    }

    @Override
    public void reducePower(int reduceAmount)
    {
        super.reducePower(reduceAmount);
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        PCLActions.Bottom.Add(new ElementalMasteryAction(amount));
        this.secondaryAmount -= 1;

        if (this.secondaryAmount <= 0) {
            PCLActions.Bottom.RemovePower(owner, owner, this);
        }
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        PCLCombatStats.onChannelOrb.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        PCLCombatStats.onChannelOrb.Unsubscribe(this);
    }

    @Override
    public void OnChannelOrb(AbstractOrb orb)
    {
        this.amount += MULTIPLIER;
    }

    @Override
    public void onEvokeOrb(AbstractOrb orb)
    {
        this.amount += MULTIPLIER;
    }

}
