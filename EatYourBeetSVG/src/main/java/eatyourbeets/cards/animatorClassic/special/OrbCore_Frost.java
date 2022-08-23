package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Frost extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Frost.class).SetPower(0, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 2;

    public OrbCore_Frost()
    {
        super(DATA, Frost::new, 2);

        Initialize(0, 0, VALUE, 2);
    }

    @Override
    protected AnimatorPower GetPower()
    {
        return new OrbCore_FrostPower(player, 1);
    }

    public static class OrbCore_FrostPower extends OrbCorePower
    {
        public OrbCore_FrostPower(AbstractCreature owner, int amount)
        {
            super(DATA, owner, amount, OrbCore_Frost.VALUE);

            updateDescription();
        }

        @Override
        protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
        {
            GameActions.Bottom.GainPlatedArmor(value);
        }
    }
}