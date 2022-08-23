package eatyourbeets.cards.animatorClassic.special;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class OrbCore_Dark extends OrbCore
{
    public static final EYBCardData DATA = Register(OrbCore_Dark.class).SetPower(0, CardRarity.SPECIAL).SetColor(CardColor.COLORLESS);

    public static final int VALUE = 1;

    public OrbCore_Dark()
    {
        super(DATA, Dark::new, 2);

        Initialize(0, 0, VALUE, 2);
    }

    @Override
    protected AnimatorPower GetPower()
    {
        return new OrbCore_DarkPower(player, 1);
    }

    public static class OrbCore_DarkPower extends OrbCorePower
    {
        public OrbCore_DarkPower(AbstractCreature owner, int amount)
        {
            super(DATA, owner, amount, VALUE);

            updateDescription();
        }

        @Override
        protected void OnSynergy(AbstractPlayer p, AbstractCard usedCard)
        {
            if (p.hand.size() < BaseMod.MAX_HAND_SIZE)
            {
                GameActions.Bottom.Add(new ApplyPowerToRandomEnemyAction(p, new VulnerablePower(null, value, false), value));
                GameActions.Bottom.Add(new ApplyPowerToRandomEnemyAction(p, new WeakPower(null, value, false), value));
            }
        }
    }
}