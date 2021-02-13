package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.animator.CounterAttackPower;
import eatyourbeets.utilities.GameActions;

public class OrihimeInoue extends AnimatorCard
{
    public static final EYBCardData DATA = Register(OrihimeInoue.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public OrihimeInoue()
    {
        super(DATA);

        Initialize(0, 5, 1, 2);
        SetUpgrade(0, 3, 0);

        SetSynergy(Synergies.Bleach);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.StackPower(new OrihimeInouePower(p, magicNumber));

        if (HasSynergy())
        {
            GameActions.Bottom.StackPower(new CounterAttackPower(p, secondaryValue));
        }
    }

    public static class OrihimeInouePower extends AnimatorPower
    {
        public OrihimeInouePower(AbstractPlayer owner, int amount)
        {
            super(owner, OrihimeInoue.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void atStartOfTurn()
        {
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
            description = FormatDescription(0, amount);
        }

        public int onAttacked(DamageInfo info, int damageAmount)
        {
            if (this.amount <= 0)
            {
                return damageAmount;
            }

            this.amount--;

            GameActions.Bottom.ChannelOrb(new Fire(), true);

            return damageAmount;
        }
    }
}