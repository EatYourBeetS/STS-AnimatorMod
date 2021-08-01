package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.animator.special.OrbCore_Dark;
import eatyourbeets.utilities.GameActions;

public class OrbCore_DarkPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_DarkPower.class);

    public OrbCore_DarkPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount, OrbCore_Dark.VALUE);
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        GameActions.Bottom.Add(new ApplyPowerToRandomEnemyAction(p, new VulnerablePower(null, potency, false), potency));
        GameActions.Bottom.Add(new ApplyPowerToRandomEnemyAction(p, new WeakPower(null, potency, false), potency));
    }
}