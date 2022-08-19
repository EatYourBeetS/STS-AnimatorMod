package eatyourbeets.powers.animatorClassic;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.animatorClassic.special.OrbCore_Aether;
import eatyourbeets.utilities.GameActions;

public class OrbCore_AetherPower extends OrbCore_AbstractPower
{
    public static final String POWER_ID = CreateFullID(OrbCore_AetherPower.class);

    public OrbCore_AetherPower(AbstractCreature owner, int amount)
    {
        super(POWER_ID, owner, amount);

        this.value = OrbCore_Aether.VALUE;

        updateDescription();
    }

    @Override
    protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
    {
        if (p.hand.size() < BaseMod.MAX_HAND_SIZE)
        {
            GameActions.Bottom.Draw(value)
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
}