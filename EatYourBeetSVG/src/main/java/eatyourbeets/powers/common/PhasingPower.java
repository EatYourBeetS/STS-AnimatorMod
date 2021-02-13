package eatyourbeets.powers.common;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.interfaces.subscribers.OnModifyDamageSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.CommonPower;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;

public class PhasingPower extends CommonPower implements OnModifyDamageSubscriber
{
    public static final String POWER_ID = CreateFullID(PhasingPower.class);

    public static final int BASE_AMOUNT = 35;
    public static final int CHANCE_REDUCTION = 5;

    protected int turns;

    public PhasingPower(AbstractCreature owner, int stacks)
    {
        super(owner, POWER_ID);

        this.amount = BASE_AMOUNT;
        this.type = PowerType.BUFF;
        this.priority = 99;
        this.turns = Math.max(0, stacks - 1);

        updateDescription();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        CombatStats.onModifyDamage.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onModifyDamage.Unsubscribe(this);

    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        if (ID.equals(power.ID) && target == owner)
        {
            this.turns += (((PhasingPower)power).turns + 1);
        }

        super.onApplyPower(power, target, source);
    }

    @Override
    public void stackPower(int stackAmount)
    {
        this.amount = BASE_AMOUNT;
        this.fontScale = 8f;
        this.updateDescription();
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        if (turns > 0)
        {
            turns -= 1;
            updateDescription();
        }
        else
        {
            GameActions.Bottom.ReducePower(this, CHANCE_REDUCTION);
        }
    }

    @Override
    public int OnModifyDamage(AbstractCreature target, DamageInfo info, int damage)
    {
        if (target == owner && info.type == DamageInfo.DamageType.NORMAL && rng.random(100) < amount)
        {
            player.tint.color.a = 0;
            GameActions.Bottom.SFX("ORB_PLASMA_CHANNEL", 1.6f);
            GameActions.Top.Wait(0.15f);
            flashWithoutSound();

            return 0;
        }

        return damage;
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        return (turns > 0) ? new ColoredString(turns, Color.WHITE, c.a) : null;
    }

    public void playApplyPowerSfx()
    {
        CardCrawlGame.sound.playA("ORB_PLASMA_CHANNEL", 2);
    }

    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount, CHANCE_REDUCTION);

        if (turns > 0)
        {
            description += FormatDescription(1, turns);
        }
        else
        {
            description += FormatDescription(2, CHANCE_REDUCTION);
        }
    }
}
