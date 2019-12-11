package eatyourbeets.powers.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.animator.special.OrbCore_Chaos;

public class OrbCore_ChaosPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_ChaosPower.class.getSimpleName());

    public OrbCore_ChaosPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);

        this.value = OrbCore_Chaos.VALUE;

        updateDescription();
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        if (p.hand.size() < BaseMod.MAX_HAND_SIZE)
        {
            GameActions.Bottom.Add(new DiscoveryAction());
        }
    }
}