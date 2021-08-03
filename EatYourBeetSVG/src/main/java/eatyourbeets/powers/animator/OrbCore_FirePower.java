package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animator.special.OrbCore_Fire;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class OrbCore_FirePower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_FirePower.class);

    public OrbCore_FirePower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount, OrbCore_Fire.VALUE);
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), potency);
    }
}