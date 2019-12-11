package eatyourbeets.powers.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.OrbCore_Dark;

public class OrbCore_DarkPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_DarkPower.class.getSimpleName());

    public OrbCore_DarkPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);

        this.value = OrbCore_Dark.VALUE;

        updateDescription();
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        if (p.hand.size() < BaseMod.MAX_HAND_SIZE)
        {
            GameActions.Bottom.Add(new ApplyPowerToRandomEnemyAction(p, new VulnerablePower(null, value, false), value));
            GameActions.Bottom.Add(new ApplyPowerToRandomEnemyAction(p, new WeakPower(null, value, false), value));
        }
    }
}