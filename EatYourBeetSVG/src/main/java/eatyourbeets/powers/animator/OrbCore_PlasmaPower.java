package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.OrbCore_Plasma;

public class OrbCore_PlasmaPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_PlasmaPower.class.getSimpleName());

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