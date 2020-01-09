package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;

public class OrbCore_EarthPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_EarthPower.class.getSimpleName());

    public OrbCore_EarthPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);

        this.value = 1;
        updateDescription();
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        GameActions.Bottom.ChannelOrb(new Earth(), true);
    }
}