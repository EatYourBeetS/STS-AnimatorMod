package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animator.special.OrbCore_Earth;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class OrbCore_EarthPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_EarthPower.class);

    public OrbCore_EarthPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount, OrbCore_Earth.VALUE);
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        GameActions.Bottom.ChannelOrb(new Earth());
    }
}