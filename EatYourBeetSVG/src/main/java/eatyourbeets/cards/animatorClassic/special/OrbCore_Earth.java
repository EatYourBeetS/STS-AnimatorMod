package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Earth extends OrbCore implements Hidden
{
    public static final EYBCardData DATA = Register(OrbCore_Earth.class).SetPower(0, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 3;

    public OrbCore_Earth()
    {
        super(DATA, Earth::new, 1);

        Initialize(0, 0, VALUE, 1);
    }

    @Override
    protected AnimatorPower GetPower()
    {
        return new OrbCore_EarthPower(player, 1);
    }

    public static class OrbCore_EarthPower extends OrbCorePower
    {
        public OrbCore_EarthPower(AbstractCreature owner, int amount)
        {
            super(DATA, owner, amount, VALUE);

            updateDescription();
        }

        @Override
        protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
        {
            GameActions.Bottom.GainTemporaryThorns(value);
        }
    }
}