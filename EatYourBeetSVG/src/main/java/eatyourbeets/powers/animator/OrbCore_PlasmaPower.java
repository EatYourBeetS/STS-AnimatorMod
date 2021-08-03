package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.special.OrbCore_Plasma;

public class OrbCore_PlasmaPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_PlasmaPower.class);

    public OrbCore_PlasmaPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount, OrbCore_Plasma.VALUE);
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        GameActions.Bottom.GainTemporaryHP(potency);
    }
}