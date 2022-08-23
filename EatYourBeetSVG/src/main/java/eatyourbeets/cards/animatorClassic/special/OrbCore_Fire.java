package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class OrbCore_Fire extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Fire.class).SetPower(0, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 3;

    public OrbCore_Fire()
    {
        super(DATA, Fire::new, 2);

        Initialize(0, 0, VALUE, 2);
    }

    @Override
    protected AnimatorPower GetPower()
    {
        return new OrbCore_FirePower(player, 1);
    }

    public static class OrbCore_FirePower extends OrbCorePower
    {
        public OrbCore_FirePower(AbstractCreature owner, int amount)
        {
            super(DATA, owner, amount, VALUE);

            updateDescription();
        }

        @Override
        protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
        {
            GameActions.Bottom.ApplyBurning(TargetHelper.Enemies(), value);
        }
    }
}