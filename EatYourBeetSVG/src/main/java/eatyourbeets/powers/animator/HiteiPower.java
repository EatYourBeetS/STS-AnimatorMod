package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.Utilities;
import eatyourbeets.actions.animator.HiteiAction2;

public class HiteiPower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(HiteiPower.class.getSimpleName());

    private int upgradeStack;
    private int unupgradedStacks;

    public HiteiPower(AbstractPlayer owner, boolean upgraded)
    {
        super(owner, POWER_ID);

        if (upgraded)
        {
            this.upgradeStack += 1;
        }
        else
        {
            this.unupgradedStacks = 1;
        }

        this.amount = 1;

        updateDescription();
    }

    @Override
    public void updateDescription()
    {
        this.description = (powerStrings.DESCRIPTIONS[0] + amount + powerStrings.DESCRIPTIONS[1]);
    }

    @Override
    public void atStartOfTurn()
    {
        super.atStartOfTurn();

        AbstractPlayer p = AbstractDungeon.player;

        for (int i = 0; i < unupgradedStacks; i++)
        {
            GameActionsHelper.AddToBottom(new HiteiAction2(2));
            GainRandomBuff(p);
        }

        for (int i = 0; i < upgradeStack; i++)
        {
            GameActionsHelper.AddToBottom(new HiteiAction2(3));
            GainRandomBuff(p);
        }

        this.flash();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
    {
        HiteiPower other = Utilities.SafeCast(power, HiteiPower.class);
        if (other != null && power.owner == target)
        {
            this.unupgradedStacks += other.unupgradedStacks;
            this.upgradeStack += other.upgradeStack;
        }

        super.onApplyPower(power, target, source);
    }

    private void GainRandomBuff(AbstractPlayer p)
    {
        int roll = AbstractDungeon.miscRng.random(38);
        if (roll <= 4)
        {
            GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, 1), 1);
        }
        else if (roll <= 8)
        {
            GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, 1), 1);
        }
        else if (roll <= 12)
        {
            GameActionsHelper.ApplyPower(p, p, new FocusPower(p, 1), 1);
        }
        else if (roll <= 16)
        {
            GameActionsHelper.ApplyPower(p, p, new ArtifactPower(p, 1), 1);
        }
        else if (roll <= 20)
        {
            GameActionsHelper.ApplyPower(p, p, new BlurPower(p, 1), 1);
        }
        else if (roll <= 24)
        {
            GameActionsHelper.ApplyPower(p, p, new ThornsPower(p, 2), 2);
        }
        else if (roll <= 28)
        {
            GameActionsHelper.ApplyPower(p, p, new PlatedArmorPower(p, 2), 2);
        }
        else if (roll <= 32)
        {
            GameActionsHelper.ApplyPower(p, p, new DrawCardNextTurnPower(p, 1), 1);
        }
        else if (roll <= 36)
        {
            GameActionsHelper.ApplyPower(p, p, new EnergizedPower(p, 1), 1);
        }
        else if (roll <= 37)
        {
            GameActionsHelper.ApplyPower(p, p, new IntangiblePlayerPower(p, 1), 1);
        }
        else
        {
            GameActionsHelper.ApplyPower(p, p, new BufferPower(p, 1), 1);
        }
    }
}
