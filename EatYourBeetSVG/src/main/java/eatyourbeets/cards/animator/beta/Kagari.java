package eatyourbeets.cards.animator.beta;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;

public class Kagari extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kagari.class).SetPower(1, CardRarity.RARE).SetColor(CardColor.COLORLESS);

    public Kagari()
    {
        super(DATA);

        Initialize(0, 0, 1, 1);
        SetUpgrade(0, 0, 0, 1);

        SetSynergy(Synergies.Rewrite);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.Draw(secondaryValue);
        GameActions.Bottom.StackPower(new KagariPower(p, magicNumber));
    }

    public static class KagariPower extends AnimatorPower
    {
        private int thornsBase;

        public KagariPower(AbstractPlayer owner, int thornsBase)
        {
            super(owner, Kagari.DATA);

            this.thornsBase = thornsBase;
            this.amount = thornsBase;

            updateDescription();
        }

        @Override
        public void stackPower(int stackAmount)
        {
            super.stackPower(stackAmount);
            updateDescription();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            this.amount = thornsBase;
            updateDescription();
        }

        @Override
        public void atEndOfRound()
        {
            this.amount = thornsBase;
            updateDescription();
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount)
        {
            if (damageAmount > 0 && damageAmount < owner.currentHealth && info.owner != null && info.type == DamageInfo.DamageType.NORMAL)
            {
                GameActions.Top.GainThorns(amount);
                updateDescription();
            }

            return damageAmount;
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}