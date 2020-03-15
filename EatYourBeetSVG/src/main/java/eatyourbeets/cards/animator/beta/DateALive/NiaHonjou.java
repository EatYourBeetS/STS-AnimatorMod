package eatyourbeets.cards.animator.beta.DateALive;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JavaUtilities;

public class NiaHonjou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(NiaHonjou.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public NiaHonjou()
    {
        super(DATA);

        Initialize(0, 2, 2);
        SetScaling(0, 1, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (upgraded)
        {
            GameActions.Bottom.Motivate(1);
        }

        GameActions.Bottom.StackPower(new NiaHonjouPower(p, magicNumber));
    }

    public static class NiaHonjouPower extends AnimatorPower
    {
        public NiaHonjouPower(AbstractPlayer owner, int amount)
        {
            super(owner, NiaHonjou.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }

        @Override
        public void stackPower(int stackAmount)
        {
            super.stackPower(stackAmount);
            updateDescription();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount, amount);
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard)
        {
            super.onAfterCardPlayed(usedCard);

            GameActions.Bottom.Scry(amount);
            GameActions.Bottom.GainBlock(amount);
        }
    }
}