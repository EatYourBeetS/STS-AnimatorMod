package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.actions.animator.ElementalMasteryAction;
import eatyourbeets.interfaces.subscribers.OnApplyPowerSubscriber;
import eatyourbeets.interfaces.subscribers.OnChannelOrbSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

public class ElementalMasteryPower extends AnimatorPower implements OnChannelOrbSubscriber, OnApplyPowerSubscriber
{
    public static final String POWER_ID = CreateFullID(ElementalMasteryPower.class);
    public static final float MULTIPLIER = 5;
    public static final float MULTIPLIER_APPLY = 2;
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
        this.description = FormatDescription(0, secondaryAmount, MULTIPLIER, MULTIPLIER_APPLY);
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return new ColoredString(secondaryAmount, Color.WHITE, c.a);
    }

    @Override
    public void playApplyPowerSfx()
    {
        CardCrawlGame.sound.playA("ORB_PLASMA_CHANNEL", -0.25f);
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

        GameActions.Bottom.Add(new ElementalMasteryAction(amount));
        this.secondaryAmount -= 1;

        if (this.secondaryAmount <= 0) {
            GameActions.Bottom.RemovePower(owner, owner, this);
        }
    }

    @Override
    public void OnApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if ((power.ID.equals(BurningPower.POWER_ID)  || power.ID.equals(ChilledPower.POWER_ID)) && source == owner && target != owner) {
            this.amount += MULTIPLIER_APPLY;
        }
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
