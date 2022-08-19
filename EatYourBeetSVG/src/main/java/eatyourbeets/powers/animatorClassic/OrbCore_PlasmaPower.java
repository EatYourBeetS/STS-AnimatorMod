package eatyourbeets.powers.animatorClassic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animatorClassic.special.OrbCore_Plasma;
import eatyourbeets.utilities.GameActions;

public class OrbCore_PlasmaPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_PlasmaPower.class);

    public OrbCore_PlasmaPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);

        this.value = OrbCore_Plasma.VALUE;

        updateDescription();
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        GameActions.Bottom.GainTemporaryHP(value);
    }
}