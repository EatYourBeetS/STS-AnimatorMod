package eatyourbeets.powers.replacement;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.utilities.GameUtilities;

public class AnimatorVulnerablePower extends VulnerablePower implements CloneablePowerInterface
{
    protected boolean applyNextTurn;

    public AnimatorVulnerablePower(AbstractCreature owner, int amount, boolean isSourceMonster)
    {
        super(owner, amount, isSourceMonster);

        //this.ID = "Animator" + ID;
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        if (owner == AbstractDungeon.player && !GameUtilities.IsPlayerTurn(false))
        {
            applyNextTurn = true;
        }
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type)
    {
        if (applyNextTurn)
        {
            return damage;
        }

        return super.atDamageReceive(damage, type);
    }

    @Override
    public void atEndOfRound()
    {
        super.atEndOfRound();

        if (applyNextTurn)
        {
            applyNextTurn = false;
            AbstractDungeon.onModifyPower();
        }
    }

    @Override
    public AbstractPower makeCopy()
    {
        return new AnimatorVulnerablePower(owner, amount, false);
    }
}
