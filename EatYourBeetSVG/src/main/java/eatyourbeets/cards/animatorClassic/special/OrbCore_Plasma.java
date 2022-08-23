package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.Plasma;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Plasma extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Plasma.class).SetPower(1, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 3;

    public OrbCore_Plasma()
    {
        super(DATA, Plasma::new, 1);

        Initialize(0, 0, VALUE, 1);
    }

    @Override
    protected AnimatorPower GetPower()
    {
        return new OrbCore_PlasmaPower(player, 1);
    }

    public static class OrbCore_PlasmaPower extends OrbCorePower
    {
        public OrbCore_PlasmaPower(AbstractCreature owner, int amount)
        {
            super(DATA, owner, amount, VALUE);

            updateDescription();
        }

        @Override
        protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
        {
            GameActions.Bottom.GainTemporaryHP(value);
        }
    }
}