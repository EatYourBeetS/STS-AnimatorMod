package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animator.special.OrbCore_Aether;
import eatyourbeets.utilities.GameActions;

public class OrbCore_AetherPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_AetherPower.class);

    public OrbCore_AetherPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount, OrbCore_Aether.VALUE);
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        GameActions.Bottom.Draw(potency)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Top.DiscardFromHand(name, 1, false)
                .SetOptions(false, false, false);
            }
        });
    }
}