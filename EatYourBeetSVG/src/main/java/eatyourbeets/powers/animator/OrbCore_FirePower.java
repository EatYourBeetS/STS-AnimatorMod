package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class OrbCore_FirePower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_FirePower.class);

    public OrbCore_FirePower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);

        //this.value = OrbCore_Fire.VALUE;

        updateDescription();
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), value);
    }
}