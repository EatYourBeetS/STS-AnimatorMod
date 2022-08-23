package eatyourbeets.cards.animatorClassic.special;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Aether extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Aether.class).SetPower(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 3;

    public OrbCore_Aether()
    {
        super(DATA, Aether::new, 1);

        Initialize(0, 0, VALUE, 1);
    }

    @Override
    protected AnimatorPower GetPower()
    {
        return new OrbCore_AetherPower(player, 1);
    }

    public static class OrbCore_AetherPower extends OrbCorePower
    {
        public OrbCore_AetherPower(AbstractCreature owner, int amount)
        {
            super(DATA, owner, amount, VALUE);

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
}