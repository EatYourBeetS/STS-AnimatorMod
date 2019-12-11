package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.OrbCore_Frost;

public class OrbCore_FrostPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_FrostPower.class.getSimpleName());

    public OrbCore_FrostPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);

        this.value = OrbCore_Frost.VALUE;

        updateDescription();
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        GameActions.Bottom.GainPlatedArmor(value);
    }
}